package net.shule.shulespotions.Items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;
import net.shule.shulespotions.util.CauldronActions.StirAction;
import net.shule.shulespotions.util.CauldronActions.StirToolType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpoonItem extends Item {

    private final StirToolType toolType;
    private final String tooltipId;
    public SpoonItem(StirToolType toolType, Properties properties,String tooltipId) {
        super(properties);
        this.toolType = toolType;
        this.tooltipId = tooltipId;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {

        Level level = ctx.getLevel();

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity be = level.getBlockEntity(ctx.getClickedPos());

        if (!(be instanceof PotionCauldronBE cauldron)) {
            return InteractionResult.PASS;
        }

        if(cauldron.getActions().size() >= cauldron.getMaxIngredients() || cauldron.getActions().isEmpty()){
            return  InteractionResult.PASS;
        }

        StirAction action = new StirAction(toolType);

        cauldron.applyAction(action, ctx.getPlayer());

        level.playSound(
                null,
                ctx.getClickedPos(),
                SoundEvents.HONEY_BLOCK_SLIDE,
                SoundSource.BLOCKS,
                1f,
                1f
        );

        return InteractionResult.SUCCESS;
    }



    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {

        tooltipComponents.add(Component.translatable("tooltip.shulespotions.spoon." + this.tooltipId)
                        .withStyle(ChatFormatting.GRAY));

        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.shulespotions.spoon."
                            + this.tooltipId + ".info").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltipComponents.add(Component.empty());
            tooltipComponents.add(Component.translatable("tooltip.shulespotions.hold_shift").withStyle(ChatFormatting.YELLOW));
        }

        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}