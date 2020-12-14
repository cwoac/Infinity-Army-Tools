package net.codersoffortune.infinity.tts;

import java.util.Objects;

public class Decal {
    private String url;
    private float offset = 0.8f;
    private float scale = 1.4f;
    private float x_offset = 0.0f;
    private float x_scale = 1.0f;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Decal decal = (Decal) o;
        return Float.compare(decal.offset, offset) == 0 &&
                Float.compare(decal.scale, scale) == 0 &&
                Float.compare(decal.x_offset, x_offset) == 0 &&
                Float.compare(decal.x_scale, x_scale) == 0 &&
                Objects.equals(url, decal.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, offset, scale);
    }

    public float getX_scale() {
        return x_scale;
    }

    public void setX_scale(float x_scale) {
        this.x_scale = x_scale;
    }

    public float getX_offset() {
        return x_offset;
    }

    public void setX_offset(float x_offset) {
        this.x_offset = x_offset;
    }
}

