
package com.airline.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = new Properties();
    private static boolean loaded = false;

    private ConfigReader() {
        // Prevent object creation.
    }

    public static synchronized void loadConfig() {
        Path configPath = Paths.get(
                System.getProperty("user.dir"),
                "src",
                "test",
                "resources",
                "config.properties"
        );

        try (InputStream input = Files.newInputStream(configPath)) {
            Properties loadedProperties = new Properties();
            loadedProperties.load(input);

            properties.clear();
            properties.putAll(loadedProperties);
            loaded = true;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to load configuration file: " + configPath, e
            );
        }
    }

    public static String getProperty(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException(
                    "Configuration key cannot be blank"
            );
        }

        String systemValue = System.getProperty(key);

        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }

        if (!loaded) {
            loadConfig();
        }

        String value = properties.getProperty(key);

        return value == null || value.isBlank()
                ? null
                : value.trim();
    }

    public static String getRequiredProperty(String key) {
        String value = getProperty(key);

        if (value == null) {
            throw new IllegalStateException(
                    "Required configuration is missing: " + key
            );
        }

        return value;
    }
}
