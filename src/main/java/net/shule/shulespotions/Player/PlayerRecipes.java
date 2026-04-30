package net.shule.shulespotions.Player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashSet;
import java.util.Set;

public class PlayerRecipes implements IPlayerRecipes, INBTSerializable<CompoundTag> {

    private final Set<ResourceLocation> recipes = new HashSet<>();

    @Override
    public Set<ResourceLocation> getRecipes() {
        return recipes;
    }

    @Override
    public boolean hasRecipe(ResourceLocation id) {
        return recipes.contains(id);
    }

    @Override
    public void addRecipe(ResourceLocation id) {
        recipes.add(id);
    }

    public boolean addRecipeIfAbsent(ResourceLocation id) {
        return recipes.add(id);
    }


        //Todos los datos de las recetas se terminan guardando en un NBT, esto despues se sincroniza con el sv
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();

        for (ResourceLocation id : recipes) {
            list.add(StringTag.valueOf(id.toString()));
        }

        tag.put("recipes", list);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        recipes.clear();

        ListTag list = nbt.getList("recipes", 8); // 8 = StringTag

        for (int i = 0; i < list.size(); i++) {
            recipes.add(ResourceLocation.parse(list.getString(i))); // recupero las recetas
        }
    }
}