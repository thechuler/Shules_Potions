package net.name.template.dataGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.name.template.Template;

import java.util.concurrent.CompletableFuture;


/* Esta es la clase principal de la generacion de datos.
Normalmente para que nuestro codigo funcione deberiamos usar unos archivos
bastante feos que se llaman JSON (preguntar a chule que es un json)

Esta clase lo hace por nosotros, y crea un json de cada aspecto importante de nuestro
mod
 */

@Mod.EventBusSubscriber(modid = Template.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static  void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packoutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new ProviderItemModel(packoutput,existingFileHelper));
        generator.addProvider(event.includeClient(), new ProviderBlockState(packoutput, existingFileHelper));
        ProviderBlockTag blockTagGenerator = generator.addProvider(event.includeServer(),
                new ProviderBlockTag(packoutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new ProviderItemTag(packoutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new ProviderWorldGen(packoutput, lookupProvider));
        generator.addProvider(event.includeServer(), new ProviderRecipe(packoutput));
        generator.addProvider(event.includeServer(), ProviderLootTable.create(packoutput,lookupProvider));
    }
}
