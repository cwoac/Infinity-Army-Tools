package net.codersoffortune.infinity.tts;

public class TTSModel {
    private String decals;
    private String meshes;
    private String name; // mostly a sanity check

    public TTSModel() {
    }

    public TTSModel(String name, String decals, String meshes) {
        this.name = name;
        this.decals = decals;
        this.meshes = meshes;
    }

    public String getName() {
        return name;
    }

    public String getMeshes() {
        return meshes;
    }

    public void setMeshes(String meshes) {
        this.meshes = meshes;
    }

    public String getDecals() {
        return decals;
    }

    public void setDecals(String decals) {
        this.decals = decals;
    }
}
