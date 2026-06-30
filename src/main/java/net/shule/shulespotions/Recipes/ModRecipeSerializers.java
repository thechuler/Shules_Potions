package net.shule.shulespotions.Recipes;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.ShulesPotions;

public class ModRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ShulesPotions.MODID);

    public static final RegistryObject<RecipeSerializer<MortarRecipe>> MORTAR =
            SERIALIZERS.register(
                    "mortar",
                    MortarRecipeSerializer::new
            );

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}