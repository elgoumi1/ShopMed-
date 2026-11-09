package com.example.model

import java.util.Locale

enum class AppLanguage(val code: String, val label: String, val nativeName: String, val isRtl: Boolean) {
    EN("en", "English", "English", false),
    FR("fr", "French", "Français", false),
    AR("ar", "Arabic", "العربية", true),
    ZH("zh", "Chinese", "中文", false)
}

enum class AppCurrency(val code: String, val symbol: String, val label: String) {
    EUR("EUR", "€", "Euro (€)"),
    MAD("MAD", "DH", "Moroccan Dirham (MAD)"),
    USD("USD", "$", "US Dollar ($)")
}

enum class TargetCountry(
    val code: String,
    val nameEn: String,
    val nameFr: String,
    val nameAr: String,
    val nameZh: String,
    val flag: String
) {
    ALL("ALL", "All Countries", "Tous les pays", "جميع الدول", "全球", "🌍"),
    MOROCCO("MA", "Morocco", "Maroc", "المغرب", "摩洛哥", "🇲🇦"),
    FRANCE("FR", "France", "France", "فرنسا", "法国", "🇫🇷"),
    SPAIN("ES", "Spain", "Espagne", "إسبانيا", "西班牙", "🇪🇸"),
    GERMANY("DE", "Germany", "Allemagne", "ألمانيا", "德国", "🇩🇪");

    fun localizedName(language: AppLanguage): String = when (language) {
        AppLanguage.EN -> nameEn
        AppLanguage.FR -> nameFr
        AppLanguage.AR -> nameAr
        AppLanguage.ZH -> nameZh
    }
}

object CurrencyRates {
    // 1 EUR = 10.85 MAD, 1 EUR = 1.08 USD
    var eurToMadRate: Double = 10.85
    var eurToUsdRate: Double = 1.08

    fun convertPrice(price: Double, fromCurrency: String, toCurrency: AppCurrency): Double {
        if (fromCurrency.equals(toCurrency.code, ignoreCase = true)) {
            return price
        }
        val priceInEur = when (fromCurrency.uppercase()) {
            "EUR" -> price
            "MAD" -> price / eurToMadRate
            "USD" -> price / eurToUsdRate
            else -> price
        }
        return when (toCurrency) {
            AppCurrency.EUR -> priceInEur
            AppCurrency.MAD -> priceInEur * eurToMadRate
            AppCurrency.USD -> priceInEur * eurToUsdRate
        }
    }

    fun formatPrice(amount: Double, currency: AppCurrency): String {
        return when (currency) {
            AppCurrency.EUR -> String.format(Locale.US, "€%.2f", amount)
            AppCurrency.MAD -> String.format(Locale.US, "%.0f MAD", amount)
            AppCurrency.USD -> String.format(Locale.US, "$%.2f", amount)
        }
    }

    fun formatRetailerOriginal(amount: Double, currencyCode: String): String {
        return if (currencyCode.equals("EUR", ignoreCase = true)) {
            String.format(Locale.US, "€%.2f", amount)
        } else if (currencyCode.equals("USD", ignoreCase = true)) {
            String.format(Locale.US, "$%.2f", amount)
        } else {
            String.format(Locale.US, "%.0f %s", amount, currencyCode)
        }
    }
}
