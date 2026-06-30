package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.shule.shulespotions.Blocks.Entities.MortarBE;
import org.jetbrains.annotations.Nullable;

public class Mortar extends BaseEntityBlock {

    protected static final VoxelShape SHAPE = Shapes.or(
            box(3.0D,0.0D,2.0D,
                    13.0D,5.0D,14.0D));
    public Mortar(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MortarBE(pPos,pState);
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {

        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        if (hand != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;

        BlockEntity be = level.getBlockEntity(pos);

        if (!(be instanceof MortarBE mortar))
            return InteractionResult.PASS;

        ItemStack held = player.getItemInHand(hand);


        if (mortar.isFinished()) {

            if (!held.is(Items.BOWL)) {
                return InteractionResult.CONSUME;
            }

            ItemStack result = mortar.takeBowl();

            if (result.isEmpty()) {
                return InteractionResult.PASS;
            }

            held.shrink(1);

            if (!player.getInventory().add(result)) {
                player.drop(result, false);
            }

            return InteractionResult.CONSUME;
        }



        if (held.isEmpty() && player.isCrouching()) {
            return mortar.removeIngredient(player)
                    ? InteractionResult.CONSUME
                    : InteractionResult.PASS;
        }


        return mortar.addIngredient(held)
                ? InteractionResult.CONSUME
                : InteractionResult.PASS;
    }
}
