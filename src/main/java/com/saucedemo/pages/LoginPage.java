package com.saucedemo.pages;

import com.saucedemo.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object para la página de Login de SauceDemo
 * URL: https://www.saucedemo.com
 */
public class LoginPage extends BasePage {

    // ---- Locators ----
    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    // ---- Constructor ----
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // ---- Actions ----

    /**
     * Navega a la URL de SauceDemo
     */
    public LoginPage open(String url) {
        driver.get(url);
        return this;
    }

    /**
     * Escribe el username
     */
    public LoginPage enterUsername(String username) {
        type(usernameField, username);
        return this;
    }

    /**
     * Escribe el password
     */
    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    /**
     * Hace click en el botón Login
     */
    public InventoryPage clickLoginButton() {
        click(loginButton);
        return new InventoryPage(driver);
    }

    /**
     * Login completo en un solo paso
     */
    public InventoryPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    /**
     * Hace login y espera error (para tests negativos)
     */
    public LoginPage loginExpectingError(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        click(loginButton);
        return this;
    }

    // ---- Assertions ----

    /**
     * Retorna el texto del mensaje de error
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /**
     * Verifica si el mensaje de error está visible
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }
}
