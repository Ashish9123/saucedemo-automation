package com.saucedemo.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage {

    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isItemInCart() {
        return !driver.findElements(cartItems).isEmpty();
    }

    public List<String> getItemNames() {
        return driver.findElements(itemNames)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public int getItemCount() {
        return driver.findElements(cartItems).size();
    }

    public void removeProduct(String productName) {
        String buttonId = "remove-" +
                productName.toLowerCase()
                        .replace(" ", "-");

        wait.until(
                ExpectedConditions.elementToBeClickable(By.id(buttonId))
        ).click();
    }

    public CheckoutPage proceedToCheckout() {
        wait.until(
                ExpectedConditions.elementToBeClickable(checkoutButton)
        ).click();

        wait.until(
                ExpectedConditions.urlContains("checkout-step-one")
        );

        return new CheckoutPage(driver);
    }
}