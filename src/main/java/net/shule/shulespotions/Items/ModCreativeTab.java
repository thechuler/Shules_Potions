package net.shule.shulespotions.Items;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.ShulesPotions;


/*Esta clase se encarga de crear un espaciecito en el inventario creativo para nuestras cosas
Este codigo agrega automaticamente las cosas a nuestra pestaña.
Aca podemos configurar por ejemplo, que icono va a tener en creativo.
 */

public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ShulesPotions.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register("main_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.ACACIA_BOAT)) //<---El icono depende del item que queramos pooner
                    .title(Component.translatable("creativetab.main_tab"))
                    .displayItems((pParameters, pOutput) -> {

                        for (int i = 0; i < ModItems.ITEMS.getEntries().size(); i++) {
                            pOutput.accept(ModItems.ITEMS.getEntries().stream().toList().get(i).get().asItem());
                        }
                    })
                    .build());





    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }


}
