package net.shule.shulespotions.Items.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class AlchemistsCaliz extends Item {

    public AlchemistsCaliz(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof PotionCauldronBE cauldron) {
            PotionLiquid liquid = cauldron.getPotionLiquid();
            if (liquid != null && (liquid.getStats().getColor() != -1 || liquid.getStats().getStability() != 0)) {
                ItemStack stack = pContext.getItemInHand();
                CompoundTag tag = stack.getOrCreateTag();
                tag.put("caliz_liquid", liquid.save());
                stack.setTag(tag);
                
                if (level.isClientSide) {
                    pContext.getPlayer().displayClientMessage(Component.literal("§aSample collected!"), true);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.useOn(pContext);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        
        if (stack.hasTag() && stack.getTag().contains("caliz_liquid")) {
            if (pLevel.isClientSide) {
                PotionLiquid liquid = PotionLiquid.load(stack.getTag().getCompound("caliz_liquid"));
                openScreen(liquid);
            }
            return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide());
        }
        
        return super.use(pLevel, pPlayer, pUsedHand);
    }
    
    private void openScreen(PotionLiquid liquid) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            net.minecraft.client.Minecraft.getInstance().setScreen(new net.shule.shulespotions.Screens.CalizScreen(liquid));
        });
    }

}
