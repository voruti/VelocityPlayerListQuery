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
    SayanVanishHook sayanVanishHook;

    @Inject
    public Hooks(ProxyServer server) {
        this.vanishBridgeHook = server
                .getPluginManager()
                .isLoaded("vanishbridge")
                ? new VanishBridgeHook(server)
                : null;
        this.sayanVanishHook = server.getPluginManager().isLoaded("sayanvanish")
                ? new SayanVanishHook(server)
                : null;
    }

    public Optional<VanishBridgeHook> vanishBridge() {
        return Optional.ofNullable(vanishBridgeHook);
    }

    public Optional<SayanVanishHook> sayanVanish() {
        return Optional.ofNullable(sayanVanishHook);
    }
}
