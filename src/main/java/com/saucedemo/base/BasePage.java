package com.saucedemo.base;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import com.saucedemo.utils.ConfigReader;

/**
 * Clase base para todos los Page Objects.
 * Provee métodos helper comunes para interacciones con el browser.
 * Implementa estrategias de retry para elementos stale (crítico en CI).
 */
public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final int MAX_RETRY = 3;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Espera a que el elemento sea clickeable y hace click.
     * Incluye scroll al elemento antes de clickear (necesario en headless
     * cuando el viewport es distinto al local).
     * Reintenta hasta MAX_RETRY veces ante StaleElementReferenceException.
     */
    protected void click(WebElement element) {
        int attempts = 0;
        while (attempts < MAX_RETRY) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element));
                scrollIntoView(element);
                element.click();
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                if (attempts == MAX_RETRY) {
                    throw new RuntimeException("El elemento siguió siendo stale después de " + MAX_RETRY + " intentos.", e);
                }
            }
        }
    }

    /**
     * Escribe texto en un campo. Espera visibilidad, limpia y escribe.
     * Reintenta ante StaleElementReferenceException.
     */
    protected void type(WebElement element, String text) {
        int attempts = 0;
        while (attempts < MAX_RETRY) {
            try {
                wait.until(ExpectedConditions.visibilityOf(element));
                element.clear();
                element.sendKeys(text);
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                if (attempts == MAX_RETRY) {
                    throw new RuntimeException("El elemento siguió siendo stale después de " + MAX_RETRY + " intentos.", e);
                }
            }
        }
    }

    /**
     * Retorna el texto de un elemento visible.
     */
    protected String getText(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getText();
    }

    /**
     * Verifica si un elemento está visible. Retorna false en lugar de lanzar excepción.
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Espera a que la URL contenga el fragmento esperado.
     * Más confiable que verificar elementos inmediatamente después de navegación.
     */
    protected void waitForUrlToContain(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    /**
     * Espera a que un elemento con un By sea visible.
     */
    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Espera a que un elemento desaparezca (útil para loaders/spinners).
     */
    protected void waitForElementToDisappear(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Hace scroll al elemento usando JavaScript.
     * Previene ElementNotInteractableException en headless con viewport pequeño.
     */
    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", element
        );
    }

    /**
     * Click via JavaScript como fallback cuando el click normal falla.
     * Útil para elementos cubiertos por overlays en CI.
     */
    protected void jsClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Retorna el título de la página actual.
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Retorna la URL actual.
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
