package com.example.hexclad.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.app.Application
import org.json.JSONArray
import org.json.JSONObject
import androidx.lifecycle.AndroidViewModel

enum class NavTab { HOME, SHOP, RECIPES, WISHLIST, CART, PROFILE, CHECKOUT }
enum class SortMode { RECOMMENDED, PRICE_LOW_TO_HIGH, PRICE_HIGH_TO_LOW }

data class HexCladProduct(
    val id: String,
    val name: String,
    val subtitle: String,
    val price: Double,
    val category: String,
    val imageUrl: String = "",
    val description: String,
    val compareAtPrice: Double? = null,
    val badge: String? = null
)

data class CartLine(val product: HexCladProduct, val quantity: Int)
data class DemoOrder(
    val orderNumber: String,
    val timestamp: Long,
    val total: Double,
    val itemSummary: String,
    val recipient: String,
    val paymentMethod: String
)

class HexCladViewModel(application: Application) : AndroidViewModel(application) {
    var currentTab by mutableStateOf(NavTab.HOME)
        private set

    var selectedProduct by mutableStateOf<HexCladProduct?>(null)
        private set

    var searchQuery by mutableStateOf("")
    var selectedCategory by mutableStateOf("All")
    var sortMode by mutableStateOf(SortMode.RECOMMENDED)
    var saleOnly by mutableStateOf(false)

    private val cart = mutableStateListOf<CartLine>()
    private val favorites = mutableStateListOf<String>()
    private val orders = mutableStateListOf<DemoOrder>()

    val cartLines: List<CartLine> get() = cart.toList()
    val favoriteIds: List<String> get() = favorites.toList()
    val demoOrders: List<DemoOrder> get() = orders.sortedByDescending { it.timestamp }
    val favoriteProducts: List<HexCladProduct> get() = products.filter { it.id in favorites }
    val cartItemCount: Int get() = cart.sumOf { it.quantity }
    val cartSubtotal: Double get() = cart.sumOf { it.product.price * it.quantity }

    // Demonstration catalog aligned to products currently listed on HexClad's public store.
    // Prices are snapshots for UI demonstration; connect the official commerce API before production.
    val products = listOf(
        HexCladProduct("cookware-set-12", "Hybrid Pots & Pans Set, 12-pc", "A complete kitchen foundation", 699.00, "Sets", "https://cdn.shopify.com/s/files/1/0037/2110/1430/files/HexClad_Group_BLACK_a83f7e65-a1a5-4902-b168-7e441466ac3c.jpg?v=1745338111", "A multi-piece hybrid cookware set for everyday meals and entertaining.", compareAtPrice = 999.00, badge = "30% OFF"),
        HexCladProduct("fry-pan-set-6", "Hybrid Fry Pan Set with Lids, 6pc", "Everyday essentials, together", 399.00, "Sets", "https://hexclad.com/cdn/shop/files/Hexclad_6Pc_Pans_Lids-and-Pan-Insides_BLACK_2048x_1.png?crop=center&height=1024&v=1762181908&width=1024", "A set of hybrid fry pans with lids for versatile daily cooking.", compareAtPrice = 532.00, badge = "25% OFF"),
        HexCladProduct("pot-set-6", "Hybrid Pot Set with Lids, 6-pc", "Versatile pots for every day", 379.00, "Sets", "https://hexclad.com/cdn/shop/files/6Pc_Pots_ForLids_OnBlack_BLACK.jpg?em-format=auto&em-width=2048&v=1686836475", "A collection of hybrid pots with lids for soups, grains, sauces, and more.", compareAtPrice = 467.00, badge = "19% OFF"),
        HexCladProduct("hybrid-pan-12", "Hybrid Fry Pan, 12inch", "The everyday workhorse", 179.00, "Cookware", "https://cdn.shopify.com/s/files/1/0037/2110/1430/files/Hexclad_12inchPan_34NoLid_BLACK_2048x.jpg?v=1686837072", "A versatile hybrid fry pan designed for searing, sautéing, and everyday cooking."),
        HexCladProduct("hybrid-pan-10", "Hybrid Fry Pan, 10inch", "Perfect for daily cooking", 149.00, "Cookware", "https://hexclad.com/cdn/shop/files/Hybrid_Frying_Pan_10_inch_1.jpg", "A versatile pan for eggs, vegetables, and quick weeknight meals."),
        HexCladProduct("hybrid-pan-8", "Hybrid Fry Pan, 8inch", "Small pan, big utility", 129.00, "Cookware", "https://cdn.shopify.com/s/files/1/0037/2110/1430/files/Hexclad_8inchPan_34_NoLID_BLACK_2048x.jpg?v=1686836875", "A compact hybrid fry pan for smaller portions and quick tasks."),
        HexCladProduct("hybrid-wok-14", "Hybrid Wok with Lid, 14inch", "High heat. Big flavor.", 219.00, "Cookware", "https://hexclad.com/cdn/shop/files/Hexclad_14inchWok_34_LID_BLACK.jpg?v=1686837441", "A roomy wok for stir-fries, searing, and family-sized meals."),
        HexCladProduct("hybrid-saucepan-1", "Hybrid Saucepan with Lid, 1-Qt", "Small-batch precision", 119.00, "Cookware", "https://hexclad.com/cdn/shop/files/Hexclad_1QTPot_34_LID_WHITE_2.jpg?v=1695245707", "A compact saucepan for sauces, reheating, and side dishes."),
        HexCladProduct("hybrid-saute-7", "Hybrid Deep Sauté Pan/Chicken Fryer with Lid, 7-Qt", "Room for one-pan meals", 229.00, "Cookware", "https://hexclad.com/cdn/shop/files/Hexclad_ChickenFryer_34_NoLID_BLACK_2048x.jpg?v=1686836528", "A deep, versatile pan for braising, frying, and one-pan dinners."),
        HexCladProduct("hybrid-griddle-12", "Hybrid Griddle Pan, 12inch", "Make more of breakfast", 139.00, "Cookware", "https://cdn.shopify.com/s/files/1/0037/2110/1430/files/Hexclad_12inchGriddle_Overhead_BLACK_2048x.jpg?v=1686837095", "A flat hybrid cooking surface for pancakes, sandwiches, and seared favorites."),
        HexCladProduct("hybrid-stockpot-12", "Hybrid Stock Pot with Lid, 12-Qt", "Big batches made easy", 219.00, "Cookware", "https://hexclad.com/cdn/shop/files/Artboard8.png?v=1723567468", "A generous stock pot for soups, stocks, pasta, and batch cooking."),
        HexCladProduct("knife-set-green", "Damascus Steel Knife Set, 7-pc (Green)", "Precision in every cut", 499.00, "Knives", "https://hexclad.com/cdn/shop/products/27A---6PC-JAPANESE-DAMASCUS-STEEL-KNIFE-SET_Extended_0721_1x1_b0838e6c-c626-489c-b17c-73c911bb21f7_2048x.jpg?v=1671822300", "A multi-piece Damascus steel knife set for everyday food preparation.", compareAtPrice = 783.00, badge = "36% OFF"),
        HexCladProduct("knife-set-studio", "Damascus Steel Studio Knife Set, 5-pc", "A refined prep collection", 399.00, "Knives", "", "A five-piece knife collection designed for confident kitchen prep."),
        HexCladProduct("cutting-board", "Hybrid Carving & Cutting Board", "A dependable prep essential", 99.00, "Accessories", "https://hexclad.com/cdn/shop/files/Hexclad_CuttingBoard_34_Front_BLACK_2048x.jpg?v=1720736075", "A practical prep surface for daily kitchen tasks."),
        HexCladProduct("utensil-set", "Kitchen Utensil Set, 9-pc", "Tools for the whole cook", 249.00, "Accessories", "https://hexclad.eu/cdn/shop/files/KitchenUtensils-1_1.png?crop=center&height=1024&v=1761341339&width=1024", "A coordinated utensil collection for everyday cooking."),
        HexCladProduct("steamer-basket", "Steamer Basket Set, 2-pc", "More ways to cook", 69.00, "Accessories", "https://hexclad.com.au/cdn/shop/files/Artboard1_6a7c0b9b-13ba-4781-a59b-b7ea2602c334.png?crop=center&height=480&v=1753899822&width=480", "A two-piece steamer basket set for vegetables, dumplings, and more."),
        HexCladProduct("salt-pepper", "HexMill Salt & Pepper Grinder Set", "Finish every dish", 249.00, "Accessories", "https://hexclad.com.au/cdn/shop/files/HexClad_HexMillSet_KWHM02-ST_RajanComp-Black_082624_01_2048x.jpg?v=1725906040", "A matching grinder set for seasoning at the table and while cooking.", compareAtPrice = 318.00, badge = "22% OFF")
    )

    private val preferences = application.getSharedPreferences("hexclad_shop_state", Application.MODE_PRIVATE)

    init {
        restoreShoppingState()
        restoreDemoOrders()
    }

    private fun restoreShoppingState() {
        val savedFavorites = preferences.getStringSet("favorites", mutableSetOf<String>()).orEmpty()
        favorites.addAll(savedFavorites.filter { id -> products.any { it.id == id } })

        val savedCart = preferences.getString("cart", "[]") ?: "[]"
        runCatching {
            val array = JSONArray(savedCart)
            for (index in 0 until array.length()) {
                val item = array.optJSONObject(index) ?: continue
                val id = item.optString("id")
                val quantity = item.optInt("quantity", 0)
                val product = products.firstOrNull { it.id == id } ?: continue
                if (quantity > 0) cart.add(CartLine(product, quantity.coerceAtMost(99)))
            }
        }
    }

    private fun restoreDemoOrders() {
        runCatching {
            val array = JSONArray(preferences.getString("demo_orders", "[]") ?: "[]")
            for (index in 0 until array.length()) {
                val item = array.optJSONObject(index) ?: continue
                orders.add(
                    DemoOrder(
                        orderNumber = item.optString("number"),
                        timestamp = item.optLong("timestamp"),
                        total = item.optDouble("total"),
                        itemSummary = item.optString("items"),
                        recipient = item.optString("recipient"),
                        paymentMethod = item.optString("payment")
                    )
                )
            }
        }
    }

    fun recordDemoOrder(recipient: String, paymentMethod: String) {
        if (cart.isEmpty()) return
        val order = DemoOrder(
            orderNumber = "DEMO-${System.currentTimeMillis().toString().takeLast(8)}",
            timestamp = System.currentTimeMillis(),
            total = cartSubtotal,
            itemSummary = cart.joinToString { "${it.quantity} × ${it.product.name}" },
            recipient = recipient,
            paymentMethod = paymentMethod
        )
        orders.add(order)
        val array = JSONArray()
        orders.forEach { saved ->
            array.put(
                JSONObject()
                    .put("number", saved.orderNumber)
                    .put("timestamp", saved.timestamp)
                    .put("total", saved.total)
                    .put("items", saved.itemSummary)
                    .put("recipient", saved.recipient)
                    .put("payment", saved.paymentMethod)
            )
        }
        preferences.edit().putString("demo_orders", array.toString()).apply()
    }

    private fun persistCart() {
        val array = JSONArray()
        cart.forEach { line ->
            array.put(JSONObject().put("id", line.product.id).put("quantity", line.quantity))
        }
        preferences.edit().putString("cart", array.toString()).apply()
    }

    private fun persistFavorites() {
        preferences.edit().putStringSet("favorites", favorites.toSet()).apply()
    }

    fun visibleProducts(): List<HexCladProduct> {
        val filtered = products.filter {
            (selectedCategory == "All" || it.category == selectedCategory) &&
                (!saleOnly || (it.compareAtPrice != null && it.compareAtPrice > it.price)) &&
                (searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true) ||
                    it.subtitle.contains(searchQuery, ignoreCase = true) ||
                    it.description.contains(searchQuery, ignoreCase = true) ||
                    it.category.contains(searchQuery, ignoreCase = true))
        }
        return when (sortMode) {
            SortMode.RECOMMENDED -> filtered
            SortMode.PRICE_LOW_TO_HIGH -> filtered.sortedBy { it.price }
            SortMode.PRICE_HIGH_TO_LOW -> filtered.sortedByDescending { it.price }
        }
    }

    fun switchTab(tab: NavTab) {
        selectedProduct = null
        currentTab = tab
    }

    fun openProduct(product: HexCladProduct) {
        selectedProduct = product
    }

    fun closeProduct() {
        selectedProduct = null
    }

    fun addToCart(product: HexCladProduct) {
        val index = cart.indexOfFirst { it.product.id == product.id }
        if (index >= 0) cart[index] = cart[index].copy(quantity = (cart[index].quantity + 1).coerceAtMost(99))
        else cart.add(CartLine(product, 1))
        persistCart()
    }

    fun changeQuantity(productId: String, delta: Int) {
        val index = cart.indexOfFirst { it.product.id == productId }
        if (index < 0) return
        val next = cart[index].quantity + delta
        if (next <= 0) cart.removeAt(index) else cart[index] = cart[index].copy(quantity = next.coerceAtMost(99))
        persistCart()
    }

    fun removeFromCart(productId: String) {
        cart.removeAll { it.product.id == productId }
        persistCart()
    }

    fun clearCart() {
        cart.clear()
        persistCart()
    }

    fun toggleFavorite(product: HexCladProduct) {
        if (product.id in favorites) favorites.remove(product.id) else favorites.add(product.id)
        persistFavorites()
    }

    fun isFavorite(product: HexCladProduct): Boolean = product.id in favorites
}
