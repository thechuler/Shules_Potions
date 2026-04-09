package net.shule.shulespotions.Recipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Recipes.Potion.PotionRecipe;
import net.shule.shulespotions.Recipes.Potion.PotionRecipeSerializer;
import net.shule.shulespotions.ShulesPotions;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ShulesPotions.MODID);

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, ShulesPotions.MODID);

    public static final RegistryObject<RecipeSerializer<PotionRecipe>> POTION_SERIALIZER =
            SERIALIZERS.register("potion", PotionRecipeSerializer::new);

    public static final RegistryObject<RecipeType<PotionRecipe>> POTION_TYPE =
            TYPES.register("potion", () -> new RecipeType<>() {
                public String toString() {
                    return "shulespotions:potion";
                }
            });

    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
        TYPES.register(bus);
    }
}
