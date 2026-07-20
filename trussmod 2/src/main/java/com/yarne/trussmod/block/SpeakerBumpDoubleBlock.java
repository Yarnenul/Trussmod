package com.yarne.trussmod.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.List;

/**
 * Speaker bump (dubbel): beams kragen 3 blokken uit naar voor EN naar achter.
 */
public class SpeakerBumpDoubleBlock extends SpeakerBumpBlock {

    public static final EnumProperty<BumpPart> PART_DOUBLE =
            EnumProperty.create("part", BumpPart.class);

    private static final List<BumpPart> PARTS = List.of(
            BumpPart.BASE,
            BumpPart.FRONT1, BumpPart.FRONT2, BumpPart.FRONT3,
            BumpPart.BACK1, BumpPart.BACK2, BumpPart.BACK3);

    public SpeakerBumpDoubleBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected List<BumpPart> parts() {
        return PARTS;
    }

    @Override
    protected EnumProperty<BumpPart> partProperty() {
        return PART_DOUBLE;
    }
}
