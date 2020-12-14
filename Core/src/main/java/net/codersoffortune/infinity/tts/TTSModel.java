package net.codersoffortune.infinity.tts;

import java.io.Serializable;

public abstract class TTSModel implements Serializable {
    protected String baseImage;
    protected String name; // mostly a sanity check

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

    public abstract String getDecals();
}
