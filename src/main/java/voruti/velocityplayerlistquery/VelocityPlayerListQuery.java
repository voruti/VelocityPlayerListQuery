package voruti.velocityplayerlistquery;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import org.slf4j.Logger;

import java.util.Collection;

@Plugin(
        id = "velocityplayerlistquery",
        name = "VelocityPlayerListQuery",
        version = BuildConstants.VERSION,
        description = "A Velocity plugin that shows current players in the server list.",
        url = "https://github.com/voruti/VelocityPlayerListQuery",
        authors = {"voruti"}
)
public class VelocityPlayerListQuery {

    @Inject
    private Logger logger;

    @Inject
    private ProxyServer server;


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Enabled");
    }

    @Subscribe
    public EventTask onServerPing(ProxyPingEvent event) {
        return EventTask.async(() -> {
            Collection<Player> players = server.getAllPlayers();

            if (!players.isEmpty()) {
                event.setPing(event.getPing().asBuilder()
                        .samplePlayers(
                                players.stream()
                                        .map(player -> new ServerPing.SamplePlayer(player.getGameProfile().getName(),
                                                player.getUniqueId()))
                                        .toArray(ServerPing.SamplePlayer[]::new))
                        .build());
            }
        });
    }
}
