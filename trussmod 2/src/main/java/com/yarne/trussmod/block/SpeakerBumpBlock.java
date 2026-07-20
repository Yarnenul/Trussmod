package com.yarne.trussmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
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
import java.util.List;

/**
 * Speaker bump (enkel): headblock op de truss met beams die 3 blokken
 * naar voor uitkragen, met kettingtakels om een line array aan te hangen.
 * 1 item plaatsen = base + 3 arm-blokken in de kijkrichting van de speler.
 */
public class SpeakerBumpBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<BumpPart> PART = EnumProperty.create("part", BumpPart.class,
            BumpPart.BASE, BumpPart.FRONT1, BumpPart.FRONT2, BumpPart.FRONT3);

    private static final List<BumpPart> PARTS =
            List.of(BumpPart.BASE, BumpPart.FRONT1, BumpPart.FRONT2, BumpPart.FRONT3);

    private static final VoxelShape BASE_SHAPE =
            Shapes.box(-4.0 / 16.0, 0.0, -4.0 / 16.0, 20.0 / 16.0, 9.0 / 16.0, 20.0 / 16.0);
    private static final VoxelShape ARM_SHAPE =
            Shapes.box(0.0, 2.0 / 16.0, 0.0, 1.0, 9.0 / 16.0, 1.0);

    public SpeakerBumpBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(partProperty(), BumpPart.BASE));
    }

    protected List<BumpPart> parts() {
        return PARTS;
    }

    protected EnumProperty<BumpPart> partProperty() {
        return PART;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(partProperty());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(partProperty()) == BumpPart.BASE ? BASE_SHAPE : ARM_SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        for (BumpPart part : parts()) {
            if (part == BumpPart.BASE) continue;
            if (!level.getBlockState(pos.relative(facing, part.offset())).canBeReplaced(context)) {
                return null;
            }
        }
        return this.defaultBlockState().setValue(FACING, facing).setValue(partProperty(), BumpPart.BASE);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction facing = state.getValue(FACING);
        for (BumpPart part : parts()) {
            if (part == BumpPart.BASE) continue;
            level.setBlock(pos.relative(facing, part.offset()),
                    state.setValue(partProperty(), part), 3);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(partProperty()) != BumpPart.BASE) {
            return true;
        }
        BlockState below = level.getBlockState(pos.below());
        if (below.getBlock() instanceof TrussTowerBlock) {
            return below.getValue(TrussTowerBlock.PART) == TrussTowerBlock.Part.TOP;
        }
        // Op gewone blokken: onderliggend blok moet een stevige bovenkant hebben
        return below.isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN
                && state.getValue(partProperty()) == BumpPart.BASE
                && !this.canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && !state.is(newState.getBlock()) && !level.isClientSide) {
            Direction facing = state.getValue(FACING);
            BlockPos basePos = pos.relative(facing, -state.getValue(partProperty()).offset());
            for (BumpPart part : parts()) {
                BlockPos target = basePos.relative(facing, part.offset());
                if (target.equals(pos)) continue;
                BlockState targetState = level.getBlockState(target);
                if (targetState.is(this) && targetState.getValue(partProperty()) == part) {
                    level.destroyBlock(target, true);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()
                && state.getValue(partProperty()) != BumpPart.BASE) {
            Direction facing = state.getValue(FACING);
            BlockPos basePos = pos.relative(facing, -state.getValue(partProperty()).offset());
            BlockState baseState = level.getBlockState(basePos);
            if (baseState.is(this) && baseState.getValue(partProperty()) == BumpPart.BASE) {
                level.setBlock(basePos, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, basePos, Block.getId(baseState));
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }
}
