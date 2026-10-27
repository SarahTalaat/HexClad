# HexClad Native Android — Audit and Delivery Status

Last reviewed: 2026-10-27

## Scope and evidence

- Reference: https://hexclad.com/
- Repository branch: `chatgpt/hexclad-native-rebuild`
- This is a native Kotlin/Jetpack Compose application. No WebView is used for app UI.
- Public website search results and crawlable pages were inspected. The local audit folder at `~/Desktop/hexclad-website-audit` is on the user's Mac and is not available to this GitHub-connected execution environment, so its CSV files and downloaded assets have **not** been read or imported here.
- A public HTML result is not proof of every JavaScript interaction. Any untested behavior is marked unverified below.

## Verified from public reference pages

- Product collections expose filters including availability and on-sale, several sort orders, and a load-more action.
- Product detail pages can expose size selection, lid options, add-to-cart, reviews, shipping/returns messaging, care information, specifications, and warranty content.
- The site publishes warranty and return policy pages.
- Catalog prices and promotions can change by date, region, variant, and promotion. Do not treat a hardcoded sample price as live inventory/pricing.

Reference pages:
- https://hexclad.com/products
- https://hexclad.com/collections/pans
- https://hexclad.com/products/10-hexclad-pan
- https://hexclad.com/products/10-hexclad-pan-with-lid
- https://hexclad.com/pages/refund-policy
- https://hexclad.com/pages/warranty

## Implementation status

| Area | Current status | Notes |
|---|---|---|
| Home | Partial demo | Native Compose layout; content and campaign sections are not a complete audited reproduction. |
| Shop/search | Partial demo | Local in-memory six-item catalog; basic query and category filter; one price-sort toggle only. |
| Product details | Partial demo | Single image, quantity, favorite and add-to-cart. Variant selectors, full gallery, zoom, reviews and verified specs are not implemented. |
| Cart | Partial demo | In-memory quantities and subtotal work; remove affordance, persistence, promotions, shipping/tax, and official checkout are incomplete. |
| Wishlist | Partial demo | Favorite toggles exist, but there is no dedicated wishlist destination or persistence. |
| Recipes/editorial | Proposal/demo | The current recipe labels/content have not been verified against the current site's rendered mobile navigation. |
| Account | Placeholder | Sign-in, registration, orders and profile are not connected to a backend. |
| Checkout/payment | Not integrated | No successful order/payment should be implied. Official supported commerce integration and credentials are required. |
| Assets | Not audited/imported in this environment | Existing image URLs are unverified; no claim is made that they all resolve. |
| Native build | Not verified | GitHub file editing does not execute the project's Gradle build or run an Android emulator. |

## Next implementation gates

1. Read the actual audit CSVs and verify the product/collection URLs and images before replacing the demo catalog.
2. Create a normalized catalog/asset manifest with source URL, product association, price timestamp/region, image variants, and download verification.
3. Replace in-memory cart/favorites with persistent storage and add repository tests.
4. Implement functional collection filtering/sorting and product variants using verified catalog data.
5. Replace placeholder account/checkout destinations with clearly labeled integration-ready flows unless official credentials/API access are supplied.
6. Add/execute Android CI or run `./gradlew testDebugUnitTest assembleDebug` in an environment with compatible JDK/Android SDK.
7. Test on an emulator and compare screenshots against equivalent mobile reference pages.

## Important accuracy rule

Until a live commerce integration is implemented and verified, catalog values in the prototype are demonstration data. Do not describe stock, current promotions, authentication, payment, or order placement as live.
