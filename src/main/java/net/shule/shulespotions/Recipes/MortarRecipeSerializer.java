package net.shule.shulespotions.Recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

public class MortarRecipeSerializer implements RecipeSerializer<MortarRecipe> {

    @Override
    public MortarRecipe fromJson(ResourceLocation id, JsonObject json) {

        JsonArray ingredientsArray = GsonHelper.getAsJsonArray(json, "ingredients");

        NonNullList<Ingredient> ingredients = NonNullList.create();

        for (JsonElement element : ingredientsArray) {
            ingredients.add(Ingredient.fromJson(element));
        }

        if (ingredients.size() != 3) {
            throw new JsonSyntaxException("Mortar recipes must have exactly 3 ingredients.");
        }

        ItemStack result = ShapedRecipe.itemStackFromJson(
                GsonHelper.getAsJsonObject(json, "result")
        );

        int bowls = GsonHelper.getAsInt(json, "bowls");
        int grinds = GsonHelper.getAsInt(json, "grinds");
        float experience = GsonHelper.getAsFloat(json, "experience");
        String color = GsonHelper.getAsString(json, "color");

        return new MortarRecipe(
                id,
                ingredients,
                result,
                bowls,
                grinds,
                experience,
                color
        );
    }

    @Override
    public @Nullable MortarRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {

        int size = buffer.readVarInt();

        NonNullList<Ingredient> ingredients =
                NonNullList.withSize(size, Ingredient.EMPTY);

        for (int i = 0; i < size; i++) {
            ingredients.set(i, Ingredient.fromNetwork(buffer));
        }

        ItemStack result = buffer.readItem();

        int bowls = buffer.readInt();
        int grinds = buffer.readInt();
        float experience = buffer.readFloat();
        String color = buffer.readUtf();

        return new MortarRecipe(
                id,
                ingredients,
                result,
                bowls,
                grinds,
                experience,
                color
        );
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, MortarRecipe recipe) {

        buffer.writeVarInt(recipe.getIngredients().size());

        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.toNetwork(buffer);
        }

        buffer.writeItem(recipe.getResult());

        buffer.writeInt(recipe.getBowls());
        buffer.writeInt(recipe.getGrinds());
        buffer.writeFloat(recipe.getExperience());
        buffer.writeUtf(recipe.getColor());
    }
}