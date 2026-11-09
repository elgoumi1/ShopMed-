package com.example.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AffiliateClick
import com.example.model.AffiliateNetworkInfo
import com.example.model.AppCurrency
import com.example.model.AppLanguage
import com.example.model.Article
import com.example.model.Category
import com.example.model.CategoryCatalog
import com.example.model.CurrencyRates
import com.example.model.PriceAlert
import com.example.model.Product
import com.example.model.TargetCountry
import com.example.repository.AffiliRepository
import com.example.repository.FilterCriteria
import com.example.repository.ProductSortOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppNavScreen {
    HOME,
    CATALOG,
    AI_ASSISTANT,
    COMPARE,
    WISHLIST,
    PRICE_ALERTS,
    BLOG,
    ADMIN,
    PRODUCT_DETAIL,
    LEGAL
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: String, // "user" or "ai"
    val text: String,
    val recommendedProductIds: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

data class UiAffiliateDialogState(
    val isVisible: Boolean = false,
    val product: Product? = null,
    val trafficSource: String = "App"
)

class AffiliViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AffiliRepository(application)

    // Current navigation destination
    private val _currentScreen = MutableStateFlow(AppNavScreen.HOME)
    val currentScreen: StateFlow<AppNavScreen> = _currentScreen.asStateFlow()

    // Navigation back stack for sub-screens
    private val screenStack = mutableListOf<AppNavScreen>()

    // Selected product for detail page
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    // Selected blog article for detail modal
    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    // Regional settings
    private val _language = MutableStateFlow(AppLanguage.EN)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _currency = MutableStateFlow(AppCurrency.EUR)
    val currency: StateFlow<AppCurrency> = _currency.asStateFlow()

    private val _targetCountry = MutableStateFlow(TargetCountry.ALL)
    val targetCountry: StateFlow<TargetCountry> = _targetCountry.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // Filter criteria
    private val _filterCriteria = MutableStateFlow(FilterCriteria())
    val filterCriteria: StateFlow<FilterCriteria> = _filterCriteria.asStateFlow()

    // Raw repository flows
    val allProducts = repository.allProducts
    val favoriteIds = repository.favoriteIds
    val compareIds = repository.compareIds
    val clickLogs = repository.clickLogs
    val priceAlerts = repository.priceAlerts
    val articles = repository.articles
    val affiliateNetworks = repository.networks

    // Filtered products list
    val filteredProducts: StateFlow<List<Product>> = combine(
        allProducts,
        _filterCriteria,
        _currency
    ) { products, criteria, curr ->
        repository.filterProducts(criteria, curr)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Comparison products
    val comparisonProducts: StateFlow<List<Product>> = combine(
        allProducts,
        compareIds
    ) { prods, ids ->
        prods.filter { ids.contains(it.id) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Wishlist products
    val wishlistProducts: StateFlow<List<Product>> = combine(
        allProducts,
        favoriteIds
    ) { prods, ids ->
        prods.filter { ids.contains(it.id) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Affiliate disclosure dialog
    private val _affiliateDialogState = MutableStateFlow(UiAffiliateDialogState())
    val affiliateDialogState: StateFlow<UiAffiliateDialogState> = _affiliateDialogState.asStateFlow()

    // Regional settings dialog
    private val _showSettingsDialog = MutableStateFlow(false)
    val showSettingsDialog: StateFlow<Boolean> = _showSettingsDialog.asStateFlow()

    // AI Chat Assistant
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "ai",
                text = "Hello! I am your AffiliShop AI Shopping Concierge. I can help you find verified bargains, compare products between Morocco and Europe, check customs duties, or find cheaper alternatives. What are you looking to buy today?",
                recommendedProductIds = listOf("prod_01", "prod_03", "prod_06")
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _aiThinking = MutableStateFlow(false)
    val aiThinking: StateFlow<Boolean> = _aiThinking.asStateFlow()

    // Feedback message (snackbar)
    private val _userNotice = MutableStateFlow<String?>(null)
    val userNotice: StateFlow<String?> = _userNotice.asStateFlow()

    fun navigateTo(screen: AppNavScreen) {
        if (_currentScreen.value != screen) {
            screenStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (screenStack.isNotEmpty()) {
            _currentScreen.value = screenStack.removeAt(screenStack.lastIndex)
            return true
        }
        if (_currentScreen.value != AppNavScreen.HOME) {
            _currentScreen.value = AppNavScreen.HOME
            return true
        }
        return false
    }

    fun openProductDetail(product: Product) {
        _selectedProduct.value = product
        navigateTo(AppNavScreen.PRODUCT_DETAIL)
    }

    fun openArticle(article: Article?) {
        _selectedArticle.value = article
        navigateTo(AppNavScreen.BLOG)
    }

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
    }

    fun setCurrency(curr: AppCurrency) {
        _currency.value = curr
    }

    fun setTargetCountry(country: TargetCountry) {
        _targetCountry.value = country
        _filterCriteria.value = _filterCriteria.value.copy(targetCountry = country)
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun toggleSettingsDialog(show: Boolean) {
        _showSettingsDialog.value = show
    }

    fun updateSearchQuery(query: String) {
        _filterCriteria.value = _filterCriteria.value.copy(query = query)
    }

    fun selectCategory(categoryId: Int?) {
        _filterCriteria.value = _filterCriteria.value.copy(categoryId = categoryId)
        navigateTo(AppNavScreen.CATALOG)
    }

    fun updateSortOrder(order: ProductSortOrder) {
        _filterCriteria.value = _filterCriteria.value.copy(sortOrder = order)
    }

    fun toggleVerifiedOnly(verified: Boolean) {
        _filterCriteria.value = _filterCriteria.value.copy(verifiedOnly = verified)
    }

    fun clearFilters() {
        _filterCriteria.value = FilterCriteria(targetCountry = _targetCountry.value)
    }

    fun toggleFavorite(productId: String) {
        repository.toggleFavorite(productId)
    }

    fun toggleCompare(productId: String) {
        if (compareIds.value.contains(productId)) {
            repository.toggleCompare(productId)
            _userNotice.value = "Removed from comparison"
        } else {
            if (compareIds.value.size >= 4) {
                _userNotice.value = "Maximum 4 products can be compared simultaneously"
            } else {
                repository.toggleCompare(productId)
                _userNotice.value = "Added to side-by-side comparison"
            }
        }
    }

    fun clearNotice() {
        _userNotice.value = null
    }

    // Affiliate link action with transparent disclosure
    fun promptAffiliateRedirect(product: Product, source: String) {
        _affiliateDialogState.value = UiAffiliateDialogState(
            isVisible = true,
            product = product,
            trafficSource = source
        )
    }

    fun dismissAffiliateDialog() {
        _affiliateDialogState.value = UiAffiliateDialogState(isVisible = false)
    }

    fun confirmAffiliateRedirect() {
        val state = _affiliateDialogState.value
        val prod = state.product ?: return
        _affiliateDialogState.value = UiAffiliateDialogState(isVisible = false)

        // Record tracking click
        val click = repository.recordAffiliateClick(
            product = prod,
            trafficSource = state.trafficSource,
            targetCountry = _targetCountry.value.code
        )

        _userNotice.value = "Redirecting to ${prod.retailerName} (Tracking ID: ${click.clickId})"

        // Open external retailer URL in browser
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(prod.affiliateUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            getApplication<Application>().startActivity(intent)
        } catch (_: Exception) {
            // In container emulator or without browser, gracefully fallback
        }
    }

    fun createPriceAlert(product: Product, targetPrice: Double, email: String) {
        repository.addPriceAlert(product, targetPrice, email)
        _userNotice.value = "Price alert set for ${product.nameEn} at $targetPrice"
    }

    fun deletePriceAlert(id: String) {
        repository.deletePriceAlert(id)
        _userNotice.value = "Price alert removed"
    }

    // AI Concierge Chat Assistant with Gemini API & Catalog Grounding
    fun sendUserMessage(query: String) {
        if (query.isBlank()) return
        val userMsg = ChatMessage(sender = "user", text = query)
        _chatMessages.value = _chatMessages.value + userMsg
        _aiThinking.value = true

        viewModelScope.launch {
            val history = _chatMessages.value.map { it.sender to it.text }
            val geminiResponse = com.example.service.GeminiShoppingService.generateShoppingAdvice(
                userMessage = query,
                catalog = allProducts.value,
                conversationHistory = history,
                language = _language.value,
                currency = _currency.value
            )

            val aiMsg = ChatMessage(
                sender = "ai",
                text = geminiResponse.replyText,
                recommendedProductIds = geminiResponse.recommendedProductIds
            )
            _chatMessages.value = _chatMessages.value + aiMsg
            _aiThinking.value = false
        }
    }

    // Admin operations
    fun importCsvCatalog(csvText: String) {
        val (imported, duplicates) = repository.importCsvProducts(csvText)
        _userNotice.value = "Imported $imported new products ($duplicates duplicates skipped)"
    }

    fun deleteAdminProduct(productId: String) {
        repository.deleteProduct(productId)
        _userNotice.value = "Product removed from catalog"
    }

    fun updateEurToMadExchangeRate(rate: Double) {
        CurrencyRates.eurToMadRate = rate
        _userNotice.value = "Updated exchange rate: 1 EUR = $rate MAD"
    }
}
