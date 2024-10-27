package voruti.velocityplayerlistquery.service.serverpingprocessor;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import voruti.velocityplayerlistquery.hook.Hooks;
import voruti.velocityplayerlistquery.hook.VanishBridgeHook;
import voruti.velocityplayerlistquery.model.Config;
import voruti.velocityplayerlistquery.model.Config.PlayerListMode;
import voruti.velocityplayerlistquery.service.ConfigService;
import voruti.velocityplayerlistquery.service.ServerListEntryBuilderService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PlayerListServerPingProcessor extends ServerPingProcessor {

    @Inject
    ConfigService configService;

    @Inject
    ProxyServer proxyServer;

    @Inject
    ServerListEntryBuilderService serverListEntryBuilderService;

    @Inject
    Hooks hooks;


    @Override
    public boolean isEnabled() {
        final Config config = this.configService.getConfig();
        final PlayerListMode playerListMode = config.playerListMode();

        return playerListMode != null
                && EnumSet.of(PlayerListMode.ADD, PlayerListMode.REPLACE).contains(playerListMode)
                && config.serverListEntryFormat() != null
                && !this.proxyServer.getAllPlayers().isEmpty();
    }

    @Override
    public void applyToServerPing(@NonNull final ServerPing.Builder serverPing) {
        super.applyToServerPing(serverPing);

        // collect players:
        final Collection<Player> players = this.hooks.vanishBridge()
                .map(VanishBridgeHook::unvanishedPlayers)
                .orElse(this.proxyServer.getAllPlayers());
        final Stream<ServerPing.SamplePlayer> playerStream = players.stream()
                // format players:
                .map(player -> new ServerPing.SamplePlayer(
                        this.serverListEntryBuilderService.buildForPlayer(player),
                        player.getGameProfile().getId()
                ))
                // sort alphabetically:
                .sorted(Comparator.comparing(ServerPing.SamplePlayer::getName));

        final Config config = this.configService.getConfig();
        final int maxListEntries = config.maxListEntries();

        // limit number of players shown in list, if configured:
        final List<ServerPing.SamplePlayer> samplePlayers;
        if (maxListEntries > 0) {
            samplePlayers = playerStream
                    .limit(maxListEntries)
                    .toList();
        } else {
            samplePlayers = playerStream.collect(Collectors.toList());
        }

        if (config.playerListMode() == PlayerListMode.REPLACE) {
            serverPing.clearSamplePlayers();
        }

        serverPing.samplePlayers(samplePlayers.toArray(new ServerPing.SamplePlayer[0]));
    }
}
