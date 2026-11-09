package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AffiliShopDao {

    // Saved items (Favorites & Compared)
    @Query("SELECT * FROM saved_items")
    fun getAllSavedItems(): Flow<List<SavedItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSavedItem(item: SavedItemEntity)

    @Query("DELETE FROM saved_items WHERE productId = :productId")
    suspend fun deleteSavedItem(productId: String)

    // Click tracking
    @Query("SELECT * FROM affiliate_clicks ORDER BY timestamp DESC")
    fun getAllClicks(): Flow<List<ClickLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClick(click: ClickLogEntity)

    @Query("SELECT COUNT(*) FROM affiliate_clicks")
    suspend fun getTotalClicksCount(): Int

    // Price Alerts
    @Query("SELECT * FROM price_alerts ORDER BY createdAt DESC")
    fun getAllPriceAlerts(): Flow<List<PriceAlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceAlert(alert: PriceAlertEntity)

    @Query("DELETE FROM price_alerts WHERE id = :id")
    suspend fun deletePriceAlert(id: String)

    // Custom Products
    @Query("SELECT * FROM custom_products")
    fun getAllCustomProducts(): Flow<List<CustomProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomProduct(product: CustomProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomProducts(products: List<CustomProductEntity>)

    @Query("DELETE FROM custom_products WHERE id = :id")
    suspend fun deleteCustomProduct(id: String)
}
