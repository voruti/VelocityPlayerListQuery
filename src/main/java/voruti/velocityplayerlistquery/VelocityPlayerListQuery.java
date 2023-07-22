package voruti.velocityplayerlistquery;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.server.ServerPing;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import voruti.velocityplayerlistquery.model.exception.InvalidServerPingException;
import voruti.velocityplayerlistquery.service.ConfigService;
import voruti.velocityplayerlistquery.service.serverpingprocessor.ServerPingProcessor;
import voruti.velocityplayerlistquery.service.serverpingprocessor.ServerPingProcessorRegistry;

import javax.inject.Inject;
import java.util.Optional;

@Plugin(
        id = "velocityplayerlistquery",
        name = "VelocityPlayerListQuery",
        version = BuildConstants.VERSION,
        description = "A Velocity plugin that shows current players in the server list.",
        url = "https://github.com/voruti/VelocityPlayerListQuery",
        authors = {"voruti"}
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VelocityPlayerListQuery {

    @Inject
    Logger logger;

    @Inject
    ConfigService configService;

    @Inject
    ServerPingProcessorRegistry serverPingProcessorRegistry;


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent ignored) {
        this.configService.reloadConfig();

        this.logger.info("Enabled");
    }

    @Subscribe
    public void onVelocityReload(ProxyReloadEvent ignored) {
        this.configService.reloadConfig();
    }


    @Subscribe
    public EventTask onServerPing(final ProxyPingEvent event) {
        this.logger.trace("Server ping event received");

        return EventTask.async(() -> {
            final ServerPing serverPing = event.getPing();

            // check if server ping is invalid:
            // Version and description (aka. MOTD) are both required to create a new ServerPing instance.
            // Version could be set later on by this plugin, but description can't.
            if (serverPing.getDescriptionComponent() == null) {
                this.logger.info("Server ping is invalid, skipping");
                return;
            }

            try {
                this.processPing(serverPing)
                        .ifPresent(event::setPing);
            } catch (InvalidServerPingException e) {
                this.logger.debug("Caught InvalidServerPingException", e);
                this.logger.info("Server ping is invalid, skipping");
            }
        });
    }

    @NonNull
    private Optional<ServerPing> processPing(@NonNull final ServerPing serverPing) {
        final ServerPing.Builder builder = serverPing.asBuilder();

        // apply all server ping processors:
        boolean isModified = false;
        for (ServerPingProcessor processor : this.serverPingProcessorRegistry.serverPingProcessorList()) {
            if (processor.isEnabled()) {
                processor.applyToServerPing(builder);
                isModified = true;
            }
        }

        if (builder.getVersion() == null) {
            throw new InvalidServerPingException("Missing version info in server ping even after processing");
        }

        return isModified
                ? Optional.of(builder.build())
                : Optional.empty();
    }
}
