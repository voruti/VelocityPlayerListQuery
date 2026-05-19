package voruti.velocityplayerlistquery.service.serverpingprocessor;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.List;
import lombok.Value;
import lombok.experimental.Accessors;

@Singleton
@Value
@Accessors(fluent = true)
public class ServerPingProcessorRegistry {

  List<ServerPingProcessor> serverPingProcessorList;

  @Inject
  public ServerPingProcessorRegistry(
      FillMissingVersionInfoServerPingProcessor fillMissingVersionInfoServerPingProcessor,
      MaxPlayerCountServerPingProcessor maxPlayerCountServerPingProcessor,
      OnlinePlayerCountServerPingProcessor onlinePlayerCountServerPingProcessor,
      PlayerListServerPingProcessor playerListServerPingProcessor,
      ReplaceVersionInfoServerPingProcessor replaceVersionInfoServerPingProcessor) {
    this.serverPingProcessorList =
        List.of(
            fillMissingVersionInfoServerPingProcessor,
            maxPlayerCountServerPingProcessor,
            onlinePlayerCountServerPingProcessor,
            playerListServerPingProcessor,
            replaceVersionInfoServerPingProcessor);
  }
}
