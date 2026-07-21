package com.yarne.trussmod.block;

import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.List;

/**
 * Speaker bump quad (4+3): 4 beams, 4 blokken naar voor en 3 naar achter.
 */
public class SpeakerBumpQuadAsymBlock extends SpeakerBumpBlock {

    public static final EnumProperty<BumpPart> PART_QUAD_ASYM =
            EnumProperty.create("part", BumpPart.class,
                    BumpPart.BASE,
                    BumpPart.FRONT1, BumpPart.FRONT2, BumpPart.FRONT3, BumpPart.FRONT4,
                    BumpPart.BACK1, BumpPart.BACK2, BumpPart.BACK3);

    private static final List<BumpPart> PARTS = List.of(
            BumpPart.BASE,
            BumpPart.FRONT1, BumpPart.FRONT2, BumpPart.FRONT3, BumpPart.FRONT4,
            BumpPart.BACK1, BumpPart.BACK2, BumpPart.BACK3);

    public SpeakerBumpQuadAsymBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected List<BumpPart> parts() {
        return PARTS;
    }

    @Override
    protected EnumProperty<BumpPart> partProperty() {
        return PART_QUAD_ASYM;
    }
}
