package com.yarne.trussmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

/**
 * Grote horizontale truss: 3 x 3 blokken doorsnede en 6 blokken lang.
 * 1 item plaatsen = de volledige ligger in de kijkrichting, met automatische
 * richting-flip zodat je liggers achter elkaar kan doorbouwen.
 */
public class BigHorizontalTrussBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty SEGMENT = IntegerProperty.create("segment", 0, 5);

    private static final VoxelShape SHAPE =
            Shapes.box(-1.0, -1.0, -1.0, 2.0, 2.0, 2.0);

    public BigHorizontalTrussBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(SEGMENT, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SEGMENT);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction preferred = context.getHorizontalDirection();
        for (Direction facing : new Direction[]{preferred, preferred.getOpposite()}) {
            boolean free = true;
            for (int i = 1; i <= 5; i++) {
                if (!level.getBlockState(pos.relative(facing, i)).canBeReplaced(context)) {
                    free = false;
                    break;
                }
            }
            if (free) {
                return this.defaultBlockState().setValue(FACING, facing).setValue(SEGMENT, 0);
            }
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction facing = state.getValue(FACING);
        for (int i = 1; i <= 5; i++) {
            level.setBlock(pos.relative(facing, i), state.setValue(SEGMENT, i), 3);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && !state.is(newState.getBlock()) && !level.isClientSide) {
            Direction facing = state.getValue(FACING);
            BlockPos startPos = pos.relative(facing, -state.getValue(SEGMENT));
            for (int i = 0; i <= 5; i++) {
                BlockPos target = startPos.relative(facing, i);
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
            Direction facing = state.getValue(FACING);
            BlockPos startPos = pos.relative(facing, -state.getValue(SEGMENT));
            BlockState startState = level.getBlockState(startPos);
            if (startState.is(this) && startState.getValue(SEGMENT) == 0) {
                level.setBlock(startPos, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, startPos, Block.getId(startState));
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }
}
