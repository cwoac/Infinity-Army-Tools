package net.codersoffortune.infinity.tts;

public class TTSModel {
    private String decals;
    private String meshes;

    public TTSModel() {
    }

    public TTSModel(String decals, String meshes) {
        this.decals = decals;
        this.meshes = meshes;
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