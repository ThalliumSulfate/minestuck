package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.stream.Stream;

/**
 * An extension to SkaianetHandler with a focus on sessions
 * @author kirderf1
 */
public abstract class SessionHandler
{
	/**
	 * The max numbers of players per session.
	 */
	public static final int MAX_SIZE = 144;
	
	final SkaianetHandler skaianetHandler;
	
	SessionHandler(SkaianetHandler skaianetHandler)
	{
		this.skaianetHandler = skaianetHandler;
	}
	
	abstract void write(CompoundTag compound);
	
	abstract SessionHandler getActual();
	
	/**
	 * Looks for the session that the player is a part of.
	 * @param player A string of the player's username.
	 * @return A session that contains at least one connection, that the player is a part of.
	 */
	public abstract Session getPlayerSession(PlayerIdentifier player);
	
	public abstract Set<Session> getSessions();
	
	Stream<SburbConnection> getConnectionStream()
	{
		return getSessions().stream().flatMap(session -> session.connections.stream());
	}
	
	abstract Session prepareSessionFor(PlayerIdentifier... players) throws MergeResult.SessionMergeException;
	
	abstract void findOrCreateAndCall(PlayerIdentifier player, SkaianetException.SkaianetConsumer<Session> consumer) throws SkaianetException;
	
	boolean doesSessionHaveMaxTier(Session session)
	{
		return session.completed;
	}
	
	void onConnectionChainBroken(Session session)
	{}
	
	/**
	 * Will check if two players can connect based on their main connections and sessions.
	 * Does NOT include session size checking.
	 * @return True if client connection is not null and client and server session is the same or 
	 * client connection is null and server connection is null.
	 */
	//TODO repalce with proper checks when creating a regular or secondary connection
	private boolean canConnect(PlayerIdentifier client, PlayerIdentifier server)
	{
		Session sClient = getPlayerSession(client), sServer = getPlayerSession(server);
		Optional<SburbConnection> cClient = skaianetHandler.getPrimaryOrCandidateConnection(client, true);
		Optional<SburbConnection> cServer = skaianetHandler.getPrimaryOrCandidateConnection(server, false);
		
		return cClient.isPresent() && cServer.isPresent() && sClient == sServer && (MinestuckConfig.SERVER.allowSecondaryConnections.get() || cClient.get() == cServer.get())	//Reconnect within session
				|| cClient.isEmpty() && cServer.isEmpty() && !(sClient != null && sClient.locked) && !(sServer != null && sServer.locked);	//Connect with a new player and potentially create a main connection
	}
	
	Session getSessionForConnecting(PlayerIdentifier client, PlayerIdentifier server) throws MergeResult.SessionMergeException
	{
		if(!canConnect(client, server))
			throw MergeResult.GENERIC_FAIL.exception();
		
		return prepareSessionFor(client, server);
	}
	
	void onConnectionClosed(ActiveConnection connection)
	{
		SburbConnection sburbConnection = skaianetHandler.getConnection(connection);
		
		Session s = Objects.requireNonNull(getPlayerSession(connection.client()));	//If the connection exists, then there should be a session that contains it
		
		if(sburbConnection != null && !sburbConnection.isMain())
			s.connections.remove(sburbConnection);
		if(sburbConnection == null || !sburbConnection.isMain())
			onConnectionChainBroken(s);
	}
	
	Map<Integer, String> getServerList(PlayerIdentifier client)
	{
		Map<Integer, String> map = new HashMap<>();
		for(PlayerIdentifier server : skaianetHandler.openedServers.getPlayers())
		{
			if(canConnect(client, server))
			{
				map.put(server.getId(), server.getUsername());
			}
		}
		return map;
	}
	
	public static SessionHandler get(MinecraftServer server)
	{
		return SkaianetHandler.get(server).sessionHandler;
	}
	
	public static SessionHandler get(Level level)
	{
		return SkaianetHandler.get(level).sessionHandler;
	}
}