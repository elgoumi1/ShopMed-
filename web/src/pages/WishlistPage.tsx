import React from 'react';
import { Heart } from 'lucide-react';
import { Product, Language, Currency } from '../types';
import { ProductCard } from '../components/ProductCard';

interface WishlistPageProps {
  products: Product[];
  favoriteIds: string[];
  compareIds: string[];
  onToggleFavorite: (id: string) => void;
  onToggleCompare: (id: string) => void;
  onSelectProduct: (p: Product) => void;
  onAffiliateClick: (p: Product) => void;
  onExploreCatalog: () => void;
  language: Language;
  currency: Currency;
}

export const WishlistPage: React.FC<WishlistPageProps> = ({
  products,
  favoriteIds,
  compareIds,
  onToggleFavorite,
  onToggleCompare,
  onSelectProduct,
  onAffiliateClick,
  onExploreCatalog,
  language,
  currency
}) => {
  const wishlisted = products.filter(p => favoriteIds.includes(p.id));

  if (wishlisted.length === 0) {
    return (
      <div className="max-w-md mx-auto px-4 py-20 text-center space-y-4">
        <div className="w-16 h-16 mx-auto rounded-3xl bg-rose-50 dark:bg-slate-800 flex items-center justify-center text-rose-500">
          <Heart className="w-8 h-8" />
        </div>
        <h2 className="text-xl font-bold text-slate-900 dark:text-white">
          Your Wishlist is Empty
        </h2>
        <p className="text-xs text-slate-400">
          Bookmark deals from Morocco and Europe to monitor price fluctuations.
        </p>
        <button
          onClick={onExploreCatalog}
          className="px-5 py-2.5 rounded-xl bg-blue-600 text-white text-xs font-bold hover:bg-blue-700 transition"
        >
          Explore Catalog
        </button>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      <div>
        <h1 className="text-2xl font-black text-slate-900 dark:text-white">
          Saved Deals & Wishlist ({wishlisted.length})
        </h1>
        <p className="text-xs text-slate-400 mt-0.5">
          Track verified deals and buy directly when price targets hit.
        </p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        {wishlisted.map(product => (
          <ProductCard
            key={product.id}
            product={product}
            language={language}
            currency={currency}
            isFavorite={true}
            isCompared={compareIds.includes(product.id)}
            onToggleFavorite={onToggleFavorite}
            onToggleCompare={onToggleCompare}
            onSelectProduct={onSelectProduct}
            onAffiliateClick={onAffiliateClick}
          />
        ))}
      </div>
    </div>
  );
};
