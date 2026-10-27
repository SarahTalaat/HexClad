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
| Home | Partial demo | Native Compose hero, promotional strip, and featured products. It is not yet a complete screen-by-screen reproduction of all audited mobile sections. |
| Shop/search | Functional local demo | 17-item static catalog; searches product name, subtitle, description, and category; scrollable category chips (including Sets); recommended and ascending/descending price sorting. Data is not a live feed. |
| Product details | Functional local demo | Product photo when a URL is available, fallback placeholder, quantity selector capped at 99, wishlist toggle, sale labels for selected products, and add-to-cart. Full galleries, zoom, variants, reviews, and verified specifications are not implemented. |
| Cart | Functional local demo | Quantity changes, removal, subtotal, and cart persistence across app launches via SharedPreferences. Live discounts, shipping/tax calculations, and official checkout are not connected. |
| Wishlist | Functional local demo | Saved products persist across app launches via SharedPreferences. |
| Recipes/editorial | Proposal/demo | Three sample recipe cards provide navigation to Shop; editorial content is not a complete reproduction of the current site's recipe experience. |
| Account | Placeholder/demo | Profile actions explain which services are not integrated. Locally saved demo orders are shown as demo history; sign-in, registration, and real customer orders are not connected. |
| Checkout/payment | Local demo only | Requires name, a well-formed email, street address, city/region, and postal code. Confirmation saves a local demo order and clears the local cart; no order is sent and no payment is processed. |
| Assets/catalog data | Partially verified | Selected set imagery and compare-at values were matched to public HexClad/Shopify listings. Other image references and the remaining catalog still require validation; all values are non-live snapshots. |
| Native build | Not verified | No Gradle build or emulator run was performed during this update. CI and unit tests were intentionally not run, per the task instructions. |

## Next implementation gates

1. Read the actual audit CSVs and verify the product/collection URLs and images before replacing the demo catalog.
2. Create a normalized catalog/asset manifest with source URL, product association, price timestamp/region, image variants, and download verification.
3. Finish verifying the remaining product/image URLs, and download approved assets locally when their license and source permit it.
4. Add product variants, richer gallery behavior, reviews, and specifications based on verified catalog data.
5. Replace placeholder account and local demo checkout with official services only when approved commerce APIs and credentials are available.
6. Open the latest branch in Android Studio and verify Gradle sync/build plus the screens on an emulator; that runtime verification has not been performed in this update.

## Important accuracy rule

Until a live commerce integration is implemented and verified, catalog values in the prototype are demonstration data. Do not describe stock, current promotions, authentication, payment, or order placement as live.
