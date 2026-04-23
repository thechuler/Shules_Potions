package net.shule.shulespotions.dataGen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.shule.shulespotions.Recipes.ModRecipes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PotionRecipeBuilder {

    private final List<JsonObject> actions = new ArrayList<>();
    private MobEffect result;

    public static PotionRecipeBuilder create() {
        return new PotionRecipeBuilder();
    }


    public PotionRecipeBuilder addItem(Item item) {
        actions.add(itemAction("add_item", item));
        return this;
    }


    public PotionRecipeBuilder addLiquid(Item item) {
        actions.add(itemAction("add_liquid", item));
        return this;
    }


    public PotionRecipeBuilder stir(Item item) {
        if (!(item instanceof Item)) {
            throw new IllegalArgumentException("Stir requires a SpoonItem");
        }

        actions.add(itemAction("stir", item));
        return this;
    }


    public PotionRecipeBuilder heat(int temperature) {
        JsonObject obj = new JsonObject();
        obj.addProperty("action", "heat");
        obj.addProperty("temperature", temperature);

        actions.add(obj);
        return this;
    }

    public PotionRecipeBuilder freeze(int temperature) {
        JsonObject obj = new JsonObject();
        obj.addProperty("action", "freeze");
        obj.addProperty("temperature", temperature);

        actions.add(obj);
        return this;
    }


    public PotionRecipeBuilder result(MobEffect effect) {
        this.result = effect;
        return this;
    }


    private JsonObject itemAction(String type, Item item) {
        JsonObject obj = new JsonObject();
        obj.addProperty("action", type);

        JsonObject itemJson = new JsonObject();
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        itemJson.addProperty("item", id.toString());

        obj.add("item", itemJson);
        return obj;
    }


    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new FinishedRecipe() {

            @Override
            public void serializeRecipeData(JsonObject json) {
                json.addProperty("type", "shulespotions:potion");

                JsonArray array = new JsonArray();
                for (JsonObject action : actions) {
                    array.add(action);
                }

                json.add("actions", array);

                ResourceLocation effectId = BuiltInRegistries.MOB_EFFECT.getKey(result);
                json.addProperty("result", effectId.toString());
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return ModRecipes.POTION_SERIALIZER.get();
            }

            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }
}