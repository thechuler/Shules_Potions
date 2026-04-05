package net.shule.shulespotions.dataGen.Loot;

import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;

public class EntityLootTable extends EntityLootSubProvider {

    public EntityLootTable() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {

    }

/*
    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return InicializarEntidades.ENTIDADES.getEntries().stream().map(RegistryObject::get);
    }
    */

}
