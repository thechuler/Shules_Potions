package net.shule.shulespotions.Blocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.shule.shulespotions.Blocks.Custom.*;
import net.shule.shulespotions.Fluids.ModFluids;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.ShulesPotions;

import java.util.function.Supplier;


public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ShulesPotions.MODID);



    public static final RegistryObject<Block> POTION_CAULDRON = registerBlock("potion_cauldron",
            () -> new PotionCauldron(BlockBehaviour.Properties.copy(Blocks.CAULDRON),4, 4,1000, 2));


    public static final RegistryObject<Block> COPPER_CAULDRON = registerBlock("copper_cauldron",
            () -> new PotionCauldron(BlockBehaviour.Properties.copy(Blocks.CAULDRON),4, 6,1000, 3));

    public static final RegistryObject<Block> METEOR_CAULDRON = registerBlock("meteor_cauldron",
            () -> new PotionCauldron(BlockBehaviour.Properties.copy(Blocks.CAULDRON),6, 12,1000, 5));


    public static final RegistryObject<Block> BIG_CAULDRON = registerBlock("big_cauldron",
            () -> new BigCauldronCore(BlockBehaviour.Properties.copy(Blocks.CAULDRON).noOcclusion(), 8, 16, 1000, 6));

    public static final RegistryObject<Block> BIG_CAULDRON_PART = BLOCKS.register("big_cauldron_part",
            () -> new BigCauldronPart(BlockBehaviour.Properties.copy(Blocks.BARRIER).noOcclusion().noLootTable()));



    public static final RegistryObject<Block> POTIONSHELF = registerBlock("potionshelf",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BOOKSHELF)));

    public static final RegistryObject<Block> MORTAR = registerBlock("mortar",
            () -> new Mortar(BlockBehaviour.Properties.copy(Blocks.STONE)));


    public static final RegistryObject<Block> ANCITE_BRICKS = registerBlock("ancite_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));

    public static final RegistryObject<Block> CRACKED_ANCITE_BRICKS = registerBlock("cracked_ancite_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));

    public static final RegistryObject<Block> CHISELED_ANCITE_BRICKS = registerBlock("chiseled_ancite_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));

    public static final RegistryObject<Block> SMOOTH_ANCITE = registerBlock("smooth_ancite",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));

    public static final RegistryObject<Block> MOSSY_ANCITE_BRICKS = registerBlock("mossy_ancite_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));

    public static final RegistryObject<Block> GROWN_MOSSY_ANCITE_BRICKS = registerBlock("grown_mossy_ancite_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));

    public static final RegistryObject<Block> OVERGROWN_MOSSY_ANCITE_BRICKS = registerBlock("overgrown_mossy_ancite_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));

    public static final RegistryObject<Block> ANCITE_ORNAMENTED_BRICKS = registerBlock("ancite_ornamented_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));

    public static final RegistryObject<Block> CHISELED_ANCITE_ORNAMENTED_BRICKS = registerBlock("chiseled_ancite_ornamented_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));

    public static final RegistryObject<Block> ANCITE_LOCK = registerBlock("ancite_lock",
            () -> new AnciteLock(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));

    public static final RegistryObject<Block> ANCITE_BLOCKING_WALL = registerBlock("ancite_blocking_wall",
            () -> new AnciteBlockingWall(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));


    public static final RegistryObject<Block> ANCITE_PILLAR = registerBlock("ancite_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));

    public static final RegistryObject<Block> ANCITE_BRICKS_STAIRS = registerBlock("ancite_bricks_stairs",
            () -> new StairBlock(() -> ModBlocks.ANCITE_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));

    public static final RegistryObject<Block> INSTABLE_ANCITE_BRICKS = registerBlock("instable_ancite_bricks",
            () -> new InstableAnciteBrick(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));


    public static final RegistryObject<Block> SITTING_ANCIENT_STATUE = registerBlock("sitting_ancient_statue",
            () -> new SittingAncientStatue(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().noLootTable()));

    public static final RegistryObject<Block> ANCIENT_PEDESTAL = registerBlock("ancient_pedestal",
            () -> new AncientPedestal(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().noLootTable(), ResourceLocation.parse(ShulesPotions.MODID + ":gameplay/ancient_pedestal")));

    public static final RegistryObject<Block> ELECTION_ANCIENT_PEDESTAL = registerBlock("election_ancient_pedestal",
            () -> new ElectionAncientPedestal(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().noLootTable()));

    public static final RegistryObject<Block> OMINOUS_PEDESTAL = registerBlock("ominous_pedestal",
            () -> new OminousPedestal(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().noLootTable(), ResourceLocation.parse(ShulesPotions.MODID + ":gameplay/ominous_pedestal")));





    public static final RegistryObject<Block> METEORITE_STONE = registerBlock("meteorite_stone",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));

    public static final RegistryObject<Block> METEORITE_STONE_BURNED = registerBlock("meteorite_stone_burned",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));

    public static final RegistryObject<Block> METEORITE_STONE_PETRIFIED = registerBlock("meteorite_stone_petrified",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));


    public static final RegistryObject<Block> ONYX_ORE = registerBlock("onyx_ore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_DIAMOND_ORE)));

    public static final RegistryObject<Block> POTION_SPLASH = registerBlock("potion_splash",
            () -> new PotionSplashBlock(BlockBehaviour.Properties.copy(Blocks.SLIME_BLOCK)));

    public static final RegistryObject<Block> SPOON_RACK = registerBlock("spoon_rack",
            () -> new SpoonRack(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));

    public static final RegistryObject<Block> SPITTER_TRAP = registerBlock("spitter_trap",
            () -> new SpitterTrap(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));

    public static final RegistryObject<Block> ANCIENT_REDSTONE_CLOCK = registerBlock("ancient_redstone_clock",
            () -> new AncientRedstoneClock(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));


    public static final RegistryObject<Block> SPIKE = registerBlock("spike",
            () -> new Spike(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).noOcclusion()));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        RegisterBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> RegisterBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    public static final RegistryObject<LiquidBlock> POTION_FLUID_BLOCK = BLOCKS.register("potion_fluid_block",
            ()-> new LiquidBlock(ModFluids.SOURCE_POTION_FLUID,BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> UNFINISHED_POTION_FLUID_BLOCK = BLOCKS.register("unfinished_potion_fluid_block",
            ()-> new LiquidBlock(ModFluids.SOURCE_UNFINISHED_POTION_FLUID,BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }

}
