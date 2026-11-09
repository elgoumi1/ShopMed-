package com.example.model

data class Product(
    val id: String,
    val nameEn: String,
    val nameFr: String,
    val nameAr: String,
    val descriptionEn: String,
    val descriptionFr: String,
    val descriptionAr: String,
    val categoryId: Int,
    val brand: String,
    val currentPrice: Double,
    val originalPrice: Double?,
    val currency: String, // "EUR" or "MAD"
    val discountPercent: Int,
    val verifiedDiscount: Boolean,
    val rating: Double,
    val reviewsCount: Int,
    val stockStatus: String,
    val retailerName: String,
    val retailerCountry: String,
    val affiliateNetwork: String,
    val affiliateUrl: String,
    val originalRetailerUrl: String,
    val imageUrls: List<String>,
    val countryAvailability: List<String>, // "MA", "FR", "ES", "DE"
    val shippingNote: String,
    val specifications: Map<String, String>,
    val isFeatured: Boolean = false,
    val isTrending: Boolean = false,
    val isBestSeller: Boolean = false,
    val isNewArrival: Boolean = false,
    val clicksCount: Int = 0,
    val lastPriceUpdate: String = "Today, 10:45 AM",
    val nameZh: String = "",
    val descriptionZh: String = ""
) {
    fun localizedTitle(language: AppLanguage): String = when (language) {
        AppLanguage.EN -> nameEn
        AppLanguage.FR -> nameFr
        AppLanguage.AR -> nameAr
        AppLanguage.ZH -> if (nameZh.isNotBlank()) nameZh else nameEn
    }

    fun localizedDescription(language: AppLanguage): String = when (language) {
        AppLanguage.EN -> descriptionEn
        AppLanguage.FR -> descriptionFr
        AppLanguage.AR -> descriptionAr
        AppLanguage.ZH -> if (descriptionZh.isNotBlank()) descriptionZh else descriptionEn
    }

    fun isAvailableInCountry(countryCode: String): Boolean {
        if (countryCode == "ALL") return true
        return countryAvailability.contains(countryCode) || countryAvailability.contains("ALL")
    }

    fun getEstimatedPrice(targetCurrency: AppCurrency): Double {
        return CurrencyRates.convertPrice(currentPrice, currency, targetCurrency)
    }

    fun getFormattedPrice(targetCurrency: AppCurrency): String {
        val converted = getEstimatedPrice(targetCurrency)
        val formatted = CurrencyRates.formatPrice(converted, targetCurrency)
        return if (targetCurrency.code.equals(currency, ignoreCase = true)) {
            formatted
        } else {
            "$formatted (est.)"
        }
    }
}
