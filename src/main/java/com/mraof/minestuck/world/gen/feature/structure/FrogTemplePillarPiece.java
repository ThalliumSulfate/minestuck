package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class FrogTemplePillarPiece extends ScatteredStructurePiece
{
	private final boolean eroded;
	private final boolean uraniumFilled;
	private final int randReduction;
	
	public FrogTemplePillarPiece(ChunkGenerator<?> generator, Random random, int x, int y, int z) //this constructor is used when the structure is first initialized
	{
		super(MSStructurePieces.FROG_TEMPLE_PILLAR, random, x - 2, y, z - 2, 5, 46, 5);
		eroded = random.nextBoolean();
		uraniumFilled = random.nextBoolean();
		randReduction = random.nextInt(10);
	}
	
	public FrogTemplePillarPiece(TemplateManager templates, CompoundNBT nbt) //this constructor is used for reading from data
	{
		super(MSStructurePieces.FROG_TEMPLE_PILLAR, nbt);
		eroded = nbt.getBoolean("eroded");
		uraniumFilled = nbt.getBoolean("uraniumFilled");
		randReduction = nbt.getInt("randReduction");
	}
	
	@Override
	protected void readAdditional(CompoundNBT tagCompound) //actually writeAdditional
	{
		tagCompound.putBoolean("eroded", eroded);
		tagCompound.putBoolean("uraniumFilled", uraniumFilled);
		tagCompound.putInt("randReduction", randReduction);
		super.readAdditional(tagCompound);
	}
	
	
	@Override
	public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGenerator, Random randomIn, MutableBoundingBox boundingBoxIn, ChunkPos chunkPosIn)
	{
		BlockState columnBlock = MSBlocks.GREEN_STONE_COLUMN.getDefaultState().with(MSDirectionalBlock.FACING, Direction.UP);
		if(eroded)
		{
			fillWithBlocks(worldIn, boundingBoxIn, 1, -20, 1, 3, 40 - randReduction, 3, columnBlock, columnBlock, false);
		} else
		{
			Block innerBlock = uraniumFilled ? MSBlocks.URANIUM_BLOCK : MSBlocks.CRUXITE_BLOCK;
			fillWithBlocks(worldIn, boundingBoxIn, 1, -20, 1, 3, 40, 3, columnBlock, columnBlock, false);
			fillWithBlocks(worldIn, boundingBoxIn, 0, 41, 0, 4, 45, 4, MSBlocks.GREEN_STONE.getDefaultState(), innerBlock.getDefaultState(), false); //top of pillar with a randomly filled center picked by uraniumFilled
		}
		
		return true;
	}
}