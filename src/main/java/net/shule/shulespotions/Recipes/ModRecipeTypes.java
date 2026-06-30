package net.shule.shulespotions.Recipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.ShulesPotions;

public class ModRecipeTypes {

    public static final RecipeType<MortarRecipe> MORTAR = new RecipeType<>() {
        @Override
        public String toString() {
            return "shulespotions:mortar";
        }
    };

    public static void register(IEventBus bus) {
        bus.addListener(ModRecipeTypes::registerTypes);
    }

    private static void registerTypes(RegisterEvent event) {
        event.register(
                Registries.RECIPE_TYPE,
                helper -> helper.register(
                        ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID, "mortar"),
                        MORTAR
                )
        );
    }
}