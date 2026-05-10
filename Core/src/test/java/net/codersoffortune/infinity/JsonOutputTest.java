package net.codersoffortune.infinity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.codersoffortune.infinity.db.DatabaseTestHelper;
import net.codersoffortune.infinity.fixture.FixtureLoader;
import net.codersoffortune.infinity.fixture.JsonTestCase;
import net.codersoffortune.infinity.metadata.SectoralList;
import net.codersoffortune.infinity.metadata.unit.CompactedUnit;
import net.codersoffortune.infinity.metadata.unit.PrintableUnit;
import net.codersoffortune.infinity.metadata.unit.Unit;
import net.codersoffortune.infinity.tts.ModelSet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsonOutputTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @TempDir
    Path tempDir;

    @BeforeAll
    void setUpTemplates() throws IOException {
        String unitTemplate = loadResource("/templates/unit_template.json");
        String bagTemplate = loadResource("/templates/bag_template");
        String factionTemplate = loadResource("/templates/faction_template");
        String meshTemplate = loadResource("/templates/mesh_template.json");
        String transmutedTemplate = loadResource("/templates/transmuted_model_template");
        String seedTemplate = loadResource("/templates/seed_embryo_template.json");
        String decalTemplate = loadResource("/templates/decal_template.json");

        Map<Integer, String> addonTemplates = new HashMap<>();
        addonTemplates.put(25, loadResource("/templates/25mm_addon.json"));
        addonTemplates.put(40, "");
        addonTemplates.put(55, "");
        addonTemplates.put(70, "");

        DatabaseTestHelper.installTemplates(
                unitTemplate,
                addonTemplates,
                bagTemplate,
                factionTemplate,
                meshTemplate,
                transmutedTemplate,
                seedTemplate,
                decalTemplate
        );
    }

    @AfterAll
    void tearDownTemplates() {
        DatabaseTestHelper.reset();
    }

    static Stream<JsonTestCase> fixtures() {
        if (FixtureLoader.class.getResource("/fixtures/") == null) {
            return Stream.empty();
        }
        try {
            List<JsonTestCase> cases = FixtureLoader.discover();
            return cases.stream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("fixtures")
    void testJsonOutput(JsonTestCase testCase) throws Exception {
        Unit unit = MAPPER.readValue(testCase.getUnitJson(), Unit.class);

        SectoralList sectoralList = SectoralList.load(String.valueOf(testCase.getSectoralId()));
        sectoralList.getMappedFilters(); // populates static allData as side effect

        SECTORAL sectoral = SECTORAL.getByID(testCase.getSectoralId());
        if (sectoral == null) {
            throw new IllegalArgumentException("Unknown sectoral id: " + testCase.getSectoralId());
        }

        Collection<CompactedUnit> compactedUnits = unit.getAllDistinctUnits();

        Path catFile = tempDir.resolve("catalogue.json");
        Files.writeString(catFile, testCase.getCatalogueJson());
        ModelSet modelSet = new ModelSet(catFile.toString());

        int cuIndex = 0;
        for (CompactedUnit cu : compactedUnits) {
            PrintableUnit pu = new PrintableUnit(cu, sectoral); // uses GlobalMappedFactionFilters which reads allData
            Optional<String> result = pu.asFactionJSON(modelSet, testCase.isDoAddons());
            cuIndex++;

            // Write generated JSON to file for inspection
            Files.createDirectories(Path.of("Core/build/test-output/"));
            Path outputPath = Path.of("Core/build/test-output/", testCase.getName() + "_" + cuIndex + ".json");
            if (result.isPresent()) {
                String prettyJson = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(MAPPER.readTree(result.get()));
                Files.writeString(outputPath, prettyJson);
            } else {
                Files.writeString(outputPath, "<empty>");
            }

            if (testCase.isExpectEmpty()) {
                Assertions.assertFalse(result.isPresent(),
                        "Expected empty result for " + pu.getName() + " but asFactionJSON returned a value");
            } else if (testCase.getExpectedJson() != null) {
                Assertions.assertTrue(result.isPresent(),
                        "asFactionJSON returned empty for " + pu.getName() + " but expected.json is present");
                assertJsonEquals(testCase.getExpectedJson(), result.get());
            }
            // smoke test: if no expected.json and not expecting empty, just verify it doesn't throw (empty is acceptable)
        }
    }

    private static String loadResource(String path) throws IOException {
        try (InputStream is = JsonOutputTest.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new IOException("Classpath resource not found: " + path);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static void assertJsonEquals(String expected, String actual) throws Exception {
        JsonNode expectedNode = MAPPER.readTree(expected);
        JsonNode actualNode = MAPPER.readTree(actual);
        if (!jsonNodesEqual(expectedNode, actualNode)) {
            String prettyExpected = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(expectedNode);
            String prettyActual = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actualNode);
            Assertions.fail("JSON mismatch.\nExpected:\n" + prettyExpected + "\nActual:\n" + prettyActual);
        }
    }

    private static boolean jsonNodesEqual(JsonNode expected, JsonNode actual) {
        if (expected.isTextual() && "<<ANY>>".equals(expected.asText())) {
            return true;
        }
        if (expected.isObject() && actual.isObject()) {
            if (!setsEqual(expected.fieldNames(), actual.fieldNames())) {
                return false;
            }
            Iterator<Map.Entry<String, JsonNode>> fields = expected.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                JsonNode actualChild = actual.get(entry.getKey());
                if (actualChild == null || !jsonNodesEqual(entry.getValue(), actualChild)) {
                    return false;
                }
            }
            return true;
        }
        if (expected.isArray() && actual.isArray()) {
            if (expected.size() != actual.size()) {
                return false;
            }
            for (int i = 0; i < expected.size(); i++) {
                if (!jsonNodesEqual(expected.get(i), actual.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return expected.equals(actual);
    }

    private static boolean setsEqual(Iterator<String> a, Iterator<String> b) {
        Set<String> setA = new HashSet<>();
        Set<String> setB = new HashSet<>();
        a.forEachRemaining(setA::add);
        b.forEachRemaining(setB::add);
        return setA.equals(setB);
    }
}
