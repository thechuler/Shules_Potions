package net.shule.shulespotions.Screens;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.shule.shulespotions.Screens.codex.BaseCodexScreen;

@OnlyIn(Dist.CLIENT)
public class ClientScreenHooks {

    public static void openCodexScreen() {
        Minecraft.getInstance().setScreen(new BaseCodexScreen());
    }

    public static void openRecipeScrollScreen(ItemStack stack) {
        Minecraft.getInstance().setScreen(new RecipeScrollScreen(stack));
    }
}
