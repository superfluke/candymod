package com.ochotonida.candymod.world.biome;

import com.ochotonida.candymod.ModBlocks;
import com.ochotonida.candymod.ModConfig;
import com.ochotonida.candymod.block.ModBlockProperties;
import com.ochotonida.candymod.enums.EnumChocolate;
import com.ochotonida.candymod.world.dimension.WorldProviderCandyWorld;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import net.minecraftforge.event.terraingen.TerrainGen;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class DecoratorBase extends BiomeDecorator {

    @Nullable
    protected WorldGenerator soilMilkGen;
    @Nullable
    protected WorldGenerator soilWhiteGen;
    @Nullable
    protected WorldGenerator soilDarkGen;
    @Nullable
    protected WorldGenerator chocolateBlockGen;
    @Nullable
    protected WorldGenerator spikesGen;
    protected WorldGenerator sugarBlockGen;
    protected WorldGenerator cookieGen;
    protected WorldGenerator teleporterGen;
    protected WorldGenerator sugarSandGen;

    @Override
    @ParametersAreNonnullByDefault
    public void decorate(World worldIn, Random random, Biome biome, BlockPos pos) {
        if (this.decorating) {
            throw new RuntimeException("Already decorating");
        } else {
            this.chunkProviderSettings = ChunkGeneratorSettings.Factory.jsonToFactory(worldIn.getWorldInfo().getGeneratorOptions()).build();
            this.chunkPos = pos;

            if (worldIn.provider instanceof WorldProviderCandyWorld) {
                initDimensionWorldGens();

            } else {
                initOverworldWorldGens();
            }
            this.genDecorations(biome, worldIn, random);
            this.decorating = false;
        }
    }

    protected void initDimensionWorldGens() {
        this.soilMilkGen = new WorldGenMinable(ModBlocks.CANDY_SOIL.getDefaultState().withProperty(ModBlockProperties.CHOCOLATE_TYPE, EnumChocolate.MILK), 25, BlockMatcher.forBlock(ModBlocks.SUGAR_BLOCK));
        this.soilWhiteGen = new WorldGenMinable(ModBlocks.CANDY_SOIL.getDefaultState().withProperty(ModBlockProperties.CHOCOLATE_TYPE, EnumChocolate.WHITE), 25, BlockMatcher.forBlock(ModBlocks.SUGAR_BLOCK));
        this.soilDarkGen = new WorldGenMinable(ModBlocks.CANDY_SOIL.getDefaultState().withProperty(ModBlockProperties.CHOCOLATE_TYPE, EnumChocolate.DARK), 25, BlockMatcher.forBlock(ModBlocks.SUGAR_BLOCK));
        this.sugarBlockGen = new WorldGenMinable(ModBlocks.SUGAR_BLOCK.getDefaultState(), 0);
        this.cookieGen = new WorldGenMinable(ModBlocks.COOKIE_ORE.getDefaultState().withProperty(ModBlockProperties.IS_SUGAR_VARIANT, true), 3, BlockMatcher.forBlock(ModBlocks.SUGAR_BLOCK));
        this.teleporterGen = new TeleporterOreGenerator();
        this.sugarSandGen = new WorldGenMinable(ModBlocks.SUGAR_SAND.getDefaultState(), 20, BlockMatcher.forBlock(ModBlocks.SUGAR_BLOCK));

        this.coalGen = new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(), 0);
        this.ironGen = new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(), 0);
        this.goldGen = new WorldGenMinable(Blocks.GOLD_ORE.getDefaultState(), 0);
        this.redstoneGen = new WorldGenMinable(Blocks.REDSTONE_ORE.getDefaultState(), 0);
        this.diamondGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), 0);
        this.lapisGen = new WorldGenMinable(Blocks.LAPIS_ORE.getDefaultState(), 0);
    }

    protected void initOverworldWorldGens() {
        this.soilMilkGen = new WorldGenMinable(ModBlocks.CANDY_SOIL.getDefaultState().withProperty(ModBlockProperties.CHOCOLATE_TYPE, EnumChocolate.MILK), 25);
        this.soilWhiteGen = new WorldGenMinable(ModBlocks.CANDY_SOIL.getDefaultState().withProperty(ModBlockProperties.CHOCOLATE_TYPE, EnumChocolate.WHITE), 25);
        this.soilDarkGen = new WorldGenMinable(ModBlocks.CANDY_SOIL.getDefaultState().withProperty(ModBlockProperties.CHOCOLATE_TYPE, EnumChocolate.DARK), 25);
        this.sugarBlockGen = new WorldGenMinable(ModBlocks.SUGAR_BLOCK.getDefaultState(), 20);
        this.cookieGen = new WorldGenMinable(ModBlocks.COOKIE_ORE.getDefaultState(), 3);
        this.sugarSandGen = new WorldGenMinable(ModBlocks.SUGAR_SAND.getDefaultState(), 30);

        this.coalGen = new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(), this.chunkProviderSettings.coalSize);
        this.ironGen = new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(), this.chunkProviderSettings.ironSize);
        this.goldGen = new WorldGenMinable(Blocks.GOLD_ORE.getDefaultState(), this.chunkProviderSettings.goldSize);
        this.redstoneGen = new WorldGenMinable(Blocks.REDSTONE_ORE.getDefaultState(), this.chunkProviderSettings.redstoneSize);
        this.diamondGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), this.chunkProviderSettings.diamondSize);
        this.lapisGen = new WorldGenMinable(Blocks.LAPIS_ORE.getDefaultState(), this.chunkProviderSettings.lapisSize);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void genDecorations(Biome biome, World worldIn, Random rand) {

        if (spikesGen != null && TerrainGen.decorate(worldIn, rand, chunkPos, DecorateBiomeEvent.Decorate.EventType.CUSTOM)) {
            spikesGen.generate(worldIn, rand, chunkPos);
        }

        super.genDecorations(biome, worldIn, rand);

        if (TerrainGen.decorate(worldIn, rand, chunkPos, DecorateBiomeEvent.Decorate.EventType.CUSTOM)) {
            genBiomeDecorations(worldIn, rand);
        }
    }

    @ParametersAreNonnullByDefault
    protected void genBiomeDecorations(World worldIn, Random rand) {
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void generateOres(World worldIn, Random rand) {
        net.minecraftforge.common.MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Pre(worldIn, rand, chunkPos));

        generateCustomOres(worldIn, rand);

        if (!(worldIn.provider instanceof WorldProviderCandyWorld)) {
            if (TerrainGen.generateOre(worldIn, rand, coalGen, chunkPos, EventType.COAL)) {
                this.genStandardOre1(worldIn, rand, this.chunkProviderSettings.coalCount, this.coalGen, this.chunkProviderSettings.coalMinHeight, this.chunkProviderSettings.coalMaxHeight);
            }
            if (TerrainGen.generateOre(worldIn, rand, ironGen, chunkPos, EventType.IRON)) {
                this.genStandardOre1(worldIn, rand, this.chunkProviderSettings.ironCount, this.ironGen, this.chunkProviderSettings.ironMinHeight, this.chunkProviderSettings.ironMaxHeight);
            }
            if (TerrainGen.generateOre(worldIn, rand, goldGen, chunkPos, EventType.GOLD)) {
                this.genStandardOre1(worldIn, rand, this.chunkProviderSettings.goldCount, this.goldGen, this.chunkProviderSettings.goldMinHeight, this.chunkProviderSettings.goldMaxHeight);
            }
            if (TerrainGen.generateOre(worldIn, rand, redstoneGen, chunkPos, EventType.REDSTONE)) {
                this.genStandardOre1(worldIn, rand, this.chunkProviderSettings.redstoneCount, this.redstoneGen, this.chunkProviderSettings.redstoneMinHeight, this.chunkProviderSettings.redstoneMaxHeight);
            }
            if (TerrainGen.generateOre(worldIn, rand, diamondGen, chunkPos, EventType.DIAMOND)) {
                this.genStandardOre1(worldIn, rand, this.chunkProviderSettings.diamondCount, this.diamondGen, this.chunkProviderSettings.diamondMinHeight, this.chunkProviderSettings.diamondMaxHeight);
            }
            if (TerrainGen.generateOre(worldIn, rand, lapisGen, chunkPos, EventType.LAPIS)) {
                this.genStandardOre2(worldIn, rand, this.chunkProviderSettings.lapisCount, this.lapisGen, this.chunkProviderSettings.lapisCenterHeight, this.chunkProviderSettings.lapisSpread);
            }
        }

        net.minecraftforge.common.MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Post(worldIn, rand, chunkPos));
    }

    @ParametersAreNonnullByDefault
    protected void generateCustomOres(World worldIn, Random rand) {

        if (chocolateBlockGen != null && TerrainGen.generateOre(worldIn, rand, chocolateBlockGen, chunkPos, EventType.CUSTOM)) {
            this.genStandardOre2(worldIn, rand, 4, chocolateBlockGen, 10, 30);
        }

        if (worldIn.provider instanceof WorldProviderCandyWorld) {
            if (TerrainGen.generateOre(worldIn, rand, sugarSandGen, chunkPos, EventType.CUSTOM)) {
                this.genStandardOre1(worldIn, rand, 20, sugarSandGen, 0, 256);
            }
            if (soilMilkGen != null && TerrainGen.generateOre(worldIn, rand, soilMilkGen, chunkPos, EventType.CUSTOM)) {
                this.genStandardOre1(worldIn, rand, 16, soilMilkGen, 0, 256);
            }
            if (soilWhiteGen != null && TerrainGen.generateOre(worldIn, rand, soilWhiteGen, chunkPos, EventType.CUSTOM)) {
                this.genStandardOre1(worldIn, rand, 16, soilWhiteGen, 0, 256);
            }
            if (soilDarkGen != null && TerrainGen.generateOre(worldIn, rand, soilDarkGen, chunkPos, EventType.CUSTOM)) {
                this.genStandardOre1(worldIn, rand, 16, soilDarkGen, 0, 256);
            }
            if (TerrainGen.generateOre(worldIn, rand, cookieGen, chunkPos, EventType.CUSTOM)) {
                this.genStandardOre2(worldIn, rand, 80, cookieGen, 32, 45);
            }
            if (TerrainGen.generateOre(worldIn, rand, teleporterGen, chunkPos, EventType.CUSTOM)) {
                this.teleporterGen.generate(worldIn, rand, chunkPos);
            }

        } else {
            if (TerrainGen.generateOre(worldIn, rand, cookieGen, chunkPos, EventType.CUSTOM)) {
                this.genStandardOre2(worldIn, rand, 50, cookieGen, 32, 45);
            }
            if (TerrainGen.generateOre(worldIn, rand, sugarSandGen, chunkPos, EventType.CUSTOM)) {
                this.genStandardOre1(worldIn, rand, 5, sugarSandGen, 0, 256);
            }
            if (TerrainGen.generateOre(worldIn, rand, sugarBlockGen, chunkPos, EventType.CUSTOM)) {
                this.genStandardOre1(worldIn, rand, 2, sugarBlockGen, 0, 30);
            }
            if (soilMilkGen != null && TerrainGen.generateOre(worldIn, rand, soilMilkGen, chunkPos, EventType.CUSTOM)) {
                this.genStandardOre1(worldIn, rand, 1, soilMilkGen, 20, 40);
            }
            if (soilWhiteGen != null && TerrainGen.generateOre(worldIn, rand, soilWhiteGen, chunkPos, EventType.CUSTOM)) {
                this.genStandardOre1(worldIn, rand, 1, soilWhiteGen, 40, 64);
            }
            if (soilDarkGen != null && TerrainGen.generateOre(worldIn, rand, soilDarkGen, chunkPos, EventType.CUSTOM)) {
                this.genStandardOre1(worldIn, rand, 1, soilDarkGen, 0, 25);
            }
        }
    }

    private static class TeleporterOreGenerator extends WorldGenerator {
        @Override
        @ParametersAreNonnullByDefault
        public boolean generate(World worldIn, Random rand, BlockPos pos) {
            int count = 6 + rand.nextInt(10);
            for (int i = 0; i < count; i++) {
                BlockPos blockpos = pos.add(rand.nextInt(16) + 8, rand.nextInt(38) + 4, rand.nextInt(16) + 8);

                net.minecraft.block.state.IBlockState state = worldIn.getBlockState(blockpos);
                if (state.getBlock().isReplaceableOreGen(state, worldIn, blockpos, net.minecraft.block.state.pattern.BlockMatcher.forBlock(ModBlocks.SUGAR_BLOCK))) {
                    worldIn.setBlockState(blockpos, ModBlocks.TELEPORTER_ORE.getDefaultState(), 16 | 2);
                }
            }
            return true;
        }
    }
}
