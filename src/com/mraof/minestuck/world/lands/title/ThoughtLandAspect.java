package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.lands.LandDimension;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.lands.decorator.structure.SmallLibraryDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.LandGenSettings;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.Vec3d;

public class ThoughtLandAspect extends TitleLandAspect
{
	public ThoughtLandAspect()
	{
		super(EnumAspect.MIND);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"thought"};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("ocean", MSBlocks.BRAIN_JUICE.getDefaultState());
		registry.setBlockState("river", MSBlocks.BRAIN_JUICE.getDefaultState());
		registry.setBlockState("structure_wool_2", Blocks.LIME_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.LIME_CARPET.getDefaultState());
	}
	
	@Override
	public void prepareWorldProvider(LandDimension worldProvider)
	{
		worldProvider.mergeFogColor(new Vec3d(0.8, 0.3, 0.8), 0.8F);
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = Math.max(settings.oceanChance, 0.2F);
	}
	
	//@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		
		
		chunkProvider.decorators.add(new SmallLibraryDecorator(MSBiomes.mediumNormal));
		//chunkProvider.sortDecorators();
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		StructureBlockRegistry registry = new StructureBlockRegistry();
		aspect.registerBlocks(registry);
		return registry.getBlockState("ocean").getMaterial() != Material.LAVA;
	}
	
}
