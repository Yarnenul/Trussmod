package com.yarne.trussmod;

import com.yarne.trussmod.block.BigHorizontalTrussBlock;
import com.yarne.trussmod.block.BigTrussTowerBlock;
import com.yarne.trussmod.block.HorizontalTrussBlock;
import com.yarne.trussmod.block.SpeakerBumpBlock;
import com.yarne.trussmod.block.SpeakerBumpDoubleBlock;
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
}
