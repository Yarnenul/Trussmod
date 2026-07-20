package com.yarne.trussmod.block;

import net.minecraft.util.StringRepresentable;

public enum BumpPart implements StringRepresentable {
    BASE("base", 0),
    FRONT1("front1", 1),
    FRONT2("front2", 2),
    FRONT3("front3", 3),
    BACK1("back1", -1),
    BACK2("back2", -2),
    BACK3("back3", -3);

    private final String name;
    private final int offset;

    BumpPart(String name, int offset) {
        this.name = name;
        this.offset = offset;
    }

    /** Offset t.o.v. het base-blok, langs de facing richting (positief = naar voor). */
    public int offset() {
        return this.offset;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
