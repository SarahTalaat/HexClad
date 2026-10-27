package com.example.hexclad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.hexclad.ui.theme.HexCladTheme

private val Black = Color(0xFF101010)
private val Cream = Color(0xFFF3F1EB)
private val Gold = Color(0xFFD6B46A)
private val Gray = Color(0xFF777777)

private data class Product(val name: String, val price: Int, val oldPrice: Int? = null, val category: String, val image: String, val tag: String? = null)

private val products = listOf(
    Product("Hybrid Pots & Pans Set, 12-pc", 699, 999, "Sets", "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?auto=format&fit=crop&w=900&q=85", "BEST SELLER"),
    Product("Hybrid Fry Pan Set with Lids, 6pc", 399, 532, "Sets", "https://images.unsplash.com/photo-1584990347449-a6d9f9d8b9e9?auto=format&fit=crop&w=900&q=85", "25% OFF"),
    Product("Hybrid Fry Pan, 12-inch", 179, null, "Pans", "https://images.unsplash.com/photo-1574269909862-7e1d70bb8078?auto=format&fit=crop&w=900&q=85"),
    Product("Hybrid Pot Set with Lids, 6-pc", 379, 467, "Pots", "https://images.unsplash.com/photo-1556911220-bff31c812dba?auto=format&fit=crop&w=900&q=85", "19% OFF"),
    Product("Damascus Steel Knife Set, 7-pc", 499, 783, "Knives", "https://images.unsplash.com/photo-1593618998160-e34014e67546?auto=format&fit=crop&w=900&q=85", "36% OFF"),
    Product("HexMill Salt & Pepper Grinder Set", 249, 318, "HexMills", "https://images.unsplash.com/photo-1590794056226-79ef3a8147e1?auto=format&fit=crop&w=900&q=85", "BEST SELLER"),
    Product("Hybrid Wok with Lid, 14-inch", 219, null, "Pans", "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?auto=format&fit=crop&w=900&q=85"),
    Product("Hybrid Deep Sauté Pan, 7-Qt", 229, null, "Pots", "https://images.unsplash.com/photo-1584990347449-a6d9f9d8b9e9?auto=format&fit=crop&w=900&q=85")
)
private val categories = listOf("Shop All", "Sets", "Pans", "Pots", "Knives", "HexMills", "Aprons", "Kitchen Tools")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HexCladTheme { HexCladApp() } }
    }
}

@Composable
private fun HexCladApp() {
    var tab by remember { mutableStateOf("Home") }
    var category by remember { mutableStateOf("Shop All") }
    var searchOpen by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var detail by remember { mutableStateOf<Product?>(null) }
    val cart = remember { mutableStateMapOf<String, Int>() }
    val saved = remember { mutableStateListOf<String>() }
    val count = cart.values.sum()
    MaterialTheme(colorScheme = lightColorScheme(primary = Black, onPrimary = Color.White, background = Color.White, surface = Color.White, onSurface = Black, secondary = Gold)) {
        Scaffold(containerColor = Color.White, bottomBar = {
            if (detail == null) NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                listOf("Home", "Shop", "Recipes", "Cart", "Account").forEach { label ->
                    val icon = when (label) { "Home" -> Icons.Default.Home; "Shop" -> Icons.Default.Storefront; "Recipes" -> Icons.Default.RestaurantMenu; "Cart" -> Icons.Default.ShoppingBag; else -> Icons.Default.PersonOutline }
                    NavigationBarItem(selected = tab == label, onClick = { tab = label }, icon = {
                        if (label == "Cart" && count > 0) BadgedBox(badge = { Badge { Text(count.toString()) } }) { Icon(icon, label) } else Icon(icon, label)
                    }, label = { Text(label, fontSize = 10.sp) }, colors = NavigationBarItemDefaults.colors(selectedIconColor = Black, selectedTextColor = Black, indicatorColor = Cream, unselectedIconColor = Gray))
                }
            }
        }) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                when {
                    detail != null -> ProductDetail(detail!!, cart[detail!!.name] ?: 0, saved.contains(detail!!.name), { detail = null }, { cart[detail!!.name] = (cart[detail!!.name] ?: 0) + 1 }, {
                        val n = detail!!.name
                        if (saved.contains(n)) saved.remove(n) else saved.add(n)
                    })
                    tab == "Shop" -> ShopPage(category, { category = it }, searchOpen, { searchOpen = it }, query, { query = it }, saved, { n -> if (saved.contains(n)) saved.remove(n) else saved.add(n) }, { detail = it }, { p -> cart[p.name] = (cart[p.name] ?: 0) + 1 })
                    tab == "Cart" -> CartPage(cart, { tab = "Shop" }, { n, q -> if (q <= 0) cart.remove(n) else cart[n] = q }, { detail = it })
                    tab == "Recipes" -> RecipesPage()
                    tab == "Account" -> AccountPage(saved.size, count)
                    else -> HomePage({ tab = "Shop" }, { category = it; tab = "Shop" }, { detail = it }, { p -> cart[p.name] = (cart[p.name] ?: 0) + 1 }, saved, { n -> if (saved.contains(n)) saved.remove(n) else saved.add(n) })
                }
            }
        }
    }
}

@Composable
private fun Promo() {
    Box(Modifier.fillMaxWidth().background(Black).padding(vertical = 9.dp), contentAlignment = Alignment.Center) {
        Text("THE HYBRID REVOLUTION  •  SHOP BEST SELLERS", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
    }
}

@Composable
private fun Header(onShop: () -> Unit, searchOpen: Boolean = false, onSearchOpen: (Boolean) -> Unit = {}, query: String = "", onQuery: (String) -> Unit = {}) {
    Column {
        Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = { onSearchOpen(!searchOpen) }) { Icon(Icons.Default.Search, "Search") }
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onShop() }) {
                Text("HEXCLAD", fontSize = 24.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
                Text("THE HYBRID REVOLUTION", fontSize = 7.sp, letterSpacing = 2.sp, color = Gray)
            }
            IconButton(onClick = onShop) { Icon(Icons.Default.ShoppingBag, "Shop") }
        }
        if (searchOpen) OutlinedTextField(value = query, onValueChange = onQuery, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), placeholder = { Text("Search cookware, knives, and more") }, leadingIcon = { Icon(Icons.Default.Search, null) }, singleLine = true)
    }
}

@Composable
private fun HomePage(onShop: () -> Unit, onCategory: (String) -> Unit, onDetail: (Product) -> Unit, onAdd: (Product) -> Unit, saved: List<String>, onSave: (String) -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 20.dp)) {
        item { Promo() }
        item { Header(onShop) }
        item {
            Box(Modifier.fillMaxWidth().height(420.dp).background(Black)) {
                AsyncImage("https://images.unsplash.com/photo-1556911220-e15b29be8c8f?auto=format&fit=crop&w=1600&q=90", "Modern kitchen", Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Color(0xE6000000), Color(0x55000000), Color.Transparent))))
                Column(Modifier.align(Alignment.CenterStart).padding(24.dp), verticalArrangement = Arrangement.spacedBy(13.dp)) {
                    Text("COOK LIKE YOU MEAN IT.", color = Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Text("Welcome to the\nhybrid revolution.", color = Color.White, fontSize = 36.sp, lineHeight = 40.sp, fontWeight = FontWeight.Black)
                    Text("The performance of stainless. The durability of cast iron. The convenience of nonstick.", color = Color.White, fontSize = 14.sp, lineHeight = 21.sp, modifier = Modifier.fillMaxWidth(.82f))
                    Button(onClick = onShop, shape = RoundedCornerShape(2.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Black)) { Text("SHOP NOW", fontWeight = FontWeight.Bold, letterSpacing = 1.sp) }
                }
            }
        }
        item {
            Column(Modifier.fillMaxWidth().padding(vertical = 26.dp)) {
                Text("SHOP BY CATEGORY", Modifier.align(Alignment.CenterHorizontally), fontSize = 19.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Spacer(Modifier.height(16.dp))
                Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    categories.drop(1).forEachIndexed { i, cat ->
                        val imgs = products.map { it.image }
                        Column(Modifier.width(118.dp).clickable { onCategory(cat) }, horizontalAlignment = Alignment.CenterHorizontally) {
                            AsyncImage(imgs[i % imgs.size], cat, Modifier.fillMaxWidth().height(115.dp).clip(RoundedCornerShape(3.dp)), contentScale = ContentScale.Crop)
                            Spacer(Modifier.height(8.dp))
                            Text(cat.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = .7.sp)
                        }
                    }
                }
            }
        }
        item {
            Column(Modifier.fillMaxWidth().background(Cream).padding(22.dp), verticalArrangement = Arrangement.spacedBy(11.dp)) {
                Text("THE LAST COOKWARE SET YOU’LL EVER BUY", color = Gray, fontSize = 10.sp, letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold)
                Text("A better kind of\nheat.", fontSize = 32.sp, lineHeight = 35.sp, fontWeight = FontWeight.Black)
                Text("Our Hybrid sets are the best way to level up your kitchen and save big.", fontSize = 14.sp, lineHeight = 21.sp)
                TextButton(onClick = onShop, contentPadding = PaddingValues(0.dp)) { Text("SHOP ALL SETS  →", color = Black, fontWeight = FontWeight.Bold) }
            }
        }
        item { SectionTitle("BEST SELLERS", "The pieces home cooks reach for every day.", onShop) }
        item {
            Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                products.take(6).forEach { p -> ProductCard(p, saved.contains(p.name), { onSave(p.name) }, { onDetail(p) }, { onAdd(p) }, Modifier.width(205.dp)) }
            }
            Spacer(Modifier.height(26.dp))
        }
        item {
            Box(Modifier.fillMaxWidth().height(280.dp).background(Black)) {
                AsyncImage(products[2].image, "Cookware", Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                Box(Modifier.fillMaxSize().background(Color(0x99000000)))
                Column(Modifier.align(Alignment.Center).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("AN UNRIVALED COOKING EXPERIENCE", color = Gold, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.4.sp)
                    Text("Made for the way\nyou cook.", color = Color.White, fontSize = 31.sp, lineHeight = 35.sp, fontWeight = FontWeight.Black)
                    Text("Built to sear. Designed to last.", color = Color.White, fontSize = 13.sp)
                    OutlinedButton(onClick = onShop, shape = RoundedCornerShape(2.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)) { Text("DISCOVER HYBRID") }
                }
            }
        }
        item {
            Column(Modifier.fillMaxWidth().background(Cream).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Text("OVER A MILLION HOME CHEFS LOVE HEXCLAD", color = Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text("50,000+ 5-star reviews", fontSize = 24.sp, fontWeight = FontWeight.Black)
                Text("“Incredible searing, effortless cleanup, and cookware I love using every day.”", fontSize = 14.sp, lineHeight = 21.sp)
            }
        }
        item { SectionTitle("MORE TO LOVE", "Tools for every part of your kitchen.", onShop) }
        item {
            Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                products.drop(2).forEach { p -> ProductCard(p, saved.contains(p.name), { onSave(p.name) }, { onDetail(p) }, { onAdd(p) }, Modifier.width(205.dp)) }
            }
        }
        item { Footer(onShop) }
    }
}

@Composable
private fun SectionTitle(title: String, subtitle: String, onAction: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 20.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 19.sp, fontWeight = FontWeight.Black, letterSpacing = .7.sp)
            Text(subtitle, fontSize = 11.sp, color = Gray)
        }
        TextButton(onClick = onAction) { Text("SHOP ALL →", color = Black, fontSize = 9.sp, fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun ProductCard(p: Product, saved: Boolean, onSave: (String) -> Unit, onOpen: () -> Unit, onAdd: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier) {
        Box(Modifier.fillMaxWidth().height(205.dp).clip(RoundedCornerShape(3.dp)).background(Cream).clickable { onOpen() }) {
            AsyncImage(p.image, p.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            if (p.tag != null) Text(p.tag, Modifier.align(Alignment.TopStart).padding(7.dp).background(Color.White).padding(horizontal = 6.dp, vertical = 5.dp), fontSize = 8.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { onSave(p.name) }, modifier = Modifier.align(Alignment.TopEnd).padding(3.dp).size(33.dp).background(Color.White.copy(alpha = .94f), CircleShape)) {
                Icon(if (saved) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder, "Save to wishlist", tint = if (saved) Color.Red else Black, modifier = Modifier.size(18.dp))
            }
        }
        Spacer(Modifier.height(9.dp))
        Text(p.name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.heightIn(min = 36.dp).clickable { onOpen() })
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            Text("$${p.price}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            if (p.oldPrice != null) Text("$${p.oldPrice}", fontSize = 11.sp, color = Gray, textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
        }
        OutlinedButton(onClick = onAdd, modifier = Modifier.fillMaxWidth().padding(top = 7.dp).height(37.dp), shape = RoundedCornerShape(2.dp), contentPadding = PaddingValues(3.dp)) {
            Icon(Icons.Default.Add, null, modifier = Modifier.size(13.dp)); Spacer(Modifier.width(4.dp)); Text("ADD TO CART", fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = .6.sp)
        }
    }
}

@Composable
private fun ShopPage(category: String, onCategory: (String) -> Unit, searchOpen: Boolean, onSearchOpen: (Boolean) -> Unit, query: String, onQuery: (String) -> Unit, saved: List<String>, onSave: (String) -> Unit, onDetail: (Product) -> Unit, onAdd: (Product) -> Unit) {
    val shown = products.filter { (category == "Shop All" || it.category.equals(category, true)) && it.name.contains(query, true) }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 22.dp)) {
        item { Promo() }
        item { Header({ onCategory("Shop All") }, searchOpen, onSearchOpen, query, onQuery) }
        item { Column(Modifier.padding(horizontal = 18.dp, vertical = 15.dp)) { Text("SHOP ALL", fontSize = 10.sp, color = Gray, fontWeight = FontWeight.Bold, letterSpacing = 2.sp); Text(if (category == "Shop All") "Find your new favorite." else category, fontSize = 27.sp, fontWeight = FontWeight.Black) } }
        item {
            Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                categories.forEach { c -> FilterChip(selected = c == category, onClick = { onCategory(c) }, label = { Text(c, fontSize = 10.sp) }, shape = RoundedCornerShape(2.dp)) }
            }
            Row(Modifier.fillMaxWidth().padding(18.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text("${shown.size} PRODUCTS", fontSize = 10.sp, fontWeight = FontWeight.Bold); Text("☷  FILTER & SORT", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
        }
        if (shown.isEmpty()) item { Text("No products match your search.", Modifier.padding(24.dp), color = Gray) }
        items(shown.chunked(2)) { pair ->
            Row(Modifier.fillMaxWidth().padding(horizontal = 13.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                pair.forEach { p -> ProductCard(p, saved.contains(p.name), onSave, { onDetail(p) }, { onAdd(p) }, Modifier.weight(1f)) }
                if (pair.size == 1) Spacer(Modifier.weight(1f))
            }
        }
        item { Footer { onCategory("Shop All") } }
    }
}

@Composable
private fun ProductDetail(p: Product, quantity: Int, saved: Boolean, onBack: () -> Unit, onAdd: () -> Unit, onSave: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 24.dp)) {
        item {
            Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
                Text("HEXCLAD", fontSize = 20.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
                IconButton(onClick = onSave) { Icon(if (saved) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder, "Wishlist") }
            }
        }
        item { AsyncImage(p.image, p.name, Modifier.fillMaxWidth().height(340.dp).background(Cream), contentScale = ContentScale.Crop) }
        item {
            Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(p.category.uppercase(), color = Gray, fontSize = 10.sp, letterSpacing = 1.5.sp, fontWeight = FontWeight.Bold)
                Text(p.name, fontSize = 27.sp, lineHeight = 31.sp, fontWeight = FontWeight.Black)
                Text("★★★★★  Customer favorite", color = Gold, fontSize = 13.sp)
                Row(verticalAlignment = Alignment.CenterVertically) { Text("$${p.price}", fontSize = 23.sp, fontWeight = FontWeight.Bold); if (p.oldPrice != null) Text("  $${p.oldPrice}", color = Gray, textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough) }
                Text("Hybrid performance. Exceptional durability. Effortless everyday cooking. Designed to deliver the sear of stainless steel with the convenience of nonstick.", fontSize = 14.sp, lineHeight = 22.sp)
                Text("✓  Metal-utensil safe     ✓  Easy cleanup     ✓  Lifetime warranty", fontSize = 12.sp, color = Gray)
                Button(onClick = onAdd, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(2.dp), colors = ButtonDefaults.buttonColors(containerColor = Black)) { Text(if (quantity > 0) "ADD ANOTHER  •  IN CART: ${quantity}" else "ADD TO CART", fontWeight = FontWeight.Bold, letterSpacing = .7.sp) }
                OutlinedButton(onClick = onSave, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(2.dp)) { Icon(if (saved) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder, null); Spacer(Modifier.width(7.dp)); Text(if (saved) "SAVED TO WISHLIST" else "ADD TO WISHLIST") }
            }
        }
    }
}

@Composable
private fun CartPage(cart: Map<String, Int>, onShop: () -> Unit, onQuantity: (String, Int) -> Unit, onDetail: (Product) -> Unit) {
    val lines = products.filter { (cart[it.name] ?: 0) > 0 }
    val subtotal = lines.sumOf { it.price * (cart[it.name] ?: 0) }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 24.dp)) {
        item { Promo() }
        item { Header(onShop) }
        item { Column(Modifier.padding(20.dp)) { Text("YOUR CART", color = Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp); Text(if (lines.isEmpty()) "Your cart is waiting." else "Ready for better cooking.", fontSize = 27.sp, fontWeight = FontWeight.Black) } }
        if (lines.isEmpty()) item {
            Column(Modifier.fillMaxWidth().padding(25.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Default.ShoppingBag, null, modifier = Modifier.size(48.dp), tint = Gray)
                Text("Your cart is empty", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Discover cookware designed to make every meal better.", color = Gray)
                Button(onClick = onShop, colors = ButtonDefaults.buttonColors(containerColor = Black), shape = RoundedCornerShape(2.dp)) { Text("SHOP BEST SELLERS") }
            }
        } else {
            items(lines) { p ->
                Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AsyncImage(p.image, p.name, Modifier.size(96.dp).clip(RoundedCornerShape(3.dp)).clickable { onDetail(p) }, contentScale = ContentScale.Crop)
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(p.name, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text("$${p.price}", fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { onQuantity(p.name, (cart[p.name] ?: 1) - 1) }, modifier = Modifier.size(30.dp)) { Icon(Icons.Default.Remove, "Decrease", modifier = Modifier.size(15.dp)) }
                            Text("${cart[p.name] ?: 0}", modifier = Modifier.padding(horizontal = 8.dp))
                            IconButton(onClick = { onQuantity(p.name, (cart[p.name] ?: 0) + 1) }, modifier = Modifier.size(30.dp)) { Icon(Icons.Default.Add, "Increase", modifier = Modifier.size(15.dp)) }
                            Spacer(Modifier.weight(1f))
                            IconButton(onClick = { onQuantity(p.name, 0) }) { Icon(Icons.Default.DeleteOutline, "Remove", tint = Gray) }
                        }
                    }
                }
                HorizontalDivider(color = Cream)
            }
            item {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Subtotal"); Text("$${subtotal}", fontWeight = FontWeight.Bold) }
                    Text("Shipping and taxes calculated at checkout.", color = Gray, fontSize = 12.sp)
                    Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Black), shape = RoundedCornerShape(2.dp)) { Text("CHECKOUT") }
                    Text("Demo checkout — no payment will be collected.", fontSize = 10.sp, color = Gray)
                }
            }
        }
        item { Footer(onShop) }
    }
}

@Composable
private fun RecipesPage() {
    LazyColumn(Modifier.fillMaxSize()) {
        item { Promo() }
        item { Header({}) }
        item {
            Box(Modifier.fillMaxWidth().height(280.dp).background(Black)) {
                AsyncImage(products[0].image, "Cooking inspiration", Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                Box(Modifier.fillMaxSize().background(Color(0x77000000)))
                Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("FROM OUR KITCHEN TO YOURS", color = Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.4.sp)
                    Text("Find your next\nsignature dish.", color = Color.White, fontSize = 30.sp, lineHeight = 35.sp, fontWeight = FontWeight.Black)
                }
            }
        }
        item { SectionTitle("RECIPES & INSPIRATION", "Ideas worth gathering around.", {}) }
        items(listOf("Crispy-skinned salmon with herbs", "Weeknight steak with pan sauce", "Golden skillet potatoes", "A better Sunday roast")) { recipe ->
            Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(products[2].image, recipe, Modifier.size(100.dp).clip(RoundedCornerShape(3.dp)), contentScale = ContentScale.Crop)
                Column(Modifier.padding(start = 12.dp)) { Text("COOKING INSPIRATION", color = Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold); Text(recipe, fontSize = 15.sp, fontWeight = FontWeight.Bold); Text("DISCOVER RECIPE →", fontSize = 10.sp) }
            }
        }
        item { Footer {} }
    }
}

@Composable
private fun AccountPage(savedCount: Int, cartCount: Int) {
    LazyColumn(Modifier.fillMaxSize()) {
        item { Promo() }
        item { Header({}) }
        item {
            Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("YOUR HEXCLAD", color = Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.7.sp)
                Text("Welcome to better cooking.", fontSize = 28.sp, fontWeight = FontWeight.Black)
                Text("Sign in to manage orders, saved items, and account details.", color = Gray, fontSize = 14.sp)
                Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Black), shape = RoundedCornerShape(2.dp)) { Text("SIGN IN / CREATE ACCOUNT") }
            }
        }
        item { AccountRow(Icons.Default.FavoriteBorder, "Wishlist", "$${savedCount} saved items") }
        item { AccountRow(Icons.Default.ShoppingBag, "Shopping bag", "$${cartCount} items") }
        item { AccountRow(Icons.Default.LocalShipping, "Orders & tracking", "View your order history") }
        item { AccountRow(Icons.Default.VerifiedUser, "Warranty", "Learn about lifetime coverage") }
        item { AccountRow(Icons.Default.HelpOutline, "Help & support", "Get help with your order") }
        item { Footer {} }
    }
}

@Composable
private fun AccountRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String) {
    Row(Modifier.fillMaxWidth().clickable {}.padding(horizontal = 20.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null)
        Column(Modifier.weight(1f).padding(start = 14.dp)) { Text(title, fontWeight = FontWeight.SemiBold); Text(subtitle, color = Gray, fontSize = 12.sp) }
        Icon(Icons.Default.ChevronRight, null, tint = Gray)
    }
    HorizontalDivider(color = Cream)
}

@Composable
private fun Footer(onShop: () -> Unit) {
    Column(Modifier.fillMaxWidth().background(Black).padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("HEXCLAD", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
        Text("The hybrid revolution in cookware.", color = Color.LightGray, fontSize = 13.sp)
        HorizontalDivider(color = Color.DarkGray)
        Text("SHOP  •  OUR STORY  •  RECIPES  •  WARRANTY  •  HELP", color = Color.White, fontSize = 10.sp, lineHeight = 18.sp)
        OutlinedButton(onClick = onShop, shape = RoundedCornerShape(2.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)) { Text("EXPLORE HEXCLAD") }
        Text("© HexClad. Demo app. Prices and availability may change.", color = Color.Gray, fontSize = 10.sp)
    }
}
