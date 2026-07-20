package com.yarne.trussmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Speaker bump: headblock met uitkragende beams en kettingtakels
 * om een line array aan te hangen. Kan enkel bovenop een tower truss
 * geplaatst worden.
 */
public class SpeakerBumpBlock extends Block {

    private static final VoxelShape SHAPE =
            Shapes.box(-4.0 / 16.0, 0.0, -4.0 / 16.0, 20.0 / 16.0, 8.0 / 16.0, 20.0 / 16.0);

    public SpeakerBumpBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.getBlock() instanceof TrussTowerBlock
                && below.getValue(TrussTowerBlock.PART) == TrussTowerBlock.Part.TOP;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !this.canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }
}
