package net.codersoffortune.infinity.fixture;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FixtureLoader {

    private FixtureLoader() {}

    public static List<JsonTestCase> discover() throws IOException {
        URL resource = FixtureLoader.class.getResource("/fixtures/");
        if (resource == null) {
            return new ArrayList<>();
        }
        Path fixturesRoot;
        try {
            fixturesRoot = Paths.get(resource.toURI());
        } catch (Exception e) {
            throw new IOException("Cannot resolve fixtures directory from classpath", e);
        }
        if (!Files.isDirectory(fixturesRoot)) {
            return new ArrayList<>();
        }
        try (Stream<Path> entries = Files.list(fixturesRoot)) {
            return entries
                    .filter(Files::isDirectory)
                    .sorted(Comparator.comparing(p -> p.getFileName().toString()))
                    .map(dir -> {
                        try {
                            return load(dir);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to load fixture: " + dir, e);
                        }
                    })
                    .collect(Collectors.toList());
        }
    }

    public static JsonTestCase load(Path fixtureDir) throws IOException {
        String name = fixtureDir.getFileName().toString();

        Path unitPath = fixtureDir.resolve("unit.json");
        if (!Files.exists(unitPath)) {
            throw new IllegalArgumentException("Missing required file unit.json in fixture: " + name);
        }
        String unitJson = Files.readString(unitPath, StandardCharsets.UTF_8);

        Path cataloguePath = fixtureDir.resolve("catalogue.json");
        if (!Files.exists(cataloguePath)) {
            throw new IllegalArgumentException("Missing required file catalogue.json in fixture: " + name);
        }
        String catalogueJson = Files.readString(cataloguePath, StandardCharsets.UTF_8);

        String expectedJson = null;
        Path expectedPath = fixtureDir.resolve("expected.json");
        if (Files.exists(expectedPath)) {
            expectedJson = Files.readString(expectedPath, StandardCharsets.UTF_8);
        }

        int sectoralId = 101;
        boolean doAddons = false;
        boolean expectEmpty = false;
        Path propsPath = fixtureDir.resolve("case.properties");
        if (Files.exists(propsPath)) {
            Properties props = new Properties();
            props.load(Files.newInputStream(propsPath));
            String sectoralProp = props.getProperty("sectoral");
            if (sectoralProp != null) {
                sectoralId = Integer.parseInt(sectoralProp.trim());
            }
            String doAddonsProp = props.getProperty("doAddons");
            if (doAddonsProp != null) {
                doAddons = Boolean.parseBoolean(doAddonsProp.trim());
            }
            String expectEmptyProp = props.getProperty("expectEmpty");
            if (expectEmptyProp != null) {
                expectEmpty = Boolean.parseBoolean(expectEmptyProp.trim());
            }
        }

        return new JsonTestCase(name, unitJson, catalogueJson, expectedJson, sectoralId, doAddons, expectEmpty);
    }
}
