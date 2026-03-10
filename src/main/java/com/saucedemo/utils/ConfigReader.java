package com.saucedemo.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Lee configuración desde config.properties.
 * Las variables de entorno tienen prioridad — esto permite que CI/CD
 * sobreescriba valores sin tocar el archivo (p.ej: BROWSER=chrome, HEADLESS=true).
 *
 * Orden de prioridad: Variable de entorno > config.properties > valor por defecto
 */
public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_PATH = "src/test/resources/config.properties";

    static {
        try {
            FileInputStream fis = new FileInputStream(CONFIG_PATH);
            properties = new Properties();
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar el archivo config.properties: " + e.getMessage());
        }
    }

    /**
     * Obtiene un valor buscando primero en variables de entorno, luego en properties.
     * Las variables de entorno usan el mismo nombre en MAYÚSCULAS con puntos reemplazados por guiones bajos.
     * Ejemplo: "base.url" → env var "BASE_URL"
     */
    private static String get(String key, String defaultValue) {
        // Convertir "base.url" → "BASE_URL" para buscar en entorno
        String envKey = key.toUpperCase().replace(".", "_");
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }
        return properties.getProperty(key, defaultValue);
    }

    public static String getBaseUrl() {
        return get("base.url", "https://www.saucedemo.com");
    }

    public static String getValidUsername() {
        return get("valid.username", "standard_user");
    }

    public static String getValidPassword() {
        return get("valid.password", "secret_sauce");
    }

    public static String getInvalidUsername() {
        return get("invalid.username", "wrong_user");
    }

    public static String getInvalidPassword() {
        return get("invalid.password", "wrong_password");
    }

    public static String getLockedUsername() {
        return get("locked.username", "locked_out_user");
    }

    public static String getLockedPassword() {
        return get("locked.password", "secret_sauce");
    }

    public static String getBrowser() {
        return get("browser", "chrome");
    }

    /**
     * Headless se activa si config.properties lo dice O si la variable de entorno HEADLESS=true.
     * BaseTest también lo fuerza si detecta CI=true.
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless", "false"));
    }

    public static int getImplicitWait() {
        return Integer.parseInt(get("implicit.wait", "0"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(get("explicit.wait", "15"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(get("page.load.timeout", "30"));
    }
}
