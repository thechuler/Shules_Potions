package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;


import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;

import net.shule.shulespotions.Particles.ModParticles;
import org.jetbrains.annotations.Nullable;

import static net.shule.shulespotions.util.ColorUtils.intToRGB;


public class PotionCauldron extends BaseEntityBlock {

    private final int MAX_INGREDIENT_COUNT;
    private final int MAX_LIQUID_LEVEL;


    private static final VoxelShape INSIDE = box(2.0D, 3.0D, 2.0D, 14.0D, 15.0D, 14.0D);

    protected static final VoxelShape SHAPE = Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE);

    public PotionCauldron(Properties pProperties, int maxIngredientCount, int maxLiquidLevel) {
        super(pProperties);
        MAX_INGREDIENT_COUNT = maxIngredientCount;
        MAX_LIQUID_LEVEL = maxLiquidLevel;

    }



    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PotionCauldronBE(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(Items.WATER_BUCKET)) {

            if (!level.isClientSide()) {

                BlockEntity be = level.getBlockEntity(pos);

                if (be instanceof PotionCauldronBE cauldron) {

                    FluidStack water = new FluidStack(
                            Fluids.WATER,
                            1000
                    );

                    int filled = cauldron.getTank().fill(
                            water,
                            IFluidHandler.FluidAction.EXECUTE
                    );

                    if (filled == 1000) {

                        if (!player.getAbilities().instabuild) {
                            player.setItemInHand(
                                    hand,
                                    new ItemStack(Items.BUCKET)
                            );
                        }

                        cauldron.setChanged();
                        cauldron.sync();

                        level.playSound(
                                null,
                                pos,
                                SoundEvents.BUCKET_FILL,
                                SoundSource.BLOCKS,
                                1.0F,
                                1.0F
                        );
                    }
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }


    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) return;

        if (!(entity instanceof ItemEntity itemEntity)) return;

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof PotionCauldronBE cauldron) {

            if(cauldron.getTank().isEmpty() || cauldron.getIngredients().size() >= MAX_INGREDIENT_COUNT) return;
            ItemStack stack = itemEntity.getItem();
            cauldron.checkItem(stack);
            level.playSound(
                    null,
                    pos,
                    SoundEvents.AMBIENT_UNDERWATER_ENTER,
                    SoundSource.BLOCKS,
                    0.6F,
                    2F + level.random.nextFloat() * 0.2F
            );

            if(stack.getCount() > 1){
                stack.shrink(1);
            }else {
                itemEntity.discard();
            }
        }
    }


    public int getMAX_INGREDIENT_COUNT() {
        return MAX_INGREDIENT_COUNT;
    }



    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (lvl, pos, st, be) -> {

            if (!(be instanceof PotionCauldronBE cauldron)) return;


            PotionCauldronBE.tick(lvl, cauldron);


            if(cauldron.getTank().isEmpty()||
                    cauldron.getIngredients().size() >= MAX_INGREDIENT_COUNT) {
                return;
            }

            if (cauldron.getTank().getFluid().getFluid() == Fluids.WATER) {
                return;
            }

            if (lvl.isClientSide) {

                int color = cauldron.getRenderColor();
                float[] rgb = intToRGB(color);

                float chance = 0.4f;

                if (lvl.random.nextFloat() < chance) {

                    double offsetX = (lvl.random.nextDouble() - 0.5) * 0.5;
                    double offsetZ = (lvl.random.nextDouble() - 0.5) * 0.5;

                    lvl.addParticle(
                            ModParticles.BUBBLE.get(),
                            pos.getX() + 0.5 + offsetX,
                            pos.getY() + 1.0,
                            pos.getZ() + 0.5 + offsetZ,
                            rgb[0],
                            rgb[1],
                            rgb[2]
                    );
                }
            }
        };
    }



    /*No tengo idea de como implementar esto, necesita la velocidad de las particulas pero tambien el canal de color
    /*
    private void spawnPotionSplash(Level level, BlockPos pos, PotionCauldronBE cauldron, ItemStack stack) {

        if (!(level instanceof ServerLevel serverLevel)) return;

        // ejemplo: obtener color del potion liquid actual
        int color = cauldron.getPotionLiquid().getColor(); // o calculado desde stats

        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        serverLevel.sendParticles(
                ModParticles.POTION_SPLASH.get(),
                pos.getX() + 0.5,
                pos.getY() + 1,
                pos.getZ() + 0.5,
                30,      // cantidad
                0.25,   // spread X
                0.2,    // spread Y
                0.25,   // spread Z
                0.05    // speed
        );
    }
*/
    }












