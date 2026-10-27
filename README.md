# HexClad Android Prototype

Native Android shopping-app prototype built with Kotlin, Jetpack Compose, Material 3, and Coil.

## Open the project

1. Clone this repository and check out `chatgpt/hexclad-native-rebuild`.
2. Open the repository root in Android Studio.
3. Allow Gradle sync to finish and install the Android SDK platform/build tools requested by the project.
4. Select the `app` run configuration and launch it on an emulator or Android device.

## Included prototype flows

- Home page with promotional hero, highlighted product photography, and featured products
- Shop catalog with search, horizontally scrollable category filters, an on-sale filter, and price sorting
- Sale labels and crossed-out reference prices for selected products where public HexClad listings expose comparison values
- Product details with sale pricing for selected items, quantity selection, official shipping/returns and warranty links, and wishlist toggle with local persistence across app launches
- Cart with quantity changes, remove actions, subtotal, and local persistence across app launches
- Checkout form with delivery fields, order summary, demo confirmation, and locally saved demo order history
- Recipe inspiration and profile/help links
- Empty states with recovery actions, image placeholders, cart-count badge, quantity-limit-aware add-to-cart feedback, and system-back handling
- Fixed brand palette with light/dark appearance following Android system preference

## Important prototype limitations

- This is a native Android app; the UI is not a WebView or a React app.
- Catalog entries are a curated sample, not a live product feed. Prices are illustrative snapshots and may change.
- All 17 sample catalog entries currently have a remote HexClad/Shopify image URL configured. The UI falls back to a placeholder if a URL is missing or an image request fails; network access is required, and the complete image set has not been runtime-verified.
- A few image URLs, sale labels, and comparison prices were matched to public HexClad product/collection listings during this update. The catalog remains a non-live snapshot: prices, offers, availability, and image URLs can change, and the remaining image references should be verified before release.
- Cart, wishlist, and local demo order history are stored on-device using app preferences. They are not synced to an account or server.
- Checkout is explicitly a local demo. It does not submit an order, collect card details, or process payments.
- Authentication, customer order history, inventory, shipping calculations, and live checkout require approved HexClad commerce services and credentials.
- Official support and policy pages open externally; their availability and URLs should be verified before release.

## Production follow-up

Connect an approved commerce API, replace illustrative catalog data with live product/variant/inventory data, download and license the required product imagery for local app resources or a supported CDN, persist cart/wishlist state, implement authentication and order history, and add real shipping/tax/payment flows through the store's authorized provider.
