package com.saucedemo.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class ProductsPage extends BasePage {

    private final By pageTitle = By.className("title");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");
    private final By productNames = By.className("inventory_item_name");
    private final By sortDropdown = By.className("product_sort_container");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public ProductsPage addProductToCart(String productName) {
        String productSlug = productName
                .toLowerCase()
                .replace(" ", "-");

        By addToCartButton = By.id("add-to-cart-" + productSlug);

        click(addToCartButton);

        return this;
    }

    public int getCartItemCount() {
        List<org.openqa.selenium.WebElement> badges =
                driver.findElements(cartBadge);

        if (badges.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(badges.get(0).getText());
    }

    public List<String> getAllProductNames() {
        return driver.findElements(productNames)
                .stream()
                .map(element -> element.getText())
                .collect(Collectors.toList());
    }

    public ProductsPage sortBy(String visibleOptionText) {
        Select sortSelect = new Select(waitVisible(sortDropdown));
        sortSelect.selectByVisibleText(visibleOptionText);

        return this;
    }

    public CartPage goToCart() {
        click(cartLink);

        wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions
                        .urlContains("cart")
        );

        return new CartPage(driver);
    }
}