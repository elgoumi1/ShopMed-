package com.example

import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testCurrencyConversionAndCategories() {
    val eurPrice = 100.0
    val convertedMad = com.example.model.CurrencyRates.convertPrice(eurPrice, "EUR", com.example.model.AppCurrency.MAD)
    assertEquals(1085.0, convertedMad, 0.01)

    // Verify all 28 categories exist
    assertEquals(28, com.example.model.CategoryCatalog.allCategories.size)
  }
}
