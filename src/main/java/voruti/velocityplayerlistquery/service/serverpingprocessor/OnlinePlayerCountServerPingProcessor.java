package voruti.velocityplayerlistquery.service.serverpingprocessor;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import voruti.velocityplayerlistquery.service.ConfigService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OnlinePlayerCountServerPingProcessor extends ServerPingProcessor {

    @Inject
    ConfigService configService;

    @Inject
    ProxyServer proxyServer;


    @Override
    public boolean isEnabled() {
        return this.configService.getConfig().replaceOnlinePlayerCount();
    }

    @Override
    public void applyToServerPing(@NonNull final ServerPing.Builder serverPing) {
        super.applyToServerPing(serverPing);

        serverPing.onlinePlayers(this.proxyServer.getPlayerCount());
    }
}
