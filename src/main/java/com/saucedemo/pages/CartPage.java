package com.saucedemo.pages;

import com.saucedemo.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object para la página del Carrito de SauceDemo
 * URL: https://www.saucedemo.com/cart.html
 */
public class CartPage extends BasePage {

    // ---- Locators ----
    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> cartItemNames;

    @FindBy(css = "[data-test='continue-shopping']")
    private WebElement continueShoppingButton;

    @FindBy(css = "[data-test='checkout']")
    private WebElement checkoutButton;

    // ---- Constructor ----
    public CartPage(WebDriver driver) {
        super(driver);
    }

    // ---- Actions ----

    /**
     * Vuelve a la página de inventario
     */
    public InventoryPage continueShopping() {
        click(continueShoppingButton);
        return new InventoryPage(driver);
    }

    /**
     * Procede al checkout
     */
    public CartPage clickCheckout() {
        click(checkoutButton);
        return this;
    }

    // ---- Assertions ----

    /**
     * Retorna el título de la sección (Your Cart)
     */
    public String getCartTitle() {
        return getText(pageTitle);
    }

    /**
     * Retorna la cantidad de items en el carrito
     */
    public int getCartItemCount() {
        return cartItems.size();
    }

    /**
     * Espera activamente a que la URL contenga 'cart.html'.
     * Más robusto en CI que verificar la URL puntualmente sin wait.
     */
    public boolean isLoaded() {
        try {
            waitForUrlToContain("cart.html");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el carrito tiene items
     */
    public boolean hasItems() {
        return !cartItems.isEmpty();
    }

    /**
     * Retorna los nombres de los items en el carrito
     */
    public List<String> getItemNames() {
        return cartItemNames.stream()
                .map(WebElement::getText)
                .collect(java.util.stream.Collectors.toList());
    }
}
