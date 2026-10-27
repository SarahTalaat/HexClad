package com.example.hexclad.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Close
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
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.AsyncImagePainter
import com.example.hexclad.viewmodel.HexCladProduct
import com.example.hexclad.viewmodel.HexCladViewModel
import com.example.hexclad.viewmodel.SortMode
import java.util.Locale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

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
        Text("SHOP HEXCLAD", Modifier.padding(start = 20.dp, top = 22.dp, bottom = 6.dp), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Ink)
        Text("Prototype catalog · prices and availability are illustrative, not live store data.", Modifier.padding(horizontal = 20.dp, vertical = 4.dp), color = Muted, style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(
            value = viewModel.searchQuery,
            onValueChange = { viewModel.searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            placeholder = { Text("Search cookware, knives, and more") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            singleLine = true,
            shape = RoundedCornerShape(4.dp)
        )
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Sets", "Cookware", "Knives", "Accessories").forEach { category ->
                FilterChip(
                    selected = viewModel.selectedCategory == category,
                    onClick = { viewModel.selectedCategory = category },
                    label = { Text(category) }
                )
            }
        }
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("${products.size} products", color = Muted, style = MaterialTheme.typography.bodySmall)
            var sortMenuExpanded by remember { mutableStateOf(false) }
            Box {
                TextButton(onClick = { sortMenuExpanded = true }) {
                    Text(when (viewModel.sortMode) {
                        SortMode.RECOMMENDED -> "Recommended"
                        SortMode.PRICE_LOW_TO_HIGH -> "Price: low to high"
                        SortMode.PRICE_HIGH_TO_LOW -> "Price: high to low"
                    }, color = Ink)
                }
                DropdownMenu(expanded = sortMenuExpanded, onDismissRequest = { sortMenuExpanded = false }) {
                    listOf(
                        SortMode.RECOMMENDED to "Recommended",
                        SortMode.PRICE_LOW_TO_HIGH to "Price: low to high",
                        SortMode.PRICE_HIGH_TO_LOW to "Price: high to low"
                    ).forEach { (mode, label) ->
                        DropdownMenuItem(text = { Text(label) }, onClick = {
                            viewModel.sortMode = mode
                            sortMenuExpanded = false
                        })
                    }
                }
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
private fun ProductImage(
    product: HexCladProduct,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    SubcomposeAsyncImage(
        model = product.imageUrl.takeIf { it.isNotBlank() },
        contentDescription = product.name,
        modifier = modifier.background(Color(0xFFF0F0EC)),
        contentScale = contentScale
    ) {
        if (painter.state is AsyncImagePainter.State.Success) {
            SubcomposeAsyncImageContent()
        } else {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(Icons.Default.RestaurantMenu, contentDescription = null, tint = Color(0xFF9B9B91), modifier = Modifier.size(38.dp))
                Spacer(Modifier.height(6.dp))
                Text("HEXCLAD", color = Color(0xFF9B9B91), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun ProductCard(product: HexCladProduct, viewModel: HexCladViewModel) {
    val context = LocalContext.current
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)).background(Color.White)
            .clickable { viewModel.openProduct(product) }
    ) {
        Box(Modifier.fillMaxWidth().height(170.dp).background(Color(0xFFF0F0EC))) {
            ProductImage(product = product, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
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
            OutlinedButton(onClick = {
                viewModel.addToCart(product)
                Toast.makeText(context, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
            }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(2.dp), contentPadding = PaddingValues(vertical = 8.dp)) {
                Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("ADD TO CART", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProductDetailScreen(product: HexCladProduct, viewModel: HexCladViewModel) {
    val context = LocalContext.current
    var quantity by remember(product.id) { mutableIntStateOf(1) }
    LazyColumn(Modifier.fillMaxSize().background(WarmWhite), contentPadding = PaddingValues(bottom = 24.dp)) {
        item {
            Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.closeProduct() }) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                Text("PRODUCT DETAILS", fontWeight = FontWeight.Bold, color = Ink, style = MaterialTheme.typography.labelLarge)
            }
            Box(Modifier.fillMaxWidth().height(330.dp).background(Color(0xFFF0F0EC))) {
                ProductImage(product = product, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
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
                    OutlinedButton(onClick = { quantity = (quantity + 1).coerceAtMost(99) }, shape = RoundedCornerShape(2.dp), contentPadding = PaddingValues(8.dp)) { Icon(Icons.Default.Add, contentDescription = "Increase quantity") }
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { viewModel.toggleFavorite(product) }) {
                        Icon(if (viewModel.isFavorite(product)) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Toggle favorite")
                    }
                }
                Button(onClick = {
                    repeat(quantity) { viewModel.addToCart(product) }
                    Toast.makeText(context, "$quantity × ${product.name} added to cart", Toast.LENGTH_SHORT).show()
                }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(2.dp), colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Color.White)) {
                    Text("ADD TO CART · ${money(product.price * quantity)}", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun WishlistScreen(viewModel: HexCladViewModel) {
    val savedProducts = viewModel.favoriteProducts
    Column(Modifier.fillMaxSize().background(WarmWhite)) {
        Text(
            "SAVED ITEMS",
            Modifier.padding(start = 20.dp, top = 24.dp, bottom = 18.dp),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            color = Ink
        )
        if (savedProducts.isEmpty()) {
            Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 48.dp)) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Muted, modifier = Modifier.size(36.dp))
                Spacer(Modifier.height(14.dp))
                Text("Nothing saved yet", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Ink)
                Spacer(Modifier.height(8.dp))
                Text("Tap the heart on a product to keep it here.", color = Muted)
                Spacer(Modifier.height(18.dp))
                Button(
                    onClick = { viewModel.switchTab(com.example.hexclad.viewmodel.NavTab.SHOP) },
                    colors = ButtonDefaults.buttonColors(containerColor = Ink)
                ) { Text("EXPLORE COOKWARE", color = Color.White) }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(savedProducts, key = { it.id }) { ProductCard(it, viewModel) }
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
                        ProductImage(product = line.product, modifier = Modifier.size(88.dp), contentScale = ContentScale.Crop)
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
                        Column(horizontalAlignment = Alignment.End) {
                            Text(money(line.product.price * line.quantity), color = Ink, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { viewModel.removeFromCart(line.product.id) }) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = "Remove ${line.product.name} from cart", tint = Muted)
                            }
                        }
                    }
                }
            }
            HorizontalDivider()
            Row(Modifier.fillMaxWidth().padding(vertical = 18.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Subtotal", color = Ink, style = MaterialTheme.typography.titleMedium)
                Text(money(viewModel.cartSubtotal), color = Ink, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Button(onClick = { viewModel.switchTab(com.example.hexclad.viewmodel.NavTab.CHECKOUT) }, modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = Ink), shape = RoundedCornerShape(2.dp)) {
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
    var selectedHelpItem by remember { mutableStateOf<String?>(null) }
    val uriHandler = LocalUriHandler.current
    Column(Modifier.fillMaxSize().background(WarmWhite).padding(22.dp)) {
        Text("MY HEXCLAD", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Ink)
        Spacer(Modifier.height(8.dp))
        Text("Your kitchen, all in one place.", color = Muted)
        Spacer(Modifier.height(24.dp))
        listOf("Sign in or create an account", "Order history", "Shipping & returns", "Warranty information", "Contact support").forEach { label ->
            Row(Modifier.fillMaxWidth().clickable { selectedHelpItem = label }.padding(vertical = 18.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(label, color = Ink, style = MaterialTheme.typography.bodyLarge)
                Text("›", color = Ink, style = MaterialTheme.typography.titleLarge)
            }
            HorizontalDivider(color = Color(0xFFE3E2DC))
        }
        Spacer(Modifier.height(20.dp))
        Text("Guest mode · Account and order services are not connected in this prototype.", color = Muted, style = MaterialTheme.typography.bodySmall)
    }
    val selected = selectedHelpItem
    if (selected != null) {
        val externalUrl = when (selected) {
            "Shipping & returns" -> "https://hexclad.com/pages/refund-policy"
            "Warranty information" -> "https://hexclad.com/pages/warranty"
            "Contact support" -> "https://hexclad.com/pages/contact"
            else -> null
        }
        AlertDialog(
            onDismissRequest = { selectedHelpItem = null },
            title = { Text(selected) },
            text = {
                Text(when (selected) {
                    "Sign in or create an account" -> "Live account creation and sign-in require HexClad's approved authentication integration. No account has been created or authenticated."
                    "Order history" -> if (viewModel.demoOrders.isEmpty()) "No demo orders yet. Orders created in this prototype will appear here; this is not the live HexClad account order history." else viewModel.demoOrders.joinToString(separator = "\n\n") { order -> "${order.orderNumber} · ${money(order.total)}\n${order.itemSummary}\nRecipient: ${order.recipient}\nPayment: ${order.paymentMethod} (demo)" }
                    "Shipping & returns" -> "Open HexClad's official policy page in your browser to review current shipping and return terms."
                    "Warranty information" -> "Open HexClad's official warranty page in your browser to review current coverage."
                    else -> "Open HexClad's official support page in your browser."
                })
            },
            confirmButton = {
                TextButton(onClick = {
                    if (externalUrl != null) uriHandler.openUri(externalUrl)
                    selectedHelpItem = null
                }) { Text(if (externalUrl != null) "Open official page" else "OK") }
            },
            dismissButton = {
                if (externalUrl != null) TextButton(onClick = { selectedHelpItem = null }) { Text("Cancel") }
            }
        )
    }
}

private fun money(amount: Double): String = "$" + String.format(Locale.US, "%.2f", amount)


@Composable
fun CheckoutScreen(viewModel: HexCladViewModel) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("Card (demo)") }
    var showConfirmation by remember { mutableStateOf(false) }
    var showValidation by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(Modifier.fillMaxSize().background(WarmWhite)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { viewModel.switchTab(com.example.hexclad.viewmodel.NavTab.CART) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back to cart")
            }
            Text("CHECKOUT", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Ink)
        }
        if (viewModel.cartLines.isEmpty()) {
            Column(Modifier.fillMaxWidth().padding(24.dp)) {
                Text("Your cart is empty", style = MaterialTheme.typography.titleLarge, color = Ink, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                Button(onClick = { viewModel.switchTab(com.example.hexclad.viewmodel.NavTab.SHOP) }) {
                    Text("Continue shopping")
                }
            }
        } else {
            LazyColumn(Modifier.weight(1f).fillMaxWidth(), contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)) {
                item {
                    Text("DELIVERY DETAILS", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Ink)
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Full name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email address") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Street address") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City / region") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = postalCode, onValueChange = { postalCode = it }, label = { Text("Postal code") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(Modifier.height(24.dp))
                    Text("PAYMENT METHOD", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Ink)
                    Spacer(Modifier.height(8.dp))
                    listOf("Card (demo)", "Pay on delivery (demo)").forEach { method ->
                        Row(Modifier.fillMaxWidth().clickable { paymentMethod = method }.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = paymentMethod == method, onClick = { paymentMethod = method })
                            Spacer(Modifier.width(8.dp))
                            Text(method, color = Ink)
                        }
                    }
                    Text("Demo only. No card details are collected and no real payment or order will be created.", color = Muted, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(24.dp))
                    Text("ORDER SUMMARY", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Ink)
                    Spacer(Modifier.height(8.dp))
                }
                items(viewModel.cartLines, key = { it.product.id }) { line ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(line.product.name, color = Ink, fontWeight = FontWeight.Medium)
                            Text("Qty ${line.quantity}", color = Muted, style = MaterialTheme.typography.bodySmall)
                        }
                        Text(money(line.product.price * line.quantity), color = Ink, fontWeight = FontWeight.Bold)
                    }
                }
                item {
                    HorizontalDivider(Modifier.padding(vertical = 10.dp))
                    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", color = Ink)
                        Text(money(viewModel.cartSubtotal), color = Ink, fontWeight = FontWeight.Bold)
                    }
                    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Shipping", color = Muted)
                        Text("Calculated by store", color = Muted, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)) {
                if (showValidation) {
                    Text("Please enter your name, a valid email, street address, city, and postal code.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(6.dp))
                }
                Button(
                    onClick = {
                        val valid = fullName.isNotBlank() && email.contains("@") && address.isNotBlank() && city.isNotBlank() && postalCode.isNotBlank()
                        if (valid) {
                            showValidation = false
                            showConfirmation = true
                        } else {
                            showValidation = true
                            Toast.makeText(context, "Complete the required delivery details", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Color.White),
                    shape = RoundedCornerShape(2.dp)
                ) {
                    Text("PLACE DEMO ORDER · ${money(viewModel.cartSubtotal)}", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Demo order complete") },
            text = { Text("Thanks, $fullName. A local demo order will be saved on this device using $paymentMethod. No order is sent to HexClad and no payment is processed.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.recordDemoOrder(fullName, paymentMethod)
                    viewModel.clearCart()
                    showConfirmation = false
                    viewModel.switchTab(com.example.hexclad.viewmodel.NavTab.HOME)
                }) { Text("Back to home") }
            }
        )
    }
}
