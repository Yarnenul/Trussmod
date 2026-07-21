package com.yarne.trussmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

/**
 * Truss 2x2: 2 x 2 blokken doorsnede, 2 blokken hoog, met 1 vierkant
 * X-element over de volledige hoogte. Per element plaatsbaar en stapelbaar.
 */
public class Truss22Block extends Block {

    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);

    private static final VoxelShape SHAPE =
            Shapes.box(-0.5, 0.0, -0.5, 1.5, 1.0, 1.5);

    public Truss22Block(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, Part.BOTTOM));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        if (pos.getY() < level.getMaxBuildHeight() - 1
                && level.getBlockState(pos.above()).canBeReplaced(context)) {
            return this.defaultBlockState();
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(PART, Part.TOP), 3);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Part part = state.getValue(PART);
        if (direction == Direction.UP && part == Part.BOTTOM) {
            if (!(neighborState.is(this) && neighborState.getValue(PART) == Part.TOP)) {
                return Blocks.AIR.defaultBlockState();
            }
        } else if (direction == Direction.DOWN && part == Part.TOP) {
            if (!(neighborState.is(this) && neighborState.getValue(PART) == Part.BOTTOM)) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative() && state.getValue(PART) == Part.TOP) {
            BlockPos bottomPos = pos.below();
            BlockState bottomState = level.getBlockState(bottomPos);
            if (bottomState.is(this) && bottomState.getValue(PART) == Part.BOTTOM) {
                level.setBlock(bottomPos, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, bottomPos, Block.getId(bottomState));
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    public enum Part implements StringRepresentable {
        BOTTOM("bottom"),
        TOP("top");

        private final String name;

        Part(String name) {
            this.name = name;
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
}
