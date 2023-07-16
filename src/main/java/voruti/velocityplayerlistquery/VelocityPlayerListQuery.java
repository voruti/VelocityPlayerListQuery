package voruti.velocityplayerlistquery;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import voruti.velocityplayerlistquery.model.Config;
import voruti.velocityplayerlistquery.service.PersistenceService;
import voruti.velocityplayerlistquery.util.ServerListEntryBuilder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Plugin(
        id = "velocityplayerlistquery",
        name = "VelocityPlayerListQuery",
        version = BuildConstants.VERSION,
        description = "A Velocity plugin that shows current players in the server list.",
        url = "https://github.com/voruti/VelocityPlayerListQuery",
        authors = {"voruti"}
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VelocityPlayerListQuery {

    @Inject
    Logger logger;

    @Inject
    ProxyServer server;

    @Inject
    @DataDirectory
    Path dataDirectory;

    ServerListEntryBuilder serverListEntryBuilder;
    Config config;


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        config = new PersistenceService(logger, dataDirectory)
                .loadConfig();
        this.serverListEntryBuilder = new ServerListEntryBuilder(logger, config);

        this.logger.info("Enabled");
    }

    @Subscribe
    public EventTask onServerPing(final ProxyPingEvent event) {
        this.logger.trace("Server ping event received, adding players to server list entry...");

        return EventTask.async(() -> {
            final ServerPing serverPing = event.getPing();
            final boolean versionUnset = serverPing.getVersion() == null ||
                    serverPing.getVersion().getName().equals("Unknown") ||
                    serverPing.getVersion().getProtocol() == 0 ||
                    serverPing.getDescriptionComponent() == null;
            final boolean setVersion = config.setVersion() || (config.onlySetUnsetVersion() && versionUnset);
            // check if server ping is invalid:
            if (versionUnset && !setVersion) {
                this.logger.info("Server ping is invalid, skipping");
                return;
            }

            // collect players:
            final Collection<Player> players = this.server.getAllPlayers();
            if (!players.isEmpty()) {
                final Stream<ServerPing.SamplePlayer> playerStream = players.stream()
                        // format players:
                        .map(player -> new ServerPing.SamplePlayer(
                                this.serverListEntryBuilder.buildForPlayer(player),
                                player.getGameProfile().getId()
                        ))
                        // sort alphabetically:
                        .sorted(Comparator.comparing(ServerPing.SamplePlayer::getName));

                // limit number of players shown in list, if configured:
                final List<ServerPing.SamplePlayer> samplePlayers;
                if (this.config.maxListEntries() > 0) {
                    samplePlayers = playerStream
                            .limit(this.config.maxListEntries())
                            .collect(Collectors.toCollection(ArrayList::new));

                    final int numberOfLeftOutPlayers = players.size() - this.config.maxListEntries();
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
                final ServerPing.Builder ping = serverPing.asBuilder()
                        .samplePlayers(samplePlayers.toArray(new ServerPing.SamplePlayer[0]));
                if (config.setMaxPlayers()) ping.maximumPlayers(this.server.getConfiguration().getShowMaxPlayers());
                if (config.setOnlinePlayers()) ping.onlinePlayers(players.size());
                if (setVersion) ping.version(new ServerPing.Version(config.versionProtocol(), config.versionName()));
                event.setPing(ping.build());
            }
        });
    }
}
