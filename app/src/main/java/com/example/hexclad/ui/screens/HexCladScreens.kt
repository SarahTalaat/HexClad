package com.example.hexclad.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.hexclad.viewmodel.HexCladProduct
import com.example.hexclad.viewmodel.HexCladViewModel
import java.util.Locale

private val Ink = Color(0xFF171717)
private val WarmWhite = Color(0xFFF7F6F2)
private val Muted = Color(0xFF6B6B67)
private val Lime = Color(0xFFD7FF00)

@Composable
fun HomeScreen(viewModel: HexCladViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(WarmWhite),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Column(Modifier.fillMaxWidth().background(Ink).padding(horizontal = 20.dp, vertical = 12.dp)) {
                Text("FREE SHIPPING ON ORDERS OVER $99", color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("HEXCLAD", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Ink)
                Text("HYBRID COOKWARE", style = MaterialTheme.typography.labelSmall, color = Muted)
            }
            Column(
                Modifier.fillMaxWidth().background(Ink).padding(horizontal = 22.dp, vertical = 30.dp)
            ) {
                Text("COOK DIFFERENT.", color = Lime, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                Text("Your kitchen.
Your rules.", color = Color.White, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black, lineHeight = MaterialTheme.typography.displaySmall.lineHeight)
                Spacer(Modifier.height(12.dp))
                Text("Meet the hybrid cookware built for the way you cook.", color = Color.White.copy(alpha = .82f), style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(20.dp))
                Button(onClick = { viewModel.switchTab(com.example.hexclad.viewmodel.NavTab.SHOP) }, colors = ButtonDefaults.buttonColors(containerColor = Lime, contentColor = Ink), shape = RoundedCornerShape(2.dp)) {
                    Text("SHOP BEST SELLERS", fontWeight = FontWeight.Bold)
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 26.dp, bottom = 14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text("BEST SELLERS", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Ink)
                TextButton(onClick = { viewModel.switchTab(com.example.hexclad.viewmodel.NavTab.SHOP) }) { Text("SHOP ALL", color = Ink) }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                viewModel.products.take(2).forEach { product ->
                    Box(Modifier.weight(1f)) { ProductCard(product, viewModel) }
                }
            }
        }
        item {
            Column(Modifier.padding(horizontal = 20.dp, vertical = 28.dp)) {
                Text("BUILT FOR EVERYDAY MASTERY", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Ink)
                Spacer(Modifier.height(8.dp))
                Text("Thoughtful design. Serious performance. Tools that make every meal feel worth it.", color = Muted, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(16.dp))
                OutlinedButton(onClick = { viewModel.switchTab(com.example.hexclad.viewmodel.NavTab.SHOP) }, shape = RoundedCornerShape(2.dp), modifier = Modifier.fillMaxWidth()) {
                    Text("EXPLORE THE COLLECTION", color = Ink, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ShopScreen(viewModel: HexCladViewModel) {
    val products = viewModel.visibleProducts()
    Column(Modifier.fillMaxSize().background(WarmWhite)) {
        Text("SHOP HEXCLAD", Modifier.padding(start = 20.dp, top = 22.dp, bottom = 14.dp), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Ink)
        OutlinedTextField(
            value = viewModel.searchQuery,
            onValueChange = { viewModel.searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            placeholder = { Text("Search cookware, knives, and more") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            singleLine = true,
            shape = RoundedCornerShape(4.dp)
        )
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Cookware", "Knives", "Accessories").forEach { category ->
                FilterChip(
                    selected = viewModel.selectedCategory == category,
                    onClick = { viewModel.selectedCategory = category },
                    label = { Text(category) }
                )
            }
        }
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("${products.size} products", color = Muted, style = MaterialTheme.typography.bodySmall)
            TextButton(onClick = { viewModel.sortByPriceAscending = !viewModel.sortByPriceAscending }) {
                Text(if (viewModel.sortByPriceAscending) "Price: low to high" else "Sort by price", color = Ink)
            }
        }
        if (products.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No products found. Try another search.", color = Muted) }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(products, key = { it.id }) { ProductCard(it, viewModel) }
            }
        }
    }
}

@Composable
private fun ProductCard(product: HexCladProduct, viewModel: HexCladViewModel) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)).background(Color.White)
            .clickable { viewModel.openProduct(product) }
    ) {
        Box(Modifier.fillMaxWidth().height(170.dp).background(Color(0xFFF0F0EC))) {
            if (product.imageUrl.isNotBlank()) {
                AsyncImage(model = product.imageUrl, contentDescription = product.name, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            }
            IconButton(onClick = { viewModel.toggleFavorite(product) }, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(if (viewModel.isFavorite(product)) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Toggle favorite", tint = if (viewModel.isFavorite(product)) Color.Red else Ink)
            }
        }
        Column(Modifier.padding(12.dp)) {
            Text(product.name, color = Ink, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(4.dp))
            Text(product.subtitle, color = Muted, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(8.dp))
            Text(money(product.price), color = Ink, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = { viewModel.addToCart(product) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(2.dp), contentPadding = PaddingValues(vertical = 8.dp)) {
                Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("ADD TO CART", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProductDetailScreen(product: HexCladProduct, viewModel: HexCladViewModel) {
    var quantity by remember(product.id) { mutableIntStateOf(1) }
    LazyColumn(Modifier.fillMaxSize().background(WarmWhite), contentPadding = PaddingValues(bottom = 24.dp)) {
        item {
            Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.closeProduct() }) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                Text("PRODUCT DETAILS", fontWeight = FontWeight.Bold, color = Ink, style = MaterialTheme.typography.labelLarge)
            }
            Box(Modifier.fillMaxWidth().height(330.dp).background(Color(0xFFF0F0EC))) {
                AsyncImage(model = product.imageUrl, contentDescription = product.name, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
            }
            Column(Modifier.padding(20.dp)) {
                Text(product.category.uppercase(Locale.US), color = Muted, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(product.name, color = Ink, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                Text(product.subtitle, color = Muted, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(14.dp))
                Text(money(product.price), color = Ink, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Text(product.description, color = Ink, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedButton(onClick = { if (quantity > 1) quantity-- }, shape = RoundedCornerShape(2.dp), contentPadding = PaddingValues(8.dp)) { Icon(Icons.Default.Remove, contentDescription = "Decrease quantity") }
                    Text("$quantity", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedButton(onClick = { quantity++ }, shape = RoundedCornerShape(2.dp), contentPadding = PaddingValues(8.dp)) { Icon(Icons.Default.Add, contentDescription = "Increase quantity") }
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { viewModel.toggleFavorite(product) }) {
                        Icon(if (viewModel.isFavorite(product)) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Toggle favorite")
                    }
                }
                Button(onClick = { repeat(quantity) { viewModel.addToCart(product) } }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(2.dp), colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Color.White)) {
                    Text("ADD TO CART · ${money(product.price * quantity)}", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CartScreen(viewModel: HexCladViewModel) {
    Column(Modifier.fillMaxSize().background(WarmWhite).padding(horizontal = 20.dp)) {
        Text("YOUR CART", Modifier.padding(top = 24.dp, bottom = 18.dp), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Ink)
        if (viewModel.cartLines.isEmpty()) {
            Spacer(Modifier.height(70.dp))
            Text("Your cart is waiting.", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Ink)
            Spacer(Modifier.height(8.dp))
            Text("Discover cookware designed to make every meal better.", color = Muted)
            Spacer(Modifier.height(20.dp))
            Button(onClick = { viewModel.switchTab(com.example.hexclad.viewmodel.NavTab.SHOP) }, colors = ButtonDefaults.buttonColors(containerColor = Ink)) { Text("SHOP NOW", color = Color.White) }
        } else {
            LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 16.dp)) {
                items(viewModel.cartLines, key = { it.product.id }) { line ->
                    Row(Modifier.fillMaxWidth().background(Color.White).padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(model = line.product.imageUrl, contentDescription = line.product.name, modifier = Modifier.size(88.dp).background(Color(0xFFF0F0EC)), contentScale = ContentScale.Crop)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(line.product.name, color = Ink, fontWeight = FontWeight.Bold)
                            Text(money(line.product.price), color = Muted)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { viewModel.changeQuantity(line.product.id, -1) }) { Icon(Icons.Default.Remove, contentDescription = "Decrease") }
                                Text("${line.quantity}")
                                IconButton(onClick = { viewModel.changeQuantity(line.product.id, 1) }) { Icon(Icons.Default.Add, contentDescription = "Increase") }
                            }
                        }
                        Text(money(line.product.price * line.quantity), color = Ink, fontWeight = FontWeight.Bold)
                    }
                }
            }
            HorizontalDivider()
            Row(Modifier.fillMaxWidth().padding(vertical = 18.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Subtotal", color = Ink, style = MaterialTheme.typography.titleMedium)
                Text(money(viewModel.cartSubtotal), color = Ink, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Button(onClick = { }, modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = Ink), shape = RoundedCornerShape(2.dp)) {
                Text("CONTINUE TO CHECKOUT", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Text("Checkout integration requires the store's approved commerce service.", Modifier.padding(top = 10.dp, bottom = 20.dp), color = Muted, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun RecipeScreen(viewModel: HexCladViewModel) {
    LazyColumn(Modifier.fillMaxSize().background(WarmWhite), contentPadding = PaddingValues(20.dp)) {
        item {
            Text("THE COOKING CLUB", style = MaterialTheme.typography.labelLarge, color = Muted, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Find your next favorite.", style = MaterialTheme.typography.headlineMedium, color = Ink, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(8.dp))
            Text("Recipe inspiration for your HexClad kitchen.", color = Muted)
            Spacer(Modifier.height(20.dp))
        }
        items(listOf("Crispy skillet chicken", "Weeknight stir-fry", "Perfect seared salmon")) { recipe ->
            Column(Modifier.fillMaxWidth().padding(bottom = 12.dp).clip(RoundedCornerShape(4.dp)).background(Color.White).padding(18.dp)) {
                Text(recipe, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Ink)
                Spacer(Modifier.height(6.dp))
                Text("Explore techniques, ingredients, and cookware tips for your next meal.", color = Muted)
                TextButton(onClick = { viewModel.switchTab(com.example.hexclad.viewmodel.NavTab.SHOP) }) { Text("SHOP COOKWARE", color = Ink) }
            }
        }
    }
}

@Composable
fun ProfileScreen(viewModel: HexCladViewModel) {
    Column(Modifier.fillMaxSize().background(WarmWhite).padding(22.dp)) {
        Text("MY HEXCLAD", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Ink)
        Spacer(Modifier.height(8.dp))
        Text("Your kitchen, all in one place.", color = Muted)
        Spacer(Modifier.height(24.dp))
        listOf("Sign in or create an account", "Order history", "Shipping & returns", "Warranty information", "Contact support").forEach { label ->
            Row(Modifier.fillMaxWidth().clickable { }.padding(vertical = 18.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(label, color = Ink, style = MaterialTheme.typography.bodyLarge)
                Text("›", color = Ink, style = MaterialTheme.typography.titleLarge)
            }
            HorizontalDivider(color = Color(0xFFE3E2DC))
        }
        Spacer(Modifier.height(20.dp))
        Text("Guest mode · Account services require official store integration.", color = Muted, style = MaterialTheme.typography.bodySmall)
    }
}

private fun money(amount: Double): String = "$" + String.format(Locale.US, "%.2f", amount)
