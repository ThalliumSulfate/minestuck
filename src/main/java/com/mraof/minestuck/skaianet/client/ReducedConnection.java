package com.mraof.minestuck.skaianet.client;

import com.mraof.minestuck.player.NamedPlayerId;
import com.mraof.minestuck.skaianet.SburbConnection;
import net.minecraft.network.FriendlyByteBuf;

/**
 * The client side version of {@link SburbConnection}
 */
public record ReducedConnection(NamedPlayerId client, NamedPlayerId server, boolean isActive, boolean isMain, boolean hasEntered)
{
	public ReducedConnection(SburbConnection connection)
	{
		this(NamedPlayerId.of(connection.getClientIdentifier()), NamedPlayerId.of(connection.getServerIdentifier()),
				connection.isActive(), connection.isMain(), connection.data().hasEntered());
	}
	
	public static ReducedConnection read(FriendlyByteBuf buffer)
	{
		boolean isMain = buffer.readBoolean();
		boolean isActive, hasEntered;
		if(isMain)
		{
			isActive = buffer.readBoolean();
			hasEntered = buffer.readBoolean();
		} else
		{
			isActive = true;
			hasEntered = false;
		}
		NamedPlayerId client = NamedPlayerId.read(buffer);
		NamedPlayerId server = NamedPlayerId.read(buffer);
		return new ReducedConnection(client, server, isActive, isMain, hasEntered);
	}
	
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(isMain);
		if(isMain)
		{
			buffer.writeBoolean(isActive);
			buffer.writeBoolean(hasEntered);
		}
		client.write(buffer);
		server.write(buffer);
	}
}
