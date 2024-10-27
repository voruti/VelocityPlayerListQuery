# VelocityPlayerListQuery

A Velocity plugin that shows current players in the server list.

Inspired by <https://github.com/rtm516/SimplePlayerListQuery>.

## Configuration options

| option                     | default       | description                                                                                                                                                                                                                                                                                                                                                                   |
| -------------------------- | ------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `playerListMode`           | `"REPLACE"`   | Configure how players are shown in the player list.<ul><li>`"UNCHANGED"`: Keep the player list as it is.</li><li>`"ADD"`: Add players to the player list. `serverListEntryFormat` and `maxListEntries` are only applied for the added players.</li><li>`"REPLACE"`: Replace the player list with players according to `serverListEntryFormat` and `maxListEntries`.</li></ul> |
| `serverListEntryFormat`    | `"%1$s"`      | Configure how players are shown in the server list.<ul><li>`%1$s`: placeholder for the player name</li><li>`%2$s`: placeholder for the server name, on which that player is currently playing on</li></ul>                                                                                                                                                                    |
| `maxListEntries`           | `16`          | Configure how many players are shown in the server list. `<= 0` for unlimited (for backwards compatibility).                                                                                                                                                                                                                                                                  |
| `replaceOnlinePlayerCount` | `false`       | Should the online player count be replaced with the number of players currently connected to Velocity? Useful when using `ping-passthrough = "all"`.                                                                                                                                                                                                                          |
| `replaceMaxPlayerCount`    | `false`       | Should the maximum player count be replaced with the Velocity configured amount? Useful when using `ping-passthrough = "all"`.                                                                                                                                                                                                                                                |
| `fillMissingVersionInfo`   | `false`       | Should the version information be replaced with what is set in `versionProtocol` and `versionName`, <strong>when no version is detected</strong>? Useful when the backend server/s is/are unavailable or still starting.                                                                                                                                                      |
| `replaceVersionInfo`       | `false`       | Should the version information <strong>always</strong> be replaced with what is set in `versionProtocol` and `versionName`? Extends `fillMissingVersionInfo`. Useful when using `ping-passthrough = "all"`.                                                                                                                                                                   |
| `versionProtocol`          | `4` (= 1.7.2) | The protocol number to be set. You can find all available protocol versions at <https://minecraft.fandom.com/wiki/Protocol_version#Java_Edition_2>. Used by `fillMissingVersionInfo` and `replaceVersionInfo`.                                                                                                                                                                 |
| `versionName`              | `"Velocity"`  | The version name to be set. Used by `fillMissingVersionInfo` and `replaceVersionInfo`.                                                                                                                                                                                                                                                                                        |
