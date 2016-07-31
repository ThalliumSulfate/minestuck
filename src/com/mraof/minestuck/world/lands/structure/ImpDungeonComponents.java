package com.mraof.minestuck.world.lands.structure;

import java.util.List;
import java.util.Random;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ImpDungeonComponents
{
	
	public static class EntryCorridor extends ImpDungeonComponent
	{
		
		boolean[] corridors = new boolean[2];
		
		public EntryCorridor()
		{}
		
		public EntryCorridor(EnumFacing coordBaseMode, int posX, int posZ, Random rand, List<StructureComponent> componentList)
		{
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 8 : 6;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 8 : 6;
			
			int height = 50 - rand.nextInt(8);
			int offset = getCoordBaseMode().getAxisDirection().equals(EnumFacing.AxisDirection.POSITIVE) ? 5 : -2;
			int x = posX + (getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 0 : offset);
			int z = posZ + (getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 0 : offset);
			
			this.boundingBox = new StructureBoundingBox(x, height, z, x + xWidth - 1, height + 6, z + zWidth - 1);
			
			BlockPos compoPos = new BlockPos(x + (xWidth/2 - 1), height, z + (zWidth/2 - 1));
			
			ImpDungeonComponent[][] compoGen = new ImpDungeonComponent[13][13];
			compoGen[6][6] = this;
			
			int xOffset = coordBaseMode.getFrontOffsetX();
			int zOffset = coordBaseMode.getFrontOffsetZ();
			if(rand.nextBoolean())
			{
				corridors[0] = !generatePart(compoGen, 6 + xOffset, 6 + zOffset, compoPos.add(xOffset*8, 0, zOffset*8), coordBaseMode, rand, componentList, 0);
				corridors[1] = !generatePart(compoGen, 6 - xOffset, 6 - zOffset, compoPos.add(-xOffset*8, 0, -zOffset*8), coordBaseMode.getOpposite(), rand, componentList, 0);
			} else
			{
				corridors[1] = !generatePart(compoGen, 6 - xOffset, 6 - zOffset, compoPos.add(-xOffset*8, 0, -zOffset*8), coordBaseMode.getOpposite(), rand, componentList, 0);
				corridors[0] = !generatePart(compoGen, 6 + xOffset, 6 + zOffset, compoPos.add(xOffset*8, 0, zOffset*8), coordBaseMode, rand, componentList, 0);
			}
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			for(int i = 0; i < corridors.length; i++)
				tagCompound.setBoolean("blocked"+i, corridors[i]);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound)
		{
			for(int i = 0; i < corridors.length; i++)
				corridors[i] = tagCompound.getBoolean("blocked"+i);
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			
			return false;
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState wallDecor = provider.blockRegistry.getBlockState("structure_primary_decorative");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState floorDecor = provider.blockRegistry.getBlockState("structure_secondary_decorative");
			IBlockState fluid = provider.blockRegistry.getBlockState("fall_fluid");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 3, 4, 0, 5, floorBlock, floorBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 3, 4, 4, 5);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 4, 3, 0, 4, fluid, fluid, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 4, 3, -1, 4, floorDecor, floorDecor, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 2, 0, 5, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 2, 5, 5, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 2, 4, 5, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 6, 4, 5, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 2, 1, 3, 2, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 6, 1, 3, 6, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 2, 4, 3, 2, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 6, 4, 3, 6, wallDecor, wallDecor, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 3, 0, 2, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 6, 3, 0, 7, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 0, 3, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 7, 3, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 1, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 0, 4, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 1, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 7, 4, 4, 7, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 0, 3, 3, 2);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 6, 3, 3, 7);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 7, 3, 3, 7, wallBlock, wallBlock, false);
			if(corridors[1])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 0, 3, 3, 0, wallBlock, wallBlock, false);
			
			return true;
		}
	}
	
	public static boolean generatePart(ImpDungeonComponent[][] compoGen, int xIndex, int zIndex, BlockPos pos, EnumFacing facing, Random rand, List<StructureComponent> compoList, int index)
	{
		if(xIndex >= compoGen.length || zIndex >= compoGen[0].length
				|| xIndex < 0 || zIndex < 0)
			return false;
		
		if(compoGen[xIndex][zIndex] != null)
			return compoGen[xIndex][zIndex].connectFrom(facing.getOpposite());
		
		if(rand.nextGaussian() >= (1.4 - index*0.2))
			return false;
		
		ImpDungeonComponent component;
		
		double i = rand.nextGaussian();
		if(i < 0.4)
		{
			component = new StraightCorridor(facing, pos, rand, compoGen, xIndex, zIndex, index, compoList);
		} else if(i < 0.7)
		{
			component = new TurnCorridor(facing, pos, rand, compoGen, xIndex, zIndex, index, compoList);
		} else
		{
			component = new CrossCorridor(facing, pos, rand, compoGen, xIndex, zIndex, index, compoList);
		}
		
		compoList.add(component);
		
		return true;
	}
	
	public static abstract class ImpDungeonComponent extends StructureComponent
	{
		protected abstract boolean connectFrom(EnumFacing facing);
		
		@Override
		protected int getXWithOffset(int x, int z)
		{
			EnumFacing enumfacing = this.getCoordBaseMode();
			
			if (enumfacing == null)
				return x;
			else switch (enumfacing)
			{
			case NORTH:
				return this.boundingBox.maxX - x;
			case SOUTH:
				return this.boundingBox.minX + x;
			case WEST:
				return this.boundingBox.maxX - z;
			case EAST:
				return this.boundingBox.minX + z;
			default:
				return x;
			}
		}
		
		@Override
		protected int getZWithOffset(int x, int z)
		{
			EnumFacing enumfacing = this.getCoordBaseMode();
			
			if (enumfacing == null)
				return z;
			else switch (enumfacing)
			{
			case NORTH:
				return this.boundingBox.maxZ - z;
			case SOUTH:
				return this.boundingBox.minZ + z;
			case WEST:
				return this.boundingBox.minZ + x;
			case EAST:
				return this.boundingBox.maxZ - x;
			default:
				return z;
			}
		}
	}
	
	public static class StraightCorridor extends ImpDungeonComponent
	{
		boolean[] corridors = new boolean[1];
		
		public StraightCorridor()
		{}
		
		public StraightCorridor(EnumFacing coordBaseMode, BlockPos pos, Random rand, ImpDungeonComponent[][] compoGen, int xIndex, int zIndex, int index, List<StructureComponent> componentList)
		{
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 8 : 4;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 8 : 4;
			
			int x = pos.getX() - (xWidth/2 - 1);
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			compoGen[xIndex][zIndex] = this;
			int xOffset = coordBaseMode.getFrontOffsetX();
			int zOffset = coordBaseMode.getFrontOffsetZ();
			corridors[0] = !generatePart(compoGen, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*8, 0, zOffset*8), coordBaseMode, rand, componentList, index + 1);
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			for(int i = 0; i < corridors.length; i++)
				tagCompound.setBoolean("blocked"+i, corridors[i]);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound)
		{
			for(int i = 0; i < corridors.length; i++)
				corridors[i] = tagCompound.getBoolean("blocked"+i);
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			if(facing.equals(getCoordBaseMode()))
				corridors[0] = false;
			return facing.getAxis().equals(getCoordBaseMode().getAxis());
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 2, 0, 7, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 0, 2, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 3, 4, 7, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 0, 2, 3, 7);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 2, 3, 7, wallBlock, wallBlock, false);
			
			return true;
		}
	}
	
	public static class CrossCorridor extends ImpDungeonComponent
	{
		boolean[] corridors = new boolean[3];
		
		public CrossCorridor()
		{}
		
		public CrossCorridor(EnumFacing coordBaseMode, BlockPos pos, Random rand, ImpDungeonComponent[][] compoGen, int xIndex, int zIndex, int index, List<StructureComponent> componentList)
		{
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = 8;
			int zWidth = 8;
			
			int x = pos.getX() - (xWidth/2 - 1);
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 5, z + zWidth - 1);
			
			compoGen[xIndex][zIndex] = this;
			int xOffset = coordBaseMode.getFrontOffsetX();
			int zOffset = coordBaseMode.getFrontOffsetZ();
			if(rand.nextBoolean())
			{
				corridors[0] = !generatePart(compoGen, xIndex - zOffset, zIndex + xOffset, pos.add(-zOffset*8, 0, xOffset*8), coordBaseMode.rotateY(), rand, componentList, index + 1);
				corridors[2] = !generatePart(compoGen, xIndex + zOffset, zIndex - xOffset, pos.add(zOffset*8, 0, -xOffset*8), coordBaseMode.rotateYCCW(), rand, componentList, index + 1);
			} else
			{
				corridors[2] = !generatePart(compoGen, xIndex + zOffset, zIndex - xOffset, pos.add(zOffset*8, 0, -xOffset*8), coordBaseMode.rotateYCCW(), rand, componentList, index + 1);
				corridors[0] = !generatePart(compoGen, xIndex - zOffset, zIndex + xOffset, pos.add(-zOffset*8, 0, xOffset*8), coordBaseMode.rotateY(), rand, componentList, index + 1);
			}
			corridors[1] = !generatePart(compoGen, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*8, 0, zOffset*8), coordBaseMode, rand, componentList, index + 2);
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			for(int i = 0; i < corridors.length; i++)
				tagCompound.setBoolean("blocked"+i, corridors[i]);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound)
		{
			for(int i = 0; i < corridors.length; i++)
				corridors[i] = tagCompound.getBoolean("blocked"+i);
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			if(facing.rotateY().equals(getCoordBaseMode()))
				corridors[0] = false;
			else if(facing.equals(getCoordBaseMode()))
				corridors[1] = false;
			else if(facing.rotateYCCW().equals(getCoordBaseMode()))
				corridors[2] = false;
			return true;
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState wallDecor = provider.blockRegistry.getBlockState("structure_primary_decorative");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 4, 0, 7, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 3, 2, 0, 4, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 3, 7, 0, 4, floorBlock, floorBlock, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 2, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 0, 5, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 5, 2, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 5, 5, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 2, 1, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 5, 1, 4, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 2, 7, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 5, 7, 4, 5, wallBlock, wallBlock, false);
			
			fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 3, 7);
			fillWithAir(worldIn, structureBoundingBoxIn, 0, 1, 3, 2, 3, 4);
			fillWithAir(worldIn, structureBoundingBoxIn, 5, 1, 3, 7, 3, 4);
			fillWithAir(worldIn, structureBoundingBoxIn, 3, 4, 3, 4, 4, 4);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 0, 4, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 2, 4, 4, 2, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 6, 4, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 5, 4, 4, 5, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 3, 1, 4, 4, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 3, 2, 4, 4, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 4, 3, 7, 4, 4, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 3, 5, 4, 4, wallDecor, wallDecor, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 2, 5, 5, 5, wallBlock, wallBlock, false);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 3, 4, wallBlock, wallBlock, false);
			if(corridors[1])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 3, 7, wallBlock, wallBlock, false);
			if(corridors[2])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 3, 7, 3, 4, wallBlock, wallBlock, false);
			
			return true;
		}
	}
	
	public static class TurnCorridor extends ImpDungeonComponent
	{
		boolean[] corridors = new boolean[2];
		
		public TurnCorridor()
		{}
		
		public TurnCorridor(EnumFacing coordBaseMode, BlockPos pos, Random rand, ImpDungeonComponent[][] compoGen, int xIndex, int zIndex, int index, List<StructureComponent> componentList)
		{
			boolean direction = rand.nextBoolean();
			if(direction)
				setCoordBaseMode(coordBaseMode.rotateY());
			else setCoordBaseMode(coordBaseMode);
			
			int xWidth = 6;
			int zWidth = 6;
			
			int i = coordBaseMode.getAxisDirection().getOffset() + 1;
			int j = direction^(coordBaseMode.getAxis() == EnumFacing.Axis.Z)^(coordBaseMode.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)?2:0;
			int x = pos.getX() - 3 + (getCoordBaseMode() == EnumFacing.NORTH || getCoordBaseMode() == EnumFacing.WEST ? 2 : 0);
			int z = pos.getZ() - 3 + (getCoordBaseMode() == EnumFacing.NORTH || getCoordBaseMode() == EnumFacing.EAST ? 2 : 0);
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			compoGen[xIndex][zIndex] = this;
			EnumFacing newFacing = direction ? coordBaseMode.rotateYCCW() : coordBaseMode.rotateY();
			int xOffset = newFacing.getFrontOffsetX();
			int zOffset = newFacing.getFrontOffsetZ();
			corridors[direction ? 0 : 1] = !generatePart(compoGen, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*8, 0, zOffset*8), newFacing, rand, componentList, index + 1);
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			for(int i = 0; i < corridors.length; i++)
				tagCompound.setBoolean("blocked"+i, corridors[i]);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound)
		{
			for(int i = 0; i < corridors.length; i++)
				corridors[i] = tagCompound.getBoolean("blocked"+i);
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			if(facing.rotateY().equals(getCoordBaseMode()))
				corridors[1] = false;
			else if(facing.getOpposite().equals(getCoordBaseMode()))
				corridors[0] = false;
			else return false;
			return true;
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 4, 0, 4, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 3, 2, 0, 4, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 0, 5, 4, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 2, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 2, 1, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 5, 4, 4, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 0, 4, 4, 4, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 3, 2, 4, 4, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 3, 4);
			fillWithAir(worldIn, structureBoundingBoxIn, 0, 1, 3, 2, 3, 4);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 3, 0, wallBlock, wallBlock, false);
			if(corridors[1])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 3, 4, wallBlock, wallBlock, false);
			
			return true;
		}
	}
	
}