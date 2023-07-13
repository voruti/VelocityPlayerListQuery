package voruti.velocityplayerlistquery.util;

import com.velocitypowered.api.proxy.Player;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import voruti.velocityplayerlistquery.model.Config;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServerListEntryBuilder {

    @NonNull
    Logger logger;
    @NonNull
    Config config;


    public String buildForPlayer(Player player) {
        logger.trace("Building server list entry for player {}...", player.getUsername());
        return String.format(
                this.config.serverListEntryFormat(),
                player.getGameProfile().getName(),
                player.getCurrentServer()
                        .map(serverConnection -> serverConnection.getServerInfo().getName())
                        .orElse("unknown")
        );
    }
}
