package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.shule.shulespotions.Entities.ModEntities;
import net.shule.shulespotions.Entities.entity.PlayerCloneEntity;
import org.jetbrains.annotations.Nullable;

public class CloningEffect extends MobEffect {
    public CloningEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }


    @Override
    public boolean isInstantenous() {
        return true;
    }


    @Override
    public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, LivingEntity pLivingEntity, int pAmplifier, double pHealth) {

        Level level = pLivingEntity.level();
        if(level.isClientSide) return;
        
        Entity createdEntity;

        if (pLivingEntity.getPersistentData().getBoolean("shulespotions:is_clone"))
            return;

        if (pLivingEntity instanceof Player player) {
           PlayerCloneEntity playerClone = ModEntities.PLAYER_CLONE.get().create(level);
            
            if (playerClone != null) {
                playerClone.setClonedPlayerUUID(player.getUUID()); // Textura
                playerClone.tame(player); // Dueño
                
                // Etiqueta de nombre visible
                playerClone.setCustomName(net.minecraft.network.chat.Component.translatable("entity.shulespotions.player_clone_name", player.getName()));
                playerClone.setCustomNameVisible(true);
                
                // Copiar equipo (armadura e ítems en las manos)
                for (net.minecraft.world.entity.EquipmentSlot slot : net.minecraft.world.entity.EquipmentSlot.values()) {
                    playerClone.setItemSlot(slot, player.getItemBySlot(slot).copy());
                }
            }
            createdEntity = playerClone;
        } else {
            // Es un mob normal: Clon genérico
            createdEntity = pLivingEntity.getType().create(level);
        }

        if (createdEntity instanceof LivingEntity clone) {
            
            // Etiquetamos a la entidad como un clon para poder renderizarle el overlay verde después
            clone.getPersistentData().putBoolean("shulespotions:is_clone", true);
            
            clone.moveTo(
                pLivingEntity.getX(), 
                pLivingEntity.getY(), 
                pLivingEntity.getZ(), 
                pLivingEntity.getYRot(), 
                pLivingEntity.getXRot()
            );
            
            if (level.addFreshEntity(clone)) {
                
                if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                    
                    // Enviar el paquete para sincronizar al cliente que esta entidad es un clon
                    net.shule.shulespotions.Messages.ModMessages.INSTANCE.send(
                        net.minecraftforge.network.PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> clone),
                        new net.shule.shulespotions.Messages.SyncCloneStatusPacket(clone.getId(), true)
                    );
                    

                    int color = this.getColor();
                    double r = ((color >> 16) & 0xFF) / 255.0;
                    double g = ((color >> 8) & 0xFF) / 255.0;
                    double b = (color & 0xFF) / 255.0;
                    

                    for (int i = 0; i < 40; i++) {

                        double px = clone.getX() + (level.random.nextDouble() - 0.5) * clone.getBbWidth() * 2.5;
                        double py = clone.getY() + level.random.nextDouble() * clone.getBbHeight() * 1.5;
                        double pz = clone.getZ() + (level.random.nextDouble() - 0.5) * clone.getBbWidth() * 2.5;
                        
                        serverLevel.sendParticles(
                            net.shule.shulespotions.Particles.ModParticles.POTION_EXPLOTION.get(), 
                            px, py, pz, 
                            0,
                            r, g, b, 
                            1.0
                        );
                    }
                }
            }
        }
    }
}
