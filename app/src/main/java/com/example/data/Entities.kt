package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_items")
data class SavedItemEntity(
    @PrimaryKey val productId: String,
    val isFavorite: Boolean = false,
    val isCompared: Boolean = false,
    val lastViewedTimestamp: Long = 0L
)

@Entity(tableName = "affiliate_clicks")
data class ClickLogEntity(
    @PrimaryKey val clickId: String,
    val productId: String,
    val productName: String,
    val retailerName: String,
    val networkName: String,
    val targetCountry: String,
    val trafficSource: String,
    val estimatedCommission: Double,
    val currency: String,
    val timestamp: Long,
    val status: String
)

@Entity(tableName = "price_alerts")
data class PriceAlertEntity(
    @PrimaryKey val id: String,
    val productId: String,
    val productName: String,
    val currentPrice: Double,
    val targetPrice: Double,
    val currency: String,
    val userEmail: String,
    val createdAt: Long,
    val isTriggered: Boolean
)

@Entity(tableName = "custom_products")
data class CustomProductEntity(
    @PrimaryKey val id: String,
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
    val currency: String,
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
    val imageUrlsJson: String,
    val countryAvailabilityJson: String,
    val shippingNote: String,
    val specificationsJson: String,
    val isFeatured: Boolean,
    val isTrending: Boolean,
    val isBestSeller: Boolean,
    val isNewArrival: Boolean,
    val clicksCount: Int,
    val lastPriceUpdate: String
)
