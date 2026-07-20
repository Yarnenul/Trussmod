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

    public static final RegistryObject<Item> SPEAKER_BUMP = ITEMS.register("speaker_bump",
            () -> new BlockItem(ModBlocks.SPEAKER_BUMP.get(), new Item.Properties()));
}
