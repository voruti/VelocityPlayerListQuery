package voruti.velocityplayerlistquery.service;

import com.velocitypowered.api.proxy.Player;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServerListEntryBuilderService {

    @Inject
    Logger logger;

    @Inject
    ConfigService configService;


    @NonNull
    public String buildForPlayer(@NonNull final Player player) {
        logger.trace("Building server list entry for player {}...", player.getUsername());
        return String.format(
                this.configService.getConfig()
                        .serverListEntryFormat(),
                player.getGameProfile().getName(),
                player.getCurrentServer()
                        .map(serverConnection -> serverConnection.getServerInfo().getName())
                        .orElse("unknown")
        );
    }
}
