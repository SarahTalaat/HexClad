package com.example.hexclad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hexclad.ui.screens.*
import com.example.hexclad.ui.theme.HexCladTheme
import com.example.hexclad.viewmodel.HexCladViewModel
import com.example.hexclad.viewmodel.NavTab

class MainActivity : ComponentActivity() {
    private val viewModel: HexCladViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HexCladTheme {
                val selectedProduct = viewModel.selectedProduct

                BackHandler(enabled = selectedProduct != null || viewModel.currentTab == NavTab.CHECKOUT) {
                    if (selectedProduct != null) {
                        viewModel.closeProduct()
                    } else {
                        viewModel.switchTab(NavTab.CART)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (selectedProduct == null && viewModel.currentTab != NavTab.CHECKOUT) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                tonalElevation = 8.dp
                            ) {
                                NavigationBarItem(
                                    selected = viewModel.currentTab == NavTab.HOME,
                                    onClick = { viewModel.switchTab(NavTab.HOME) },
                                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
                                    label = { Text("Home") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.Black,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primary,
                                        unselectedIconColor = Color.Gray,
                                        unselectedTextColor = Color.Gray
                                    )
                                )
                                NavigationBarItem(
                                    selected = viewModel.currentTab == NavTab.SHOP,
                                    onClick = { viewModel.switchTab(NavTab.SHOP) },
                                    icon = { Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = "Shop") },
                                    label = { Text("Shop") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.Black,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primary,
                                        unselectedIconColor = Color.Gray,
                                        unselectedTextColor = Color.Gray
                                    )
                                )
                                NavigationBarItem(
                                    selected = viewModel.currentTab == NavTab.RECIPES,
                                    onClick = { viewModel.switchTab(NavTab.RECIPES) },
                                    icon = { Icon(imageVector = Icons.Default.RestaurantMenu, contentDescription = "Recipes") },
                                    label = { Text("Recipes") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.Black,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primary,
                                        unselectedIconColor = Color.Gray,
                                        unselectedTextColor = Color.Gray
                                    )
                                )
                                NavigationBarItem(
                                    selected = viewModel.currentTab == NavTab.WISHLIST,
                                    onClick = { viewModel.switchTab(NavTab.WISHLIST) },
                                    icon = { Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "Wishlist") },
                                    label = { Text("Saved") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.Black,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primary,
                                        unselectedIconColor = Color.Gray,
                                        unselectedTextColor = Color.Gray
                                    )
                                )
                                NavigationBarItem(
                                    selected = viewModel.currentTab == NavTab.CART,
                                    onClick = { viewModel.switchTab(NavTab.CART) },
                                    icon = {
                                        BadgedBox(
                                            badge = {
                                                if (viewModel.cartItemCount > 0) {
                                                    Badge(containerColor = MaterialTheme.colorScheme.primary) {
                                                        Text("${viewModel.cartItemCount}", color = Color.Black)
                                                    }
                                                }
                                            }
                                        ) {
                                            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart")
                                        }
                                    },
                                    label = { Text("Cart") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.Black,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primary,
                                        unselectedIconColor = Color.Gray,
                                        unselectedTextColor = Color.Gray
                                    )
                                )
                                NavigationBarItem(
                                    selected = viewModel.currentTab == NavTab.PROFILE,
                                    onClick = { viewModel.switchTab(NavTab.PROFILE) },
                                    icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Profile") },
                                    label = { Text("Profile") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.Black,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primary,
                                        unselectedIconColor = Color.Gray,
                                        unselectedTextColor = Color.Gray
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        if (selectedProduct != null) {
                            ProductDetailScreen(product = selectedProduct, viewModel = viewModel)
                        } else {
                            when (viewModel.currentTab) {
                                NavTab.HOME -> HomeScreen(viewModel = viewModel)
                                NavTab.SHOP -> ShopScreen(viewModel = viewModel)
                                NavTab.RECIPES -> RecipeScreen(viewModel = viewModel)
                                NavTab.WISHLIST -> WishlistScreen(viewModel = viewModel)
                                NavTab.CART -> CartScreen(viewModel = viewModel)
                                NavTab.PROFILE -> ProfileScreen(viewModel = viewModel)
                                NavTab.CHECKOUT -> CheckoutScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
