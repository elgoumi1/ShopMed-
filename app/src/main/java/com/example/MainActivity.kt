package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AffiliBottomBar
import com.example.ui.components.AffiliTopBar
import com.example.ui.components.AffiliateDisclosureDialog
import com.example.ui.components.LanguageCurrencyDialog
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AIAssistantScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.BlogScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.ComparisonScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LegalScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.AffiliShopTheme
import com.example.viewmodel.AffiliViewModel
import com.example.viewmodel.AppNavScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: AffiliViewModel = viewModel()
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val language by viewModel.language.collectAsState()
            val currency by viewModel.currency.collectAsState()
            val targetCountry by viewModel.targetCountry.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()

            val favoriteIds by viewModel.favoriteIds.collectAsState()
            val compareIds by viewModel.compareIds.collectAsState()
            val filteredProducts by viewModel.filteredProducts.collectAsState()
            val allProducts by viewModel.allProducts.collectAsState()
            val comparisonProducts by viewModel.comparisonProducts.collectAsState()
            val wishlistProducts by viewModel.wishlistProducts.collectAsState()
            val clickLogs by viewModel.clickLogs.collectAsState()
            val networks by viewModel.affiliateNetworks.collectAsState()
            val articles by viewModel.articles.collectAsState()
            val selectedArticle by viewModel.selectedArticle.collectAsState()
            val selectedProduct by viewModel.selectedProduct.collectAsState()
            val filterCriteria by viewModel.filterCriteria.collectAsState()

            val chatMessages by viewModel.chatMessages.collectAsState()
            val aiThinking by viewModel.aiThinking.collectAsState()
            val affiliateDialogState by viewModel.affiliateDialogState.collectAsState()
            val showSettingsDialog by viewModel.showSettingsDialog.collectAsState()
            val userNotice by viewModel.userNotice.collectAsState()

            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(userNotice) {
                userNotice?.let {
                    snackbarHostState.showSnackbar(it)
                    viewModel.clearNotice()
                }
            }

            // Handle Back Button on sub-screens
            BackHandler(enabled = currentScreen != AppNavScreen.HOME) {
                viewModel.navigateBack()
            }

            // RTL support when Arabic is selected
            val layoutDirection = if (language.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                AffiliShopTheme(darkTheme = isDarkMode) {
                    Scaffold(
                        topBar = {
                            if (currentScreen != AppNavScreen.PRODUCT_DETAIL) {
                                AffiliTopBar(
                                    currentLanguage = language,
                                    currentCurrency = currency,
                                    currentCountry = targetCountry,
                                    isDarkMode = isDarkMode,
                                    wishlistCount = favoriteIds.size,
                                    compareCount = compareIds.size,
                                    onOpenPreferences = { viewModel.toggleSettingsDialog(true) },
                                    onToggleDarkMode = { viewModel.toggleDarkMode() },
                                    onNavigateWishlist = { viewModel.navigateTo(AppNavScreen.WISHLIST) },
                                    onNavigateCompare = { viewModel.navigateTo(AppNavScreen.COMPARE) }
                                )
                            }
                        },
                        bottomBar = {
                            if (currentScreen != AppNavScreen.PRODUCT_DETAIL && currentScreen != AppNavScreen.LEGAL) {
                                AffiliBottomBar(
                                    currentScreen = currentScreen,
                                    currentLanguage = language,
                                    onNavigate = { viewModel.navigateTo(it) }
                                )
                            }
                        },
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (currentScreen) {
                                AppNavScreen.HOME -> HomeScreen(
                                    products = allProducts,
                                    favoriteIds = favoriteIds,
                                    compareIds = compareIds,
                                    articles = articles,
                                    currentLanguage = language,
                                    currentCurrency = currency,
                                    currentCountry = targetCountry,
                                    searchQuery = filterCriteria.query,
                                    onSearchChange = {
                                        viewModel.updateSearchQuery(it)
                                        viewModel.navigateTo(AppNavScreen.CATALOG)
                                    },
                                    onProductClick = { viewModel.openProductDetail(it) },
                                    onCategoryClick = { viewModel.selectCategory(it) },
                                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                                    onToggleCompare = { viewModel.toggleCompare(it) },
                                    onAffiliateClick = { viewModel.promptAffiliateRedirect(it, "Home Featured") },
                                    onNavigate = { viewModel.navigateTo(it) },
                                    onArticleClick = { viewModel.openArticle(it) }
                                )

                                AppNavScreen.CATALOG -> CatalogScreen(
                                    products = filteredProducts,
                                    favoriteIds = favoriteIds,
                                    compareIds = compareIds,
                                    filterCriteria = filterCriteria,
                                    currentLanguage = language,
                                    currentCurrency = currency,
                                    onSearchChange = { viewModel.updateSearchQuery(it) },
                                    onCategorySelect = { viewModel.selectCategory(it) },
                                    onSortOrderChange = { viewModel.updateSortOrder(it) },
                                    onToggleVerifiedOnly = { viewModel.toggleVerifiedOnly(it) },
                                    onClearFilters = { viewModel.clearFilters() },
                                    onProductClick = { viewModel.openProductDetail(it) },
                                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                                    onToggleCompare = { viewModel.toggleCompare(it) },
                                    onAffiliateClick = { viewModel.promptAffiliateRedirect(it, "Catalog Browser") }
                                )

                                AppNavScreen.PRODUCT_DETAIL -> ProductDetailScreen(
                                    product = selectedProduct,
                                    currentLanguage = language,
                                    currentCurrency = currency,
                                    isFavorite = selectedProduct?.let { favoriteIds.contains(it.id) } ?: false,
                                    isCompared = selectedProduct?.let { compareIds.contains(it.id) } ?: false,
                                    onBackClick = { viewModel.navigateBack() },
                                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                                    onToggleCompare = { viewModel.toggleCompare(it) },
                                    onAffiliateClick = { viewModel.promptAffiliateRedirect(it, "Product Detail") },
                                    onSetPriceAlert = { prod, target, email ->
                                        viewModel.createPriceAlert(prod, target, email)
                                    }
                                )

                                AppNavScreen.COMPARE -> ComparisonScreen(
                                    comparisonProducts = comparisonProducts,
                                    currentLanguage = language,
                                    currentCurrency = currency,
                                    onRemoveFromCompare = { viewModel.toggleCompare(it) },
                                    onAffiliateClick = { viewModel.promptAffiliateRedirect(it, "Comparison Matrix") },
                                    onExploreCatalog = { viewModel.navigateTo(AppNavScreen.CATALOG) },
                                    onAddSampleProducts = {
                                        viewModel.toggleCompare("prod_01")
                                        viewModel.toggleCompare("prod_02")
                                    }
                                )

                                AppNavScreen.AI_ASSISTANT -> ChatScreen(
                                    messages = chatMessages,
                                    allProducts = allProducts,
                                    isThinking = aiThinking,
                                    currentLanguage = language,
                                    currentCurrency = currency,
                                    onSendMessage = { viewModel.sendUserMessage(it) },
                                    onProductClick = { viewModel.openProductDetail(it) },
                                    onAffiliateClick = { viewModel.promptAffiliateRedirect(it, "ChatScreen Gemini Recommendation") }
                                )

                                AppNavScreen.WISHLIST -> WishlistScreen(
                                    wishlistProducts = wishlistProducts,
                                    favoriteIds = favoriteIds,
                                    compareIds = compareIds,
                                    currentLanguage = language,
                                    currentCurrency = currency,
                                    onProductClick = { viewModel.openProductDetail(it) },
                                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                                    onToggleCompare = { viewModel.toggleCompare(it) },
                                    onAffiliateClick = { viewModel.promptAffiliateRedirect(it, "Wishlist") },
                                    onExploreCatalog = { viewModel.navigateTo(AppNavScreen.CATALOG) }
                                )

                                AppNavScreen.BLOG -> BlogScreen(
                                    articles = articles,
                                    selectedArticle = selectedArticle,
                                    onArticleClick = { viewModel.openArticle(it) },
                                    onBackToList = { viewModel.openArticle(null) }
                                )

                                AppNavScreen.ADMIN -> AdminDashboardScreen(
                                    products = allProducts,
                                    clickLogs = clickLogs,
                                    networks = networks,
                                    currentCurrency = currency,
                                    onDeleteProduct = { viewModel.deleteAdminProduct(it) },
                                    onImportCsv = { viewModel.importCsvCatalog(it) },
                                    onUpdateExchangeRate = { viewModel.updateEurToMadExchangeRate(it) },
                                    onProductClick = { viewModel.openProductDetail(it) }
                                )

                                AppNavScreen.LEGAL -> LegalScreen(
                                    onBack = { viewModel.navigateBack() }
                                )

                                else -> Unit
                            }
                        }
                    }

                    // Affiliate Disclosure Confirmation Dialog
                    if (affiliateDialogState.isVisible) {
                        AffiliateDisclosureDialog(
                            product = affiliateDialogState.product,
                            currentLanguage = language,
                            currentCurrency = currency,
                            onDismiss = { viewModel.dismissAffiliateDialog() },
                            onConfirmRedirect = { viewModel.confirmAffiliateRedirect() }
                        )
                    }

                    // Preferences Dialog (Language, Currency, Target Country)
                    if (showSettingsDialog) {
                        LanguageCurrencyDialog(
                            currentLanguage = language,
                            currentCurrency = currency,
                            currentCountry = targetCountry,
                            onLanguageSelected = { viewModel.setLanguage(it) },
                            onCurrencySelected = { viewModel.setCurrency(it) },
                            onCountrySelected = { viewModel.setTargetCountry(it) },
                            onDismiss = { viewModel.toggleSettingsDialog(false) }
                        )
                    }
                }
            }
        }
    }
}
