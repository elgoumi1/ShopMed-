package com.example.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AffiliateClick(
    val clickId: String,
    val productId: String,
    val productName: String,
    val retailerName: String,
    val networkName: String,
    val targetCountry: String,
    val trafficSource: String, // "Home Banner", "Category Page", "AI Concierge", "Comparison Tool", "Direct Search"
    val estimatedCommission: Double,
    val currency: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Tracking Active" // "Tracking Active", "Pending Validation", "Approved"
) {
    fun formattedDate(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}

data class PriceAlert(
    val id: String,
    val productId: String,
    val productName: String,
    val currentPrice: Double,
    val targetPrice: Double,
    val currency: String,
    val userEmail: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isTriggered: Boolean = false
)

data class Article(
    val id: String,
    val title: String,
    val excerpt: String,
    val content: String,
    val category: String,
    val author: String,
    val readTime: String,
    val imageUrl: String,
    val date: String,
    val targetMarket: String // "Morocco & EU", "Morocco", "Europe"
)

data class AffiliateNetworkInfo(
    val id: String,
    val name: String,
    val status: String, // "Active", "Connected", "Pending Approval"
    val defaultCommissionRate: String,
    val activeLinks: Int,
    val totalClicks: Int,
    val website: String,
    val trackingTag: String
)
