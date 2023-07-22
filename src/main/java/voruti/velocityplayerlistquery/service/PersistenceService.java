package voruti.velocityplayerlistquery.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import voruti.velocityplayerlistquery.model.Config;

import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersistenceService {

    final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @Inject
    Logger logger;

    @Inject
    @DataDirectory
    Path dataDirectory;


    @NonNull
    public Config loadConfig() {
        this.logger.trace("Loading config...");

        try {
            // checks:
            if (!Files.exists(this.dataDirectory)) {
                Files.createDirectories(this.dataDirectory);
            }

            final Path configPath = this.dataDirectory.resolve(Config.FILE_NAME);
            if (!Files.exists(configPath)) {
                this.writeFile(configPath, Config.DEFAULT);
                return Config.DEFAULT;
            }
            if (!Files.isRegularFile(configPath) || !Files.isReadable(configPath)) {
                throw new IllegalStateException("Config file is invalid");
            }

            // load:
            final Config loadedConfig = this.gson.fromJson(
                    Files.readString(configPath),
                    Config.class
            );
            return loadedConfig != null
                    ? loadedConfig
                    : Config.DEFAULT;
        } catch (IOException | IllegalStateException e) {
            this.logger.error("Error while loading config", e);
            return Config.DEFAULT;
        }
    }

    private <T> void writeFile(@NonNull final Path path, @NonNull final T content) throws IOException {
        this.logger.trace("Writing file: {}", path);

        Files.write(path, this.gson.toJson(content).getBytes());
    }
}
