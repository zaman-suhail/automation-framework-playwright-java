# Playwright Automation Framework

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Playwright](https://img.shields.io/badge/Playwright-1.59.0-purple?style=for-the-badge&logo=playwright)
![TestNG](https://img.shields.io/badge/TestNG-7.12.0-blue?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-3.8-red?style=for-the-badge&logo=apachemaven)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-CI/CD-black?style=for-the-badge&logo=githubactions)

> A production-ready Playwright automation framework built with **Java + TestNG**, **9 design patterns**, and full CI/CD via GitHub Actions.

---

## Quick Start

```bash
# 1. Clone the repo
git clone https://github.com/zaman-suhail/automation-framework-playwright-java.git

# 2. Download Playwright browsers (run once only)
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"

# 3. Set your website URL
# Edit: src/test/resources/config/config.properties
url=https://your-website.com

# 4. Run tests
mvn test

# 5. Open the HTML report
xdg-open reports/Report_*.html
```

---

## Tech Stack

| Tool | Version | Purpose |
|---|---|---|
| Java | 21 | Programming language |
| Playwright | 1.59.0 | Browser automation — auto-wait, CDP protocol |
| TestNG | 7.12.0 | Test runner, annotations, DataProvider |
| ExtentReports | 5.1.2 | Dark theme HTML reports with screenshots |
| Log4j2 | 2.26.0 | Logging to console and log file |
| Jackson | 2.21.2 | Reads JSON test data |
| Commons IO | 2.22.0 | File operations |
| Maven Compiler | 3.13.0 | Compiles Java 21 source |
| Maven Surefire | 3.2.5 | Runs TestNG suite from testng.xml |
| GitHub Actions | — | CI/CD pipeline |

---

## Design Patterns

| Pattern | File | What It Does |
|---|---|---|
| **Singleton** | `ConfigReader.java` | Config loaded once, shared everywhere |
| **Singleton + ThreadLocal** | `PlaywrightManager.java` | One Page per thread — parallel safe |
| **Factory** | `PlaywrightFactory.java` | Creates Chromium/Firefox/WebKit from config |
| **Page Object Model** | `LoginPage.java`, `ProductsPage.java` | Locators in one place — change once, fix all |
| **Fluent Interface** | `LoginPage.java` | `.enterUsername().enterPassword().clickLogin()` |
| **Template Method** | `BasePage.java`, `BaseTest.java` | Common actions in parent — child inherits |
| **Observer** | `PlaywrightListener.java` | Auto full-page screenshot on fail, auto report |
| **Data-Driven** | `TestDataReader.java` | JSON data feeds multiple test runs |
| **Facade** | `CheckoutFacade.java` | Complex workflow behind one simple method |

---

## Folder Structure

```
playwright-framework/
├── .github/
│   └── workflows/
│       └── playwright-tests.yml         # GitHub Actions CI/CD
├── src/
│   ├── main/java/com/framework/
│   │   ├── config/
│   │   │   └── ConfigReader.java        # Singleton Pattern
│   │   ├── driver/
│   │   │   └── PlaywrightManager.java   # Singleton + ThreadLocal
│   │   │                                # Playwright > Browser > Context > Page
│   │   ├── factory/
│   │   │   └── PlaywrightFactory.java   # Factory Pattern
│   │   │                                # chromium | firefox | webkit
│   │   ├── pages/
│   │   │   ├── BasePage.java            # Template Method — common actions
│   │   │   ├── LoginPage.java           # POM + Fluent Interface
│   │   │   └── ProductsPage.java        # Page Object Model
│   │   ├── facade/
│   │   │   └── CheckoutFacade.java      # Facade Pattern
│   │   ├── listeners/
│   │   │   └── PlaywrightListener.java  # Observer Pattern
│   │   └── utils/
│   │       ├── ExtentReportManager.java # HTML Reports
│   │       └── TestDataReader.java      # Data-Driven Pattern
│   ├── main/resources/
│   │   └── log4j2.xml                   # Logging configuration
│   └── test/
│       ├── java/com/framework/tests/
│       │   ├── BaseTest.java            # Template Method
│       │   └── LoginTest.java           # All patterns combined
│       └── resources/
│           ├── config/
│           │   └── config.properties    # Framework settings
│           └── testdata/
│               └── loginData.json       # Test data (JSON)
├── reports/                             # HTML reports (auto-generated)
│   └── screenshots/                     # Full-page screenshots on failure
├── logs/                                # Log files (auto-generated)
├── testng.xml                           # TestNG suite config
├── .gitignore
└── pom.xml                              # Maven dependencies
```

---

## pom.xml — Key Dependencies

```xml
<!-- Playwright — main dependency -->
<dependency>
    <groupId>com.microsoft.playwright</groupId>
    <artifactId>playwright</artifactId>
    <version>1.59.0</version>
</dependency>

<!-- TestNG — NO scope test — listeners need it in main folder -->
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>7.12.0</version>
</dependency>

<!-- Java 21 compiler -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.13.0</version>
    <configuration>
        <source>21</source>
        <target>21</target>
    </configuration>
</plugin>
```

---

## Configuration

Edit `src/test/resources/config/config.properties`:

```properties
# Browser: chromium | firefox | webkit
browser=chromium

# Your website URL
url=https://www.saucedemo.com

# Set true for CI/CD (no browser window)
headless=false

# Slow down actions for debugging (ms) — 0 for normal speed
slow.mo=0

# Default timeout for all actions (ms)
timeout=15000

reports.folder=reports/
logs.folder=logs/
```

---

## How PlaywrightManager Works

`PlaywrightManager` manages a full lifecycle chain:

```
Playwright → Browser → BrowserContext → Page
```

```java
public static Page getPage() {
    if (pg.get() == null) {
        Playwright    p = Playwright.create();            // 1. Start Playwright
        Browser       b = PlaywrightFactory.create(p);   // 2. Launch browser
        BrowserContext c = b.newContext(...);             // 3. Isolated context
        Page       page = c.newPage();                   // 4. Open tab
        // Stored in ThreadLocal — parallel tests are safe
    }
    return pg.get();
}

// Closes everything in reverse order
public static void close() {
    // page → context → browser → playwright
}
```

---

## PlaywrightFactory — Factory Pattern

```java
public static Browser create(Playwright pw) {
    String browser = ConfigReader.getInstance().getBrowser();

    switch (browser.toLowerCase()) {
        case "firefox": return pw.firefox().launch(opts);
        case "webkit":  return pw.webkit().launch(opts);
        default:        return pw.chromium().launch(opts);
    }
}
```

Set `browser=firefox` in config — `PlaywrightFactory` launches Firefox. No test code change needed.

---

## BasePage — Auto-Wait

Playwright auto-waits. No explicit wait code needed:

```java
protected void click(String selector) {
    page.locator(selector).click();   // auto-waits until element is clickable
}

protected void fill(String selector, String text) {
    if (!text.isEmpty()) page.locator(selector).fill(text);  // auto-waits
}

protected boolean isVisible(String selector) {
    return page.locator(selector).isVisible();
}
```

---

## Run Options

```bash
# Default — Chromium, all tests
mvn test

# Firefox
mvn test -Dbrowser=firefox

# WebKit (Safari engine)
mvn test -Dbrowser=webkit

# Headless — no browser window
mvn test -Dheadless=true

# Smoke tests only
mvn test -Dgroups=smoke

# Regression tests only
mvn test -Dgroups=regression
```

---

## Test Data

`src/test/resources/testdata/loginData.json`:

```json
{
  "validUsers": [
    { "username": "standard_user", "password": "secret_sauce", "description": "Standard user" },
    { "username": "problem_user",  "password": "secret_sauce", "description": "Problem user"  }
  ],
  "invalidUsers": [
    { "username": "wrong_user",    "password": "wrong_pass",   "expectedError": "do not match"        },
    { "username": "",              "password": "",             "expectedError": "Username is required" },
    { "username": "standard_user", "password": "wrong_pass",  "expectedError": "do not match"        }
  ]
}
```

Add more users here — tests run for each row automatically.

---

## Reports

```bash
xdg-open reports/Report_*.html
```

**Report contains:**
- Pass / Fail / Skip — green / red / yellow
- Full-page screenshots on failure — `reports/screenshots/`
- System info — OS, Java, Browser, URL, Date
- Full stack trace for failed tests
- Execution time per test

**Log file:** `logs/framework.log`

---

## Add Your Own Page

**Step 1** — Update URL:
```properties
url=https://your-website.com
```

**Step 2** — Create page object:
```java
public class MyPage extends BasePage {

    private static final String MY_BUTTON = "#your-button-id";
    private static final String MY_FIELD  = "#your-field-name";

    public MyPage clickButton() {
        click(MY_BUTTON);     // BasePage method — auto-waits
        return this;
    }

    public MyPage enterText(String text) {
        fill(MY_FIELD, text); // BasePage method — auto-waits
        return this;
    }

    @Override
    public boolean isPageLoaded() {
        return isVisible(MY_BUTTON);
    }
}
```

**Step 3** — Create test:
```java
public class MyTest extends BaseTest {

    @Test(description = "My feature test")
    public void testMyFeature() {
        ExtentReportManager.info("Testing my feature");

        new MyPage()
            .enterText("hello")
            .clickButton();

        Assert.assertTrue(new MyPage().isPageLoaded(), "Page did not load!");
        ExtentReportManager.pass("Test passed!");
    }
}
```

**Step 4** — Add to `testng.xml`:
```xml
<class name="com.framework.tests.MyTest"/>
```

**Step 5** — Run:
```bash
mvn test
```

---

## GitHub Actions — CI/CD

Every push to `main` automatically:

1. Sets up Java 21
2. Caches Maven dependencies
3. Runs `mvn test -Dheadless=true`
    - Playwright downloads Chromium automatically — no extra step
4. Uploads HTML report + screenshots as artifact (30 days)

**Download report:**
1. GitHub → **Actions** tab
2. Click latest run
3. Scroll to **Artifacts**
4. Download `PW-Report-N` zip → open `.html`

**Manual trigger:**
- Actions → Playwright Tests → Run workflow → Run

---

## Common Issues

| Problem | Solution |
|---|---|
| `Playwright browsers not found` | Run: `mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"` |
| `package org.testng does not exist` | Remove `<scope>test</scope>` from TestNG in `pom.xml` |
| `Config file not found` | Check: `src/test/resources/config/config.properties` |
| `Element not found` | Check selector with F12 DevTools |
| Report not generated | Surefire `3.2.5` + `PlaywrightListener` in `testng.xml`? |
| GitHub Actions fails | Pass `-Dheadless=true` — browser downloads automatically |
| `Source option 5` error | `maven-compiler-plugin` with `<source>21</source>` in `pom.xml` |

---

## Locator Guide

```java
page.locator("#login-button")          // By ID — best option
page.locator(".inventory_list")        // By class
page.locator("[data-test='error']")    // By attribute — most reliable
page.locator("text=Login")             // By visible text
page.locator("[placeholder='Username']") // By placeholder
page.locator("input#user-name")        // Tag + ID combined
page.locator(".form input")            // Parent > child
```

> All locators **auto-retry** until element appears or timeout. No `WebDriverWait` needed.

---

*Built with Java 21 · Playwright 1.59.0 · TestNG 7.12.0 · 9 Design Patterns · GitHub Actions*