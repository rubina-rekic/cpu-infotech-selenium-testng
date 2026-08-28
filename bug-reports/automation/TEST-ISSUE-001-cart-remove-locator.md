# TEST-ISSUE-001: Cart Remove Button Locator Matches Multiple Elements

## Summary

The automated cart regression test failed when attempting to remove a product from the shopping cart.

The Selenium locator used for the remove-product button was too generic and matched multiple elements on the cart page. As a result, Selenium was unable to identify the intended clickable element and the test failed with a timeout.

This was identified as an **automation test issue**, not an application bug.

## Severity

**Minor** - The issue affected the automated test execution but did not prevent a user from removing a product from the cart manually.

## Priority

**Medium** - The affected regression test could not complete successfully until the locator was corrected. Since the issue prevented reliable automated verification of cart removal functionality, it needed to be fixed before relying on this regression test.

## Environment

* **Browser:** Google Chrome
* **Browser Version:** 152.0.7977.64
* **OS:** Windows 11
* **Java:** 17.0.20
* **Selenium:** 4.23.0
* **Test Framework:** TestNG
* **Build Tool:** Maven
* **Application:** CPU Infotech e-commerce website
* **Test:** `CartRegressionTest.addingAndRemovingProduct_updatesCartCorrectly`

## Steps to Reproduce

1. Run the `CartRegressionTest.addingAndRemovingProduct_updatesCartCorrectly` test.
2. Open the CPU Infotech website.
3. Navigate to the Coffee Appliances category.
4. Open the first product.
5. Add the product to the cart.
6. Verify that the cart item count increases by one.
7. Open the shopping cart.
8. Attempt to remove the product using the automated test.

## Expected Result

The automated test should identify the product's remove button and click it successfully.

The product should then be removed from the cart and the cart should display the empty-cart state.

## Actual Result

The test failed while waiting for the remove-product element to become clickable or visible.

The original Selenium locator was:

```java
By.cssSelector("a.remove.remove-product")
```

The test failed with a timeout similar to:

```text
Timeout Expected condition failed:
waiting for element to be clickable:
By.cssSelector: a.remove.remove-product
```

After changing the wait to visibility, the test still failed because Selenium could not uniquely identify the intended element.

## Investigation

The cart page was inspected manually using Chrome DevTools.

The following locator was tested:

```javascript
document.querySelectorAll('a.remove.remove-product').length
```

The result was:

```text
2
```

This confirmed that the locator matched **two different elements** on the cart page.

The relevant elements were:

```html
<a role="button"
   href="https://cpuinfotech.ba/cart/?remove_item=..."
   class="remove remove-product"
   ...>
</a>

<a role="button"
   href="https://cpuinfotech.ba/cart/?remove_item=..."
   class="remove remove-product position-absolute"
   ...>
</a>
```

Both elements shared the classes:

```text
remove
remove-product
```

Therefore, the original selector:

```css
a.remove.remove-product
```

was not specific enough.

## Root Cause

The root cause was an **ambiguous Selenium locator**.

The selector:

```java
By.cssSelector("a.remove.remove-product")
```

matched multiple elements on the cart page.

Selenium's `elementToBeClickable()` / `visibilityOfElementLocated()` conditions were therefore not targeting the intended remove button reliably.

This was a problem with the automated test implementation rather than with the application's cart functionality.

## Fix

The locator was made more specific by including the `position-absolute` class that identifies the intended remove button.

### Before

```java
private final By removeItemLink =
        By.cssSelector("a.remove.remove-product");
```

### After

```java
private final By removeItemLink =
        By.cssSelector("a.remove.remove-product.position-absolute");
```

The updated selector uniquely identifies the intended element.

## Verification

After updating the locator, the regression test was executed again.

The test successfully:

1. Opened the website.
2. Navigated to the product category.
3. Opened a product.
4. Added the product to the cart.
5. Verified that the cart count increased by one.
6. Opened the cart.
7. Located the correct remove button.
8. Removed the product.
9. Verified that the cart was empty.

**Result: PASS**

## Impact

The issue affected only the automated regression test.

The shopping cart functionality itself was not identified as defective. The remove button was present and functional on the website; the problem was that the automation script used an overly broad locator.

## Status

**Resolved**

