package voruti.velocityplayerlistquery.service.serverpingprocessor;

import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.proxy.server.ServerPing.Version;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import voruti.velocityplayerlistquery.model.Config;
import voruti.velocityplayerlistquery.model.exception.InvalidServerPingException;
import voruti.velocityplayerlistquery.service.ConfigService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class FillMissingVersionInfoServerPingProcessor extends ServerPingProcessor {

    @Inject
    ConfigService configService;

    @Inject
    ReplaceVersionInfoServerPingProcessor replaceVersionService;


    @Override
    public boolean isEnabled() {
        return this.configService.getConfig().fillMissingVersionInfo()
                && !this.replaceVersionService.isEnabled();
    }

    @Override
    public void applyToServerPing(@NonNull final ServerPing.Builder serverPing) {
        super.applyToServerPing(serverPing);

        final Config config = this.configService.getConfig();
        final Optional<Version> optionalCurrentVersion = Optional.ofNullable(serverPing.getVersion());

        // get final version name:
        final String resultName = optionalCurrentVersion
                .map(Version::getName)
                .orElseGet(config::versionName);

        // throw if still null:
        if (resultName == null) {
            throw new InvalidServerPingException("Missing version name in config and server ping");
        }

        // build final version object:
        serverPing.version(new Version(
                optionalCurrentVersion
                        .map(Version::getProtocol)
                        .orElseGet(config::versionProtocol),
                resultName
        ));
    }
}
