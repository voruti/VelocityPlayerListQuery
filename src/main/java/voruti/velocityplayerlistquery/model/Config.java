package voruti.velocityplayerlistquery.model;

import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class Config {

    public static final String FILE_NAME = "config.json";

    public static final Config DEFAULT_CONFIG = new Config(
            "%1$s",
            16,
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

    /**
     * Configure how many players are shown in the server list. {@code <= 0} for unlimited (for backwards compatibility).
     */
    int maxListEntries;
    /**
     * Should the online Players be replaced with Velocity Players?
     */
    boolean setOnlinePlayers;
    /**
     * Should the maximum Player count be replaced with the Velocity configured amount?
     */
    boolean setMaxPlayers;
}
