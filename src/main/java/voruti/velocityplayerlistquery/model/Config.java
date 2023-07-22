package voruti.velocityplayerlistquery.model;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import org.checkerframework.checker.nullness.qual.Nullable;

@Value
@Builder
@Accessors(fluent = true)
public class Config {

    public static final String FILE_NAME = "config.json";

    public static final Config DEFAULT = new ConfigBuilder()
            .serverListEntryFormat("%1$s")
            .maxListEntries(16)
            .replaceOnlinePlayerCount(false)
            .replaceMaxPlayerCount(false)
            .fillMissingVersionInfo(false)
            .replaceVersionInfo(false)
            .versionProtocol(4) // 1.7.2
            .versionName("Velocity")
            .build();


    /**
     * Configure how players are shown in the server list.
     * <ul>
     *      <li>{@code %1$s}: placeholder for the player name</li>
     *      <li>{@code %2$s}: placeholder for the server name, on which that player is currently playing on</li>
     * </ul>
     */
    @Nullable
    String serverListEntryFormat;

    /**
     * Configure how many players are shown in the server list.
     * {@code <= 0} for unlimited (for backwards compatibility).
     */
    int maxListEntries;

    /**
     * Should the online player count be replaced with the number of players currently connected to Velocity?
     * Useful when using <code>ping-passthrough = "all"</code>.
     */
    boolean replaceOnlinePlayerCount;
    /**
     * Should the maximum player count be replaced with the Velocity configured amount?
     * Useful when using <code>ping-passthrough = "all"</code>.
     */
    boolean replaceMaxPlayerCount;

    /**
     * Should the version information be replaced with
     * what is set in {@link #versionProtocol} and {@link #versionName},
     * <strong>when no version is detected</strong>?
     * Useful when the backend server/s is/are unavailable or still starting.
     */
    boolean fillMissingVersionInfo;
    /**
     * Should the version information <strong>always</strong> be replaced with
     * what is set in {@link #versionProtocol} and {@link #versionName}?
     * Extends {@link #fillMissingVersionInfo}.
     * Useful when using <code>ping-passthrough = "all"</code>.
     */
    boolean replaceVersionInfo;
    /**
     * The protocol number to be set.
     * Used by {@link #fillMissingVersionInfo} and {@link #replaceVersionInfo}.
     */
    int versionProtocol;
    /**
     * The version name to be set.
     * Used by {@link #fillMissingVersionInfo} and {@link #replaceVersionInfo}.
     */
    @Nullable
    String versionName;
}
