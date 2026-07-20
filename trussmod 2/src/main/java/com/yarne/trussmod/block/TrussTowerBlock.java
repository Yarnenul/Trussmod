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
 * Tower truss: 1,5 x 1,5 blok breed en 3 blokken hoog.
 * Je plaatst 1 item en de volledige toren van 3 blokken verschijnt.
 * Visueel bestaat de toren uit 2 truss-segmenten van elk 1,5 blok hoog.
 */
public class TrussTowerBlock extends Block {

    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);

    // 1,5 blok breed (24 px), gecentreerd op het blok: -4 tot 20
    private static final VoxelShape SHAPE =
            Shapes.box(-4.0 / 16.0, 0.0, -4.0 / 16.0, 20.0 / 16.0, 1.0, 20.0 / 16.0);

    public TrussTowerBlock(Properties properties) {
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
        if (pos.getY() < level.getMaxBuildHeight() - 2
                && level.getBlockState(pos.above()).canBeReplaced(context)
                && level.getBlockState(pos.above(2)).canBeReplaced(context)) {
            return this.defaultBlockState();
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(PART, Part.MIDDLE), 3);
        level.setBlock(pos.above(2), state.setValue(PART, Part.TOP), 3);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Part part = state.getValue(PART);
        if (direction == Direction.UP && part != Part.TOP) {
            Part expected = (part == Part.BOTTOM) ? Part.MIDDLE : Part.TOP;
            if (!(neighborState.is(this) && neighborState.getValue(PART) == expected)) {
                return Blocks.AIR.defaultBlockState();
            }
        } else if (direction == Direction.DOWN && part != Part.BOTTOM) {
            Part expected = (part == Part.TOP) ? Part.MIDDLE : Part.BOTTOM;
            if (!(neighborState.is(this) && neighborState.getValue(PART) == expected)) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            // In creative: verwijder de bottom eerst zonder drop, net zoals bij deuren
            Part part = state.getValue(PART);
            if (part != Part.BOTTOM) {
                BlockPos bottomPos = pos.below(part == Part.TOP ? 2 : 1);
                BlockState bottomState = level.getBlockState(bottomPos);
                if (bottomState.is(this) && bottomState.getValue(PART) == Part.BOTTOM) {
                    level.setBlock(bottomPos, Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, bottomPos, Block.getId(bottomState));
                }
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    public enum Part implements StringRepresentable {
        BOTTOM("bottom"),
        MIDDLE("middle"),
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
