package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.shule.shulespotions.Blocks.Entities.SpikeBE;
import net.shule.shulespotions.Items.custom.PotionLiquidBottleItem;

import org.jetbrains.annotations.Nullable;

public class Spike extends BaseEntityBlock {


    public static final BooleanProperty HAS_POTION = BooleanProperty.create("has_potion");
    public static final DirectionProperty FACING = BlockStateProperties.FACING;


    protected static final VoxelShape SHAPE_UP = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 14.0D, 12.0D);
    protected static final VoxelShape COLLISION_SHAPE_UP = Block.box(4.5D, 0.0D, 4.5D, 11.5D, 13.5D, 11.5D);


    protected static final VoxelShape SHAPE_DOWN = Block.box(4.0D, 2.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    protected static final VoxelShape COLLISION_SHAPE_DOWN = Block.box(4.5D, 2.5D, 4.5D, 11.5D, 16.0D, 11.5D);


    protected static final VoxelShape SHAPE_NORTH = Block.box(4.0D, 4.0D, 2.0D, 12.0D, 12.0D, 16.0D);
    protected static final VoxelShape COLLISION_SHAPE_NORTH = Block.box(4.5D, 4.5D, 2.5D, 11.5D, 11.5D, 16.0D);


    protected static final VoxelShape SHAPE_SOUTH = Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 14.0D);
    protected static final VoxelShape COLLISION_SHAPE_SOUTH = Block.box(4.5D, 4.5D, 0.0D, 11.5D, 11.5D, 13.5D);


    protected static final VoxelShape SHAPE_WEST = Block.box(2.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D);
    protected static final VoxelShape COLLISION_SHAPE_WEST = Block.box(2.5D, 4.5D, 4.5D, 16.0D, 11.5D, 11.5D);


    protected static final VoxelShape SHAPE_EAST = Block.box(0.0D, 4.0D, 4.0D, 14.0D, 12.0D, 12.0D);
    protected static final VoxelShape COLLISION_SHAPE_EAST = Block.box(0.0D, 4.5D, 4.5D, 13.5D, 11.5D, 11.5D);

    public Spike(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HAS_POTION, false).setValue(FACING, Direction.UP));
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getClickedFace());
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HAS_POTION, FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SpikeBE(pPos, pState) ;
    }
    
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACING)) {
            case DOWN -> SHAPE_DOWN;
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> SHAPE_UP;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACING)) {
            case DOWN -> COLLISION_SHAPE_DOWN;
            case NORTH -> COLLISION_SHAPE_NORTH;
            case SOUTH -> COLLISION_SHAPE_SOUTH;
            case WEST -> COLLISION_SHAPE_WEST;
            case EAST -> COLLISION_SHAPE_EAST;
            default -> COLLISION_SHAPE_UP;
        };
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }


    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (!pLevel.isClientSide && pEntity instanceof LivingEntity livingEntity) {
            DamageSource source = pEntity.damageSources().magic();
            if (livingEntity.hurt(source, 1)) {
                if(pLevel.getBlockEntity(pPos) instanceof SpikeBE be){
                    ItemStack potion = be.getStoredPotion();
                    if (!potion.isEmpty() && potion.getItem() instanceof PotionLiquidBottleItem bottle) {
                        bottle.applyPotion(potion, null, null, livingEntity, true);
                    }
                }
            }
        }

        super.stepOn(pLevel, pPos, pState, pEntity);
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!pLevel.isClientSide && pEntity instanceof LivingEntity livingEntity) {
            DamageSource source = pEntity.damageSources().magic();
            if (livingEntity.hurt(source, 1)) {
                if(pLevel.getBlockEntity(pPos) instanceof SpikeBE be){
                    ItemStack potion = be.getStoredPotion();
                    if (!potion.isEmpty() && potion.getItem() instanceof PotionLiquidBottleItem bottle) {
                        bottle.applyPotion(potion, null, null, livingEntity, true);
                    }
                }
            }
        }
        super.entityInside(pState, pLevel, pPos, pEntity);
    }





    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof PotionLiquidBottleItem bottle) {
                if (bottle.hasFluid(stack)) {
                    if (!level.isClientSide) {
                        if (level.getBlockEntity(pos) instanceof SpikeBE be) {
                            ItemStack copy = stack.copy();
                            copy.setCount(1);
                            be.setPotionData(copy);
                            level.setBlockAndUpdate(pos, state.setValue(HAS_POTION, true));
                            level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
                            
                            if (!player.isCreative()) {
                                stack.shrink(1);
                            }
                        }
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }

        return InteractionResult.PASS;
    }
}
