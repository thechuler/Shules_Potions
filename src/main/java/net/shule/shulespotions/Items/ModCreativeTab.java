package net.shule.shulespotions.Items;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Blocks.ModBlocks;
import net.shule.shulespotions.Items.custom.RecipeSheetItem;
import net.shule.shulespotions.ShulesPotions;


/*Esta clase se encarga de crear un espaciecito en el inventario creativo para nuestras cosas
Este codigo agrega automaticamente las cosas a nuestra pestaña.
Aca podemos configurar por ejemplo, que icono va a tener en creativo.
 */

public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ShulesPotions.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register("main_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.POTION_CAULDRON.get().asItem())) //<---El icono depende del item que queramos pooner
                    .title(Component.translatable("creativetab.main_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        ModItems.ITEMS.getEntries().forEach(entry -> {
                            Item item = entry.get();

                            if (!(item instanceof RecipeSheetItem)) {
                                pOutput.accept(item);
                            }
                        });
                    })
                    .build());


    public static final RegistryObject<CreativeModeTab> RECIPE_TABS = CREATIVE_TABS.register("recipe_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ABSORPTION_RECIPE.get())) //<---El icono depende del item que queramos pooner
                    .title(Component.translatable("creativetab.recipe_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        ModItems.ITEMS.getEntries().stream()
                                .map(RegistryObject::get)
                                .filter(item -> item instanceof RecipeSheetItem)
                                .forEach(pOutput::accept);
                    })
                    .build());





    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }


}
