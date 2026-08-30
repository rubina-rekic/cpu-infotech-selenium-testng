# BUG-002: `orderby` query parameter (price sorting) returns 403 Forbidden

## Summary
Using the "Sort by price" dropdown on category listing pages triggers a
request containing an `orderby=price` query parameter, which returns an
HTTP 403 Forbidden error. This was reproduced on multiple categories
(tested on "Fenovi za kosu" and "Kafe Aparati"), and occurs via a **POST**
request, unlike BUG-001 which was observed via GET requests. Because the
page's JavaScript does not surface this failure to the user, the UI is
left showing an infinite loading spinner instead of any visible error.

## Related Issue
See **BUG-001** (`filter_proizvodac` query parameter returns 403 for any
value). This bug was discovered while investigating what looked at first
like a performance/loading issue, and turned out to follow the same
pattern as BUG-001: a specific query parameter name appears to be blocked
at the server level regardless of context. These are logged as two
separate bugs since they affect different features (manufacturer filtering
vs. price sorting) and were confirmed independently, but they likely share
the same underlying root cause and may be worth investigating together.

## Severity
**Major** — Price sorting is non-functional across at least two categories
tested (likely site-wide, though not every category was individually
verified). A workaround exists (browsing unsorted), so not Critical, but a
commonly-used sorting feature being broken - combined with a UI that gives
no error feedback, only an infinite spinner - is a significant usability
issue.

## Priority
**High** — Combined with BUG-001, this suggests a systemic issue affecting
multiple product-browsing conveniences, not an isolated edge case. Worth
prioritizing investigation of the shared root cause.

## Environment
- **Browser:** Google Chrome (version 152.0.7977.64)
- **OS:** Windows 11
- **Date found:** August 30, 2026
- **Categories tested:** "Fenovi za kosu" (Hair dryers), "Kafe Aparati" (Coffee appliances)

## Steps to Reproduce
1. Go to any product category page, e.g. https://cpuinfotech.ba/kategorija/fenovi-za-kosu/
2. Use the "Sortiraj po" (Sort by) dropdown to select "Razvrstaj po cijeni" (Sort by price)
3. Observe the page after selecting the sort option

## Expected Result
The product listing re-orders by price (ascending or descending, depending
on the option chosen), without errors.

## Actual Result
The page shows a loading spinner that never resolves. Chrome DevTools
Console shows a failed request:
```
POST https://cpuinfotech.ba/kategorija/fenovi-za-kosu/?orderby=price&paged=1
403 (Forbidden)
```
The user is given no visible error message - the UI appears to hang
indefinitely, which is arguably worse from a user experience perspective
than BUG-001's behavior (which at least shows a clear, if unstyled, 403
error page).

## Evidence
Screenshot showing the Chrome DevTools Console with the failed request:
`orderby-403-console-error.png`

## Investigation
1. Initially, this appeared to be a performance/slow-loading issue (the
   category has only ~6 products, so a long load time seemed unusual).
2. Chrome DevTools Network tab was checked with the "Doc" filter applied
   (as used previously for other requests on this site) - no request
   appeared, which was initially confusing.
3. Switching to "All" (removing the "Doc" filter) revealed the actual
   request was **not** a standard document/page load - it uses a
   different request type not captured by the "Doc" filter.
4. Console tab showed the 403 error directly (see "Actual Result" above),
   confirming this is a server-level rejection, not a slow response.
5. Retested on a second, unrelated category ("Kafe Aparati") with the same
   sort option - same 403 error occurred, confirming this is not specific
   to the "Fenovi za kosu" category.

## Root Cause
Not confirmed (no access to server/WAF configuration), but the pattern
strongly resembles BUG-001: a specific query parameter name (`orderby` in
this case, `filter_proizvodac` in BUG-001) appears to be blocked at the
server/WAF level regardless of the value or category it's used on. Given
two independent parameters showing the same failure mode, it's possible
the underlying rule blocks a broader set of parameter names.

## Additional Notes
- This is a **speculative root cause based on black-box testing** where actual
  diagnosis would require access to the site's server/WAF configuration.
- Reported to site owner for awareness; not something the tester has
  access or ability to fix directly.


## Status
Open - reported to site owner, pending investigation/fix.