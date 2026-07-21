package com.yarne.trussmod;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TrussMod.MODID);

    public static final RegistryObject<Item> TRUSS_TOWER = ITEMS.register("truss_tower",
            () -> new BlockItem(ModBlocks.TRUSS_TOWER.get(), new Item.Properties()));

    public static final RegistryObject<Item> HORIZONTAL_TRUSS = ITEMS.register("horizontal_truss",
            () -> new BlockItem(ModBlocks.HORIZONTAL_TRUSS.get(), new Item.Properties()));

    public static final RegistryObject<Item> BIG_TRUSS_TOWER = ITEMS.register("big_truss_tower",
            () -> new BlockItem(ModBlocks.BIG_TRUSS_TOWER.get(), new Item.Properties()));

    public static final RegistryObject<Item> BIG_HORIZONTAL_TRUSS = ITEMS.register("big_horizontal_truss",
            () -> new BlockItem(ModBlocks.BIG_HORIZONTAL_TRUSS.get(), new Item.Properties()));

    public static final RegistryObject<Item> SPEAKER_BUMP = ITEMS.register("speaker_bump",
            () -> new BlockItem(ModBlocks.SPEAKER_BUMP.get(), new Item.Properties()));

    public static final RegistryObject<Item> SPEAKER_BUMP_DOUBLE = ITEMS.register("speaker_bump_double",
            () -> new BlockItem(ModBlocks.SPEAKER_BUMP_DOUBLE.get(), new Item.Properties()));

    public static final RegistryObject<Item> TRUSS_2X2 = ITEMS.register("truss_2x2",
            () -> new BlockItem(ModBlocks.TRUSS_2X2.get(), new Item.Properties()));

    public static final RegistryObject<Item> HORIZONTAL_TRUSS_2X2 = ITEMS.register("horizontal_truss_2x2",
            () -> new BlockItem(ModBlocks.HORIZONTAL_TRUSS_2X2.get(), new Item.Properties()));

    public static final RegistryObject<Item> SPEAKER_BUMP_QUAD = ITEMS.register("speaker_bump_quad",
            () -> new BlockItem(ModBlocks.SPEAKER_BUMP_QUAD.get(), new Item.Properties()));

    public static final RegistryObject<Item> SPEAKER_BUMP_QUAD_ASYM = ITEMS.register("speaker_bump_quad_asym",
            () -> new BlockItem(ModBlocks.SPEAKER_BUMP_QUAD_ASYM.get(), new Item.Properties()));
}
