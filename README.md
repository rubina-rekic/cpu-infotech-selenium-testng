# CPU Infotech - Selenium + TestNG Test Automation Framework

A Selenium WebDriver + TestNG test automation framework built against
[cpuinfotech.ba](https://cpuinfotech.ba), a live WooCommerce e-commerce
site - used here as a portfolio project demonstrating UI test
automation, Page Object Model design, and QA documentation practices.

> **Note on testing scope:** This project tests a real, production
> e-commerce site. Testing was performed with **explicit permission from
> the site owner**. All tests are designed to be non-destructive - no real
> orders are placed, no spam accounts are created, and the automated suite
> is run on-demand rather than on every commit, to avoid generating
> unnecessary load on a live production server.

## Tech Stack

- **Java 17** + **Maven**
- **Selenium WebDriver 4** - browser automation
- **TestNG** - test runner, grouping (`smoke` / `regression`), assertions
- **WebDriverManager** - automatic browser driver management
- **Page Object Model** - one class per page, shared wait logic in `BasePage`

