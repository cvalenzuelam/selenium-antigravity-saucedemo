package com.saucedemo.base;

import com.saucedemo.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * Clase base para todos los tests.
 * Maneja la inicialización y cierre del WebDriver.
 * Adaptada para correr localmente y en CI/CD (headless automático).
 */
public class BaseTest {

    protected WebDriver driver;

    /**
     * Detecta si el proceso se está ejecutando en un entorno CI.
     * GitHub Actions, Jenkins, CircleCI, etc. setean la variable CI=true.
     */
    private boolean isRunningInCI() {
        String ciEnv = System.getenv("CI");
        return "true".equalsIgnoreCase(ciEnv);
    }

    @BeforeMethod
    public void setUp() {
        String browser = ConfigReader.getBrowser().toLowerCase();

        // El headless se activa si: config.properties lo dice O si estamos en CI
        boolean headless = ConfigReader.isHeadless() || isRunningInCI();

        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                    firefoxOptions.addArguments("--width=1920");
                    firefoxOptions.addArguments("--height=1080");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();

                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }

                // Opciones críticas para CI (y buenas prácticas en general):
                chromeOptions.addArguments("--no-sandbox");               // Requerido en contenedores Linux
                chromeOptions.addArguments("--disable-dev-shm-usage");    // Evita crashes por memoria compartida limitada
                chromeOptions.addArguments("--disable-gpu");               // Sin GPU en servidores
                chromeOptions.addArguments("--window-size=1920,1080");    // Ventana fija, evita elementos fuera de viewport
                chromeOptions.addArguments("--disable-extensions");        // Sin extensiones que interfieran
                chromeOptions.addArguments("--disable-infobars");          // Sin barras de notificación
                chromeOptions.addArguments("--disable-notifications");     // Sin popups de notificaciones
                chromeOptions.addArguments("--disable-popup-blocking");    // Sin bloqueo de popups
                chromeOptions.addArguments("--remote-allow-origins=*");   // Evita CORS issues en CDP

                // Deshabilitar animaciones para tests más rápidos y estables
                chromeOptions.addArguments("--disable-animations");
                chromeOptions.addArguments("--force-prefers-reduced-motion");

                driver = new ChromeDriver(chromeOptions);
                break;
        }

        // IMPORTANTE: NO usar implicitlyWait junto con explicitWait.
        // Pueden generar comportamientos impredecibles (espera doble).
        // Usamos SOLO explicit waits (WebDriverWait) en BasePage.
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
