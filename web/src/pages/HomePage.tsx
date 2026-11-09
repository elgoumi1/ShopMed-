import React, { useState } from 'react';
import { Sparkles, Flame, ShieldAlert, ArrowRight, Zap, CheckCircle2, Mail, Search } from 'lucide-react';
import { Product, Language, Currency } from '../types';
import { ProductCard } from '../components/ProductCard';

interface HomePageProps {
  products: Product[];
  categories: any[];
  language: Language;
  currency: Currency;
  favoriteIds: string[];
  compareIds: string[];
  onToggleFavorite: (id: string) => void;
  onToggleCompare: (id: string) => void;
  onSelectProduct: (p: Product) => void;
  onAffiliateClick: (p: Product) => void;
  onNavigate: (tab: string) => void;
  onSelectCategory: (catId: number) => void;
}

export const HomePage: React.FC<HomePageProps> = ({
  products,
  categories,
  language,
  currency,
  favoriteIds,
  compareIds,
  onToggleFavorite,
  onToggleCompare,
  onSelectProduct,
  onAffiliateClick,
  onNavigate,
  onSelectCategory
}) => {
  const [emailInput, setEmailInput] = useState('');
  const [subscribed, setSubscribed] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');

  const verifiedDeals = products.filter(p => p.verifiedDiscount);
  const moroccanStocks = products.filter(p => p.countryAvailability.includes('MA') && p.currency === 'MAD');

  return (
    <div className="space-y-12 pb-16">
      {/* 1. Flash Promo Bar */}
      <div className="bg-gradient-to-r from-amber-500/10 via-amber-500/20 to-amber-500/10 border-b border-amber-500/20 py-2.5 px-4 text-center">
        <div className="max-w-7xl mx-auto flex items-center justify-center gap-2 text-xs font-bold text-slate-800 dark:text-slate-200">
          <Zap className="w-4 h-4 text-amber-500" />
          <span>Morocco & Europe Cross-Border Deals: Up to 36% Verified Price Reductions on Amazon, Jumia & Fnac</span>
        </div>
      </div>

      {/* 2. Hero Promotional Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="relative rounded-3xl overflow-hidden bg-gradient-to-br from-[#0F3B5F] via-[#0A192F] to-[#04202C] text-white p-8 md:p-12 shadow-xl border border-slate-700/50">
          <div className="max-w-2xl relative z-10 space-y-4">
            <span className="inline-block px-3 py-1 rounded-full bg-rose-500/90 text-white text-xs font-black uppercase tracking-wider">
              AI-Powered Affiliate Marketplace
            </span>
            <h1 className="text-3xl md:text-5xl font-black tracking-tight leading-tight">
              Shop Smart Across Morocco, Europe & Beyond
            </h1>
            <p className="text-sm md:text-base text-slate-300 leading-relaxed">
              Real-time verified discounts, transparent EUR/MAD currency conversions, and automated customs feasibility checks. Powered by Gemini AI.
            </p>

            {/* Quick search input */}
            <div className="pt-2 flex flex-col sm:flex-row gap-3">
              <div className="relative flex-1">
                <Search className="w-5 h-5 text-slate-400 absolute left-3.5 top-3.5" />
                <input
                  type="text"
                  placeholder="Search smartphones, perfumes, laptops..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  onKeyDown={(e) => {
                    if (e.key === 'Enter') onNavigate('catalog');
                  }}
                  className="w-full pl-10 pr-4 py-3 rounded-xl bg-white/10 backdrop-blur border border-white/20 text-white placeholder-slate-400 text-sm focus:outline-none focus:ring-2 focus:ring-amber-400"
                />
              </div>
              <button
                onClick={() => onNavigate('ai')}
                className="px-5 py-3 rounded-xl bg-amber-500 hover:bg-amber-400 text-slate-900 text-sm font-bold flex items-center justify-center gap-2 transition shadow-lg"
              >
                <Sparkles className="w-4 h-4" />
                <span>Ask AI Concierge</span>
              </button>
            </div>
          </div>
        </div>
      </section>

      {/* 3. Category Quick Navigation */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-bold text-slate-900 dark:text-white">
            Top Categories
          </h2>
          <button
            onClick={() => onNavigate('catalog')}
            className="text-xs font-bold text-blue-600 dark:text-blue-400 hover:underline flex items-center gap-1"
          >
            <span>View All 28 Rayons</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </button>
        </div>

        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-6 gap-3">
          {categories.slice(0, 6).map(cat => (
            <div
              key={cat.id}
              onClick={() => {
                onSelectCategory(cat.id);
                onNavigate('catalog');
              }}
              className="bg-white dark:bg-[#111C30] border border-slate-200 dark:border-slate-800 rounded-2xl p-4 text-center cursor-pointer hover:border-blue-500 hover:shadow-md transition group"
            >
              <div className="w-12 h-12 mx-auto rounded-xl bg-blue-50 dark:bg-slate-800 flex items-center justify-center text-2xl group-hover:scale-110 transition mb-2">
                {cat.icon}
              </div>
              <h3 className="text-xs font-bold text-slate-800 dark:text-slate-200 truncate">
                {language === 'ar' ? cat.nameAr : language === 'fr' ? cat.nameFr : language === 'zh' ? cat.nameZh : cat.nameEn}
              </h3>
            </div>
          ))}
        </div>
      </section>

      {/* 4. Hot Verified Deals */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between mb-5">
          <div className="flex items-center gap-2">
            <Flame className="w-6 h-6 text-rose-500" />
            <h2 className="text-xl font-bold text-slate-900 dark:text-white">
              Hot Verified Price Drops
            </h2>
          </div>
          <span className="text-xs font-bold text-emerald-600 dark:text-emerald-400 bg-emerald-50 dark:bg-emerald-950/60 px-2.5 py-1 rounded-full">
            Verified Partner Discounts
          </span>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {verifiedDeals.map(product => (
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
      </section>

      {/* 5. Morocco Terroir & Local Warehouse Spotlight */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="bg-amber-50/50 dark:bg-slate-900/60 border border-amber-200/60 dark:border-slate-800 rounded-3xl p-6 md:p-8">
          <div className="flex items-center justify-between mb-6">
            <div>
              <div className="flex items-center gap-2">
                <span className="text-xl">🇲🇦</span>
                <h2 className="text-xl font-bold text-slate-900 dark:text-white">
                  Morocco Local Stocks & Terroir Products
                </h2>
              </div>
              <p className="text-xs text-slate-500 dark:text-slate-400 mt-1">
                Zero customs duties · Express 24-48h delivery across Casablanca, Rabat, Marrakech & Tangier
              </p>
            </div>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {moroccanStocks.map(product => (
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
        </div>
      </section>

      {/* 6. Newsletter Subscription */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="bg-slate-100 dark:bg-slate-800/80 rounded-3xl p-8 text-center max-w-2xl mx-auto">
          <Mail className="w-8 h-8 text-blue-600 dark:text-blue-400 mx-auto mb-3" />
          <h3 className="text-xl font-bold text-slate-900 dark:text-white mb-2">
            Never Miss a Verified Deal Drop
          </h3>
          <p className="text-xs text-slate-500 dark:text-slate-400 mb-6">
            Subscribe to our weekly curated deal digest for Morocco and Europe with instant price drop alerts.
          </p>

          {subscribed ? (
            <div className="flex items-center justify-center gap-2 text-emerald-600 font-bold text-sm">
              <CheckCircle2 className="w-5 h-5" />
              <span>You are subscribed to ShopMed Deal Alerts!</span>
            </div>
          ) : (
            <form
              onSubmit={(e) => {
                e.preventDefault();
                if (emailInput.trim()) setSubscribed(true);
              }}
              className="flex flex-col sm:flex-row gap-2 max-w-md mx-auto"
            >
              <input
                type="email"
                placeholder="Enter your email"
                required
                value={emailInput}
                onChange={(e) => setEmailInput(e.target.value)}
                className="flex-1 px-4 py-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-900 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <button
                type="submit"
                className="px-5 py-2.5 rounded-xl bg-[#0F3B5F] hover:bg-[#0c2e4a] text-white text-sm font-bold transition"
              >
                Subscribe
              </button>
            </form>
          )}
        </div>
      </section>
    </div>
  );
};
