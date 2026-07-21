package com.yarne.trussmod;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TrussMod.MODID);

    public static final RegistryObject<CreativeModeTab> STAGE_TRUSS = TABS.register("stage_truss",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.trussmod.stage_truss"))
                    .icon(() -> new ItemStack(ModItems.TRUSS_TOWER.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.TRUSS_TOWER.get());
                        output.accept(ModItems.HORIZONTAL_TRUSS.get());
                        output.accept(ModItems.BIG_TRUSS_TOWER.get());
                        output.accept(ModItems.BIG_HORIZONTAL_TRUSS.get());
                        output.accept(ModItems.SPEAKER_BUMP.get());
                        output.accept(ModItems.SPEAKER_BUMP_DOUBLE.get());
                    })
                    .build());
}
