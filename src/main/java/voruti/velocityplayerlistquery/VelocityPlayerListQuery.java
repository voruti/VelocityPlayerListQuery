package voruti.velocityplayerlistquery;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
        id = "velocityplayerlistquery",
        name = "VelocityPlayerListQuery",
        version = BuildConstants.VERSION,
        description = "A Velocity plugin that shows current players in the server list.",
        url = "https://github.com/voruti/VelocityPlayerListQuery",
        authors = {"voruti"}
)
public class VelocityPlayerListQuery {

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
