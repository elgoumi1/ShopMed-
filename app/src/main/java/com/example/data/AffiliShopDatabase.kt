package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        SavedItemEntity::class,
        ClickLogEntity::class,
        PriceAlertEntity::class,
        CustomProductEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AffiliShopDatabase : RoomDatabase() {
    abstract fun dao(): AffiliShopDao

    companion object {
        @Volatile
        private var INSTANCE: AffiliShopDatabase? = null

        fun getInstance(context: Context): AffiliShopDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AffiliShopDatabase::class.java,
                    "affilishop_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
