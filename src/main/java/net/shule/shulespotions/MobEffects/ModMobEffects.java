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
            ()-> new ComeBackEffect(MobEffectCategory.BENEFICIAL,0x3ba6bd));

    public static RegistryObject<MobEffect> POTION_SPLASHED = MOB_EFFECTS.register("potion_splashed",
            ()-> new PotionSplashedEffect(MobEffectCategory.HARMFUL,0xc130a3));

    public static RegistryObject<MobEffect> FLY = MOB_EFFECTS.register("fly",
            ()-> new FlyEffect(MobEffectCategory.BENEFICIAL,0xacfdff));

    public static RegistryObject<MobEffect> STONE_CRUSHER = MOB_EFFECTS.register("stone_crusher",
            ()-> new StoneCrusherEffect(MobEffectCategory.BENEFICIAL,0x434343));

    public static RegistryObject<MobEffect> INSTABILITY = MOB_EFFECTS.register("instability",
            ()-> new InstabilityEffect(MobEffectCategory.HARMFUL,0x3d108f));


    public static RegistryObject<MobEffect> ENDER_DISRUPTION = MOB_EFFECTS.register("ender_disruption",
            ()-> new EnderDisruptionEffect(MobEffectCategory.BENEFICIAL,0x3d108f));

    public static RegistryObject<MobEffect> OOZING = MOB_EFFECTS.register("oozing",
            ()-> new OozingEffect(MobEffectCategory.HARMFUL,0x119f62));

    public static RegistryObject<MobEffect> INFESTED = MOB_EFFECTS.register("infested",
            ()-> new InfestedEffect(MobEffectCategory.HARMFUL,0x434343));

    public static RegistryObject<MobEffect> NETHER_CORRUPTION = MOB_EFFECTS.register("nether_corruption",
            ()-> new NetherCorruptionEffect(MobEffectCategory.NEUTRAL,0x434343));

    public static RegistryObject<MobEffect> SHADOW_MANIPULATION = MOB_EFFECTS.register("shadow_manipulation",
            ()-> new ShadowManipulationEffect(MobEffectCategory.BENEFICIAL,0x0f0f0f));

    public static RegistryObject<MobEffect> CLONING = MOB_EFFECTS.register("cloning",
            ()-> new CloningEffect(MobEffectCategory.NEUTRAL,0x17ff00));


    public static RegistryObject<MobEffect> LOW_GRAVITY = MOB_EFFECTS.register("low_gravity",
            ()-> new LowGravityEffect(MobEffectCategory.HARMFUL,0xdf6218));

    public static RegistryObject<MobEffect> HIGH_GRAVITY = MOB_EFFECTS.register("high_gravity",
            ()-> new HighGravityEffect(MobEffectCategory.HARMFUL,0x952fa9));


    public static RegistryObject<MobEffect> CRAFT_MASTER = MOB_EFFECTS.register("craft_master",
                ()-> new CraftMasterEffect(MobEffectCategory.BENEFICIAL,0x454545));

    public static RegistryObject<MobEffect> BUTTER_FINGERS = MOB_EFFECTS.register("butter_fingers",
            ()-> new ButterFingersEffect(MobEffectCategory.HARMFUL,0xfff880));

    public static RegistryObject<MobEffect> FERTILIZER = MOB_EFFECTS.register("fertilizer",
            ()-> new FertilizerEffect(MobEffectCategory.BENEFICIAL,0xfa7de6));

    public static RegistryObject<MobEffect> MIGRAINE = MOB_EFFECTS.register("potion_migraine",
            ()-> new PotionMigraineEffect(MobEffectCategory.BENEFICIAL,0xbe33c5));

    public static RegistryObject<MobEffect> DECAPITATED = MOB_EFFECTS.register("decapitated",
            ()-> new DecapitatedEffect(MobEffectCategory.NEUTRAL,0x454545));


    public static RegistryObject<MobEffect> SWAP = MOB_EFFECTS.register("swap",
            ()-> new SwapEffect(MobEffectCategory.NEUTRAL,0x5c3dcd));

    public static RegistryObject<MobEffect> ORBITAL_GRAVITY = MOB_EFFECTS.register("orbital_gravity",
            ()-> new OrbitalGravityEffect(MobEffectCategory.NEUTRAL,0x119f62));

    public static RegistryObject<MobEffect> EXPLOSIVE_SNEEZE = MOB_EFFECTS.register("explosive_sneeze",
            ()-> new ExplosiveSneezeEffect(MobEffectCategory.HARMFUL,0x34da45));

    public static RegistryObject<MobEffect> FROST_WALK = MOB_EFFECTS.register("frost_walk",
            ()-> new FrostWalkEffect(MobEffectCategory.BENEFICIAL,0x85e9f5));



    public static void register(IEventBus bus) {
        MOB_EFFECTS.register(bus);
    }

}
