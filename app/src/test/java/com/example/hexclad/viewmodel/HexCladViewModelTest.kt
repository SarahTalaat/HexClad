package com.example.hexclad.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HexCladViewModelTest {
    private lateinit var viewModel: HexCladViewModel

    @Before
    fun setUp() {
        viewModel = HexCladViewModel()
    }

    @Test
    fun addingSameProductIncrementsExistingCartLine() {
        val product = viewModel.products.first()

        viewModel.addToCart(product)
        viewModel.addToCart(product)

        assertEquals(1, viewModel.cartLines.size)
        assertEquals(2, viewModel.cartLines.single().quantity)
        assertEquals(product.price * 2, viewModel.cartSubtotal, 0.001)
        assertEquals(2, viewModel.cartItemCount)
    }

    @Test
    fun decrementingLastUnitRemovesCartLine() {
        val product = viewModel.products.first()
        viewModel.addToCart(product)

        viewModel.changeQuantity(product.id, -1)

        assertTrue(viewModel.cartLines.isEmpty())
        assertEquals(0, viewModel.cartItemCount)
        assertEquals(0.0, viewModel.cartSubtotal, 0.001)
    }

    @Test
    fun removeFromCartUpdatesCountAndSubtotal() {
        val first = viewModel.products[0]
        val second = viewModel.products[1]
        viewModel.addToCart(first)
        viewModel.addToCart(second)

        viewModel.removeFromCart(first.id)

        assertEquals(1, viewModel.cartLines.size)
        assertEquals(second.id, viewModel.cartLines.single().product.id)
        assertEquals(second.price, viewModel.cartSubtotal, 0.001)
    }

    @Test
    fun searchIsCaseInsensitiveAndMatchesProductName() {
        viewModel.searchQuery = "wOk"

        val results = viewModel.visibleProducts()

        assertTrue(results.isNotEmpty())
        assertTrue(results.all { it.name.contains("wok", ignoreCase = true) })
    }

    @Test
    fun categoryFilterAndPriceSortAreAppliedTogether() {
        viewModel.selectedCategory = "Cookware"
        viewModel.sortByPriceAscending = true

        val results = viewModel.visibleProducts()

        assertTrue(results.isNotEmpty())
        assertTrue(results.all { it.category == "Cookware" })
        assertEquals(results.sortedBy { it.price }, results)
    }

    @Test
    fun favoriteCanBeAddedAndRemoved() {
        val product = viewModel.products.first()

        viewModel.toggleFavorite(product)
        assertTrue(viewModel.isFavorite(product))

        viewModel.toggleFavorite(product)
        assertFalse(viewModel.isFavorite(product))
    }
}
