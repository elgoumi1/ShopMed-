package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AppCurrency
import com.example.model.AppLanguage
import com.example.model.CategoryCatalog
import com.example.model.Product
import com.example.model.TargetCountry
import com.example.repository.FilterCriteria
import com.example.repository.ProductSortOrder
import com.example.ui.components.ProductCard
import com.example.ui.theme.EmeraldSuccess

@Composable
fun CatalogScreen(
    products: List<Product>,
    favoriteIds: Set<String>,
    compareIds: Set<String>,
    filterCriteria: FilterCriteria,
    currentLanguage: AppLanguage,
    currentCurrency: AppCurrency,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (Int?) -> Unit,
    onSortOrderChange: (ProductSortOrder) -> Unit,
    onToggleVerifiedOnly: (Boolean) -> Unit,
    onClearFilters: () -> Unit,
    onProductClick: (Product) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onToggleCompare: (String) -> Unit,
    onAffiliateClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterPanel by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        // Search & Filter header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = filterCriteria.query,
                    onValueChange = onSearchChange,
                    placeholder = { Text("Filter products or brands...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        if (filterCriteria.query.isNotBlank()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("catalog_search_bar")
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (showFilterPanel || filterCriteria.verifiedOnly || filterCriteria.categoryId != null) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    modifier = Modifier.clickable { showFilterPanel = !showFilterPanel }
                ) {
                    IconButton(onClick = { showFilterPanel = !showFilterPanel }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Toggle Filters",
                            tint = if (showFilterPanel || filterCriteria.verifiedOnly || filterCriteria.categoryId != null) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }

            // Expandable Filter Controls
            if (showFilterPanel) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Sort Order
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Sort By",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                FilterChip(
                                    selected = filterCriteria.sortOrder == ProductSortOrder.POPULARITY,
                                    onClick = { onSortOrderChange(ProductSortOrder.POPULARITY) },
                                    label = { Text("Popular") }
                                )
                                FilterChip(
                                    selected = filterCriteria.sortOrder == ProductSortOrder.PRICE_LOW_TO_HIGH,
                                    onClick = { onSortOrderChange(ProductSortOrder.PRICE_LOW_TO_HIGH) },
                                    label = { Text("Price ↑") }
                                )
                                FilterChip(
                                    selected = filterCriteria.sortOrder == ProductSortOrder.HIGHEST_DISCOUNT,
                                    onClick = { onSortOrderChange(ProductSortOrder.HIGHEST_DISCOUNT) },
                                    label = { Text("Discount %") }
                                )
                            }
                        }

                        // Verified Discounts Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Verified Discounts Only",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Show only verified real retailer price drops",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = filterCriteria.verifiedOnly,
                                onCheckedChange = onToggleVerifiedOnly
                            )
                        }

                        // Reset button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = onClearFilters) {
                                Text("Reset Filters", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        // Horizontal Category Tabs (All 28 Categories)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = filterCriteria.categoryId == null,
                    onClick = { onCategorySelect(null) },
                    label = { Text("All (${products.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("cat_all_chip")
                )
            }

            items(CategoryCatalog.allCategories) { category ->
                val isSelected = filterCriteria.categoryId == category.id
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelect(category.id) },
                    label = {
                        Text(
                            text = "${category.id}. ${category.localizedName(currentLanguage)}"
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("cat_chip_${category.id}")
                )
            }
        }

        // Product Catalog List
        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🔍",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No matching products found",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Try adjusting your price range, search terms, or country filters.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onClearFilters) {
                        Text("Reset All Filters")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(
                        text = "Showing ${products.size} products from Amazon EU, Jumia & European Retailers",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                items(products, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        currentLanguage = currentLanguage,
                        currentCurrency = currentCurrency,
                        isFavorite = favoriteIds.contains(product.id),
                        isCompared = compareIds.contains(product.id),
                        onProductClick = onProductClick,
                        onToggleFavorite = onToggleFavorite,
                        onToggleCompare = onToggleCompare,
                        onAffiliateClick = onAffiliateClick
                    )
                }
            }
        }
    }
}
