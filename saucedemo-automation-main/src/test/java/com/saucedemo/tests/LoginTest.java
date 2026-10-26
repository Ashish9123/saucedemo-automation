package com.saucedemo.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;

public class LoginTest extends BaseTest {

    @Test
    public void validLoginTest() {

        LoginPage loginPage = new LoginPage(driver);

        ProductsPage productsPage = loginPage.loginAs(
                "standard_user",
                "secret_sauce"
        );

        Assert.assertEquals(
                productsPage.getPageTitle(),
                "Products",
                "Login failed or Products page did not open"
        );
    }

    @Test
    public void invalidPasswordTest() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.attemptLogin(
                "standard_user",
                "wrong_password"
        );

        Assert.assertTrue(
                loginPage.isErrorDisplayed(),
                "Login error message should be displayed"
        );

        Assert.assertTrue(
                loginPage.getErrorMessage()
                        .contains("Username and password do not match"),
                "Incorrect error message was displayed"
        );
    }

    @Test
    public void invalidUsernameTest() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.attemptLogin(
                "wrong_user",
                "secret_sauce"
        );

        Assert.assertTrue(
                loginPage.isErrorDisplayed(),
                "Login error message should be displayed"
        );

        Assert.assertTrue(
                loginPage.getErrorMessage()
                        .contains("Username and password do not match"),
                "Incorrect error message was displayed"
        );
    }

    @Test
    public void emptyCredentialsTest() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.attemptLogin("", "");

        Assert.assertTrue(
                loginPage.isErrorDisplayed(),
                "Login error message should be displayed"
        );

        Assert.assertTrue(
                loginPage.getErrorMessage()
                        .contains("Username is required"),
                "Username required message was not displayed"
        );
    }
}