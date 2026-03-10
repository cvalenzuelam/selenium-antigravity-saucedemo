package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests para la funcionalidad del Carrito de SauceDemo
 */
public class CartTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod
    public void loginAndAddItems() {
        inventoryPage = new LoginPage(driver)
                .open(ConfigReader.getBaseUrl())
                .login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());
    }

    @Test(description = "Agregar un item y verificarlo en el carrito")
    public void testAddOneItemToCart() {
        CartPage cartPage = inventoryPage
                .addBackpackToCart()
                .goToCart();

        Assert.assertTrue(cartPage.isLoaded(),
                "Debería estar en la página del carrito");
        Assert.assertEquals(cartPage.getCartItemCount(), 1,
                "El carrito debería tener 1 item");
    }

    @Test(description = "Agregar dos items y verificarlos en el carrito")
    public void testAddTwoItemsToCart() {
        CartPage cartPage = inventoryPage
                .addBackpackToCart()
                .addBikeLightToCart()
                .goToCart();

        Assert.assertTrue(cartPage.hasItems(),
                "El carrito debería tener items");
        Assert.assertEquals(cartPage.getCartItemCount(), 2,
                "El carrito debería tener 2 items");
    }

    @Test(description = "Verificar nombre del producto en el carrito")
    public void testProductNameInCart() {
        CartPage cartPage = inventoryPage
                .addBackpackToCart()
                .goToCart();

        Assert.assertTrue(cartPage.getItemNames().contains("Sauce Labs Backpack"),
                "El carrito debería contener 'Sauce Labs Backpack'");
    }

    @Test(description = "El título de la página del carrito debe ser 'Your Cart'")
    public void testCartPageTitle() {
        CartPage cartPage = inventoryPage
                .addBackpackToCart()
                .goToCart();

        Assert.assertEquals(cartPage.getCartTitle(), "Your Cart",
                "El título debería ser 'Your Cart'");
    }
}
