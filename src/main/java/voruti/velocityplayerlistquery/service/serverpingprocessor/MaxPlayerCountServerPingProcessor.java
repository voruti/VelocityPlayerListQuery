package voruti.velocityplayerlistquery.service.serverpingprocessor;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import voruti.velocityplayerlistquery.service.ConfigService;

@Singleton
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class MaxPlayerCountServerPingProcessor extends ServerPingProcessor {

  @Inject ConfigService configService;

  @Inject ProxyServer proxyServer;

  @Override
  public boolean isEnabled() {
    return this.configService.getConfig().replaceMaxPlayerCount();
  }

  @Override
  public void applyToServerPing(@NonNull final ServerPing.Builder serverPing) {
    super.applyToServerPing(serverPing);

    serverPing.maximumPlayers(this.proxyServer.getConfiguration().getShowMaxPlayers());
  }
}
