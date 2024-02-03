package voruti.velocityplayerlistquery.hook;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.loapu.vanishbridge.api.VanishBridgeProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VanishBridgeHook {

    ProxyServer server;


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
