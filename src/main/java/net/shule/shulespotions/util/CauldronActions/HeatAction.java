package net.shule.shulespotions.util.CauldronActions;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.shule.shulespotions.Blocks.Custom.PotionCauldron;
import net.shule.shulespotions.ShulesPotions;
import net.shule.shulespotions.util.CauldronState;

public class HeatAction implements CauldronAction {
    private final ResourceLocation FRAME = ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID, "item/frame/freeze_action_frame");
    private final int EXPECTED_TEMPERATURE;

    public HeatAction(int expectedTemperature) {
        EXPECTED_TEMPERATURE = expectedTemperature;
    }

    public static FreezeAction fromNetwork(FriendlyByteBuf buffer) {
        int temp = buffer.readInt();
        return new FreezeAction(temp);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(EXPECTED_TEMPERATURE);
    }

    @Override
    public String getType() {
        return "heat";
    }

    @Override
    public void execute(CauldronActionContext ctx) {
        ctx.cauldron.setTemperature(0);
    }

    @Override
    public boolean canTrigger(CauldronActionContext ctx) {
        if (ctx.cauldron.getLevel() == null) return false;

        if (ctx.cauldron.getBlockState().getValue(PotionCauldron.STATE) != CauldronState.HOT) {
            ctx.cauldron.setTemperature(0);
            return false;
        }

        int temp = ctx.cauldron.getTemperature() + 1;
        ctx.cauldron.setTemperature(temp);
        return temp >= EXPECTED_TEMPERATURE;
    }

    @Override
    public CauldronActionTrigger getTrigger() {
        return CauldronActionTrigger.TICK;
    }

    @Override
    public String getActionDescription() {
        return "Heat";
    }

    @Override
    public ItemStack getAsociatedItem() {
        return new ItemStack(Items.BLAZE_POWDER);
    }

    @Override
    public ResourceLocation getFrameResource() {
        return FRAME;
    }
}
