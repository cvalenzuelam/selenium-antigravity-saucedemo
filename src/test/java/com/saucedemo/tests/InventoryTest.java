package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests para la página de Inventario de SauceDemo
 */
public class InventoryTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod
    public void loginBeforeTest() {
        inventoryPage = new LoginPage(driver)
                .open(ConfigReader.getBaseUrl())
                .login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());
    }

    @Test(description = "Verificar que la página de inventario carga correctamente")
    public void testInventoryPageLoads() {
        Assert.assertTrue(inventoryPage.isLoaded(),
                "La URL debería contener 'inventory.html'");
        Assert.assertEquals(inventoryPage.getInventoryTitle(), "Products",
                "El título de la página debería ser 'Products'");
    }

    @Test(description = "Verificar que se muestran 6 productos")
    public void testInventoryHasSixProducts() {
        Assert.assertEquals(inventoryPage.getItemCount(), 6,
                "Deberían mostrarse exactamente 6 productos");
    }

    @Test(description = "Agregar un producto al carrito actualiza el badge")
    public void testAddOneItemUpdatesCartBadge() {
        inventoryPage.addBackpackToCart();
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), "1",
                "El badge del carrito debería mostrar '1'");
    }

    @Test(description = "Agregar dos productos al carrito actualiza el badge")
    public void testAddTwoItemsUpdateCartBadge() {
        inventoryPage.addBackpackToCart().addBikeLightToCart();
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), "2",
                "El badge del carrito debería mostrar '2'");
    }
}
