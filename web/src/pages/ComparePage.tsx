import React from 'react';
import { Scale, X, ExternalLink, Star } from 'lucide-react';
import { Product, Language, Currency } from '../types';

interface ComparePageProps {
  products: Product[];
  compareIds: string[];
  onRemoveCompare: (id: string) => void;
  onSelectProduct: (p: Product) => void;
  onAffiliateClick: (p: Product) => void;
  onExploreCatalog: () => void;
  onAddSamples: () => void;
  language: Language;
  currency: Currency;
}

export const ComparePage: React.FC<ComparePageProps> = ({
  products,
  compareIds,
  onRemoveCompare,
  onSelectProduct,
  onAffiliateClick,
  onExploreCatalog,
  onAddSamples,
  language,
  currency
}) => {
  const compared = products.filter(p => compareIds.includes(p.id));

  if (compared.length === 0) {
    return (
      <div className="max-w-2xl mx-auto px-4 py-20 text-center space-y-4">
        <div className="w-16 h-16 mx-auto rounded-3xl bg-blue-50 dark:bg-slate-800 flex items-center justify-center text-blue-600 dark:text-blue-400">
          <Scale className="w-8 h-8" />
        </div>
        <h2 className="text-2xl font-black text-slate-900 dark:text-white">
          Side-by-Side Product Comparison
        </h2>
        <p className="text-sm text-slate-500 dark:text-slate-400 max-w-md mx-auto">
          Compare specifications, retailers, shipping constraints, and prices for up to 4 products across Morocco and Europe.
        </p>
        <div className="pt-4 flex justify-center gap-3">
          <button
            onClick={onAddSamples}
            className="px-5 py-2.5 rounded-xl bg-blue-600 text-white text-xs font-bold hover:bg-blue-700 transition"
          >
            Compare iPhone 15 Pro vs Galaxy S24 Ultra
          </button>
          <button
            onClick={onExploreCatalog}
            className="px-5 py-2.5 rounded-xl border border-slate-300 dark:border-slate-700 text-xs font-bold hover:bg-slate-100 dark:hover:bg-slate-800 transition"
          >
            Browse Catalog
          </button>
        </div>
      </div>
    );
  }

  // Find lowest price
  const lowest = compared.reduce((min, p) => p.currentPrice < min.currentPrice ? p : min, compared[0]);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-black text-slate-900 dark:text-white">
            Product Comparison Matrix
          </h1>
          <p className="text-xs text-slate-400 mt-0.5">
            Comparing {compared.length} of 4 items
          </p>
        </div>
        <button
          onClick={onExploreCatalog}
          className="px-4 py-2 bg-slate-100 dark:bg-slate-800 rounded-xl text-xs font-bold text-slate-700 dark:text-slate-200 hover:bg-slate-200"
        >
          + Add More Products
        </button>
      </div>

      <div className="overflow-x-auto pb-6">
        <div className="grid grid-flow-col auto-cols-[280px] gap-4 min-w-full">
          {compared.map(p => {
            const isLowest = p.id === lowest.id && compared.length > 1;

            return (
              <div
                key={p.id}
                className="bg-white dark:bg-[#111C30] border border-slate-200 dark:border-slate-800 rounded-2xl p-4 flex flex-col justify-between shadow-sm relative"
              >
                <div>
                  <div className="flex items-center justify-between mb-2">
                    <span className="text-[11px] font-bold text-blue-600 dark:text-blue-400 uppercase">
                      {p.brand}
                    </span>
                    <button
                      onClick={() => onRemoveCompare(p.id)}
                      className="p-1 text-slate-400 hover:text-rose-500 rounded-md"
                      title="Remove"
                    >
                      <X className="w-4 h-4" />
                    </button>
                  </div>

                  <div className="relative h-36 bg-slate-100 dark:bg-slate-800 rounded-xl overflow-hidden mb-3">
                    <img
                      src={p.imageUrls[0]}
                      alt={p.nameEn}
                      className="w-full h-full object-cover"
                    />
                    {isLowest && (
                      <span className="absolute top-2 left-2 bg-emerald-600 text-white text-[10px] font-black px-2 py-0.5 rounded shadow">
                        ★ Lowest Price
                      </span>
                    )}
                  </div>

                  <h3 className="text-xs font-bold text-slate-800 dark:text-white line-clamp-2 mb-2">
                    {p.nameEn}
                  </h3>

                  <div className="text-lg font-black text-slate-900 dark:text-white mb-1">
                    {p.currentPrice} {p.currency}
                  </div>

                  <button
                    onClick={() => onAffiliateClick(p)}
                    className="w-full py-2 bg-[#0F3B5F] hover:bg-[#0c2e4a] text-white text-xs font-bold rounded-xl flex items-center justify-center gap-1.5 transition mb-4 shadow-sm"
                  >
                    <span>Buy on {p.retailerName}</span>
                    <ExternalLink className="w-3.5 h-3.5" />
                  </button>

                  {/* Criteria Rows */}
                  <div className="space-y-2 text-xs border-t border-slate-100 dark:border-slate-800 pt-3">
                    <div className="bg-slate-50 dark:bg-slate-800/60 p-2 rounded-lg">
                      <span className="text-[10px] text-slate-400 block font-semibold">Retailer & Network</span>
                      <span className="font-bold text-slate-800 dark:text-slate-200">{p.retailerName} ({p.affiliateNetwork})</span>
                    </div>

                    <div className="bg-slate-50 dark:bg-slate-800/60 p-2 rounded-lg">
                      <span className="text-[10px] text-slate-400 block font-semibold">Rating & Reviews</span>
                      <span className="font-bold text-slate-800 dark:text-slate-200">★ {p.rating} / 5 ({p.reviewsCount} reviews)</span>
                    </div>

                    <div className="bg-slate-50 dark:bg-slate-800/60 p-2 rounded-lg">
                      <span className="text-[10px] text-slate-400 block font-semibold">Shipping / Availability</span>
                      <span className="font-semibold text-slate-700 dark:text-slate-300">{p.shippingNote}</span>
                    </div>

                    {Object.entries(p.specifications).map(([k, v]) => (
                      <div key={k} className="bg-slate-50 dark:bg-slate-800/60 p-2 rounded-lg">
                        <span className="text-[10px] text-slate-400 block font-semibold">{k}</span>
                        <span className="font-medium text-slate-800 dark:text-slate-200">{v}</span>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};
