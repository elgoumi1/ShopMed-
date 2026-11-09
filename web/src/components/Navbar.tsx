import React from 'react';
import { ShoppingBag, Globe, Sparkles, Heart, Scale, Shield, Moon, Sun, Menu, X } from 'lucide-react';
import { Language, Currency, Country } from '../types';

interface NavbarProps {
  currentTab: string;
  setCurrentTab: (tab: string) => void;
  language: Language;
  setLanguage: (lang: Language) => void;
  currency: Currency;
  setCurrency: (curr: Currency) => void;
  country: Country;
  setCountry: (c: Country) => void;
  isDark: boolean;
  setIsDark: (dark: boolean) => void;
  wishlistCount: number;
  compareCount: number;
}

export const Navbar: React.FC<NavbarProps> = ({
  currentTab,
  setCurrentTab,
  language,
  setLanguage,
  currency,
  setCurrency,
  country,
  setCountry,
  isDark,
  setIsDark,
  wishlistCount,
  compareCount
}) => {
  const [mobileMenuOpen, setMobileMenuOpen] = React.useState(false);

  return (
    <header className="sticky top-0 z-40 bg-white/95 dark:bg-[#0A192F]/95 backdrop-blur border-b border-slate-200 dark:border-slate-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Brand Logo */}
          <div 
            onClick={() => setCurrentTab('home')}
            className="flex items-center gap-2 cursor-pointer"
          >
            <div className="w-9 h-9 rounded-xl bg-gradient-to-tr from-[#0F3B5F] to-[#059669] flex items-center justify-center text-white shadow-md">
              <ShoppingBag className="w-5 h-5" />
            </div>
            <div className="flex flex-col">
              <div className="flex items-center">
                <span className="text-xl font-black text-[#0F3B5F] dark:text-blue-400">Shop</span>
                <span className="text-xl font-black text-amber-500">Med</span>
              </div>
              <span className="text-[10px] text-slate-500 -mt-1 font-medium">Morocco & Europe Deals</span>
            </div>
          </div>

          {/* Desktop Nav Links */}
          <nav className="hidden md:flex items-center gap-1">
            {[
              { id: 'home', label: language === 'ar' ? 'العروض' : language === 'fr' ? 'Bons Plans' : language === 'zh' ? '特惠首页' : 'Deals' },
              { id: 'catalog', label: language === 'ar' ? 'الأقسام' : language === 'fr' ? 'Rayons' : language === 'zh' ? '分类商城' : 'Catalog' },
              { id: 'ai', label: language === 'ar' ? 'المساعد الذكي' : language === 'fr' ? 'IA Shopping' : language === 'zh' ? 'AI导购' : 'AI Concierge', icon: Sparkles },
              { id: 'compare', label: language === 'ar' ? 'المقارنة' : language === 'fr' ? 'Comparer' : language === 'zh' ? '对比' : 'Compare', badge: compareCount },
              { id: 'wishlist', label: language === 'ar' ? 'المفضلة' : language === 'fr' ? 'Favoris' : language === 'zh' ? '收藏' : 'Wishlist', badge: wishlistCount },
              { id: 'admin', label: language === 'ar' ? 'الإدارة' : language === 'fr' ? 'Admin' : language === 'zh' ? '后台管理' : 'Admin Hub' }
            ].map(item => (
              <button
                key={item.id}
                onClick={() => {
                  setCurrentTab(item.id);
                  setMobileMenuOpen(false);
                }}
                className={`px-3 py-1.5 rounded-lg text-sm font-semibold transition flex items-center gap-1.5 relative ${
                  currentTab === item.id
                    ? 'bg-blue-50 dark:bg-slate-800 text-[#0F3B5F] dark:text-blue-400 font-bold'
                    : 'text-slate-600 dark:text-slate-300 hover:text-slate-900 dark:hover:text-white'
                }`}
              >
                {item.icon && <item.icon className="w-3.5 h-3.5 text-amber-500" />}
                {item.label}
                {item.badge !== undefined && item.badge > 0 && (
                  <span className="ml-1 px-1.5 py-0.2 text-[10px] font-bold rounded-full bg-amber-500 text-white">
                    {item.badge}
                  </span>
                )}
              </button>
            ))}
          </nav>

          {/* Regional Settings Pill & Theme Toggle */}
          <div className="flex items-center gap-2">
            {/* Language Selector */}
            <select
              value={language}
              onChange={(e) => setLanguage(e.target.value as Language)}
              className="bg-slate-100 dark:bg-slate-800 text-xs font-semibold px-2 py-1.5 rounded-lg border-0 cursor-pointer focus:ring-1 focus:ring-blue-500"
            >
              <option value="en">🇬🇧 EN</option>
              <option value="fr">🇫🇷 FR</option>
              <option value="ar">🇲🇦 العربية</option>
              <option value="zh">🇨🇳 中文</option>
            </select>

            {/* Currency Selector */}
            <select
              value={currency}
              onChange={(e) => setCurrency(e.target.value as Currency)}
              className="bg-slate-100 dark:bg-slate-800 text-xs font-semibold px-2 py-1.5 rounded-lg border-0 cursor-pointer focus:ring-1 focus:ring-blue-500"
            >
              <option value="EUR">€ EUR</option>
              <option value="MAD">DH MAD</option>
              <option value="USD">$ USD</option>
            </select>

            {/* Dark mode button */}
            <button
              onClick={() => setIsDark(!isDark)}
              className="p-2 rounded-lg text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800 transition"
              title="Toggle theme"
            >
              {isDark ? <Sun className="w-4 h-4 text-amber-400" /> : <Moon className="w-4 h-4" />}
            </button>

            {/* Mobile menu trigger */}
            <button
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              className="md:hidden p-2 text-slate-600 dark:text-slate-300"
            >
              {mobileMenuOpen ? <X className="w-5 h-5" /> : <Menu className="w-5 h-5" />}
            </button>
          </div>
        </div>

        {/* Mobile dropdown */}
        {mobileMenuOpen && (
          <div className="md:hidden py-3 border-t border-slate-200 dark:border-slate-800 flex flex-col gap-1">
            {[
              { id: 'home', label: 'Deals & Home' },
              { id: 'catalog', label: '28 Categories' },
              { id: 'ai', label: 'AI Shopping Assistant' },
              { id: 'compare', label: `Compare (${compareCount})` },
              { id: 'wishlist', label: `Wishlist (${wishlistCount})` },
              { id: 'admin', label: 'Admin Hub' }
            ].map(item => (
              <button
                key={item.id}
                onClick={() => {
                  setCurrentTab(item.id);
                  setMobileMenuOpen(false);
                }}
                className="text-left px-3 py-2 rounded-lg text-sm font-semibold hover:bg-slate-100 dark:hover:bg-slate-800"
              >
                {item.label}
              </button>
            ))}
          </div>
        )}
      </div>
    </header>
  );
};
