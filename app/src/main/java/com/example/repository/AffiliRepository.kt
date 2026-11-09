package com.example.repository

import android.content.Context
import com.example.data.AffiliShopDatabase
import com.example.data.ClickLogEntity
import com.example.data.CustomProductEntity
import com.example.data.PriceAlertEntity
import com.example.data.SampleData
import com.example.data.SavedItemEntity
import com.example.model.AffiliateClick
import com.example.model.AffiliateNetworkInfo
import com.example.model.AppCurrency
import com.example.model.AppLanguage
import com.example.model.Article
import com.example.model.CategoryCatalog
import com.example.model.CurrencyRates
import com.example.model.PriceAlert
import com.example.model.Product
import com.example.model.TargetCountry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.UUID

enum class ProductSortOrder {
    POPULARITY,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    HIGHEST_DISCOUNT,
    RATING
}

data class FilterCriteria(
    val query: String = "",
    val categoryId: Int? = null,
    val targetCountry: TargetCountry = TargetCountry.ALL,
    val verifiedOnly: Boolean = false,
    val selectedBrand: String? = null,
    val selectedRetailer: String? = null,
    val maxPrice: Double? = null,
    val sortOrder: ProductSortOrder = ProductSortOrder.POPULARITY
)

class AffiliRepository(context: Context) {
    private val database = AffiliShopDatabase.getInstance(context)
    private val dao = database.dao()
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    private val _allProducts = MutableStateFlow<List<Product>>(SampleData.products)
    val allProducts: StateFlow<List<Product>> = _allProducts.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    private val _compareIds = MutableStateFlow<Set<String>>(emptySet())
    val compareIds: StateFlow<Set<String>> = _compareIds.asStateFlow()

    private val _clickLogs = MutableStateFlow<List<AffiliateClick>>(SampleData.sampleInitialClicks)
    val clickLogs: StateFlow<List<AffiliateClick>> = _clickLogs.asStateFlow()

    private val _priceAlerts = MutableStateFlow<List<PriceAlert>>(emptyList())
    val priceAlerts: StateFlow<List<PriceAlert>> = _priceAlerts.asStateFlow()

    private val _articles = MutableStateFlow<List<Article>>(SampleData.articles)
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()

    private val _networks = MutableStateFlow<List<AffiliateNetworkInfo>>(SampleData.affiliateNetworks)
    val networks: StateFlow<List<AffiliateNetworkInfo>> = _networks.asStateFlow()

    init {
        // Load persisted items from Room
        repositoryScope.launch {
            dao.getAllSavedItems().collect { savedList ->
                val favs = savedList.filter { it.isFavorite }.map { it.productId }.toSet()
                val comps = savedList.filter { it.isCompared }.map { it.productId }.toSet()
                _favoriteIds.value = favs
                _compareIds.value = comps
            }
        }

        repositoryScope.launch {
            dao.getAllClicks().collect { entityClicks ->
                if (entityClicks.isNotEmpty()) {
                    val mapped = entityClicks.map {
                        AffiliateClick(
                            clickId = it.clickId,
                            productId = it.productId,
                            productName = it.productName,
                            retailerName = it.retailerName,
                            networkName = it.networkName,
                            targetCountry = it.targetCountry,
                            trafficSource = it.trafficSource,
                            estimatedCommission = it.estimatedCommission,
                            currency = it.currency,
                            timestamp = it.timestamp,
                            status = it.status
                        )
                    }
                    _clickLogs.value = mapped + SampleData.sampleInitialClicks
                }
            }
        }

        repositoryScope.launch {
            dao.getAllPriceAlerts().collect { entityAlerts ->
                _priceAlerts.value = entityAlerts.map {
                    PriceAlert(
                        id = it.id,
                        productId = it.productId,
                        productName = it.productName,
                        currentPrice = it.currentPrice,
                        targetPrice = it.targetPrice,
                        currency = it.currency,
                        userEmail = it.userEmail,
                        createdAt = it.createdAt,
                        isTriggered = it.isTriggered
                    )
                }
            }
        }

        repositoryScope.launch {
            dao.getAllCustomProducts().collect { customEntities ->
                if (customEntities.isNotEmpty()) {
                    val customProds = customEntities.map { entity ->
                        Product(
                            id = entity.id,
                            nameEn = entity.nameEn,
                            nameFr = entity.nameFr,
                            nameAr = entity.nameAr,
                            descriptionEn = entity.descriptionEn,
                            descriptionFr = entity.descriptionFr,
                            descriptionAr = entity.descriptionAr,
                            categoryId = entity.categoryId,
                            brand = entity.brand,
                            currentPrice = entity.currentPrice,
                            originalPrice = entity.originalPrice,
                            currency = entity.currency,
                            discountPercent = entity.discountPercent,
                            verifiedDiscount = entity.verifiedDiscount,
                            rating = entity.rating,
                            reviewsCount = entity.reviewsCount,
                            stockStatus = entity.stockStatus,
                            retailerName = entity.retailerName,
                            retailerCountry = entity.retailerCountry,
                            affiliateNetwork = entity.affiliateNetwork,
                            affiliateUrl = entity.affiliateUrl,
                            originalRetailerUrl = entity.originalRetailerUrl,
                            imageUrls = listOf("https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600"),
                            countryAvailability = listOf("MA", "FR", "ES", "DE"),
                            shippingNote = entity.shippingNote,
                            specifications = mapOf("Import" to "Verified Admin Entry"),
                            isFeatured = entity.isFeatured,
                            isTrending = entity.isTrending,
                            isBestSeller = entity.isBestSeller,
                            isNewArrival = entity.isNewArrival,
                            clicksCount = entity.clicksCount,
                            lastPriceUpdate = entity.lastPriceUpdate
                        )
                    }
                    _allProducts.value = SampleData.products + customProds
                }
            }
        }
    }

    fun toggleFavorite(productId: String) {
        val currentFavs = _favoriteIds.value
        val isNowFav = !currentFavs.contains(productId)
        repositoryScope.launch {
            val existing = dao.getAllSavedItems().firstOrNull()?.find { it.productId == productId }
            val updated = existing?.copy(isFavorite = isNowFav) ?: SavedItemEntity(productId = productId, isFavorite = isNowFav)
            dao.insertOrUpdateSavedItem(updated)
        }
        _favoriteIds.value = if (isNowFav) currentFavs + productId else currentFavs - productId
    }

    fun toggleCompare(productId: String) {
        val current = _compareIds.value
        val isNowCompared = !current.contains(productId)
        if (isNowCompared && current.size >= 4) {
            // max 4 items compared
            return
        }
        repositoryScope.launch {
            val existing = dao.getAllSavedItems().firstOrNull()?.find { it.productId == productId }
            val updated = existing?.copy(isCompared = isNowCompared) ?: SavedItemEntity(productId = productId, isCompared = isNowCompared)
            dao.insertOrUpdateSavedItem(updated)
        }
        _compareIds.value = if (isNowCompared) current + productId else current - productId
    }

    fun recordAffiliateClick(
        product: Product,
        trafficSource: String,
        targetCountry: String
    ): AffiliateClick {
        val commissionRate = 0.05 // average 5% commission
        val estCommission = product.currentPrice * commissionRate
        val click = AffiliateClick(
            clickId = "clk_" + UUID.randomUUID().toString().substring(0, 8),
            productId = product.id,
            productName = product.nameEn,
            retailerName = product.retailerName,
            networkName = product.affiliateNetwork,
            targetCountry = targetCountry,
            trafficSource = trafficSource,
            estimatedCommission = estCommission,
            currency = product.currency,
            timestamp = System.currentTimeMillis(),
            status = "Tracking Active"
        )

        // Increment product clicks
        val updatedList = _allProducts.value.map {
            if (it.id == product.id) it.copy(clicksCount = it.clicksCount + 1) else it
        }
        _allProducts.value = updatedList
        _clickLogs.value = listOf(click) + _clickLogs.value

        repositoryScope.launch {
            dao.insertClick(
                ClickLogEntity(
                    clickId = click.clickId,
                    productId = click.productId,
                    productName = click.productName,
                    retailerName = click.retailerName,
                    networkName = click.networkName,
                    targetCountry = click.targetCountry,
                    trafficSource = click.trafficSource,
                    estimatedCommission = click.estimatedCommission,
                    currency = click.currency,
                    timestamp = click.timestamp,
                    status = click.status
                )
            )
        }
        return click
    }

    fun addPriceAlert(product: Product, targetPrice: Double, userEmail: String) {
        val alert = PriceAlert(
            id = "alt_" + UUID.randomUUID().toString().substring(0, 8),
            productId = product.id,
            productName = product.nameEn,
            currentPrice = product.currentPrice,
            targetPrice = targetPrice,
            currency = product.currency,
            userEmail = userEmail,
            isTriggered = targetPrice >= product.currentPrice
        )
        _priceAlerts.value = listOf(alert) + _priceAlerts.value
        repositoryScope.launch {
            dao.insertPriceAlert(
                PriceAlertEntity(
                    id = alert.id,
                    productId = alert.productId,
                    productName = alert.productName,
                    currentPrice = alert.currentPrice,
                    targetPrice = alert.targetPrice,
                    currency = alert.currency,
                    userEmail = alert.userEmail,
                    createdAt = alert.createdAt,
                    isTriggered = alert.isTriggered
                )
            )
        }
    }

    fun deletePriceAlert(id: String) {
        _priceAlerts.value = _priceAlerts.value.filter { it.id != id }
        repositoryScope.launch {
            dao.deletePriceAlert(id)
        }
    }

    fun addCustomProduct(product: Product) {
        _allProducts.value = _allProducts.value + product
        repositoryScope.launch {
            dao.insertCustomProduct(
                CustomProductEntity(
                    id = product.id,
                    nameEn = product.nameEn,
                    nameFr = product.nameFr,
                    nameAr = product.nameAr,
                    descriptionEn = product.descriptionEn,
                    descriptionFr = product.descriptionFr,
                    descriptionAr = product.descriptionAr,
                    categoryId = product.categoryId,
                    brand = product.brand,
                    currentPrice = product.currentPrice,
                    originalPrice = product.originalPrice,
                    currency = product.currency,
                    discountPercent = product.discountPercent,
                    verifiedDiscount = product.verifiedDiscount,
                    rating = product.rating,
                    reviewsCount = product.reviewsCount,
                    stockStatus = product.stockStatus,
                    retailerName = product.retailerName,
                    retailerCountry = product.retailerCountry,
                    affiliateNetwork = product.affiliateNetwork,
                    affiliateUrl = product.affiliateUrl,
                    originalRetailerUrl = product.originalRetailerUrl,
                    imageUrlsJson = "",
                    countryAvailabilityJson = "",
                    shippingNote = product.shippingNote,
                    specificationsJson = "",
                    isFeatured = product.isFeatured,
                    isTrending = product.isTrending,
                    isBestSeller = product.isBestSeller,
                    isNewArrival = product.isNewArrival,
                    clicksCount = product.clicksCount,
                    lastPriceUpdate = product.lastPriceUpdate
                )
            )
        }
    }

    fun deleteProduct(productId: String) {
        _allProducts.value = _allProducts.value.filter { it.id != productId }
        repositoryScope.launch {
            dao.deleteCustomProduct(productId)
        }
    }

    fun importCsvProducts(csvText: String): Pair<Int, Int> {
        var imported = 0
        var duplicates = 0
        val existingNames = _allProducts.value.map { it.nameEn.lowercase().trim() }.toSet()
        val newEntities = mutableListOf<CustomProductEntity>()
        val newProds = mutableListOf<Product>()

        csvText.lines().forEach { line ->
            val parts = line.split(",")
            if (parts.size >= 6) {
                val name = parts[0].trim()
                if (name.equals("name", ignoreCase = true) || name.isEmpty()) return@forEach
                if (existingNames.contains(name.lowercase())) {
                    duplicates++
                    return@forEach
                }

                val brand = parts[1].trim()
                val catId = parts[2].trim().toIntOrNull() ?: 1
                val price = parts[3].trim().toDoubleOrNull() ?: 99.0
                val currency = parts[4].trim().uppercase()
                val retailer = parts[5].trim()
                val url = if (parts.size > 6) parts[6].trim() else "https://example.com/affiliate?tag=affilishop"

                val id = "csv_" + UUID.randomUUID().toString().substring(0, 6)
                val prod = Product(
                    id = id,
                    nameEn = name,
                    nameFr = name,
                    nameAr = name,
                    descriptionEn = "Bulk imported catalog product for $brand from $retailer.",
                    descriptionFr = "Produit importé en masse pour $brand chez $retailer.",
                    descriptionAr = "منتج مستورد عبر لوحة التحكم لـ $brand من $retailer.",
                    categoryId = catId,
                    brand = brand,
                    currentPrice = price,
                    originalPrice = price * 1.25,
                    currency = if (currency == "MAD") "MAD" else "EUR",
                    discountPercent = 20,
                    verifiedDiscount = true,
                    rating = 4.5,
                    reviewsCount = 120,
                    stockStatus = "In Stock",
                    retailerName = retailer,
                    retailerCountry = if (currency == "MAD") "MA" else "FR",
                    affiliateNetwork = if (currency == "MAD") "Jumia KOL" else "Amazon Associates",
                    affiliateUrl = url,
                    originalRetailerUrl = url,
                    imageUrls = listOf("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600"),
                    countryAvailability = listOf("MA", "FR", "ES", "DE"),
                    shippingNote = "Standard delivery",
                    specifications = mapOf("Origin" to "Bulk CSV Import"),
                    isFeatured = false,
                    isNewArrival = true
                )

                newProds.add(prod)
                newEntities.add(
                    CustomProductEntity(
                        id = prod.id,
                        nameEn = prod.nameEn,
                        nameFr = prod.nameFr,
                        nameAr = prod.nameAr,
                        descriptionEn = prod.descriptionEn,
                        descriptionFr = prod.descriptionFr,
                        descriptionAr = prod.descriptionAr,
                        categoryId = prod.categoryId,
                        brand = prod.brand,
                        currentPrice = prod.currentPrice,
                        originalPrice = prod.originalPrice,
                        currency = prod.currency,
                        discountPercent = prod.discountPercent,
                        verifiedDiscount = prod.verifiedDiscount,
                        rating = prod.rating,
                        reviewsCount = prod.reviewsCount,
                        stockStatus = prod.stockStatus,
                        retailerName = prod.retailerName,
                        retailerCountry = prod.retailerCountry,
                        affiliateNetwork = prod.affiliateNetwork,
                        affiliateUrl = prod.affiliateUrl,
                        originalRetailerUrl = prod.originalRetailerUrl,
                        imageUrlsJson = "",
                        countryAvailabilityJson = "",
                        shippingNote = prod.shippingNote,
                        specificationsJson = "",
                        isFeatured = false,
                        isTrending = false,
                        isBestSeller = false,
                        isNewArrival = true,
                        clicksCount = 0,
                        lastPriceUpdate = "Just now"
                    )
                )
                imported++
            }
        }

        if (newProds.isNotEmpty()) {
            _allProducts.value = _allProducts.value + newProds
            repositoryScope.launch {
                dao.insertCustomProducts(newEntities)
            }
        }
        return Pair(imported, duplicates)
    }

    fun filterProducts(
        criteria: FilterCriteria,
        displayCurrency: AppCurrency
    ): List<Product> {
        return _allProducts.value.filter { p ->
            // Query match
            val matchesQuery = if (criteria.query.isBlank()) true else {
                p.nameEn.contains(criteria.query, ignoreCase = true) ||
                p.nameFr.contains(criteria.query, ignoreCase = true) ||
                p.nameAr.contains(criteria.query, ignoreCase = true) ||
                p.brand.contains(criteria.query, ignoreCase = true) ||
                p.retailerName.contains(criteria.query, ignoreCase = true)
            }

            // Category match
            val matchesCategory = criteria.categoryId == null || p.categoryId == criteria.categoryId

            // Country match
            val matchesCountry = criteria.targetCountry == TargetCountry.ALL ||
                    p.isAvailableInCountry(criteria.targetCountry.code)

            // Verified discount match
            val matchesVerified = !criteria.verifiedOnly || p.verifiedDiscount

            // Brand match
            val matchesBrand = criteria.selectedBrand == null || p.brand.equals(criteria.selectedBrand, ignoreCase = true)

            // Retailer match
            val matchesRetailer = criteria.selectedRetailer == null || p.retailerName.equals(criteria.selectedRetailer, ignoreCase = true)

            // Max price match (converted)
            val matchesPrice = criteria.maxPrice == null || p.getEstimatedPrice(displayCurrency) <= criteria.maxPrice

            matchesQuery && matchesCategory && matchesCountry && matchesVerified && matchesBrand && matchesRetailer && matchesPrice
        }.let { list ->
            when (criteria.sortOrder) {
                ProductSortOrder.POPULARITY -> list.sortedByDescending { it.clicksCount }
                ProductSortOrder.PRICE_LOW_TO_HIGH -> list.sortedBy { it.getEstimatedPrice(displayCurrency) }
                ProductSortOrder.PRICE_HIGH_TO_LOW -> list.sortedByDescending { it.getEstimatedPrice(displayCurrency) }
                ProductSortOrder.HIGHEST_DISCOUNT -> list.sortedByDescending { it.discountPercent }
                ProductSortOrder.RATING -> list.sortedByDescending { it.rating }
            }
        }
    }
}
