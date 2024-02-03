package voruti.velocityplayerlistquery.hook;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Hooks {

    VanishBridgeHook vanishBridgeHook;


    @Inject
    public Hooks(ProxyServer server) {
        this.vanishBridgeHook = server.getPluginManager().isLoaded("vanishbridge")
                ? new VanishBridgeHook(server)
                : null;
    }


    public Optional<VanishBridgeHook> vanishBridge() {
        return Optional.ofNullable(vanishBridgeHook);
    }
}
