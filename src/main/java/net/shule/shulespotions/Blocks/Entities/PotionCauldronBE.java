package net.shule.shulespotions.Blocks.Entities;


import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.shule.shulespotions.Blocks.Custom.PotionCauldron;
import net.shule.shulespotions.Blocks.ModBlockEntities;
import net.shule.shulespotions.Fluids.ModFluids;
import net.shule.shulespotions.Fluids.PotionFluidHelper;
import net.shule.shulespotions.Particles.ModParticles;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.Potions.PotionLiquidUtils;
import net.shule.shulespotions.StabilityEvent.Events.PotionExplodeEvent;
import net.shule.shulespotions.StabilityEvent.StabilityEvent;
import net.shule.shulespotions.util.CauldronActions.AddIngredientAction;
import net.shule.shulespotions.util.CauldronActions.CauldronAction;
import net.shule.shulespotions.util.CauldronActions.CauldronActionRegistry;
import net.shule.shulespotions.util.CauldronActions.CauldronContext;
import net.shule.shulespotions.util.ColorUtils;
import org.jetbrains.annotations.NotNull;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

import static net.shule.shulespotions.util.ColorUtils.*;

public class PotionCauldronBE extends BlockEntity {

    public static final int INSTABILITY_PARTICLES_EVENT = 1;

    private final List<CauldronAction> actions = new ArrayList<>();
    private int renderColor;
    private int startColor;
    private int targetColor;
    private float lerpProgress = 1.0f;
    private int explosionTriggerId = 0;
    private int lastExplosionTriggerId = 0;
    private int explosionColor = 0;
    private boolean isFirstLoad = true;
    public enum CauldronState { IDLE, BREWING, FINISHED, RUINED }
    private CauldronState state = CauldronState.IDLE;
    private List<MobEffect> effects = new ArrayList<>();


    private final FluidTank tank = new FluidTank(1000) {
        @Override
        protected void onContentsChanged() {
            if (isEmpty()) {
                if (!actions.isEmpty()) {
                    actions.clear();
                }
                state = CauldronState.IDLE;
                effects.clear();
            }
            setChanged();

            if (level != null && !level.isClientSide) {
                sync();
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            Fluid fluid = stack.getFluid();
            return fluid == Fluids.WATER ||
                    fluid == ModFluids.SOURCE_POTION_FLUID.get();
        }

    };


    private final LazyOptional<IFluidHandler> fluidHandler =
            LazyOptional.of(() -> tank);


    public PotionCauldronBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POTION_CAULDRON_BE.get(), pos, state);
        renderColor = randomColor();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public net.minecraft.world.phys.AABB getRenderBoundingBox() {
        return new net.minecraft.world.phys.AABB(worldPosition).inflate(256.0D);
    }


    public void checkItem(ItemStack item) {

        if (actions.size() >= getMaxIngredients()) return;

        if (this.getState() == CauldronState.FINISHED) return;
        if (tank.isEmpty()) return;
        

        FluidStack fluid = tank.getFluid();

        if (fluid.getFluid() == Fluids.WATER) {

            FluidStack potionFluid = new FluidStack(
                    ModFluids.SOURCE_POTION_FLUID.get(),
                    fluid.getAmount()
            );

            PotionFluidHelper.withPotionLiquid(
                    potionFluid,
                    createInitialPotionLiquid()
            );

            tank.setFluid(potionFluid);
        }

        if (state == CauldronState.IDLE) {
            state = CauldronState.BREWING;
        }

        applyAction(new AddIngredientAction(item.getItem()), null);
    }


    protected PotionLiquid createInitialPotionLiquid() {
        PotionLiquid potion = new PotionLiquid();

        potion.getStats().setStability(100);
        potion.getStats().setDurationTicks(100);

        return potion;
    }

    public void applyAction(CauldronAction action, @Nullable Player player) {

        CauldronContext ctx = new CauldronContext(this, player);

        action.apply(ctx);

        actions.add(action);

        setChanged();
        sync();
    }




    public PotionLiquid getPotionLiquid() {
        return PotionFluidHelper.getPotionLiquid(this.tank.getFluid());
    }


    public void setPotionLiquid(PotionLiquid potion) {

        FluidStack fluid = this.tank.getFluid();


        PotionFluidHelper.withPotionLiquid(fluid, potion);

        startColorTransition(potion.getStats().getColor());


        sync();
        setChanged();
    }





    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        assert pkt.getTag() != null;
        this.load(pkt.getTag());
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }


    public void sync() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }


    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }


    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {

        super.saveAdditional(tag);

        tag.put("SPTank", tank.writeToNBT(new CompoundTag()));
        tag.putInt("SPcolor", renderColor);
        tag.putInt("ExplosionTriggerId", explosionTriggerId);
        tag.putInt("ExplosionColor", explosionColor);
        tag.putString("CauldronState", state.name());
        ListTag actionList = new ListTag();

        for (CauldronAction action : actions) {

            CompoundTag actionTag = new CompoundTag();

            actionTag.putString("Type", action.getType());

            actionTag.put("Data", action.save());

            actionList.add(actionTag);
        }

        tag.put("Actions", actionList);

        if (effects != null) {
            ListTag effectsTag = new ListTag();
            for (MobEffect effect : effects) {
                net.minecraft.resources.ResourceLocation id = net.minecraftforge.registries.ForgeRegistries.MOB_EFFECTS.getKey(effect);
                if (id != null) {
                    effectsTag.add(net.minecraft.nbt.StringTag.valueOf(id.toString()));
                }
            }
            tag.put("Effects", effectsTag);
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {

        super.load(tag);

        tank.readFromNBT(tag.getCompound("SPTank"));
        explosionTriggerId = tag.getInt("ExplosionTriggerId");
        explosionColor = tag.getInt("ExplosionColor");
        if (tag.contains("CauldronState")) {
            state = CauldronState.valueOf(tag.getString("CauldronState"));
        } else if (tag.contains("IsRecipeFinished")) {
            state = tag.getBoolean("IsRecipeFinished") ? CauldronState.FINISHED : CauldronState.BREWING;
        }
        renderColor = tag.getInt("SPcolor");


        actions.clear();

        if (tag.contains("Actions")) {

            ListTag actionList =
                    tag.getList("Actions", Tag.TAG_COMPOUND);

            for (int i = 0; i < actionList.size(); i++) {

                CompoundTag actionTag =
                        actionList.getCompound(i);

                String type =
                        actionTag.getString("Type");

                CompoundTag data =
                        actionTag.getCompound("Data");

                CauldronAction action = CauldronActionRegistry.load(type, data);
                actions.add(action);
            }
        }

        if (tag.contains("Effects")) {
            effects = new ArrayList<>();
            ListTag effectsTag = tag.getList("Effects", Tag.TAG_STRING);
            for (int i = 0; i < effectsTag.size(); i++) {
                String idStr = effectsTag.getString(i);
                net.minecraft.resources.ResourceLocation id = net.minecraft.resources.ResourceLocation.tryParse(idStr);
                if (id == null) continue;
                
                MobEffect effect = net.minecraftforge.registries.ForgeRegistries.MOB_EFFECTS.getValue(id);
                if (effect != null) {
                    effects.add(effect);
                }
            }
        }
    }


    public int getMaxIngredients() {
        if (this.level == null) return 0;

        BlockState state = this.getBlockState();
        if (state.getBlock() instanceof PotionCauldron cauldron) {
            return cauldron.getMAX_INGREDIENT_COUNT();
        }

        return 0;
    }

    public static void tick(Level level, PotionCauldronBE be) {
        if (level.isClientSide) return;

        if (be.lerpProgress < 1.0f) {
            be.lerpProgress += 0.05f;

            if (be.lerpProgress > 1.0f) be.lerpProgress = 1.0f;

            be.renderColor = lerpColor(be.startColor, be.targetColor, be.lerpProgress);

            be.setChanged();
            be.sync();
        }


    }


   ;
    public void clientTick() {

        assert level != null;
        if (!level.isClientSide)
            return;

        if (isFirstLoad) {
            lastExplosionTriggerId = explosionTriggerId;
            isFirstLoad = false;
        }

        if (explosionTriggerId != lastExplosionTriggerId) {
            lastExplosionTriggerId = explosionTriggerId;
            spawnPotionExplosionParticles(explosionColor);
        }

        if(this.getTank().isEmpty()) {
            return;
        }

        if (this.getTank().getFluid().getFluid() == Fluids.WATER) {
            return;
        }

        if(this.getState() == CauldronState.FINISHED){
            return;
        }

        int color = getRenderColor();
        float[] rgb = intToRGB(color);

        float chance = 0.4f;

        if (this.level.random.nextFloat() < chance) {
            double offsetX = (this.level.random.nextDouble() - 0.5) * 0.5;
            double offsetZ = (this.level.random.nextDouble() - 0.5) * 0.5;

            PotionLiquid potion = this.getPotionLiquid();
            int stability = potion.getStats().getStability();
            
            float instabilityChance = Math.max(0.0f, (100.0f - stability) / 100.0f);

            net.minecraft.core.particles.SimpleParticleType particleType = 
                    this.level.random.nextFloat() < instabilityChance 
                            ? ModParticles.INSTABILITY_BUBBLE.get() 
                            : ModParticles.BUBBLE.get();

            this.level.addParticle(
                    particleType,
                    this.getBlockPos().getX() + 0.5 + offsetX,
                    this.getBlockPos().getY() + 1.0,
                    this.getBlockPos().getZ() + 0.5 + offsetZ,
                    rgb[0],
                    rgb[1],
                    rgb[2]
            );
        }


    }


    public void triggerExplosionParticles(int color) {

        if (level == null || level.isClientSide)
            return;

        explosionTriggerId++;
        explosionColor = color;

        setChanged();
        sync();
    }

    private void spawnPotionExplosionParticles(int color) {

        if (level == null || !level.isClientSide)
            return;

        float[] rgb = ColorUtils.intToRGB(color);

        RandomSource random = level.getRandom();

        double centerX = worldPosition.getX() + 0.5;
        double centerY = worldPosition.getY() + 1.0;
        double centerZ = worldPosition.getZ() + 0.5;
/*
        net.minecraftforge.fml.DistExecutor.unsafeRunWhenOn(net.minecraftforge.api.distmarker.Dist.CLIENT, () -> () ->
                net.shule.shulespotions.Events.CameraShakeHandler.triggerShake(centerX, centerY, centerZ, 25, 2.0f));
*/
        int particleCount = 200;

        for (int i = 0; i < particleCount; i++) {

            double angle = random.nextDouble() * Math.PI * 2.0;

            double radius = random.nextDouble() * 2.5;

            double offsetX = Math.cos(angle) * radius;
            double offsetZ = Math.sin(angle) * radius;

            double offsetY = random.nextDouble() * 1.8;

            level.addParticle(
                    ModParticles.POTION_EXPLOTION.get(),
                    centerX + offsetX,
                    centerY + offsetY,
                    centerZ + offsetZ,
                    rgb[0],
                    rgb[1],
                    rgb[2]
            );
        }
    }

    public void startColorTransition(int newColor) {
        this.startColor = this.renderColor;
        this.targetColor = newColor;
        this.lerpProgress = 0.0f;
    }

    public int getRenderColor() {
        return this.renderColor;
    }

    public void setRenderColor(int renderColor) {
        this.renderColor = renderColor;
        setChanged();
        sync();
    }

    public List<CauldronAction> getActions() {
        return actions;
    }

    public FluidTank getTank() {
        return tank;
    }

    public CauldronState getState() {
        return state;
    }

    public void setState(CauldronState state) {
        this.state = state;
    }

    public List<MobEffect> getEffects() {
        return effects;
    }



    public void FinishBrewing(){
        if (this.state == CauldronState.FINISHED) {
            return;
        }
        
        PotionLiquid potionLiquid = this.getPotionLiquid();
        int stability = potionLiquid.getStats().getStability();
        float chance = Math.max(0, Math.min(100, (50 - stability) * 2));
        
        if (level != null && !level.isClientSide) {
            CauldronContext ctx = new CauldronContext(this, null);
            StabilityEvent event = new PotionExplodeEvent();
            
            if (level.random.nextFloat() * 100 < chance) {
                event.trigger(ctx);
                return;
            }
            
            this.effects = PotionLiquidUtils.resolve(potionLiquid);
            this.state = CauldronState.FINISHED;
            
            level.playSound(
                    null,
                    worldPosition,
                    net.minecraft.sounds.SoundEvents.BREWING_STAND_BREW,
                    net.minecraft.sounds.SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );
            
            setChanged();
            sync();
        }
    }



}