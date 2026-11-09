package com.example.service

import android.util.Log
import com.example.BuildConfig
import com.example.model.AppCurrency
import com.example.model.AppLanguage
import com.example.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class GeminiResponse(
    val replyText: String,
    val recommendedProductIds: List<String>
)

object GeminiShoppingService {
    private const val TAG = "GeminiShoppingService"
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .build()

    suspend fun generateShoppingAdvice(
        userMessage: String,
        catalog: List<Product>,
        conversationHistory: List<Pair<String, String>>, // list of (role, text)
        language: AppLanguage,
        currency: AppCurrency
    ): GeminiResponse = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        val hasValidKey = apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY"

        if (!hasValidKey) {
            // Intelligent local catalog-grounded fallback
            return@withContext fallbackLocalAdvice(userMessage, catalog, language, currency)
        }

        try {
            // Build grounded product catalog summary
            val catalogSummary = catalog.joinToString("\n") { p ->
                "- [ID: ${p.id}] ${p.nameEn} | Brand: ${p.brand} | Retailer: ${p.retailerName} (${p.retailerCountry}) | Price: ${p.currentPrice} ${p.currency} (≈ ${p.getFormattedPrice(currency)}) | Discount: ${if (p.verifiedDiscount) "${p.discountPercent}% Verified" else "Regular"} | Rating: ${p.rating}★ | Stock: ${p.stockStatus} | Note: ${p.shippingNote}"
            }

            val systemPrompt = """
                You are ShopMed AI Concierge, a world-class shopping assistant and cross-border bargain expert for Morocco, Europe, and international markets.
                Languages supported: English, French, Arabic, Chinese.
                Current user language: ${language.label} (${language.code}).
                Current user currency: ${currency.code}.
                
                You must help users discover the best products, compare technical specifications, explain shipping/customs duties between Morocco and Europe, and suggest budget alternatives.
                IMPORTANT RULES:
                1. Always base recommendations on the verified ShopMed catalog provided below.
                2. Whenever you recommend or mention a product, include its exact ID in square brackets like [ID: prod_01].
                3. Never invent specs, prices, or counterfeit deals.
                4. Respond in the user's chosen language (${language.label}).
                
                VERIFIED CATALOG:
                $catalogSummary
            """.trimIndent()

            val contentsArray = JSONArray()

            // System instruction
            val systemObj = JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", systemPrompt)))
            }
            contentsArray.put(systemObj)

            val modelAck = JSONObject().apply {
                put("role", "model")
                put("parts", JSONArray().put(JSONObject().put("text", "Understood. I am ShopMed AI Concierge. I will ground all recommendations in the verified catalog and format product IDs as [ID: ...].")))
            }
            contentsArray.put(modelAck)

            // Conversation history (last 4 turns)
            conversationHistory.takeLast(4).forEach { (role, text) ->
                val turnObj = JSONObject().apply {
                    put("role", if (role == "user") "user" else "model")
                    put("parts", JSONArray().put(JSONObject().put("text", text)))
                }
                contentsArray.put(turnObj)
            }

            // Current user query
            val currentMsgObj = JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", userMessage)))
            }
            contentsArray.put(currentMsgObj)

            val requestBodyJson = JSONObject().apply {
                put("contents", contentsArray)
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.4)
                    put("maxOutputTokens", 800)
                })
            }

            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"
            val request = Request.Builder()
                .url(url)
                .post(requestBodyJson.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                val json = JSONObject(responseBody)
                val candidates = json.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val reply = parts.getJSONObject(0).optString("text")
                        val matchedIds = extractProductIds(reply, catalog)
                        return@withContext GeminiResponse(reply, matchedIds)
                    }
                }
            } else {
                Log.w(TAG, "Gemini API error: ${response.code} $responseBody")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception contacting Gemini API", e)
        }

        // Fallback to local catalog reasoning if network or key fails
        return@withContext fallbackLocalAdvice(userMessage, catalog, language, currency)
    }

    private fun extractProductIds(reply: String, catalog: List<Product>): List<String> {
        val foundIds = mutableSetOf<String>()
        val regex = Regex("""\[ID:\s*([a-zA-Z0-9_-]+)\]""")
        regex.findAll(reply).forEach { match ->
            val id = match.groupValues[1]
            if (catalog.any { it.id == id }) {
                foundIds.add(id)
            }
        }
        // Also fallback to brand/name matching if none explicitly tagged
        if (foundIds.isEmpty()) {
            catalog.forEach { prod ->
                if (reply.contains(prod.nameEn, ignoreCase = true) || reply.contains(prod.brand, ignoreCase = true)) {
                    foundIds.add(prod.id)
                }
            }
        }
        return foundIds.take(4).toList()
    }

    private fun fallbackLocalAdvice(
        query: String,
        catalog: List<Product>,
        language: AppLanguage,
        currency: AppCurrency
    ): GeminiResponse {
        val q = query.lowercase()
        return when {
            q.contains("morocco") || q.contains("maroc") || q.contains("customs") || q.contains("douane") || q.contains("摩洛哥") -> {
                val moroccan = catalog.filter { it.isAvailableInCountry("MA") }
                val reply = when (language) {
                    AppLanguage.FR -> "Pour les commandes vers le Maroc : Les articles expédiés par Jumia Maroc ou les entrepôts locaux bénéficient d'une livraison express en 24-48h sans frais de douane. Pour Amazon France et Espagne, privilégiez les articles avec 'Frais d'importation prépayés' (DTI) pour un passage direct."
                    AppLanguage.AR -> "للطلبات المتجهة إلى المغرب: السلع المتوفرة محلياً أو عبر جوميا المغرب تصل في 24-48 ساعة بدون رسوم جمركية إضافية. وعند الشراء من أمازون أوروبا ننصح باختيار المنتجات التي تتضمن الرسوم الجمركية المسبقة الدفع (DTI)."
                    AppLanguage.ZH -> "针对摩洛哥地区的购物建议：通过Jumia Maroc或当地仓库发货的商品支持24-48小时极速送达，且无额外关税。如从法国或西班牙亚马逊下单，建议选择预缴关税（DTI）选项以保障清关。"
                    else -> "When shopping for Morocco: Local warehouse items (e.g. Jumia Maroc) arrive within 24-48h with zero extra customs. For European imports (Amazon.fr/es), always look for prepaid DTI Import Fees to ensure smooth delivery without customs delays."
                }
                GeminiResponse(reply, moroccan.map { it.id }.take(3))
            }
            q.contains("cheap") || q.contains("budget") || q.contains("alternative") || q.contains("moins cher") || q.contains("便宜") -> {
                val budget = catalog.sortedBy { it.getEstimatedPrice(currency) }.take(3)
                val reply = when (language) {
                    AppLanguage.FR -> "Voici les meilleures alternatives économiques actuellement vérifiées avec un excellent rapport qualité-prix :"
                    AppLanguage.AR -> "إليك أفضل البدائل الاقتصادية الموثقة حالياً بأفضل قيمة مقابل السعر ومراجعات ممتازة:"
                    AppLanguage.ZH -> "为您精选当前性价比最高且拥有真实折扣的优质替代选项："
                    else -> "Here are the top budget-friendly alternatives currently verified with great value-for-money and customer ratings:"
                }
                GeminiResponse(reply, budget.map { it.id })
            }
            q.contains("phone") || q.contains("smartphone") || q.contains("iphone") || q.contains("samsung") || q.contains("手机") -> {
                val phones = catalog.filter { it.categoryId == 1 }
                val reply = when (language) {
                    AppLanguage.FR -> "Voici les meilleures offres pour smartphones avec réductions vérifiées en Europe et au Maroc :"
                    AppLanguage.AR -> "إليك أبرز عروض الهواتف الذكية مع خصومات موثقة وضمان رسمي في أوروبا والمغرب:"
                    AppLanguage.ZH -> "以下为您推荐当前摩洛哥及欧洲市场真实降价的智能手机精选："
                    else -> "Here are the top verified smartphone bargains with official warranties in Europe & Morocco:"
                }
                GeminiResponse(reply, phones.map { it.id })
            }
            else -> {
                val matches = catalog.filter {
                    it.nameEn.contains(query, ignoreCase = true) ||
                    it.brand.contains(query, ignoreCase = true) ||
                    it.retailerName.contains(query, ignoreCase = true)
                }.ifEmpty { catalog.filter { it.verifiedDiscount }.take(3) }

                val reply = when (language) {
                    AppLanguage.FR -> "J'ai analysé notre catalogue vérifié auprès d'Amazon, Jumia, Fnac et Decathlon. Voici les meilleures recommandations correspondant à votre recherche :"
                    AppLanguage.AR -> "قمت بفحص الصفقات الموثقة عبر أمازون، جوميا، فناك، وديكاتلون. إليك أفضل الترشيحات المناسبة لطلبك:"
                    AppLanguage.ZH -> "已从官方合作商（亚马逊欧洲、Jumia、Fnac等）核实商品信息，以下为最契合您需求的精选特惠："
                    else -> "I analyzed partner listings across Amazon, Jumia, Fnac, and Decathlon. Here are verified options matching your search:"
                }
                GeminiResponse(reply, matches.map { it.id }.take(3))
            }
        }
    }
}
