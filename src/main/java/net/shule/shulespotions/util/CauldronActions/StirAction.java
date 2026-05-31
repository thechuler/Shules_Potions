package net.shule.shulespotions.util.CauldronActions;



import net.minecraft.nbt.CompoundTag;
import net.shule.shulespotions.Potions.PotionLiquid;


public class StirAction extends CauldronAction {

    private final StirToolType toolType;

    public StirAction(StirToolType toolType) {
        this.toolType = toolType;
    }

    @Override
    public void apply(CauldronContext ctx) {

        PotionLiquid potion = ctx.getPotion();
        var stats = potion.getStats();


        switch (toolType) {

            case WOOD -> {
                potion.setDuration(potion.getDuration() + 100);

            }

            case GOLD -> {

                stats.setPurity(stats.getPurity() * 2);
                stats.setVitality(stats.getVitality() * 2);
                stats.setFlavor(stats.getFlavor() * 2);
                stats.setStability(stats.getStability() * 2);

            }
        }
        ctx.getCauldron().setPotionLiquid(potion);

    }

    @Override
    public CompoundTag save() {

        CompoundTag tag = new CompoundTag();

        tag.putString("ToolType", toolType.name());

        return tag;
    }

    public static StirAction load(CompoundTag tag) {

        StirToolType type = StirToolType.valueOf(
                tag.getString("ToolType")
        );

        return new StirAction(type);
    }

    @Override
    public String getType() {
        return "stir";
    }
}