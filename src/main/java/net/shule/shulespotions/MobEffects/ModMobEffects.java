package net.shule.shulespotions.MobEffects;

import net.minecraft.world.effect.MobEffect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.MobEffects.Custom.*;
import net.shule.shulespotions.ShulesPotions;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ShulesPotions.MODID);


    public static RegistryObject<MobEffect> LEECH = MOB_EFFECTS.register("leech",
            ()-> new LeechEffect(MobEffectCategory.BENEFICIAL,0x281043));


    public static RegistryObject<MobEffect> COMEBACK = MOB_EFFECTS.register("comeback",
            ()-> new ComeBackEffect(MobEffectCategory.BENEFICIAL,0x281043));

    public static RegistryObject<MobEffect> POTION_SPLASHED = MOB_EFFECTS.register("potion_splashed",
            ()-> new PotionSplashedEffect(MobEffectCategory.HARMFUL,0xc130a3));

    public static RegistryObject<MobEffect> FLY = MOB_EFFECTS.register("fly",
            ()-> new PotionSplashedEffect(MobEffectCategory.BENEFICIAL,0xacfdff));

    public static RegistryObject<MobEffect> STONE_CRUSHER = MOB_EFFECTS.register("stone_crusher",
            ()-> new PotionSplashedEffect(MobEffectCategory.BENEFICIAL,0xacfdff));

    public static RegistryObject<MobEffect> INSTABILITY = MOB_EFFECTS.register("instability",
            ()-> new InstabilityEffect(MobEffectCategory.HARMFUL,0x3d108f));


    public static RegistryObject<MobEffect> ENDER_DISRUPTION = MOB_EFFECTS.register("ender_disruption",
            ()-> new EnderDisruptionEffect(MobEffectCategory.BENEFICIAL,0x3d108f));

    public static RegistryObject<MobEffect> OOZING = MOB_EFFECTS.register("oozing",
            ()-> new OozingEffect(MobEffectCategory.HARMFUL,0x3d108f));

    public static RegistryObject<MobEffect> INFESTED = MOB_EFFECTS.register("infested",
            ()-> new InfestedEffect(MobEffectCategory.HARMFUL,0x434343));

    public static RegistryObject<MobEffect> NETHER_CORRUPTION = MOB_EFFECTS.register("nether_corruption",
            ()-> new NetherCorruptionEffect(MobEffectCategory.NEUTRAL,0x434343));

    public static RegistryObject<MobEffect> SHADOW_MANIPULATION = MOB_EFFECTS.register("shadow_manipulation",
            ()-> new ShadowManipulationEffect(MobEffectCategory.BENEFICIAL,0x0f0f0f));

    public static RegistryObject<MobEffect> CLONING = MOB_EFFECTS.register("cloning",
            ()-> new CloningEffect(MobEffectCategory.NEUTRAL,0x17ff00));

    public static void register(IEventBus bus) {
        MOB_EFFECTS.register(bus);
    }

}
