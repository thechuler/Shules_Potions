package net.shule.shulespotions.MobEffects;

import net.minecraft.world.effect.MobEffect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.MobEffects.Custom.LeechEffect;
import net.shule.shulespotions.ShulesPotions;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ShulesPotions.MODID);


    public static RegistryObject<MobEffect> LEECH = MOB_EFFECTS.register("leech",
            ()-> new LeechEffect(MobEffectCategory.BENEFICIAL,0x668822));


    public static void register(IEventBus bus) {
        MOB_EFFECTS.register(bus);
    }

}
