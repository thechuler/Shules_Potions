package net.shule.shulespotions.Particles;


import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.ShulesPotions;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ShulesPotions.MODID);

    public static final RegistryObject<SimpleParticleType> BUBBLE =
            PARTICLES.register("bubble", () -> new SimpleParticleType(true));



    public static void register(IEventBus bus) {
        PARTICLES.register(bus);
    }

}
