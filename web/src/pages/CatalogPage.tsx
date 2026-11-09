import React, { useState } from 'react';
import { Filter, SlidersHorizontal, ArrowUpDown, X } from 'lucide-react';
import { Product, Language, Currency, Category } from '../types';
import { ProductCard } from '../components/ProductCard';

interface CatalogPageProps {
  products: Product[];
  categories: Category[];
  selectedCategoryId: number | null;
  onSelectCategory: (id: number | null) => void;
  language: Language;
  currency: Currency;
  favoriteIds: string[];
  compareIds: string[];
  onToggleFavorite: (id: string) => void;
  onToggleCompare: (id: string) => void;
  onSelectProduct: (p: Product) => void;
  onAffiliateClick: (p: Product) => void;
}

export const CatalogPage: React.FC<CatalogPageProps> = ({
  products,
  categories,
  selectedCategoryId,
  onSelectCategory,
  language,
  currency,
  favoriteIds,
  compareIds,
  onToggleFavorite,
  onToggleCompare,
  onSelectProduct,
  onAffiliateClick
}) => {
  const [search, setSearch] = useState('');
  const [verifiedOnly, setVerifiedOnly] = useState(false);
  const [selectedCountry, setSelectedCountry] = useState('ALL');
  const [sortBy, setSortBy] = useState<'popular' | 'priceAsc' | 'priceDesc' | 'discount'>('popular');

  const filtered = products.filter(p => {
    const matchCat = selectedCategoryId === null || p.categoryId === selectedCategoryId;
    const matchVerified = !verifiedOnly || p.verifiedDiscount;
    const matchCountry = selectedCountry === 'ALL' || p.countryAvailability.includes(selectedCountry);
    const title = language === 'ar' ? p.nameAr : language === 'fr' ? p.nameFr : language === 'zh' ? p.nameZh : p.nameEn;
    const matchQuery = !search.trim() || title.toLowerCase().includes(search.toLowerCase()) || p.brand.toLowerCase().includes(search.toLowerCase());
    return matchCat && matchVerified && matchCountry && matchQuery;
  }).sort((a, b) => {
    if (sortBy === 'priceAsc') return a.currentPrice - b.currentPrice;
    if (sortBy === 'priceDesc') return b.currentPrice - a.currentPrice;
    if (sortBy === 'discount') return b.discountPercent - a.discountPercent;
    return b.clicksCount - a.clicksCount;
  });

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      {/* Title & Filter bar */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-black text-slate-900 dark:text-white">
            Product Catalog & Deals Explorer
          </h1>
          <p className="text-xs text-slate-500 dark:text-slate-400 mt-1">
            Browse verified listings across Morocco, France, Spain, and Europe
          </p>
        </div>

        {/* Filter controls */}
        <div className="flex items-center gap-2 flex-wrap">
          <input
            type="text"
            placeholder="Filter catalog..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="px-3.5 py-2 rounded-xl border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 text-xs focus:ring-1 focus:ring-blue-500"
          />

          <select
            value={selectedCountry}
            onChange={(e) => setSelectedCountry(e.target.value)}
            className="px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 text-xs font-semibold"
          >
            <option value="ALL">🌍 All Countries</option>
            <option value="MA">🇲🇦 Morocco Only</option>
            <option value="FR">🇫🇷 France</option>
            <option value="ES">🇪🇸 Spain</option>
            <option value="DE">🇩🇪 Germany</option>
          </select>

          <select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value as any)}
            className="px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 text-xs font-semibold"
          >
            <option value="popular">Most Popular</option>
            <option value="priceAsc">Price: Low to High</option>
            <option value="priceDesc">Price: High to Low</option>
            <option value="discount">Highest Discount %</option>
          </select>

          <label className="flex items-center gap-1.5 px-3 py-2 bg-emerald-50 dark:bg-emerald-950/40 text-emerald-700 dark:text-emerald-300 rounded-xl text-xs font-bold cursor-pointer border border-emerald-200 dark:border-emerald-800/50">
            <input
              type="checkbox"
              checked={verifiedOnly}
              onChange={(e) => setVerifiedOnly(e.target.checked)}
              className="rounded text-emerald-600"
            />
            <span>Verified Deals Only</span>
          </label>
        </div>
      </div>

      {/* Category Pills Carousel */}
      <div className="flex items-center gap-2 overflow-x-auto pb-2 scrollbar-none">
        <button
          onClick={() => onSelectCategory(null)}
          className={`px-4 py-2 rounded-full text-xs font-bold transition whitespace-nowrap ${
            selectedCategoryId === null
              ? 'bg-[#0F3B5F] text-white shadow-sm'
              : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200'
          }`}
        >
          All Categories ({products.length})
        </button>
        {categories.map(cat => (
          <button
            key={cat.id}
            onClick={() => onSelectCategory(cat.id)}
            className={`px-4 py-2 rounded-full text-xs font-bold transition flex items-center gap-1.5 whitespace-nowrap ${
              selectedCategoryId === cat.id
                ? 'bg-[#0F3B5F] text-white shadow-sm'
                : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200'
            }`}
          >
            <span>{cat.icon}</span>
            <span>{language === 'ar' ? cat.nameAr : language === 'fr' ? cat.nameFr : language === 'zh' ? cat.nameZh : cat.nameEn}</span>
          </button>
        ))}
      </div>

      {/* Product Grid */}
      {filtered.length === 0 ? (
        <div className="text-center py-16 bg-white dark:bg-[#111C30] rounded-3xl border border-slate-200 dark:border-slate-800">
          <p className="text-base font-bold text-slate-700 dark:text-slate-300">No products match your filters</p>
          <p className="text-xs text-slate-400 mt-1">Try resetting the country or verified discounts filter</p>
          <button
            onClick={() => {
              setSearch('');
              setVerifiedOnly(false);
              setSelectedCountry('ALL');
              onSelectCategory(null);
            }}
            className="mt-4 px-4 py-2 bg-blue-600 text-white rounded-xl text-xs font-bold"
          >
            Reset Filters
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {filtered.map(product => (
            <ProductCard
              key={product.id}
              product={product}
              language={language}
              currency={currency}
              isFavorite={favoriteIds.includes(product.id)}
              isCompared={compareIds.includes(product.id)}
              onToggleFavorite={onToggleFavorite}
              onToggleCompare={onToggleCompare}
              onSelectProduct={onSelectProduct}
              onAffiliateClick={onAffiliateClick}
            />
          ))}
        </div>
      )}
    </div>
  );
};
