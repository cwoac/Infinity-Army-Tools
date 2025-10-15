package net.codersoffortune.infinity;

public enum GAME {
    N5("infinity N5", "https://api.corvusbelli.com/army/infinity/en/metadata", "resources/metadata_n5.json"),
    CODE_ONE("Code One", "https://api.corvusbelli.com/army/codeone/en/metadata", "resources/metadata_c1.json");

    private final String name;
    private final String metadataURL;
    private final String metadataFile;

    GAME(String name, String metadataURL, String metadataFile) {
        this.name = name;
        this.metadataURL = metadataURL;
        this.metadataFile = metadataFile;
    }

    public String getMetadataURL() {
        return metadataURL;
    }

    public String getName() {
        return name;
    }

    public String getMetadataFile() {
        return metadataFile;
    }
}
