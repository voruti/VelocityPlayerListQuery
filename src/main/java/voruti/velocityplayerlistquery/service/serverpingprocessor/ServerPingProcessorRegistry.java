package voruti.velocityplayerlistquery.service.serverpingprocessor;

import lombok.Value;
import lombok.experimental.Accessors;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

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
        this.serverPingProcessorList = List.of(
                fillMissingVersionInfoServerPingProcessor,
                maxPlayerCountServerPingProcessor,
                onlinePlayerCountServerPingProcessor,
                playerListServerPingProcessor,
                replaceVersionInfoServerPingProcessor
        );
    }
}
