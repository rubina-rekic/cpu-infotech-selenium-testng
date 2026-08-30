# BUG-003: Grid/List view toggle button returns 403 Forbidden

## Summary
Clicking the grid/list view toggle button on category listing pages
triggers a request containing `count`, `paged`, and `gridcookie` query
parameters, which returns an HTTP 403 Forbidden error. As with BUG-002,
the page's JavaScript does not surface this failure to the user - a
loading overlay (`blockUI`) appears over the page and never resolves,
leaving the user staring at a stuck/darkened screen with no error message.

## Related Issues
See **BUG-001** (`filter_proizvodac` parameter, GET, 403) and **BUG-002**
(`orderby` parameter, POST, 403). This is now the **third** distinct
product-browsing feature found to trigger a 403 error via a specific set
of query parameters, following the same failure pattern: a valid site
feature makes a request with certain query parameter names, and the
server rejects it outright rather than processing it. Logged as a separate
bug (different feature, different parameters) but strongly suspected to
share the same root cause as BUG-001 and BUG-002.

## Severity
**Major** — The grid/list view toggle is completely non-functional. Unlike
BUG-001 (clear 403 error page) or BUG-002 (infinite spinner), this
produces a darkened/blocked overlay over the entire page with a "wait"
cursor, which may be the most confusing failure mode of the three for a
real user, since there's no obvious way to tell the page is stuck versus
just briefly busy.

## Priority
**High** — Combined with BUG-001 and BUG-002, this is now the third
confirmed instance of the same failure pattern affecting a different
feature. Strongly suggests the site owner should investigate the
underlying WAF/server rule as a single systemic issue rather than treating
each affected feature as an isolated case.

## Environment
- **Browser:** Google Chrome
- **OS:** Windows 11
- **Date found:** August 30, 2026
- **Category tested:** "Fenovi za kosu" (Hair dryers)

## Steps to Reproduce
1. Go to a product category page, e.g. https://cpuinfotech.ba/kategorija/fenovi-za-kosu/
2. Click the grid/list view toggle button (switches between grid and list display of products)

## Expected Result
The product listing switches display format (grid ↔ list) without errors.

## Actual Result
A loading overlay (`<div class="blockUI blockOverlay">`, semi-transparent
black overlay with a "wait" cursor) appears over the page and never
resolves. Chrome DevTools Console shows a failed request:
```
POST https://cpuinfotech.ba/kategorija/fenovi-za-kosu/?count=24&paged&gridcookie=list
403 (Forbidden)
```

## Investigation
Diagnosed using the same approach established for BUG-002: Console tab
checked directly for failed requests rather than relying on the Network
tab's "Doc" filter (which does not capture this request type, consistent
with what was observed for the `orderby` sorting request in BUG-002).

## Root Cause
Not confirmed (no server/WAF access), but consistent with the pattern from
BUG-001 and BUG-002: a specific combination of query parameter names
appears to be blocked at the server level. Three different parameters
(`filter_proizvodac`, `orderby`, and now `count`/`paged`/`gridcookie`)
across three different features all show the same 403 rejection behavior,
which makes a single shared underlying cause (e.g. a WAF rule pattern
matching broadly on query string structure, not a specific parameter name)
more likely than three unrelated, coincidental bugs.

## Additional Notes
- Reported to site owner for awareness; not something the tester has
  access or ability to fix directly.
- **Recommendation given to site owner:** given three independent features
  affected by what looks like the same root cause, investigating the
  WAF/security rule configuration directly (rather than each symptom
  individually) is likely the most efficient path to a fix.

## Status
Open — reported to site owner, pending investigation/fix.