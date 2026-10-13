package net.shule.shulespotions.Potions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.common.brewing.VanillaBrewingRecipe;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BrewingRecipeAutoMapper {

    public static void mapExternalRecipes() {

        mapVanillaPotionMixes();


        mapForgeBrewingRecipes();
    }

    private static void mapVanillaPotionMixes() {
        try {
            List<PotionBrewing.Mix<Potion>> potionMixes = ObfuscationReflectionHelper.getPrivateValue(
                    PotionBrewing.class, null, "f_43494_"
            );
            if (potionMixes == null) return;

            for (PotionBrewing.Mix<Potion> mix : potionMixes) {
                try {

                    ItemStack[] ingredientStacks = mix.ingredient.getItems();
                    if (ingredientStacks.length == 0) continue;

                    Potion fromPotion = mix.from.get();
                    Potion toPotion = mix.to.get();

                    Set<MobEffect> baseEffects = fromPotion.getEffects().stream()
                            .map(MobEffectInstance::getEffect)
                            .collect(Collectors.toSet());

                    List<MobEffectInstance> newEffects = toPotion.getEffects().stream()
                            .filter(inst -> !baseEffects.contains(inst.getEffect()))
                            .toList();

                    if (newEffects.isEmpty()) continue;

                    int color = PotionUtils.getColor(toPotion);
                    registerIngredientItems(ingredientStacks, newEffects, color);
                } catch (Exception e) {
                    System.err.println("Error auto-mapping vanilla potion mix: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to access PotionBrewing.POTION_MIXES: " + e.getMessage());
        }
    }

    private static void mapForgeBrewingRecipes() {
        List<IBrewingRecipe> recipes = BrewingRecipeRegistry.getRecipes();
        List<ItemStack> candidateInputs = null;

        for (IBrewingRecipe recipe : recipes) {
            try {
                if (recipe instanceof VanillaBrewingRecipe) {
                    continue;
                }

                if (recipe instanceof BrewingRecipe brewingRecipe) {
                    ItemStack[] inputs = brewingRecipe.getInput().getItems();
                    ItemStack[] ingredients = brewingRecipe.getIngredient().getItems();

                    if (inputs.length == 0 || ingredients.length == 0) {
                        continue;
                    }

                    ItemStack baseInput = inputs[0];
                    ItemStack output = brewingRecipe.getOutput();

                    processStackRecipe(baseInput, ingredients, output);
                } else {
                    if (candidateInputs == null) {
                        candidateInputs = buildCandidateInputs();
                    }
                    mapCustomIBrewingRecipe(recipe, candidateInputs);
                }
            } catch (Exception e) {
                System.err.println("Error auto-mapping Forge brewing recipe: " + e.getMessage());
            }
        }
    }

    private static void mapCustomIBrewingRecipe(IBrewingRecipe recipe, List<ItemStack> candidateInputs) {
        ItemStack matchedInput = null;
        for (ItemStack candidateInput : candidateInputs) {
            if (recipe.isInput(candidateInput)) {
                matchedInput = candidateInput;
                break;
            }
        }
        if (matchedInput == null) return;

        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (item == Items.AIR || ItemStatRegistry.hasStats(item)) continue;

            ItemStack testIngredient = new ItemStack(item);
            if (recipe.isIngredient(testIngredient)) {
                ItemStack output = recipe.getOutput(matchedInput.copy(), testIngredient);
                if (!output.isEmpty()) {
                    processStackRecipe(matchedInput, new ItemStack[]{testIngredient}, output);
                }
            }
        }
    }

    private static void processStackRecipe(ItemStack baseInput, ItemStack[] ingredients, ItemStack output) {
        if (output == null || output.isEmpty()) return;

        List<MobEffectInstance> outputEffects = PotionUtils.getMobEffects(output);
        if (outputEffects.isEmpty()) return;

        Set<MobEffect> baseEffects = PotionUtils.getMobEffects(baseInput).stream()
                .map(MobEffectInstance::getEffect)
                .collect(Collectors.toSet());

        List<MobEffectInstance> newEffects = outputEffects.stream()
                .filter(inst -> !baseEffects.contains(inst.getEffect()))
                .toList();

        if (newEffects.isEmpty()) return;

        int color = PotionUtils.getColor(output);
        registerIngredientItems(ingredients, newEffects, color);
    }

    private static void registerIngredientItems(ItemStack[] ingredients, List<MobEffectInstance> newEffects, int color) {
        ResourceLocation effectId = ForgeRegistries.MOB_EFFECTS.getKey(newEffects.get(0).getEffect());
        if (effectId == null) return;

        for (ItemStack ingredientStack : ingredients) {
            if (ingredientStack == null || ingredientStack.isEmpty()) continue;

            Item item = ingredientStack.getItem();
            if (ItemStatRegistry.hasStats(item)) continue;

            IngredientStat newStat = new IngredientStat();
            newStat.setColor(color);
            newStat.setEffectWeight(effectId, 100);

            ItemStatRegistry.register(item, newStat);
            System.out.println("Auto-mapped potion ingredient: " + ForgeRegistries.ITEMS.getKey(item) + " -> " + effectId);
        }
    }

    private static List<ItemStack> buildCandidateInputs() {
        List<ItemStack> inputs = new ArrayList<>();
        inputs.add(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD));
        for (Potion potion : ForgeRegistries.POTIONS.getValues()) {
            if (potion != Potions.AWKWARD && potion != Potions.EMPTY) {
                inputs.add(PotionUtils.setPotion(new ItemStack(Items.POTION), potion));
            }
        }
        inputs.add(new ItemStack(Items.GLASS_BOTTLE));
        return inputs;
    }
}
