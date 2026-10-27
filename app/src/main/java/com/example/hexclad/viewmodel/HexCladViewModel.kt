package com.example.hexclad.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class NavTab { HOME, SHOP, RECIPES, WISHLIST, CART, PROFILE }

data class HexCladProduct(
    val id: String,
    val name: String,
    val subtitle: String,
    val price: Double,
    val category: String,
    val imageUrl: String = "",
    val description: String
)

data class CartLine(val product: HexCladProduct, val quantity: Int)

class HexCladViewModel : ViewModel() {
    var currentTab by mutableStateOf(NavTab.HOME)
        private set

    var selectedProduct by mutableStateOf<HexCladProduct?>(null)
        private set

    var searchQuery by mutableStateOf("")
    var selectedCategory by mutableStateOf("All")
    var sortByPriceAscending by mutableStateOf(false)

    private val cart = mutableStateListOf<CartLine>()
    private val favorites = mutableStateListOf<String>()

    val cartLines: List<CartLine> get() = cart.toList()
    val favoriteIds: List<String> get() = favorites.toList()
    val favoriteProducts: List<HexCladProduct> get() = products.filter { it.id in favorites }
    val cartItemCount: Int get() = cart.sumOf { it.quantity }
    val cartSubtotal: Double get() = cart.sumOf { it.product.price * it.quantity }

    val products = listOf(
        HexCladProduct("hybrid-pan", "Hybrid Frying Pan", "The original hybrid technology", 99.00, "Cookware",
            "https://hexclad.com/cdn/shop/files/Hybrid_Frying_Pan_10_inch_1.jpg", "A versatile hybrid pan designed for everyday cooking, with HexClad's distinctive hybrid cooking surface."),
        HexCladProduct("hybrid-wok", "Hybrid Wok", "High heat. Big flavor.", 149.00, "Cookware",
            "https://hexclad.com/cdn/shop/files/Hybrid_Wok_12_inch_1.jpg", "A roomy wok for stir-fries, searing, and weeknight meals."),
        HexCladProduct("hybrid-saucepan", "Hybrid Saucepan", "Everyday cooking, elevated", 129.00, "Cookware",
            "https://hexclad.com/cdn/shop/files/Hybrid_Saucepan_2_Quart_1.jpg", "A versatile saucepan for sauces, grains, soups, and sides."),
        HexCladProduct("knife-set", "Japanese Damascus Knife Set", "Precision in every cut", 399.00, "Knives",
            "https://hexclad.com/cdn/shop/files/Knife_Set_1.jpg", "A precision knife set designed for confident prep."),
        HexCladProduct("cutting-board", "Cutting Board", "A dependable prep essential", 49.00, "Accessories",
            "https://hexclad.com/cdn/shop/files/Cutting_Board_1.jpg", "A durable prep surface for daily kitchen tasks."),
        HexCladProduct("hybrid-pot", "Hybrid Stock Pot", "Room for your best recipes", 199.00, "Cookware",
            "https://hexclad.com/cdn/shop/files/Hybrid_Stock_Pot_1.jpg", "A generous stock pot for family meals and batch cooking.")
    )

    fun visibleProducts(): List<HexCladProduct> {
        val filtered = products.filter {
            (selectedCategory == "All" || it.category == selectedCategory) &&
                (searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true) ||
                    it.subtitle.contains(searchQuery, ignoreCase = true))
        }
        return if (sortByPriceAscending) filtered.sortedBy { it.price } else filtered
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
        if (index >= 0) cart[index] = cart[index].copy(quantity = cart[index].quantity + 1)
        else cart.add(CartLine(product, 1))
    }

    fun changeQuantity(productId: String, delta: Int) {
        val index = cart.indexOfFirst { it.product.id == productId }
        if (index < 0) return
        val next = cart[index].quantity + delta
        if (next <= 0) cart.removeAt(index) else cart[index] = cart[index].copy(quantity = next)
    }

    fun removeFromCart(productId: String) {
        cart.removeAll { it.product.id == productId }
    }

    fun toggleFavorite(product: HexCladProduct) {
        if (product.id in favorites) favorites.remove(product.id) else favorites.add(product.id)
    }

    fun isFavorite(product: HexCladProduct): Boolean = product.id in favorites
}
