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
 * Horizontale truss: 1,5 x 1,5 blok doorsnede en 3 blokken lang.
 * 1 item plaatsen = de volledige ligger van 3 blokken in de kijkrichting
 * van de speler. Visueel 2 truss-segmenten van elk 1,5 blok, net zoals de toren.
 */
public class HorizontalTrussBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);

    private static final VoxelShape SHAPE_Z =
            Shapes.box(-4.0 / 16.0, -4.0 / 16.0, 0.0, 20.0 / 16.0, 20.0 / 16.0, 1.0);
    private static final VoxelShape SHAPE_X =
            Shapes.box(0.0, -4.0 / 16.0, -4.0 / 16.0, 1.0, 20.0 / 16.0, 20.0 / 16.0);

    public HorizontalTrussBlock(Properties properties) {
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
        Direction facing = context.getHorizontalDirection();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        for (Part part : Part.values()) {
            if (part == Part.START) continue;
            if (!level.getBlockState(pos.relative(facing, part.offset())).canBeReplaced(context)) {
                return null;
            }
        }
        return this.defaultBlockState().setValue(FACING, facing).setValue(PART, Part.START);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction facing = state.getValue(FACING);
        for (Part part : Part.values()) {
            if (part == Part.START) continue;
            level.setBlock(pos.relative(facing, part.offset()), state.setValue(PART, part), 3);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && !state.is(newState.getBlock()) && !level.isClientSide) {
            Direction facing = state.getValue(FACING);
            BlockPos startPos = pos.relative(facing, -state.getValue(PART).offset());
            for (Part part : Part.values()) {
                BlockPos target = startPos.relative(facing, part.offset());
                if (target.equals(pos)) continue;
                BlockState targetState = level.getBlockState(target);
                if (targetState.is(this) && targetState.getValue(PART) == part) {
                    level.destroyBlock(target, true);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative() && state.getValue(PART) != Part.START) {
            Direction facing = state.getValue(FACING);
            BlockPos startPos = pos.relative(facing, -state.getValue(PART).offset());
            BlockState startState = level.getBlockState(startPos);
            if (startState.is(this) && startState.getValue(PART) == Part.START) {
                level.setBlock(startPos, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, startPos, Block.getId(startState));
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    public enum Part implements StringRepresentable {
        START("start", 0),
        MIDDLE("middle", 1),
        END("end", 2);

        private final String name;
        private final int offset;

        Part(String name, int offset) {
            this.name = name;
            this.offset = offset;
        }

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
}
