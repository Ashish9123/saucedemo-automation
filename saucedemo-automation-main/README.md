# SauceDemo Automation Testing Framework

Selenium WebDriver + TestNG automation framework for [saucedemo.com](https://www.saucedemo.com/),
built using the Page Object Model (POM). Covers Login, Product Search/Browse, Add to Cart,
and Checkout workflows, with Maven for build/dependency management, WebDriverManager for
automatic browser driver setup, and ExtentReports for HTML execution reports.

## Prerequisites

Install these on your machine before running the framework:

1. **Java JDK 11+** — `java -version` to check.
2. **Maven 3.6+** — `mvn -version` to check.
3. **Google Chrome** (or Firefox) installed — WebDriverManager downloads the matching
   driver automatically, you don't need to download chromedriver yourself.
4. An IDE such as **IntelliJ IDEA** or **Eclipse** is recommended but not required — everything
   also runs from the command line.

## Project Structure

```
SauceDemoAutomation/
├── pom.xml                          # Maven dependencies (Selenium, TestNG, WebDriverManager, ExtentReports)
├── testng.xml                       # TestNG suite definition + listener registration
├── Jenkinsfile                      # CI pipeline definition
├── src/main/java/com/saucedemo/
│   ├── base/DriverManager.java      # WebDriver creation/teardown (thread-safe)
│   ├── pages/                       # Page Object classes (one per screen)
│   │   ├── BasePage.java
│   │   ├── LoginPage.java
│   │   ├── ProductsPage.java
│   │   ├── CartPage.java
│   │   └── CheckoutPage.java
│   └── utils/ConfigReader.java      # Reads config.properties
├── src/main/resources/
│   └── config.properties            # Base URL, browser choice, test credentials
└── src/test/java/com/saucedemo/
    ├── tests/
    │   ├── BaseTest.java            # @BeforeMethod/@AfterMethod driver setup/teardown
    │   ├── LoginTest.java           # Valid/invalid/locked-out/blank login scenarios
    │   └── E2ECheckoutTest.java     # Add to cart -> checkout -> confirmation, remove item, sorting
    └── listeners/
        └── ExtentReportListener.java # Builds the HTML report, screenshots failures
```

## How to Run

1. Open a terminal in the project's root folder (where `pom.xml` is).
2. Install dependencies and compile:
   ```
   mvn clean compile
   ```
3. Run the full suite:
   ```
   mvn test
   ```
   This uses `testng.xml`, which Maven Surefire is already configured to pick up (see `pom.xml`).

4. **View the report:** after the run finishes, open the newest file in
   `test-output/ExtentReports/SauceDemo_Report_<timestamp>.html` in a browser.
   Failed tests include a screenshot taken at the moment of failure.

### Run a single test class

```
mvn test -Dtest=LoginTest
```

### Switch browser or run headless

Edit `src/main/resources/config.properties`:
```
browser=chrome        # or firefox
headless=true         # run without opening a visible browser window
```

## What's Covered

| Test Class | Scenarios |
|---|---|
| `LoginTest` | Valid login, wrong password, locked-out account, blank credentials |
| `E2ECheckoutTest` | Full checkout (login → cart → checkout → confirmation), removing an item from cart, sorting products |

## Notes for Extending This Framework

- **Add a new page:** create a class in `pages/` that extends `BasePage`, define its locators
  as `By` constants, and expose methods that return the *next* page object (this is what makes
  tests read like a user journey instead of raw Selenium calls).
- **Add a new test:** extend `BaseTest`, then write TestNG `@Test` methods using the page
  objects — never call `driver.findElement(...)` directly inside a test class.
- **CI:** the included `Jenkinsfile` runs `mvn test` on every build and archives the ExtentReports
  HTML and any failure screenshots as Jenkins build artifacts.
