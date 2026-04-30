package net.shule.shulespotions.Player;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



//Esta clase es la que permite a minecraft guardar las capabilitys (datos de jugador)
public class PlayerRecipesProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final Capability<PlayerRecipes> PLAYER_RECIPES =
            CapabilityManager.get(new CapabilityToken<>() {});

    private final PlayerRecipes backend = new PlayerRecipes();
    private final LazyOptional<PlayerRecipes> optional = LazyOptional.of(() -> backend);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == PLAYER_RECIPES ? optional.cast() : LazyOptional.empty(); //Si tengo capabilitys las devuelvo, si no empty
    }

    @Override
    public CompoundTag serializeNBT() {
        return backend.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        backend.deserializeNBT(nbt);
    }
}