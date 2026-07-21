package com.yarne.trussmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

/**
 * Chain Bridle: los plaatsbaar item dat je onder de takels van een bump hangt.
 * De 4 schuine kettinglijnen sluiten exact aan op de haken erboven en komen
 * samen in een centrale link waar een vanilla chain naadloos op aansluit.
 * Detecteert bij plaatsing automatisch de bump erboven: richting wordt
 * overgenomen en bij een quad bump (4 beams) wordt de brede variant gebruikt.
 */
public class ChainBridleBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WIDE = BooleanProperty.create("wide");

    private static final VoxelShape SHAPE =
            Shapes.box(5.0 / 16.0, 0.0, 5.0 / 16.0, 11.0 / 16.0, 9.0 / 16.0, 11.0 / 16.0);

    public ChainBridleBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WIDE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WIDE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState above = context.getLevel().getBlockState(context.getClickedPos().above());
        Direction facing = context.getHorizontalDirection();
        boolean wide = false;
        if (above.getBlock() instanceof SpeakerBumpBlock) {
            facing = above.getValue(SpeakerBumpBlock.FACING);
            wide = above.getBlock() instanceof SpeakerBumpQuadBlock
                    || above.getBlock() instanceof SpeakerBumpQuadAsymBlock;
        }
        return this.defaultBlockState().setValue(FACING, facing).setValue(WIDE, wide);
    }
}
