package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.LandDimension;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, value = Dist.CLIENT)
public class MSMusicTicker
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
		{
			tick(Minecraft.getInstance());
		}
	}
	
	@SubscribeEvent
	public static void playSound(PlaySoundEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		if(mc.player != null && MSDimensions.isLandDimension(mc.player.dimension)
				&& event.getSound().getSoundLocation().equals(mc.getAmbientMusicType().getSound().getName()))
			event.setResultSound(null);
	}
	
	private static boolean wasInLand = false;
	private static int ticksUntilMusic;
	private static ISound currentMusic;
	
	private static void tick(Minecraft mc)
	{
		if(mc.player != null && MSDimensions.isLandDimension(mc.player.dimension))
		{
			LandDimension dim = (LandDimension) mc.player.world.getDimension();
			
			LandTypePair pair = dim.landTypes;
			
			if(!wasInLand)
			{
				LOGGER.debug("Entered");
				ticksUntilMusic = MathHelper.nextInt(mc.world.rand, 0, 6000);
			}
			
			if(currentMusic == null)
			{
				ticksUntilMusic--;
				if(ticksUntilMusic < 0)
				{
					currentMusic = SimpleSound.music(MSSoundEvents.MUSIC_LAND);
					mc.getSoundHandler().play(currentMusic);
				}
			} else if(!mc.getSoundHandler().isPlaying(currentMusic))
			{
				currentMusic = null;
				ticksUntilMusic = MathHelper.nextInt(mc.world.rand, 12000, 24000);
			}
			
			wasInLand = true;
		} else
		{
			wasInLand = false;
			if(currentMusic != null)
			{
				mc.getSoundHandler().stop(currentMusic);
				currentMusic = null;
			}
		}
	}
}