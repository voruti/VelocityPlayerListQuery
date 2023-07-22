package voruti.velocityplayerlistquery.service.serverpingprocessor;

import com.velocitypowered.api.proxy.server.ServerPing;
import lombok.NonNull;

public abstract class ServerPingProcessor {

    /**
     * Evaluates and returns whether itself is enabled.
     *
     * @return whether itself is enabled
     */
    public abstract boolean isEnabled();

    /**
     * Applies the changes immanent to this processor to the given {@code serverPing}.
     *
     * @param serverPing the server ping to process
     */
    public void applyToServerPing(@NonNull final ServerPing.Builder serverPing) {
        if (!this.isEnabled()) {
            throw new IllegalStateException("Server ping processor is not enabled");
        }
    }
}
