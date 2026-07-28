package net.shule.shulespotions.util.CauldronActions;



import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.Potions.PotionLiquid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


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
                potion.getStats().setDurationSeconds(potion.getStats().getDurationSeconds() + 40);

            }

            case STONE -> {
             stats.setPurity(stats.getPurity() + 20);
            }

            case IRON -> {
                stats.setStability(stats.getStability() + 30);
                stats.setFlavor(stats.getFlavor() - 20);
            }


            case GOLD -> {
                stats.setPurity(stats.getPurity() * 2);
                potion.getStats().setDurationSeconds(potion.getStats().getDurationSeconds() * 2);
                stats.setStability(stats.getStability()-50);
            }

            case DIAMOND -> {
                int purity = stats.getPurity();
                int durationSeg = stats.getDurationSeconds();

                stats.setPurity(purity + durationSeg / 2);
                stats.setDurationSeconds(durationSeg / 2);
            }



            case BASTION -> {

                Map.Entry<ResourceLocation, Integer> strongest =
                        stats.getEffectWeights()
                                .entrySet()
                                .stream()
                                .max(Map.Entry.comparingByValue())
                                .orElse(null);

                Map.Entry<ResourceLocation, Integer> weakest =
                        stats.getEffectWeights()
                                .entrySet()
                                .stream()
                                .min(Map.Entry.comparingByValue())
                                .orElse(null);

                if (strongest != null && weakest != null
                        && !strongest.getKey().equals(weakest.getKey())) {

                    ResourceLocation strongestEffect = strongest.getKey();
                    ResourceLocation weakestEffect = weakest.getKey();

                    int weakestWeight = weakest.getValue();

                    stats.addEffectWeight(weakestEffect, -weakestWeight);

                    stats.addEffectWeight(strongestEffect, weakestWeight);
                }
            }

            case SPONGE -> {
                Map<ResourceLocation, Integer> effects = stats.getEffectWeights();
                List<ResourceLocation> keys = new ArrayList<>(effects.keySet());
                ResourceLocation selected = keys.get(RandomSource.create().nextInt(keys.size()));
                int weight = stats.getEffectWeight(selected);
                stats.addEffectWeight(selected, -weight);
                stats.setStability(stats.getStability() + 70);
            }

            case ENDER -> {

                Map<ResourceLocation, Integer> effects =
                        stats.getEffectWeights();

                if (effects.size() > 1) {

                    List<ResourceLocation> keys =
                            new ArrayList<>(effects.keySet());

                    List<Integer> weights =
                            new ArrayList<>(effects.values());

                    Collections.shuffle(weights);

                    for (int i = 0; i < keys.size(); i++) {
                        stats.setEffectWeight(
                                keys.get(i),
                                weights.get(i)
                        );
                    }
                }
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