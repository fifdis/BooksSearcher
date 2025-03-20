package com.marafinet.bookssearcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.marafinet.bookssearcher.ui.theme.BooksSearcherTheme

class MainActivity : ComponentActivity() {
    private val viewModel: BooksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BooksSearcherTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf("search", "favorites")
    var selectedItem by remember { mutableStateOf("search") }

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                selected = selectedItem == screen,
                onClick = {
                    selectedItem = screen
                    navController.navigate(screen)
                },
                icon = {
                    Text(if (screen == "search") "📖" else "⭐️")
                },
                label = { Text(if (screen == "search") "Поиск" else "Избранное") }
            )
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, viewModel: BooksViewModel, modifier: Modifier) {
    NavHost(navController, startDestination = "search", modifier = modifier) {
        composable("search") { BooksListScreen(viewModel) }
        composable("favorites") { FavoritesScreen() }
    }
}

@Composable
fun BooksListScreen(viewModel: BooksViewModel = viewModel()) {
    val bookState = viewModel.books.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(bookState.value) { book ->
            BookItemView(book)
        }
    }
}

@Composable
fun BookItemView(book: BookItem) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = book.volumeInfo.imageLinks?.thumbnail ?: "https://imgholder.ru/600x300/8493a8/adb9ca&text=IMAGE+HOLDER&font=kelson",
                contentDescription = "Обложка",
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = book.volumeInfo.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2
            )
        }
    }
}
@Composable
fun FavoritesScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Здесь пока нет избранных книг")
    }
}
