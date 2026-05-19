package voruti.velocityplayerlistquery.hook;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import java.util.ArrayList;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.sayandev.sayanvanish.api.SayanVanishAPI;
import org.sayandev.sayanvanish.api.User;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SayanVanishHook {

  ProxyServer server;

  public Collection<Player> unvanishedPlayers() {
    var api = SayanVanishAPI.getInstance();
    var players = server.getAllPlayers();
    var vanishedUsers = api.getVanishedUsers();
    Collection<Player> vanishedPlayers = new ArrayList<>();

    for (User user : vanishedUsers) {
      var player = server.getPlayer(user.getUniqueId());
      player.ifPresent(vanishedPlayers::add);
    }

    Collection<Player> unvanishedPlayers = new ArrayList<>(players);
    unvanishedPlayers.removeAll(vanishedPlayers);

    return unvanishedPlayers;
  }

  public int unvanishedPlayerCount() {
    return unvanishedPlayers().size();
  }
}
