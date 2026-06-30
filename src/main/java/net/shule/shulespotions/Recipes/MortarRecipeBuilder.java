package net.shule.shulespotions.Recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class MortarRecipeBuilder {

    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients = NonNullList.create();

    private final int bowls;
    private final int grinds;
    private final float experience;
    private final String color;

    public MortarRecipeBuilder(ItemStack result, int bowls, int grinds, float experience, String color) {
        this.result = result;
        this.bowls = bowls;
        this.grinds = grinds;
        this.experience = experience;
        this.color = color;
    }

    public static MortarRecipeBuilder mortar(ItemLike result, int bowls, int grinds, float xp,String color) {
        return new MortarRecipeBuilder(new ItemStack(result), bowls, grinds, xp,color);
    }

    public MortarRecipeBuilder addIngredient(ItemLike item) {
        ingredients.add(Ingredient.of(item));
        return this;
    }

    public MortarRecipeBuilder addIngredient(TagKey<Item> tag) {
        ingredients.add(Ingredient.of(tag));
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, ingredients, result, bowls, grinds, experience,color));
    }

    public String getColor() {
        return color;
    }

    // ---------------- RESULT ----------------

    private static class Result implements FinishedRecipe {

        private final ResourceLocation id;
        private final NonNullList<Ingredient> ingredients;
        private final ItemStack result;
        private final int bowls;
        private final int grinds;
        private final float experience;
        private final String color;

        public Result(ResourceLocation id,
                      NonNullList<Ingredient> ingredients,
                      ItemStack result,
                      int bowls,
                      int grinds,
                      float experience, String color) {

            this.id = id;
            this.ingredients = ingredients;
            this.result = result;
            this.bowls = bowls;
            this.grinds = grinds;
            this.experience = experience;
            this.color = color;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {

            JsonArray ingredientsArray = new JsonArray();

            for (Ingredient ingredient : ingredients) {
                ingredientsArray.add(ingredient.toJson());
            }

            json.add("ingredients", ingredientsArray);

            JsonObject resultObj = new JsonObject();
            resultObj.addProperty(
                    "item",
                    BuiltInRegistries.ITEM.getKey(result.getItem()).toString()
            );
            resultObj.addProperty("count", result.getCount());

            json.add("result", resultObj);

            json.addProperty("bowls", bowls);
            json.addProperty("grinds", grinds);
            json.addProperty("experience", experience);
            json.addProperty("color", color);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public net.minecraft.world.item.crafting.RecipeSerializer<?> getType() {
            return ModRecipeSerializers.MORTAR.get();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}