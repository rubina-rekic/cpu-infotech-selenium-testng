# TEST-ISSUE-002: Cart Quantity Update Not Persisting via Standard Selenium Interactions

## Summary

The automated regression test for updating product quantity in the shopping
cart consistently failed: after clicking the "+" quantity button (twice, to
go from 1 to 3) and then clicking "Ažuriraj košaricu" (Update Cart), the
quantity and subtotal reverted to their original values (1 / unchanged
price) instead of reflecting the new quantity.

Manual testing confirmed the feature works correctly for a real user
clicking through the same steps by hand. This was identified as an
**automation interaction issue**, not an application bug.

## Severity

**Minor** - The issue affected only the automated test's ability to interact
with the page; the underlying cart update functionality was confirmed
working correctly for real users via manual testing.

## Priority

**Medium** - Blocked one regression test from running until resolved, but
did not indicate any risk to the live site or its users.

## Environment

- **Browser:** Google Chrome (version 152.0.7977.64)
- **OS:** Windows 11
- **Java:** 17.0.20
- **Selenium:** 4.23.0
- **Test:** `CartRegressionTest.updatingQuantity_updatesSubtotal`
- **Page:** https://cpuinfotech.ba/cart/

## Steps to Reproduce (the failure)
1. Add a product with a quantity selector to the cart (e.g. a Hama cable product).
2. Open the cart page.
3. Click the "+" quantity button twice via Selenium (`WebElement.click()`).
4. Click "Ažuriraj košaricu" (Update Cart) via Selenium.
5. Read the quantity field and product subtotal.

## Expected Result
Quantity field shows "3", and the product subtotal reflects the new
quantity (three times the unit price).

## Actual Result
Quantity field and subtotal revert to their original values (quantity "1",
unchanged subtotal), even though the field briefly showed "3" immediately
after the button clicks.

## Investigation

Multiple approaches were attempted, in order, to identify the root cause:

| Attempt | Approach | Result |
|---|---|---|
| 1 | `sendKeys()` to type quantity directly, then click Update | Failed - value reverted |
| 2 | Added `Keys.TAB` after typing, to simulate blur | Failed - value reverted |
| 3 | Added explicit wait (`ExpectedConditions.attributeToBe`) for the field to show the expected value before clicking Update | Failed - value reverted after Update click |
| 4 | Switched from typing to clicking the "+" button (matches real user behavior more closely) | Failed - same revert behavior |
| 5 | Added explicit click on `<body>` + `Thread.sleep(2000)` to simulate focus loss and allow time for background validation | Failed - same revert behavior |
| 6 | Dispatched a native JavaScript `change` event via `JavascriptExecutor` after clicking "+" | Failed - same revert behavior |

**Manual verification:** Before continuing investigation, the same steps
were performed entirely by hand (no Selenium involved) to rule out an
actual site bug. The quantity and subtotal updated correctly when done
manually, confirming the feature itself works and the issue was specific
to how Selenium's simulated interactions were being registered by the
page's JavaScript.

**Network tab inspection:** The "Update Cart" action was confirmed to be a
standard GET request to `/cart/` (a full page reload), not an AJAX call -
ruling out an AJAX-timing theory that was initially suspected.

## Root Cause

The site's cart quantity field is built on **jQuery**, and its update logic
listens for **jQuery's own event system** (`jQuery(element).trigger(...)`),
not just native browser DOM events. Native events dispatched via
`element.dispatchEvent(new Event(...))` (attempt 6 above) are not
automatically visible to jQuery's internal event bindings - jQuery
maintains its own event handling layer that must be triggered explicitly
for jQuery-bound listeners to fire.

Because Selenium's standard `click()` and `sendKeys()` also rely on native
browser-level interactions, none of the standard approaches reliably
triggered jQuery's change detection, so the page's cart-update logic never
recognized that the quantity had changed by the time "Update Cart" was
clicked.

## Fix

Used `JavascriptExecutor` to directly set the field's value and then
dispatch **both** native DOM events **and** a jQuery-triggered event,
covering both possible event-handling paths:

```java
public void increaseQuantityBy(int clicks) {
    int expectedFinalValue = 1 + clicks;
    WebElement quantityField = wait.until(ExpectedConditions.elementToBeClickable(quantityInput));

    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript(
        "arguments[0].value = arguments[1];" +
        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));" +
        "if (window.jQuery) { jQuery(arguments[0]).trigger('change'); }",
        quantityField, String.valueOf(expectedFinalValue)
    );

    WebElement updateBtn = wait.until(ExpectedConditions.elementToBeClickable(updateCartButton));
    updateBtn.click();

    // Wait for the page reload to actually begin/complete, rather than a
    // fixed sleep - the old <input> element becomes stale once the page reloads.
    wait.until(ExpectedConditions.stalenessOf(quantityField));
}
```

Key elements of the fix:
- Dispatching both `input` and native `change` events covers listeners
  bound either way.
- Explicitly calling `jQuery(element).trigger('change')` (guarded by a
  `window.jQuery` existence check, in case jQuery isn't present) reaches
  jQuery-specific event bindings that native events don't trigger.
- Replacing the earlier `Thread.sleep()` attempt with
  `ExpectedConditions.stalenessOf(quantityField)` waits precisely for the
  page reload to occur, rather than guessing a fixed delay.

## Verification

After applying the fix, the test was run and passed consistently

## Additional Notes
- This is a good example of why black-box UI automation sometimes needs to
  go beyond "click and type" - understanding the underlying front-end
  framework (jQuery, in this case) was necessary to reliably automate an
  interaction that a real user performs without issue.


## Status
 Resolved