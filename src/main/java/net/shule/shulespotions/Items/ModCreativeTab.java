package net.shule.shulespotions.Items;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Blocks.ModBlocks;
import net.shule.shulespotions.ShulesPotions;


public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ShulesPotions.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register("main_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.POTION_CAULDRON.get().asItem())) //<---El icono depende del item que queramos pooner
                    .title(Component.translatable("creativetab.main_tab"))
                    .withBackgroundLocation(ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/test.png"))
                    .withTabsImage(ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/tab.png"))
                    .displayItems((parameters, output) -> {

                        ModItems.ITEMS.getEntries().forEach(entry -> output.accept(entry.get()));

                    })
                    .build());








    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }


}
