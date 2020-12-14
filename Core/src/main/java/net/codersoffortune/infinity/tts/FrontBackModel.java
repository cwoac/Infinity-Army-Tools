package net.codersoffortune.infinity.tts;

import net.codersoffortune.infinity.db.Database;

import java.util.Objects;

public class FrontBackModel extends TTSModel {

    private Decal front;
    private Decal back;

    public Decal getFront() {
        return front;
    }

    public void setFront(Decal front) {
        this.front = front;
    }


    public Decal getBack() {
        return back;
    }

    public void setBack(Decal back) {
        this.back = back;
    }

    public FrontBackModel(){}


    @Override
    public String getDecals() {
        Decal b = getBack();
        if( b == null ) {
            b = getFront();
        }

        return String.format(Database.getDecalTemplate(),
                front.getX_offset(),
                front.getOffset(),
                front.getX_scale(),
                front.getScale(),
                front.getUrl(),
                b.getX_offset(),
                b.getOffset(),
                b.getX_scale(),
                b.getScale(),
                b.getUrl());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FrontBackModel that = (FrontBackModel) o;
        return Objects.equals(front, that.front) &&
                Objects.equals(back, that.back) &&
                Objects.equals(baseImage, that.baseImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(front, back, baseImage);
    }
}
