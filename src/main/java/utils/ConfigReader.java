package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads test configuration from config.properties, which is gitignored
 * See config.properties.example for the expected format.
 */
public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException(
                    "config.properties not found in src/test/resources. " +
                    "Copy config.properties.example, rename it to config.properties, " +
                    "and fill in real values."
                );
            }
            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String getLogin() {
        return properties.getProperty("test.login");
    }

    public static String getPassword() {
        return properties.getProperty("test.password");
    }
}