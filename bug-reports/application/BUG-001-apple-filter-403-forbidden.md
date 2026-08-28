# BUG-001: `filter_proizvodac` query parameter returns 403 Forbidden for any value

## Summary
Using the `filter_proizvodac` (manufacturer filter) query parameter on the
"Mobiteli" category page returns an HTTP 403 Forbidden error
**regardless of the value passed** - including real manufacturer names
(Apple, Samsung, Xiaomi) and even a nonsense value that does not correspond
to any real manufacturer. The category page loads correctly when no
`filter_proizvodac` parameter is present.

## Severity
**Major** - The entire manufacturer-filtering feature on this category is
non-functional, not just a single brand. A workaround exists (browsing the
unfiltered category or using the general search bar), so this is not rated
Critical, but a whole feature being unusable is more significant than an
isolated broken link.

## Priority
**Medium-High** - Manufacturer filtering is a commonly used shopping aid on
an electronics e-commerce site, and this affects it universally on at least
this category. Not necessarily an emergency outage, but likely worth
prioritizing given how broadly it's broken.

## Environment
- **Browser:** Google Chrome 
- **OS:** Windows 11
- **Date found:** August 25, 2026
- **Access method tested:** Both direct URL navigation and clicking through
  the site's own navigation menu (hover "Mobiteli & Televizori" → click a
  manufacturer submenu link)

## Steps to Reproduce
1. Go to https://cpuinfotech.ba/kategorija/mobiteli/ (loads correctly, no filter)
2. Append any `?filter_proizvodac=<value>` query parameter to the URL, for example:
   - `?filter_proizvodac=apple`
   - `?filter_proizvodac=samsung`
   - `?filter_proizvodac=xiaomi`
   - `?filter_proizvodac=nepostojecibrend123` (nonsense value, used to test
     whether the issue is brand-specific or parameter-wide)
3. Observe the response for each

## Expected Result
The category page loads and displays a product listing filtered to the
requested manufacturer (or an empty/no-results state for a nonsense value
that matches no products) - consistent with how the unfiltered category
page behaves.

## Actual Result
Every tested value of `filter_proizvodac`  real brand names and a made-up
nonsense value alike — returns an HTTP 403 Forbidden error page served
directly by nginx, with no site navigation or content rendered at all.

## Evidence
Screenshots showing identical 403 responses for:
- ![Apple 403 Error](images/apple-filter-403-forbidden.png) (?filter_proizvodac=apple)

- ![Xiaomi 403 Error](images/xiaomi-filter-403-forbidden.png) (?filter_proizvodac=xiaomi)

(Samsung and the nonsense-value test produced the same 403 page; screenshots
of those weren't separately saved, but are trivially reproducible via the
steps above.)

## Root Cause Analysis 
To narrow down whether this was brand-specific or parameter-wide, the
following was tested in sequence:

| Test | URL | Result |
|---|---|---|
| No filter | `/kategorija/mobiteli/` |  Loads normally |
| Apple filter | `/kategorija/mobiteli/?filter_proizvodac=apple` |  403 |
| Samsung filter | `/kategorija/mobiteli/?filter_proizvodac=samsung` |  403 |
| Xiaomi filter | `/kategorija/mobiteli/?filter_proizvodac=xiaomi` |  403 |
| Nonsense value | `/kategorija/mobiteli/?filter_proizvodac=nepostojecibrend123` |  403 |

Since even a nonsense value that matches no real product data still
triggers the 403, the issue is very unlikely to be inside WooCommerce's
product-filtering logic itself (which would more likely return an empty
result set for a nonexistent brand, not a server-level 403). This points to
a server/hosting-level rule (e.g. a WAF or nginx) blocking requests
that contain the `filter_proizvodac` query parameter name itself,
independent of its value.

## Additional Notes
- This is a **speculative root cause based on black-box testing** - the actual fix would need to be diagnosed
  and applied by whoever manages the site's hosting/security configuration.
- Reported to site owner for awareness; not something the tester has access
  or ability to fix directly.

## Status
Open - pending investigation/fix.