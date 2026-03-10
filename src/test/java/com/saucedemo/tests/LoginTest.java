package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests para la funcionalidad de Login de SauceDemo
 */
public class LoginTest extends BaseTest {

    @Test(description = "Login exitoso con credenciales válidas")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = loginPage
                .open(ConfigReader.getBaseUrl())
                .login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());

        Assert.assertTrue(inventoryPage.isLoaded(),
                "Debería redirigir a la página de inventario después del login");
        Assert.assertEquals(inventoryPage.getInventoryTitle(), "Products",
                "El título debería ser 'Products'");
    }

    @Test(description = "Login fallido con credenciales inválidas")
    public void testFailedLoginWithInvalidCredentials() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(ConfigReader.getBaseUrl())
                 .loginExpectingError(
                         ConfigReader.getInvalidUsername(),
                         ConfigReader.getInvalidPassword()
                 );

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Debería mostrar un mensaje de error");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"),
                "El mensaje de error debería indicar credenciales incorrectas");
    }

    @Test(description = "Login fallido con usuario bloqueado")
    public void testLockedOutUser() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(ConfigReader.getBaseUrl())
                 .loginExpectingError(
                         ConfigReader.getLockedUsername(),
                         ConfigReader.getLockedPassword()
                 );

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Debería mostrar un mensaje de error");
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"),
                "El mensaje de error debería indicar que el usuario está bloqueado");
    }

    @Test(description = "Login fallido sin ingresar username")
    public void testLoginWithoutUsername() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(ConfigReader.getBaseUrl())
                 .loginExpectingError("", ConfigReader.getValidPassword());

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Debería mostrar un mensaje de error");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"),
                "El mensaje de error debería indicar que el username es requerido");
    }
}
