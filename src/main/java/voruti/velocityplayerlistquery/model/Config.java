package voruti.velocityplayerlistquery.model;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder
@Accessors(fluent = true)
public class Config {

    public static final String FILE_NAME = "config.json";

    public static final Config DEFAULT_CONFIG = new ConfigBuilder()
            .serverListEntryFormat("%1$s")
            .maxListEntries(16)
            .replaceOnlinePlayerCount(false)
            .replaceMaxPlayerCount(false)
            .setVersion(false)
            .onlySetUnsetVersion(false)
            .versionName("Unknown")
            .versionProtocol(0)
            .build();


    /**
     * Configure how players are shown in the server list.
     * <ul>
     *      <li>{@code %1$s}: placeholder for the player name</li>
     *      <li>{@code %2$s}: placeholder for the server name, on which that player is currently playing on</li>
     * </ul>
     */
    String serverListEntryFormat;

    /**
     * Configure how many players are shown in the server list.
     * {@code <= 0} for unlimited (for backwards compatibility).
     */
    int maxListEntries;

    /**
     * Should the online player count be replaced with Velocity players?
     * Useful when using <code>ping-passthrough = "all"</code>.
     */
    boolean replaceOnlinePlayerCount;
    /**
     * Should the maximum player count be replaced with the Velocity configured amount?
     * Useful when using <code>ping-passthrough = "all"</code>.
     */
    boolean replaceMaxPlayerCount;

    /**
     * Should version information be set?
     */
    boolean setVersion;
    /**
     * Will only set version information, if the version information is undefined.
     */
    boolean onlySetUnsetVersion;

    /**
     * The version name to be set.
     */
    String versionName;
    /**
     * The protocol number to be set.
     */
    int versionProtocol;
}
