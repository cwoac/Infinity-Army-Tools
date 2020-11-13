package net.codersoffortune.infinity.tts;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TTSModel {
    private String decals;
    private String meshes;
    private String name; // mostly a sanity check

    private static Pattern decalPattern = Pattern.compile("\"ImageURL\":\"(?<url>[^\"]*)\"");
    private static Pattern meshPattern = Pattern.compile("\"MeshURL\":\"(?<url>[^\"]*)\"");

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

    private static Set<String> extractDecals(String decals) {
        Set<String> result = new HashSet<>();
        Matcher m = decalPattern.matcher(decals);
        while( m.find() ) {
            result.add(m.group("url"));
        }
        return result;
    }

    private static Set<String> extractMeshes(String meshes) {
        Set<String> result = new HashSet<>();
        Matcher m = meshPattern.matcher(meshes);
        while( m.find() ) {
            result.add(m.group("url"));
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TTSModel ttsModel = (TTSModel) o;
        return extractDecals(decals).equals(extractDecals(ttsModel.decals)) &&
                extractMeshes(meshes).equals(extractMeshes(ttsModel.meshes));
    }

    @Override
    public int hashCode() {
        return Objects.hash(decals, meshes);
    }
}
