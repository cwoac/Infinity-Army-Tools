package net.codersoffortune.infinity.fixture;

public class JsonTestCase {
    private final String name;
    private final String unitJson;
    private final String catalogueJson;
    private final String expectedJson;
    private final int sectoralId;
    private final boolean doAddons;
    private final boolean expectEmpty;

    public JsonTestCase(String name, String unitJson, String catalogueJson, String expectedJson,
                        int sectoralId, boolean doAddons, boolean expectEmpty) {
        this.name = name;
        this.unitJson = unitJson;
        this.catalogueJson = catalogueJson;
        this.expectedJson = expectedJson;
        this.sectoralId = sectoralId;
        this.doAddons = doAddons;
        this.expectEmpty = expectEmpty;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() { return name; }
    public String getUnitJson() { return unitJson; }
    public String getCatalogueJson() { return catalogueJson; }
    public String getExpectedJson() { return expectedJson; }
    public int getSectoralId() { return sectoralId; }
    public boolean isDoAddons() { return doAddons; }
    public boolean isExpectEmpty() { return expectEmpty; }
}
