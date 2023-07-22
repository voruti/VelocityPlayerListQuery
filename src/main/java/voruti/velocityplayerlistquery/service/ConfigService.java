package voruti.velocityplayerlistquery.service;

import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import voruti.velocityplayerlistquery.model.Config;

import javax.inject.Singleton;

@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigService {

    @Inject
    Logger logger;

    @Inject
    PersistenceService persistenceService;

    @Nullable
    Config cachedConfig;


    @NonNull
    public Config getConfig() {
        if (this.cachedConfig == null) {
            this.reloadConfig();
        }

        return this.cachedConfig;
    }

    public void reloadConfig() {
        logger.trace("Loading config from persistence service...");

        this.cachedConfig = this.persistenceService.loadConfig();
    }
}
