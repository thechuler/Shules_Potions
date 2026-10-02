package net.shule.shulespotions.Items.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class LootBundleItem extends Item {


    private final ResourceLocation lootTableId;

    public LootBundleItem(Properties properties, ResourceLocation lootTableId) {
        super(properties);
        this.lootTableId = lootTableId;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {


            int amountToOpen = player.isCrouching() ? itemStack.getCount() : 1;

            LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(this.lootTableId);

            LootParams.Builder builder = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.THIS_ENTITY, player)
                    .withParameter(LootContextParams.ORIGIN, player.position());

            LootParams lootParams = builder.create(LootContextParamSets.GIFT);


            for (int i = 0; i < amountToOpen; i++) {
                ObjectArrayList<ItemStack> generatedLoot = lootTable.getRandomItems(lootParams);
                int attempts = 0;
                while (generatedLoot.isEmpty() && attempts < 20) {
                    generatedLoot = lootTable.getRandomItems(lootParams);
                    attempts++;
                }

                for (ItemStack lootStack : generatedLoot) {
                    if (!player.getInventory().add(lootStack)) {
                        player.drop(lootStack, false);
                    }
                }
            }


            if (!player.getAbilities().instabuild) {
                itemStack.shrink(amountToOpen);
            }
            

            serverLevel.playSound(null, player.blockPosition(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 1.0F, 1.0F + level.random.nextFloat() * 0.2F);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}