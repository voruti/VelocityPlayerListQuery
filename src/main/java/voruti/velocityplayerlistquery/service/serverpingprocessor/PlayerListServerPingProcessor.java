package voruti.velocityplayerlistquery.service.serverpingprocessor;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import voruti.velocityplayerlistquery.service.ConfigService;
import voruti.velocityplayerlistquery.service.ServerListEntryBuilderService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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


    @Override
    public boolean isEnabled() {
        return this.configService.getConfig().serverListEntryFormat() != null
                && !this.proxyServer.getAllPlayers().isEmpty();
        // TODO: 3 options: keep player list, add to player list, replace player list; currently: add to
        // TODO: .clearSamplePlayers()
    }

    @Override
    public void applyToServerPing(@NonNull final ServerPing.Builder serverPing) {
        super.applyToServerPing(serverPing);

        // collect players:
        final Collection<Player> players = this.proxyServer.getAllPlayers();
        final Stream<ServerPing.SamplePlayer> playerStream = players.stream()
                // format players:
                .map(player -> new ServerPing.SamplePlayer(
                        this.serverListEntryBuilderService.buildForPlayer(player),
                        player.getGameProfile().getId()
                ))
                // sort alphabetically:
                .sorted(Comparator.comparing(ServerPing.SamplePlayer::getName));

        // limit number of players shown in list, if configured:
        final List<ServerPing.SamplePlayer> samplePlayers;
        final int maxListEntries = this.configService.getConfig().maxListEntries();
        if (maxListEntries > 0) {
            samplePlayers = playerStream
                    .limit(maxListEntries)
                    .collect(Collectors.toCollection(ArrayList::new));

            final int numberOfLeftOutPlayers = players.size() - maxListEntries;
            if (numberOfLeftOutPlayers > 0) {
                samplePlayers.add(
                        new ServerPing.SamplePlayer(
                                String.format("...and %d more", numberOfLeftOutPlayers),
                                UUID.randomUUID()
                        )
                );
            }
        } else {
            samplePlayers = playerStream.collect(Collectors.toList());
        }

        serverPing.samplePlayers(samplePlayers.toArray(new ServerPing.SamplePlayer[0]));
    }
}
