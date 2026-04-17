package net.shule.shulespotions.Recipes.Potion;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.storage.loot.Serializer;
import net.shule.shulespotions.util.CauldronActions.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PotionRecipeSerializer implements RecipeSerializer<PotionRecipe> {
    @Override
    public PotionRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
        JsonArray actionsArray = pSerializedRecipe.getAsJsonArray("actions");
        List<CauldronAction> actions = new ArrayList<>();

        //leemos el campo de actions
        for (JsonElement e : actionsArray) {
            JsonObject obj = e.getAsJsonObject();
            String actionType = obj.get("action").getAsString();

            switch (actionType) {
                case "add_item":
                    actions.add(new AddItemAction(
                            ShapedRecipe.itemStackFromJson(obj.get("item").getAsJsonObject())
                    ));
                    break;

                case "add_liquid":
                    actions.add(new AddLiquidAction(
                            ShapedRecipe.itemStackFromJson(obj.get("item").getAsJsonObject())
                    ));
                    break;

                case "stir":
                    actions.add(new StirAction(
                            ShapedRecipe.itemStackFromJson(obj.get("item").getAsJsonObject())
                    ));
                    break;

                case "freeze":
                    actions.add(new FreezeAction( obj.has("temperature") ? obj.get("temperature").getAsInt() : -100));
                    break;

                case "heat":
                    actions.add(new HeatAction( obj.has("temperature") ? obj.get("temperature").getAsInt() : 100));
                    break;




                //TODO agregar aca el resto de acciones(case) o ver implementacion con codec
            }
        }

        //leemos el campo result
        String effectId = pSerializedRecipe.get("result").getAsString();

        return new PotionRecipe(pRecipeId, actions, effectId);
    }

    @Override
    public @Nullable PotionRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        int size = pBuffer.readInt();

        List<CauldronAction> actions = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            String type = pBuffer.readUtf();

            CauldronAction action = switch (type) {
                case "add_item" -> AddItemAction.fromNetwork(pBuffer);
                case "add_liquid" -> AddLiquidAction.fromNetwork(pBuffer);
                case "stir" -> StirAction.fromNetwork(pBuffer);
                case "freeze" -> FreezeAction.fromNetwork(pBuffer);
                case "heat" -> HeatAction.fromNetwork(pBuffer);
                //agregar el resto de acciones o ver codec
                default -> throw new RuntimeException("Unknown action: " + type);
            };

            actions.add(action);
        }

        String effectId = pBuffer.readUtf();

        return new PotionRecipe(pRecipeId, actions, effectId);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, PotionRecipe pRecipe) {
        //el int que se guarda indica cuantas acciones se guardaron en el buffer
        pBuffer.writeInt(pRecipe.getActions().size());

        for (CauldronAction action : pRecipe.getActions()) {
            pBuffer.writeUtf(action.getType());
            action.toNetwork(pBuffer);
        }

        pBuffer.writeUtf(pRecipe.getEffectId());
    }
}
