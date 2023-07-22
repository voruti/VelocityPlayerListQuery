package voruti.velocityplayerlistquery.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import voruti.velocityplayerlistquery.model.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PersistenceService {

    @NonNull
    Logger logger;
    @NonNull
    Path dataDirectory;

    @NonNull
    final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();


    public Config loadConfig() {
        logger.trace("Loading config...");

        try {
            // checks:
            if (!Files.exists(dataDirectory)) {
                Files.createDirectories(dataDirectory);
            }

            final Path configPath = dataDirectory.resolve(Config.FILE_NAME);
            if (!Files.exists(configPath)) {
                this.writeFile(configPath, Config.DEFAULT);
                return Config.DEFAULT;
            }
            if (!Files.isRegularFile(configPath) || !Files.isReadable(configPath)) {
                throw new IllegalStateException("Config file is invalid");
            }

            // load:
            return gson.fromJson(
                    Files.readString(configPath),
                    Config.class
            );
        } catch (IOException | IllegalStateException e) {
            logger.error("Error while loading config", e);
            return Config.DEFAULT;
        }
    }

    private <T> void writeFile(Path path, T content) throws IOException {
        logger.trace("Writing file: {}", path);

        Files.write(path, gson.toJson(content).getBytes());
    }
}
