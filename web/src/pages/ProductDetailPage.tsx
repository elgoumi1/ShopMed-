import React, { useState } from 'react';
import { ArrowLeft, Star, ExternalLink, ShieldCheck, Heart, Scale, Truck, Bell } from 'lucide-react';
import { Product, Language, Currency } from '../types';

interface ProductDetailPageProps {
  product: Product;
  onBack: () => void;
  language: Language;
  currency: Currency;
  isFavorite: boolean;
  isCompared: boolean;
  onToggleFavorite: (id: string) => void;
  onToggleCompare: (id: string) => void;
  onAffiliateClick: (p: Product) => void;
}

export const ProductDetailPage: React.FC<ProductDetailPageProps> = ({
  product,
  onBack,
  language,
  currency,
  isFavorite,
  isCompared,
  onToggleFavorite,
  onToggleCompare,
  onAffiliateClick
}) => {
  const [alertOpen, setAlertOpen] = useState(false);
  const [alertTarget, setAlertTarget] = useState((product.currentPrice * 0.9).toFixed(0));
  const [alertSuccess, setAlertSuccess] = useState(false);

  const title = language === 'ar' ? product.nameAr : language === 'fr' ? product.nameFr : language === 'zh' ? product.nameZh : product.nameEn;
  const desc = language === 'ar' ? product.descriptionAr : language === 'fr' ? product.descriptionFr : language === 'zh' ? product.descriptionZh : product.descriptionEn;

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8">
      <button
        onClick={onBack}
        className="flex items-center gap-1.5 text-xs font-bold text-slate-500 hover:text-slate-800 dark:hover:text-white"
      >
        <ArrowLeft className="w-4 h-4" />
        <span>Back to listings</span>
      </button>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8 items-start">
        {/* Gallery */}
        <div className="bg-white dark:bg-[#111C30] p-4 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-sm overflow-hidden">
          <img
            src={product.imageUrls[0]}
            alt={title}
            className="w-full h-80 object-cover rounded-2xl"
          />
        </div>

        {/* Info */}
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <span className="text-xs font-bold text-blue-600 dark:text-blue-400 uppercase tracking-wider">
              {product.brand}
            </span>
            <div className="flex items-center gap-1 text-xs">
              <Star className="w-4 h-4 text-amber-500 fill-amber-500" />
              <span className="font-bold text-slate-800 dark:text-slate-200">{product.rating}</span>
              <span className="text-slate-400">({product.reviewsCount} reviews)</span>
            </div>
          </div>

          <h1 className="text-2xl font-black text-slate-900 dark:text-white">
            {title}
          </h1>

          {/* Pricing Box */}
          <div className="bg-slate-50 dark:bg-slate-800/60 p-4 rounded-2xl border border-slate-200 dark:border-slate-700/60 space-y-2">
            <div className="flex items-baseline justify-between">
              <div>
                <span className="text-xs text-slate-400 block font-semibold">Retailer Price</span>
                <span className="text-2xl font-black text-slate-900 dark:text-white">
                  {product.currentPrice} {product.currency}
                </span>
              </div>
              {product.originalPrice && (
                <div className="text-right">
                  <span className="text-xs text-slate-400 block">Original Price</span>
                  <span className="text-sm text-slate-400 line-through">
                    {product.originalPrice} {product.currency}
                  </span>
                </div>
              )}
            </div>

            <div className="text-[11px] text-slate-500 dark:text-slate-400 bg-white dark:bg-slate-900/60 p-2.5 rounded-xl border border-slate-100 dark:border-slate-800">
              ℹ️ Checkout is completed in official retailer currency ({product.currency}) on {product.retailerName}&apos;s secure platform.
            </div>
          </div>

          {/* Actions */}
          <div className="flex gap-3">
            <button
              onClick={() => onAffiliateClick(product)}
              className="flex-1 py-3 bg-[#0F3B5F] hover:bg-[#0c2e4a] text-white text-sm font-bold rounded-2xl flex items-center justify-center gap-2 shadow-md transition"
            >
              <span>Buy on {product.retailerName}</span>
              <ExternalLink className="w-4 h-4" />
            </button>
            <button
              onClick={() => onToggleFavorite(product.id)}
              className={`p-3 rounded-2xl border transition ${
                isFavorite
                  ? 'bg-rose-50 border-rose-200 text-rose-500 dark:bg-rose-950/40'
                  : 'border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-300'
              }`}
              title="Wishlist"
            >
              <Heart className={`w-5 h-5 ${isFavorite ? 'fill-rose-500' : ''}`} />
            </button>
            <button
              onClick={() => onToggleCompare(product.id)}
              className={`p-3 rounded-2xl border transition ${
                isCompared
                  ? 'bg-amber-50 border-amber-200 text-amber-500 dark:bg-amber-950/40'
                  : 'border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-300'
              }`}
              title="Compare"
            >
              <Scale className="w-5 h-5" />
            </button>
            <button
              onClick={() => setAlertOpen(true)}
              className="p-3 rounded-2xl border border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-300 hover:text-blue-500 transition"
              title="Price Alert"
            >
              <Bell className="w-5 h-5" />
            </button>
          </div>

          {/* Shipping note */}
          <div className="bg-white dark:bg-[#111C30] p-4 rounded-2xl border border-slate-200 dark:border-slate-800 space-y-1">
            <div className="flex items-center gap-2 text-xs font-bold text-slate-800 dark:text-slate-200">
              <Truck className="w-4 h-4 text-blue-600" />
              <span>Cross-Border Logistics & Availability</span>
            </div>
            <p className="text-xs text-slate-500 dark:text-slate-400 pl-6">
              {product.shippingNote}
            </p>
          </div>

          {/* Specifications */}
          <div className="space-y-2 pt-2">
            <h4 className="text-xs font-bold uppercase tracking-wider text-slate-500">
              Specifications
            </h4>
            <div className="divide-y divide-slate-100 dark:divide-slate-800 text-xs">
              {Object.entries(product.specifications).map(([k, v]) => (
                <div key={k} className="py-2 flex justify-between">
                  <span className="text-slate-400 font-semibold">{k}</span>
                  <span className="font-bold text-slate-800 dark:text-slate-200">{v}</span>
                </div>
              ))}
            </div>
          </div>

          {/* Description */}
          <div className="space-y-1 pt-2">
            <h4 className="text-xs font-bold uppercase tracking-wider text-slate-500">
              Overview
            </h4>
            <p className="text-xs text-slate-600 dark:text-slate-300 leading-relaxed">
              {desc}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};
