package net.name.template.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.name.template.Template;



/*TAGS: Los tags son (como su nombre lo indica) etiquetas que sirven para agrupar cosas en minecraft.
Con tags podemos decir cosas como: "Todos estos bloques son minables con mi herramienta", O
"Mi comida va a usar todos estos items como proteina en su crafteo"
*/

public class ModTags {


    //Estos son los tags que aplicamos a bloques
    public static class Blocks{

     //   public static final TagKey<Block> NEED_THERIUM_TOOL =tag("need_therium_tool");

        private static TagKey<Block> tag(String name){
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Template.MODID,name));
        }
    }




//Estos son los tags que aplicamos a los items
    public static class Items{


        private static TagKey<Item> tag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Template.MODID,name));
        }
    }


}
