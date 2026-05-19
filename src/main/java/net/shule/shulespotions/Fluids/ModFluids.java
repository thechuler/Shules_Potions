package net.shule.shulespotions.Fluids;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Blocks.ModBlocks;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.ShulesPotions;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, ShulesPotions.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_POTION_FLUID = FLUIDS.register("potion_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.POTION_FLUID_PROPERTIES));

    public static final RegistryObject<FlowingFluid> FLOWING_POTION_FLUID = FLUIDS.register("flowing_potion_fluid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.POTION_FLUID_PROPERTIES));


    public static final ForgeFlowingFluid.Properties POTION_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.POTION_FLUID_TYPE, SOURCE_POTION_FLUID, FLOWING_POTION_FLUID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.POTION_FLUID_BLOCK)
            .bucket(ModItems.POTION_BARREL);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}