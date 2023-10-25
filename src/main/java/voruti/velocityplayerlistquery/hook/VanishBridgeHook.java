package voruti.velocityplayerlistquery.hook;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.loapu.vanishbridge.api.model.VanishBridgePlayer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import dev.loapu.vanishbridge.api.VanishBridgeProvider;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;

@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VanishBridgeHook {
	@Inject
	ProxyServer server;
	
	public boolean hooked() {
		return server.getPluginManager().isLoaded("vanishbridge");
	}
	
	public Collection<Player> unvanishedPlayers() {
		var allPlayers = server.getAllPlayers();
		Collection<Player> unvanishedPlayers = new ArrayList<>();
		var vanishedPlayers = VanishBridgeProvider.get().vanishedPlayers();
		allPlayers.forEach(player -> {
			if (vanishedPlayers.stream().noneMatch(vanishedPlayer -> vanishedPlayer.uuid().equals(player.getUniqueId()))) {
				unvanishedPlayers.add(player);
			}
		});
		
		return unvanishedPlayers;
	}
	
	public int unvanishedPlayerCount() {
		return unvanishedPlayers().size();
	}
}
