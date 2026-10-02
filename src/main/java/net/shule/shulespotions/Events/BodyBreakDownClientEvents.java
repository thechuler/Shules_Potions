package net.shule.shulespotions.Events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.Input;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.shule.shulespotions.Entities.entity.PlayerHeadEntity;
import net.shule.shulespotions.Messages.Custom.ControlHeadPacket;
import net.shule.shulespotions.Messages.ModMessages;
import net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.Renders.BodyBreakDownArmorLayer;
import net.shule.shulespotions.Renders.BodyBreakDownCustomHeadLayer;
import net.shule.shulespotions.ShulesPotions;

import java.lang.reflect.Field;
import java.util.List;

@Mod.EventBusSubscriber(modid = ShulesPotions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BodyBreakDownClientEvents {

    public static int pendingHeadEntityId = -1;

    public static boolean isEffectActiveLocally(Minecraft mc) {
        if (mc.player == null) return false;
        if (mc.cameraEntity instanceof PlayerHeadEntity) return true;
        if (mc.player.hasEffect(ModMobEffects.BODY_BREAK_DOWN.get())) return true;
        if (mc.player.getPersistentData().contains(BodyBreakDownEffect.NBT_MISSING_LIMBS)) {
            return mc.player.getPersistentData().getByte(BodyBreakDownEffect.NBT_MISSING_LIMBS) != 0;
        }
        return false;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        if (isEffectActiveLocally(mc) && mc.screen instanceof AbstractContainerScreen) {
            mc.setScreen(null);
            if (mc.player != null) mc.player.closeContainer();
        }

        if (mc.player != null && mc.player.isDeadOrDying() && mc.cameraEntity != mc.player) {
            mc.setCameraEntity(mc.player);
        }

        if (mc.cameraEntity != null && mc.cameraEntity != mc.player && mc.cameraEntity.isRemoved()) {
            mc.setCameraEntity(mc.player);
        }

        if (pendingHeadEntityId != -1) {
            Entity head = mc.level.getEntity(pendingHeadEntityId);
            if (head != null) {
                mc.setCameraEntity(head);
                pendingHeadEntityId = -1;
            }
        } else if (mc.cameraEntity == mc.player && mc.player.getPersistentData().contains("shulespotions:missing_limbs")) {
            byte mask = mc.player.getPersistentData().getByte("shulespotions:missing_limbs");
            if ((mask & 1) != 0) {
                for (Entity entity : mc.level.entitiesForRendering()) {
                    if (entity instanceof PlayerHeadEntity head && mc.player.getUUID().equals(head.getPlayerUUID())) {
                        mc.setCameraEntity(head);
                        break;
                    }
                }
            }
        }

        if (mc.cameraEntity instanceof PlayerHeadEntity head) {
            head.setLocallyControlled(true);
            float playerYaw = mc.player.getYRot();
            float playerPitch = mc.player.getXRot();

            float forward = (mc.options.keyUp.isDown() ? 1.0F : 0.0F) - (mc.options.keyDown.isDown() ? 1.0F : 0.0F);
            float strafe = (mc.options.keyLeft.isDown() ? 1.0F : 0.0F) - (mc.options.keyRight.isDown() ? 1.0F : 0.0F);
            boolean jumping = mc.options.keyJump.isDown();

            float f = strafe * strafe + forward * forward;
            boolean hasInput = f >= 1.0E-4F;
            float forwardNorm = 0.0F;
            float strafeNorm = 0.0F;
            float sin = Mth.sin(playerYaw * ((float) Math.PI / 180F));
            float cos = Mth.cos(playerYaw * ((float) Math.PI / 180F));

            if (hasInput) {
                f = Mth.sqrt(f);
                if (f < 1.0F) f = 1.0F;
                forwardNorm = forward / f;
                strafeNorm = strafe / f;
            }

            Vec3 currentMotion = head.getDeltaMovement();
            if (jumping) {
                if (head.onGround()) {
                    double jumpY = 0.30D;
                    if (hasInput) {
                        double hopSpeed = 0.14D;
                        double mx = (strafeNorm * cos - forwardNorm * sin) * hopSpeed;
                        double mz = (forwardNorm * cos + strafeNorm * sin) * hopSpeed;
                        head.setDeltaMovement(mx, jumpY, mz);
                    } else {
                        head.setDeltaMovement(currentMotion.x, jumpY, currentMotion.z);
                    }
                } else if (head.isInWater()) {
                    double swimSpeed = hasInput ? 0.08D : 0.0D;
                    double mx = hasInput ? (strafeNorm * cos - forwardNorm * sin) * swimSpeed : currentMotion.x;
                    double mz = hasInput ? (forwardNorm * cos + strafeNorm * sin) * swimSpeed : currentMotion.z;
                    head.setDeltaMovement(mx, 0.08D, mz);
                }
            } else if (hasInput && head.onGround()) {
                double crawlSpeed = 0.10D;
                double mx = (strafeNorm * cos - forwardNorm * sin) * crawlSpeed;
                double mz = (forwardNorm * cos + strafeNorm * sin) * crawlSpeed;
                head.setDeltaMovement(mx, currentMotion.y, mz);
            }

            head.xRotO = head.getXRot();
            head.yRotO = head.getYRot();
            head.setYRot(playerYaw);
            head.setYHeadRot(playerYaw);
            head.setYBodyRot(playerYaw);
            head.setXRot(playerPitch);

            if (forward != 0.0F || strafe != 0.0F || jumping || mc.player.tickCount % 5 == 0) {
                ModMessages.INSTANCE.sendToServer(new ControlHeadPacket(
                        head.getId(), forward, strafe, jumping, playerYaw, playerPitch
                ));
            }
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (mc.cameraEntity instanceof PlayerHeadEntity) {
            event.setCanceled(true);
            return;
        }

        if (mc.player.getPersistentData().contains("shulespotions:missing_limbs")) {
            byte missingLimbs = mc.player.getPersistentData().getByte("shulespotions:missing_limbs");
            if (missingLimbs > 0) {
                if ((missingLimbs & 1) != 0) {
                    event.setCanceled(true);
                    return;
                }

                if ((missingLimbs & 2) != 0 && event.getHand() == InteractionHand.MAIN_HAND) {
                    event.setCanceled(true);
                    return;
                }

                if ((missingLimbs & 4) != 0 && event.getHand() == InteractionHand.OFF_HAND) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.cameraEntity instanceof PlayerHeadEntity head && mc.player != null) {
            event.setYaw(mc.player.getYRot());
            event.setPitch(mc.player.getXRot());
            event.setRoll(0.0F);
        }
    }

    @SubscribeEvent
    public static void onComputeFov(ViewportEvent.ComputeFov event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.cameraEntity instanceof PlayerHeadEntity) {
            event.setFOV(mc.options.fov().get());
        }
    }

    @SubscribeEvent
    public static void onMovementInput(MovementInputUpdateEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.cameraEntity instanceof PlayerHeadEntity) {
            Input input = event.getInput();
            input.forwardImpulse = 0.0F;
            input.leftImpulse = 0.0F;
            input.up = false;
            input.down = false;
            input.left = false;
            input.right = false;
            input.jumping = false;
            input.shiftKeyDown = false;
        }
    }

    @SubscribeEvent
    public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
        LivingEntity entity = event.getEntity();
        if (entity.getPersistentData().contains("shulespotions:missing_limbs")) {
            byte missingLimbs = entity.getPersistentData().getByte("shulespotions:missing_limbs");
            if (missingLimbs > 0) {
                EntityModel<?> model = event.getRenderer().getModel();
                if (model instanceof HumanoidModel<?> humanoid) {
                    if ((missingLimbs & 1) != 0) {
                        humanoid.head.visible = false;
                        humanoid.hat.visible = false;
                    }
                    if ((missingLimbs & 2) != 0) {
                        humanoid.rightArm.visible = false;
                        if (model instanceof PlayerModel<?> playerModel) {
                            playerModel.rightSleeve.visible = false;
                        }
                    }
                    if ((missingLimbs & 4) != 0) {
                        humanoid.leftArm.visible = false;
                        if (model instanceof PlayerModel<?> playerModel) {
                            playerModel.leftSleeve.visible = false;
                        }
                    }
                    if ((missingLimbs & 8) != 0) {
                        humanoid.rightLeg.visible = false;
                        if (model instanceof PlayerModel<?> playerModel) {
                            playerModel.rightPants.visible = false;
                        }
                    }
                    if ((missingLimbs & 16) != 0) {
                        humanoid.leftLeg.visible = false;
                        if (model instanceof PlayerModel<?> playerModel) {
                            playerModel.leftPants.visible = false;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        if (entity.getPersistentData().contains("shulespotions:missing_limbs")) {
            EntityModel<?> model = event.getRenderer().getModel();
            if (model instanceof HumanoidModel<?> humanoid) {
                humanoid.head.visible = true;
                humanoid.hat.visible = true;
                humanoid.rightArm.visible = true;
                humanoid.leftArm.visible = true;
                humanoid.rightLeg.visible = true;
                humanoid.leftLeg.visible = true;
                if (model instanceof PlayerModel<?> playerModel) {
                    playerModel.rightSleeve.visible = true;
                    playerModel.leftSleeve.visible = true;
                    playerModel.rightPants.visible = true;
                    playerModel.leftPants.visible = true;
                }
            }
        }
    }

    public static <T extends LivingEntity, M extends HumanoidModel<T>> void hookPlayerArmorLayers(
            LivingEntityRenderer<T, M> renderer, EntityRendererProvider.Context context) {
        try {
            Field layersField = ObfuscationReflectionHelper.findField(LivingEntityRenderer.class, "f_115291_");
            layersField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<RenderLayer<T, M>> layers = (List<RenderLayer<T, M>>) layersField.get(renderer);

            for (int i = 0; i < layers.size(); i++) {
                RenderLayer<T, M> layer = layers.get(i);
                if (layer instanceof HumanoidArmorLayer && !(layer instanceof BodyBreakDownArmorLayer)) {
                    HumanoidModel<T> innerModel = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
                    HumanoidModel<T> outerModel = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
                    layers.set(i, new BodyBreakDownArmorLayer<>(renderer, innerModel, outerModel, context.getModelManager()));
                } else if (layer instanceof CustomHeadLayer && !(layer instanceof BodyBreakDownCustomHeadLayer)) {
                    layers.set(i, new BodyBreakDownCustomHeadLayer<>(renderer, context.getModelSet(), context.getItemInHandRenderer()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event) {
        Minecraft mc = Minecraft.getInstance();
        if (isEffectActiveLocally(mc)) {
            if (event.getScreen() instanceof AbstractContainerScreen) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (isEffectActiveLocally(mc)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (isEffectActiveLocally(mc)) {
            if (event.isAttack() || event.isUseItem()) {
                event.setCanceled(true);
                event.setSwingHand(false);
            }
        }
    }

    @SubscribeEvent
    public static void onClientTickPre(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getInstance();
        if (isEffectActiveLocally(mc)) {
            while (mc.options.keyAttack.consumeClick()) {}
            while (mc.options.keyUse.consumeClick()) {}
            while (mc.options.keyDrop.consumeClick()) {}
            while (mc.options.keySwapOffhand.consumeClick()) {}
            while (mc.options.keyInventory.consumeClick()) {}
            for (var key : mc.options.keyHotbarSlots) {
                while (key.consumeClick()) {}
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay().id().equals(VanillaGuiOverlay.HOTBAR.id())) {
            Minecraft mc = Minecraft.getInstance();
            if (isEffectActiveLocally(mc)) {
                int width = event.getWindow().getGuiScaledWidth();
                int height = event.getWindow().getGuiScaledHeight();
                int x = width / 2 - 91;
                int y = height - 22;

                event.getGuiGraphics().fill(x, y, x + 182, y + 22, 0x88AA0000);
            }
        }
    }
}
