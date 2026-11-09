import React from 'react';
import { Star, Verified, Heart, Scale, ExternalLink } from 'lucide-react';
import { Product, Language, Currency } from '../types';

interface ProductCardProps {
  product: Product;
  language: Language;
  currency: Currency;
  isFavorite: boolean;
  isCompared: boolean;
  onToggleFavorite: (id: string) => void;
  onToggleCompare: (id: string) => void;
  onSelectProduct: (p: Product) => void;
  onAffiliateClick: (p: Product) => void;
}

export const ProductCard: React.FC<ProductCardProps> = ({
  product,
  language,
  currency,
  isFavorite,
  isCompared,
  onToggleFavorite,
  onToggleCompare,
  onSelectProduct,
  onAffiliateClick
}) => {
  const title = language === 'ar' ? product.nameAr : language === 'fr' ? product.nameFr : language === 'zh' ? product.nameZh : product.nameEn;

  // Currency rate calculator
  const formatEstimated = () => {
    let rate = 1;
    if (currency === 'MAD') rate = 10.85;
    if (currency === 'USD') rate = 1.08;

    let priceInEur = product.currentPrice;
    if (product.currency === 'MAD') priceInEur = product.currentPrice / 10.85;
    if (product.currency === 'USD') priceInEur = product.currentPrice / 1.08;

    const converted = priceInEur * rate;
    const symbol = currency === 'EUR' ? '€' : currency === 'MAD' ? 'MAD' : '$';
    const isSame = currency === product.currency;

    if (currency === 'MAD') {
      return `${Math.round(converted)} MAD ${isSame ? '' : '(est.)'}`;
    }
    return `${symbol}${converted.toFixed(2)} ${isSame ? '' : '(est.)'}`;
  };

  const countryFlag = product.retailerCountry === 'MA' ? '🇲🇦' : product.retailerCountry === 'FR' ? '🇫🇷' : product.retailerCountry === 'ES' ? '🇪🇸' : '🇪🇺';

  return (
    <div className="bg-white dark:bg-[#111C30] rounded-2xl border border-slate-200 dark:border-slate-800 overflow-hidden shadow-sm hover:shadow-md transition flex flex-col group">
      {/* Thumbnail Box */}
      <div className="relative h-48 bg-slate-100 dark:bg-slate-800 overflow-hidden cursor-pointer" onClick={() => onSelectProduct(product)}>
        <img
          src={product.imageUrls[0]}
          alt={title}
          className="w-full h-full object-cover group-hover:scale-105 transition duration-300"
          loading="lazy"
        />

        {/* Retailer & Verified Badges */}
        <div className="absolute top-2.5 left-2.5 flex items-center gap-1.5 flex-wrap">
          <span className="bg-white/90 dark:bg-slate-900/90 text-slate-800 dark:text-slate-200 text-[11px] font-bold px-2 py-0.5 rounded-md shadow-sm">
            {countryFlag} {product.retailerName}
          </span>
          {product.verifiedDiscount && (
            <span className="bg-emerald-600 text-white text-[11px] font-bold px-2 py-0.5 rounded-md flex items-center gap-1 shadow-sm">
              <Verified className="w-3 h-3" />
              -{product.discountPercent}%
            </span>
          )}
        </div>

        {/* Favorite & Compare Actions */}
        <div className="absolute top-2.5 right-2.5 flex items-center gap-1.5">
          <button
            onClick={(e) => {
              e.stopPropagation();
              onToggleCompare(product.id);
            }}
            className={`w-8 h-8 rounded-full flex items-center justify-center transition shadow-sm ${
              isCompared ? 'bg-amber-500 text-white' : 'bg-white/90 dark:bg-slate-900/90 text-slate-600 dark:text-slate-300 hover:text-amber-500'
            }`}
            title="Compare"
          >
            <Scale className="w-4 h-4" />
          </button>
          <button
            onClick={(e) => {
              e.stopPropagation();
              onToggleFavorite(product.id);
            }}
            className={`w-8 h-8 rounded-full flex items-center justify-center transition shadow-sm ${
              isFavorite ? 'bg-rose-500 text-white' : 'bg-white/90 dark:bg-slate-900/90 text-slate-600 dark:text-slate-300 hover:text-rose-500'
            }`}
            title="Wishlist"
          >
            <Heart className={`w-4 h-4 ${isFavorite ? 'fill-white' : ''}`} />
          </button>
        </div>
      </div>

      {/* Product Content */}
      <div className="p-4 flex-1 flex flex-col justify-between">
        <div>
          <div className="flex items-center justify-between text-xs mb-1">
            <span className="font-bold text-[#0F3B5F] dark:text-blue-400 uppercase tracking-wider">{product.brand}</span>
            <div className="flex items-center gap-1 text-slate-500 text-xs">
              <Star className="w-3.5 h-3.5 text-amber-500 fill-amber-500" />
              <span className="font-semibold text-slate-700 dark:text-slate-300">{product.rating}</span>
              <span>({product.reviewsCount})</span>
            </div>
          </div>

          <h3 
            onClick={() => onSelectProduct(product)}
            className="font-bold text-sm text-slate-900 dark:text-white line-clamp-2 cursor-pointer hover:text-blue-600 dark:hover:text-blue-400 mb-2"
          >
            {title}
          </h3>

          <p className="text-xs text-slate-500 dark:text-slate-400 line-clamp-1 mb-3">
            🚚 {product.shippingNote}
          </p>
        </div>

        {/* Pricing & CTA */}
        <div className="pt-3 border-t border-slate-100 dark:border-slate-800 flex items-center justify-between">
          <div>
            <div className="text-lg font-black text-slate-900 dark:text-white">
              {formatEstimated()}
            </div>
            {product.originalPrice && (
              <div className="text-xs text-slate-400 line-through">
                Was: {product.originalPrice} {product.currency}
              </div>
            )}
          </div>

          <button
            onClick={() => onAffiliateClick(product)}
            className="px-3.5 py-2 rounded-xl bg-[#0F3B5F] hover:bg-[#0c2e4a] text-white text-xs font-bold transition flex items-center gap-1.5 shadow-sm"
          >
            <span>Buy Deal</span>
            <ExternalLink className="w-3.5 h-3.5" />
          </button>
        </div>
      </div>
    </div>
  );
};
