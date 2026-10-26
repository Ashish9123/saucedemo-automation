package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutPage extends BasePage {

    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");

    private final By continueButton = By.id("continue");
    private final By checkoutError =
            By.cssSelector("h3[data-test='error']");

    private final By orderTotal =
            By.className("summary_total_label");

    private final By finishButton = By.id("finish");

    private final By completeHeader =
            By.className("complete-header");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage fillCheckoutInfo(
            String firstName,
            String lastName,
            String postalCode) {

        type(firstNameField, firstName);
        type(lastNameField, lastName);
        type(postalCodeField, postalCode);

        click(continueButton);

        wait.until(driver ->
                driver.getCurrentUrl().contains("checkout-step-two")
                        || !driver.findElements(checkoutError).isEmpty()
        );

        if (!driver.findElements(checkoutError).isEmpty()) {
            String errorMessage =
                    driver.findElement(checkoutError).getText();

            throw new IllegalStateException(
                    "Checkout information was not accepted: "
                            + errorMessage
            );
        }

        return this;
    }

    public String getOrderTotal() {

        wait.until(
                ExpectedConditions.urlContains("checkout-step-two")
        );

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(orderTotal)
        ).getText();
    }

    public CheckoutPage finishCheckout() {

        wait.until(
                ExpectedConditions.elementToBeClickable(finishButton)
        ).click();

        wait.until(
                ExpectedConditions.urlContains("checkout-complete")
        );

        return this;
    }

    public boolean isOrderComplete() {

        try {
            String message = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            completeHeader
                    )
            ).getText();

            return message.equalsIgnoreCase(
                    "Thank you for your order!"
            );

        } catch (Exception e) {
            return false;
        }
    }
}