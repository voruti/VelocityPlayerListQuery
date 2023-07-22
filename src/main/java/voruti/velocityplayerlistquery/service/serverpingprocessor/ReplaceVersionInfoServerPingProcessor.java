package voruti.velocityplayerlistquery.service.serverpingprocessor;

import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.proxy.server.ServerPing.Version;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import voruti.velocityplayerlistquery.model.Config;
import voruti.velocityplayerlistquery.service.ConfigService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReplaceVersionInfoServerPingProcessor extends ServerPingProcessor {

    @Inject
    ConfigService configService;


    @Override
    public boolean isEnabled() {
        final Config config = this.configService.getConfig();

        return config.replaceVersionInfo()
                && config.versionName() != null;
    }

    @Override
    public void applyToServerPing(@NonNull final ServerPing.Builder serverPing) {
        super.applyToServerPing(serverPing);

        final Config config = this.configService.getConfig();

        serverPing.version(new Version(
                config.versionProtocol(),
                config.versionName() // should not be null, checked in isEnabled()
        ));
    }
}
