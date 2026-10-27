# HexClad Android Prototype

Native Android shopping-app prototype built with Kotlin, Jetpack Compose, Material 3, and Coil.

## Open the project

1. Clone this repository and check out `chatgpt/hexclad-native-rebuild`.
2. Open the repository root in Android Studio.
3. Allow Gradle sync to finish and install the Android SDK platform/build tools requested by the project.
4. Select the `app` run configuration and launch it on an emulator or Android device.

## Included prototype flows

- Home page with promotional hero and featured products
- Shop catalog with search, category filters, and price sorting
- Product details, quantity selection, and wishlist toggle with local persistence across app launches
- Cart with quantity changes, remove actions, subtotal, and local persistence across app launches
- Checkout form with delivery fields, order summary, demo confirmation, and locally saved demo order history
- Recipe inspiration and profile/help links
- Empty states, image placeholders, cart-count badge, and add-to-cart toast feedback

## Important prototype limitations

- This is a native Android app; the UI is not a WebView or a React app.
- Catalog entries are a curated sample, not a live product feed. Prices are illustrative snapshots and may change.
- Some product images are loaded from remote HexClad CDN paths; the UI shows a placeholder when no image URL is configured. Network access is required for remote images.
- Cart, wishlist, and local demo order history are stored on-device using app preferences. They are not synced to an account or server.
- Checkout is explicitly a local demo. It does not submit an order, collect card details, or process payments.
- Authentication, customer order history, inventory, shipping calculations, and live checkout require approved HexClad commerce services and credentials.
- Official support and policy pages open externally; their availability and URLs should be verified before release.

## Production follow-up

Connect an approved commerce API, replace illustrative catalog data with live product/variant/inventory data, download and license the required product imagery for local app resources or a supported CDN, persist cart/wishlist state, implement authentication and order history, and add real shipping/tax/payment flows through the store's authorized provider.
