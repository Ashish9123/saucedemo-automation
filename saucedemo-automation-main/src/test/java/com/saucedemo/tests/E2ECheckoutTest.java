package com.saucedemo.tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;

public class E2ECheckoutTest extends BaseTest {

    @Test
    public void completeCheckoutFlow() {

        LoginPage loginPage = new LoginPage(driver);

        ProductsPage productsPage = loginPage.loginAs(
                "standard_user",
                "secret_sauce"
        );

        Assert.assertEquals(
                productsPage.getPageTitle(),
                "Products",
                "Products page did not open after login"
        );

        productsPage.addProductToCart(
                "Sauce Labs Backpack"
        );

        Assert.assertEquals(
                productsPage.getCartItemCount(),
                1,
                "Cart should contain one product"
        );

        CartPage cartPage = productsPage.goToCart();

        Assert.assertTrue(
                cartPage.isItemInCart(),
                "Cart should contain the selected product"
        );

        List<String> productNames =
                cartPage.getItemNames();

        Assert.assertTrue(
                productNames.contains("Sauce Labs Backpack"),
                "Sauce Labs Backpack is not present in the cart"
        );

        CheckoutPage checkoutPage =
                cartPage.proceedToCheckout();

        checkoutPage.fillCheckoutInfo(
                "Ritesh",
                "Kumar",
                "173212"
        );

        String orderTotal =
                checkoutPage.getOrderTotal();

        Assert.assertTrue(
                orderTotal.contains("Total:"),
                "Order total is not displayed"
        );

        checkoutPage.finishCheckout();

        Assert.assertTrue(
                checkoutPage.isOrderComplete(),
                "Order was not completed successfully"
        );
    }

    @Test
    public void removeProductFromCartTest() {

        LoginPage loginPage = new LoginPage(driver);

        ProductsPage productsPage = loginPage.loginAs(
                "standard_user",
                "secret_sauce"
        );

        productsPage.addProductToCart(
                "Sauce Labs Backpack"
        );

        CartPage cartPage =
                productsPage.goToCart();

        Assert.assertEquals(
                cartPage.getItemCount(),
                1,
                "Cart should initially contain one product"
        );

        cartPage.removeProduct(
                "Sauce Labs Backpack"
        );

        Assert.assertEquals(
                cartPage.getItemCount(),
                0,
                "Cart should be empty after removing the product"
        );
    }
}