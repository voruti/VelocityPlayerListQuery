package voruti.velocityplayerlistquery.model;

import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class Config {

    public static final String FILE_NAME = "config.json";

    public static final Config DEFAULT_CONFIG = new Config(
            "%1$s",
            false,
            false
    );


    /**
     * Configure how players are shown in the server list.
     * <ul>
     *      <li>{@code %1$s}: placeholder for the player name</li>
     *      <li>{@code %2$s}: placeholder for the server name, on which that player is currently playing on</li>
     * </ul>
     */
    String serverListEntryFormat;
    boolean setOnlinePlayers;
    boolean setMaxPlayers;
}
