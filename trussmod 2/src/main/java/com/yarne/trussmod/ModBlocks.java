package com.yarne.trussmod;

import com.yarne.trussmod.block.BigHorizontalTrussBlock;
import com.yarne.trussmod.block.BigTrussTowerBlock;
import com.yarne.trussmod.block.ChainBridleBlock;
import com.yarne.trussmod.block.HorizontalTrussBlock;
import com.yarne.trussmod.block.SpeakerBumpBlock;
import com.yarne.trussmod.block.SpeakerBumpDoubleBlock;
import com.yarne.trussmod.block.SpeakerBumpQuadAsymBlock;
import com.yarne.trussmod.block.SpeakerBumpQuadBlock;
import com.yarne.trussmod.block.Truss22Block;
import com.yarne.trussmod.block.Truss22HorizontalBlock;
import com.yarne.trussmod.block.TrussTowerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, TrussMod.MODID);

    public static final RegistryObject<Block> TRUSS_TOWER = BLOCKS.register("truss_tower",
            () -> new TrussTowerBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Block> HORIZONTAL_TRUSS = BLOCKS.register("horizontal_truss",
            () -> new HorizontalTrussBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Block> BIG_TRUSS_TOWER = BLOCKS.register("big_truss_tower",
            () -> new BigTrussTowerBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Block> BIG_HORIZONTAL_TRUSS = BLOCKS.register("big_horizontal_truss",
            () -> new BigHorizontalTrussBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Block> SPEAKER_BUMP = BLOCKS.register("speaker_bump",
            () -> new SpeakerBumpBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Block> SPEAKER_BUMP_DOUBLE = BLOCKS.register("speaker_bump_double",
            () -> new SpeakerBumpDoubleBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Block> TRUSS_2X2 = BLOCKS.register("truss_2x2",
            () -> new Truss22Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Block> HORIZONTAL_TRUSS_2X2 = BLOCKS.register("horizontal_truss_2x2",
            () -> new Truss22HorizontalBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Block> SPEAKER_BUMP_QUAD = BLOCKS.register("speaker_bump_quad",
            () -> new SpeakerBumpQuadBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Block> SPEAKER_BUMP_QUAD_ASYM = BLOCKS.register("speaker_bump_quad_asym",
            () -> new SpeakerBumpQuadAsymBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Block> CHAIN_BRIDLE = BLOCKS.register("chain_bridle",
            () -> new ChainBridleBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(1.5F, 6.0F)
                    .sound(SoundType.CHAIN)
                    .noOcclusion()));
}
