package com.yarne.trussmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

/**
 * Grote tower truss: 3 x 3 blokken doorsnede en 6 blokken hoog (verhouding 1:2).
 * 1 item plaatsen = de volledige toren, met 2 X-bracing segmenten van elk 3 blokken.
 * Segment 1 en 4 hosten elk de render van hun helft (48px, binnen modelbereik).
 */
public class BigTrussTowerBlock extends Block {

    public static final IntegerProperty SEGMENT = IntegerProperty.create("segment", 0, 5);

    private static final VoxelShape SHAPE =
            Shapes.box(-1.0, 0.0, -1.0, 2.0, 1.0, 2.0);

    public BigTrussTowerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SEGMENT, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SEGMENT);
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
        if (pos.getY() >= level.getMaxBuildHeight() - 5) {
            return null;
        }
        for (int i = 1; i <= 5; i++) {
            if (!level.getBlockState(pos.above(i)).canBeReplaced(context)) {
                return null;
            }
        }
        return this.defaultBlockState().setValue(SEGMENT, 0);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        for (int i = 1; i <= 5; i++) {
            level.setBlock(pos.above(i), state.setValue(SEGMENT, i), 3);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && !state.is(newState.getBlock()) && !level.isClientSide) {
            BlockPos basePos = pos.below(state.getValue(SEGMENT));
            for (int i = 0; i <= 5; i++) {
                BlockPos target = basePos.above(i);
                if (target.equals(pos)) continue;
                BlockState targetState = level.getBlockState(target);
                if (targetState.is(this) && targetState.getValue(SEGMENT) == i) {
                    level.destroyBlock(target, true);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative() && state.getValue(SEGMENT) != 0) {
            BlockPos basePos = pos.below(state.getValue(SEGMENT));
            BlockState baseState = level.getBlockState(basePos);
            if (baseState.is(this) && baseState.getValue(SEGMENT) == 0) {
                level.setBlock(basePos, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, basePos, Block.getId(baseState));
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }
}
