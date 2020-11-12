package net.codersoffortune.infinity.tts;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TTSModel ttsModel = (TTSModel) o;
        return Objects.equals(decals, ttsModel.decals) &&
                Objects.equals(meshes, ttsModel.meshes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(decals, meshes);
    }
}
