package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage =
            By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl);
        return this;
    }

    public ProductsPage loginAs(
            String username,
            String password) {

        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);

        return new ProductsPage(driver);
    }

    public ProductsPage login(
            String username,
            String password) {

        return loginAs(username, password);
    }

    public LoginPage attemptLogin(
            String username,
            String password) {

        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);

        return this;
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }
}