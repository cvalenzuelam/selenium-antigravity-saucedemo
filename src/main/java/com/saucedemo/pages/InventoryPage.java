package com.saucedemo.pages;

import com.saucedemo.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object para la página de Inventario (productos) de SauceDemo
 * URL: https://www.saucedemo.com/inventory.html
 */
public class InventoryPage extends BasePage {

    // ---- Locators ----
    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> itemNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> itemPrices;

    @FindBy(css = "[data-test='add-to-cart-sauce-labs-backpack']")
    private WebElement addBackpackToCart;

    @FindBy(css = "[data-test='add-to-cart-sauce-labs-bike-light']")
    private WebElement addBikeLightToCart;

    @FindBy(css = "[data-test='shopping-cart-link']")
    private WebElement cartIcon;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    // ---- Constructor ----
    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    // ---- Actions ----

    /**
     * Agrega el Backpack al carrito
     */
    public InventoryPage addBackpackToCart() {
        click(addBackpackToCart);
        return this;
    }

    /**
     * Agrega la Bike Light al carrito
     */
    public InventoryPage addBikeLightToCart() {
        click(addBikeLightToCart);
        return this;
    }

    /**
     * Navega al carrito
     */
    public CartPage goToCart() {
        click(cartIcon);
        return new CartPage(driver);
    }

    /**
     * Hace logout
     */
    public LoginPage logout() {
        click(menuButton);
        click(logoutLink);
        return new LoginPage(driver);
    }

    // ---- Assertions ----

    /**
     * Retorna el título de la sección (Products)
     */
    public String getInventoryTitle() {
        return getText(pageTitle);
    }

    /**
     * Retorna la cantidad de productos en la página
     */
    public int getItemCount() {
        return inventoryItems.size();
    }

    /**
     * Retorna el número en el badge del carrito
     */
    public String getCartBadgeCount() {
        try {
            return getText(cartBadge);
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * Espera activamente a que la URL contenga 'inventory.html'.
     * Más robusto en CI que verificar la URL puntualmente sin wait.
     */
    public boolean isLoaded() {
        try {
            waitForUrlToContain("inventory.html");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
