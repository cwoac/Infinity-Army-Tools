package net.codersoffortune.infinity.tts;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TTSModel implements Serializable {
    private String decals;
    private String baseImage;
    private String name; // mostly a sanity check

    private Set<String> extractedDecals;

    private static final Pattern decalPattern = Pattern.compile("\"ImageURL\":\"(?<url>[^\"]*)\"");

    public TTSModel() {
    }

    public TTSModel(String name, String decals, String baseImage) {
        this.name = name;
        setDecals(decals);
        setBaseImage(baseImage);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBaseImage() {
        return baseImage;
    }

    public void setBaseImage(String baseImage) {
        this.baseImage = baseImage;
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
        TTSModel ttsModel = (TTSModel) o;
        return Objects.equals(extractedDecals, ttsModel.extractedDecals) &&
               Objects.equals(baseImage, ttsModel.baseImage) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(extractedDecals, baseImage);
    }
}
