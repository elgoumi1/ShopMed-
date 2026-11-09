import React, { useState } from 'react';
import { Navbar } from './components/Navbar';
import { Footer } from './components/Footer';
import { HomePage } from './pages/HomePage';
import { CatalogPage } from './pages/CatalogPage';
import { ProductDetailPage } from './pages/ProductDetailPage';
import { ComparePage } from './pages/ComparePage';
import { AiChatPage } from './pages/AiChatPage';
import { WishlistPage } from './pages/WishlistPage';
import { AdminPage } from './pages/AdminPage';
import { AffiliateModal } from './components/AffiliateModal';
import { PRODUCTS, CATEGORIES } from './data/products';
import { Product, Language, Currency, Country, AffiliateClick } from './types';

export const App: React.FC = () => {
  const [currentTab, setCurrentTab] = useState<string>('home');
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [selectedCategoryId, setSelectedCategoryId] = useState<number | null>(null);

  const [language, setLanguage] = useState<Language>('en');
  const [currency, setCurrency] = useState<Currency>('EUR');
  const [country, setCountry] = useState<Country>('ALL');
  const [isDark, setIsDark] = useState<boolean>(false);

  const [favoriteIds, setFavoriteIds] = useState<string[]>(['prod_01', 'prod_06']);
  const [compareIds, setCompareIds] = useState<string[]>([]);
  const [affiliateModalProduct, setAffiliateModalProduct] = useState<Product | null>(null);

  const [productsList, setProductsList] = useState<Product[]>(PRODUCTS);
  const [clicks, setClicks] = useState<AffiliateClick[]>([
    {
      clickId: 'clk_101',
      productId: 'prod_01',
      productName: 'Apple iPhone 15 Pro',
      retailerName: 'Amazon.fr',
      networkName: 'Amazon Associates',
      targetCountry: 'FR',
      trafficSource: 'Home Featured',
      estimatedCommission: 29.67,
      currency: 'EUR',
      timestamp: Date.now() - 1000 * 60 * 15,
      status: 'Tracking Active'
    },
    {
      clickId: 'clk_102',
      productId: 'prod_03',
      productName: 'Xiaomi Redmi Note 13 Pro',
      retailerName: 'Jumia Maroc',
      networkName: 'Jumia KOL Program',
      targetCountry: 'MA',
      trafficSource: 'Morocco Spotlight',
      estimatedCommission: 149.94,
      currency: 'MAD',
      timestamp: Date.now() - 1000 * 60 * 42,
      status: 'Approved'
    }
  ]);

  const handleToggleFavorite = (id: string) => {
    setFavoriteIds(prev => prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]);
  };

  const handleToggleCompare = (id: string) => {
    setCompareIds(prev => {
      if (prev.includes(id)) return prev.filter(x => x !== id);
      if (prev.length >= 4) return prev;
      return [...prev, id];
    });
  };

  const handleSelectProduct = (p: Product) => {
    setSelectedProduct(p);
    setCurrentTab('detail');
  };

  const handleAffiliateClick = (p: Product) => {
    setAffiliateModalProduct(p);
  };

  const handleConfirmRedirect = () => {
    if (!affiliateModalProduct) return;
    const click: AffiliateClick = {
      clickId: 'clk_' + Math.random().toString(36).substring(2, 9),
      productId: affiliateModalProduct.id,
      productName: affiliateModalProduct.nameEn,
      retailerName: affiliateModalProduct.retailerName,
      networkName: affiliateModalProduct.affiliateNetwork,
      targetCountry: affiliateModalProduct.retailerCountry,
      trafficSource: currentTab,
      estimatedCommission: affiliateModalProduct.currentPrice * 0.05,
      currency: affiliateModalProduct.currency,
      timestamp: Date.now(),
      status: 'Tracking Active'
    };
    setClicks(prev => [click, ...prev]);

    const url = affiliateModalProduct.affiliateUrl;
    setAffiliateModalProduct(null);
    window.open(url, '_blank', 'noopener,noreferrer');
  };

  const handleImportCsv = (csvText: string) => {
    let added = 0;
    let skipped = 0;
    const lines = csvText.split('\n');

    lines.forEach(line => {
      const parts = line.split(',');
      if (parts.length >= 6) {
        const name = parts[0].trim();
        if (name.toLowerCase() === 'name' || !name) return;
        if (productsList.some(p => p.nameEn.toLowerCase() === name.toLowerCase())) {
          skipped++;
          return;
        }

        const brand = parts[1].trim();
        const cat = parseInt(parts[2].trim()) || 1;
        const price = parseFloat(parts[3].trim()) || 99;
        const curr = parts[4].trim();
        const retailer = parts[5].trim();
        const url = parts[6]?.trim() || 'https://amazon.fr';

        const newP: Product = {
          id: 'csv_' + Math.random().toString(36).substring(2, 8),
          nameEn: name,
          nameFr: name,
          nameAr: name,
          nameZh: name,
          descriptionEn: `Imported item for ${brand}.`,
          descriptionFr: `Article importé pour ${brand}.`,
          descriptionAr: `منتج مستورد لـ ${brand}.`,
          descriptionZh: `${brand} 导入商品。`,
          categoryId: cat,
          brand,
          currentPrice: price,
          originalPrice: price * 1.2,
          currency: curr,
          discountPercent: 15,
          verifiedDiscount: true,
          rating: 4.6,
          reviewsCount: 110,
          stockStatus: 'In Stock',
          retailerName: retailer,
          retailerCountry: curr === 'MAD' ? 'MA' : 'FR',
          affiliateNetwork: 'Amazon Associates',
          affiliateUrl: url,
          originalRetailerUrl: url,
          imageUrls: ['https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600'],
          countryAvailability: ['MA', 'FR', 'ES', 'DE'],
          shippingNote: 'Fast shipping',
          specifications: { 'Origin': 'Bulk CSV' },
          clicksCount: 0
        };

        setProductsList(prev => [...prev, newP]);
        added++;
      }
    });

    return { added, skipped };
  };

  return (
    <div className={`min-h-screen ${isDark ? 'dark bg-[#0B1320]' : 'bg-slate-50'}`}>
      <Navbar
        currentTab={currentTab}
        setCurrentTab={setCurrentTab}
        language={language}
        setLanguage={setLanguage}
        currency={currency}
        setCurrency={setCurrency}
        country={country}
        setCountry={setCountry}
        isDark={isDark}
        setIsDark={setIsDark}
        wishlistCount={favoriteIds.length}
        compareCount={compareIds.length}
      />

      <main className="min-h-[70vh]">
        {currentTab === 'home' && (
          <HomePage
            products={productsList}
            categories={CATEGORIES}
            language={language}
            currency={currency}
            favoriteIds={favoriteIds}
            compareIds={compareIds}
            onToggleFavorite={handleToggleFavorite}
            onToggleCompare={handleToggleCompare}
            onSelectProduct={handleSelectProduct}
            onAffiliateClick={handleAffiliateClick}
            onNavigate={setCurrentTab}
            onSelectCategory={(id) => {
              setSelectedCategoryId(id);
              setCurrentTab('catalog');
            }}
          />
        )}

        {currentTab === 'catalog' && (
          <CatalogPage
            products={productsList}
            categories={CATEGORIES}
            selectedCategoryId={selectedCategoryId}
            onSelectCategory={setSelectedCategoryId}
            language={language}
            currency={currency}
            favoriteIds={favoriteIds}
            compareIds={compareIds}
            onToggleFavorite={handleToggleFavorite}
            onToggleCompare={handleToggleCompare}
            onSelectProduct={handleSelectProduct}
            onAffiliateClick={handleAffiliateClick}
          />
        )}

        {currentTab === 'detail' && selectedProduct && (
          <ProductDetailPage
            product={selectedProduct}
            onBack={() => setCurrentTab('catalog')}
            language={language}
            currency={currency}
            isFavorite={favoriteIds.includes(selectedProduct.id)}
            isCompared={compareIds.includes(selectedProduct.id)}
            onToggleFavorite={handleToggleFavorite}
            onToggleCompare={handleToggleCompare}
            onAffiliateClick={handleAffiliateClick}
          />
        )}

        {currentTab === 'compare' && (
          <ComparePage
            products={productsList}
            compareIds={compareIds}
            onRemoveCompare={handleToggleCompare}
            onSelectProduct={handleSelectProduct}
            onAffiliateClick={handleAffiliateClick}
            onExploreCatalog={() => setCurrentTab('catalog')}
            onAddSamples={() => setCompareIds(['prod_01', 'prod_02'])}
            language={language}
            currency={currency}
          />
        )}

        {currentTab === 'ai' && (
          <AiChatPage
            products={productsList}
            language={language}
            currency={currency}
            onSelectProduct={handleSelectProduct}
            onAffiliateClick={handleAffiliateClick}
          />
        )}

        {currentTab === 'wishlist' && (
          <WishlistPage
            products={productsList}
            favoriteIds={favoriteIds}
            compareIds={compareIds}
            onToggleFavorite={handleToggleFavorite}
            onToggleCompare={handleToggleCompare}
            onSelectProduct={handleSelectProduct}
            onAffiliateClick={handleAffiliateClick}
            onExploreCatalog={() => setCurrentTab('catalog')}
            language={language}
            currency={currency}
          />
        )}

        {currentTab === 'admin' && (
          <AdminPage
            products={productsList}
            clicks={clicks}
            onImportCsv={handleImportCsv}
          />
        )}
      </main>

      <Footer language={language} onNavigate={setCurrentTab} />

      <AffiliateModal
        product={affiliateModalProduct}
        currency={currency}
        onClose={() => setAffiliateModalProduct(null)}
        onConfirm={handleConfirmRedirect}
      />
    </div>
  );
};

export default App;
