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
import java.util.Collection;

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
            Collection<Player> players = this.server.getAllPlayers();

            if (!players.isEmpty()) {
                final ServerPing.Builder ping = event.getPing().asBuilder()
                        .samplePlayers(
                                players.stream()
                                        .map(player -> new ServerPing.SamplePlayer(
                                                this.serverListEntryBuilder.buildForPlayer(player),
                                                player.getGameProfile().getId()
                                        ))
                                        .toArray(ServerPing.SamplePlayer[]::new)
                        );
                if (config.setMaxPlayers()) ping.maximumPlayers(players.size());
                if (config.setOnlinePlayers()) ping.onlinePlayers(players.size());
                event.setPing(ping.build());
            }
        });
    }
}
