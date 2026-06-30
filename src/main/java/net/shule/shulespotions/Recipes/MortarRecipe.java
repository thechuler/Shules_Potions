package net.shule.shulespotions.Recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MortarRecipe implements Recipe<MortarContainer> {

    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;
    private final int bowls;
    private final int grinds;
    private final float experience;
    private final String color;

    public MortarRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients,
                        ItemStack result, int bowls, int grinds, float experience, String color){
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
        this.bowls = bowls;
        this.grinds = grinds;
        this.experience = experience;
        this.color = color;
    }

    @Override
    public boolean matches(MortarContainer container, Level level) {

        List<ItemStack> remaining = new ArrayList<>();

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);

            if (!stack.isEmpty()) {
                remaining.add(stack);
            }
        }

        if (remaining.size() != ingredients.size())
            return false;

        for (Ingredient ingredient : ingredients) {

            boolean found = false;

            Iterator<ItemStack> iterator = remaining.iterator();

            while (iterator.hasNext()) {

                ItemStack stack = iterator.next();

                if (ingredient.test(stack)) {

                    iterator.remove();

                    found = true;

                    break;
                }
            }

            if (!found)
                return false;
        }

        return true;
    }

    @Override
    public ItemStack assemble(MortarContainer pContainer, RegistryAccess pRegistryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.MORTAR.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.MORTAR;
    }

    public int getBowls() {
        return bowls;
    }

    public float getExperience() {
        return experience;
    }

    public int getGrinds() {
        return grinds;
    }

    public ItemStack getResult() {
        return result;
    }

    public String getColor() {
        return color;
    }


}
