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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

/**
 * Horizontale truss 2x2: 2 x 2 blokken doorsnede, 2 blokken lang,
 * 1 vierkant X-element. Per element plaatsbaar, met automatische
 * richting-flip zodat je segmenten achter elkaar kan doorbouwen.
 */
public class Truss22HorizontalBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);

    private static final VoxelShape SHAPE_Z =
            Shapes.box(-0.5, -0.5, 0.0, 1.5, 1.5, 1.0);
    private static final VoxelShape SHAPE_X =
            Shapes.box(0.0, -0.5, -0.5, 1.0, 1.5, 1.5);

    public Truss22HorizontalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, Part.START));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING).getAxis() == Direction.Axis.Z ? SHAPE_Z : SHAPE_X;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction preferred = context.getHorizontalDirection();
        for (Direction facing : new Direction[]{preferred, preferred.getOpposite()}) {
            if (level.getBlockState(pos.relative(facing)).canBeReplaced(context)) {
                return this.defaultBlockState().setValue(FACING, facing).setValue(PART, Part.START);
            }
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        level.setBlock(pos.relative(state.getValue(FACING)), state.setValue(PART, Part.END), 3);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && !state.is(newState.getBlock()) && !level.isClientSide) {
            Direction facing = state.getValue(FACING);
            BlockPos other = state.getValue(PART) == Part.START
                    ? pos.relative(facing)
                    : pos.relative(facing.getOpposite());
            Part expected = state.getValue(PART) == Part.START ? Part.END : Part.START;
            BlockState otherState = level.getBlockState(other);
            if (otherState.is(this) && otherState.getValue(PART) == expected
                    && otherState.getValue(FACING) == facing) {
                level.destroyBlock(other, true);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative() && state.getValue(PART) == Part.END) {
            Direction facing = state.getValue(FACING);
            BlockPos startPos = pos.relative(facing.getOpposite());
            BlockState startState = level.getBlockState(startPos);
            if (startState.is(this) && startState.getValue(PART) == Part.START
                    && startState.getValue(FACING) == facing) {
                level.setBlock(startPos, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, startPos, Block.getId(startState));
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    public enum Part implements StringRepresentable {
        START("start"),
        END("end");

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
