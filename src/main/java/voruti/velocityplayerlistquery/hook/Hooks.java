package voruti.velocityplayerlistquery.hook;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.ProxyServer;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Hooks {

    SayanVanishHook sayanVanishHook;

    @Inject
    public Hooks(ProxyServer server) {
        this.sayanVanishHook = server.getPluginManager().isLoaded("sayanvanish")
                ? new SayanVanishHook(server)
                : null;
    }

    public Optional<SayanVanishHook> sayanVanish() {
        return Optional.ofNullable(sayanVanishHook);
    }
}
