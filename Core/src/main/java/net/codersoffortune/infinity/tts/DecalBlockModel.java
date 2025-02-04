package net.codersoffortune.infinity.tts;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A model using the old style single block for defining the decals.
 * These blocks are just copied, escaped and pasted from TTS
 */
public class DecalBlockModel extends TTSModel {
    private String decals;

    private Set<String> extractedDecals;

    private static final Pattern decalPattern = Pattern.compile("\"ImageURL\":\"(?<url>[^\"]*)\"");

    public DecalBlockModel() {
    }

    public DecalBlockModel(String name, String decals, String baseImage) {
        this.name = name;
        setDecals(decals);
        setBaseImage(baseImage);
    }

    public String getDecals() {
        return decals;
    }

    public void setDecals(String decals) {
        this.decals = decals;
        this.extractedDecals = extractDecals(decals);
    }

    private static Set<String> extractDecals(String decals) {
        Set<String> result = new HashSet<>();
        Matcher m = decalPattern.matcher(decals);
        while( m.find() ) {
            result.add(m.group("url"));
        }
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DecalBlockModel ttsModel = (DecalBlockModel) o;
        return Objects.equals(extractedDecals, ttsModel.extractedDecals) &&
               Objects.equals(baseImage, ttsModel.baseImage) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(extractedDecals, baseImage);
    }
}
