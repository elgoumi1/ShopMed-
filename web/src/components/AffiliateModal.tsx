import React from 'react';
import { ExternalLink, ShieldCheck, Info, X } from 'lucide-react';
import { Product, Currency } from '../types';

interface AffiliateModalProps {
  product: Product | null;
  currency: Currency;
  onClose: () => void;
  onConfirm: () => void;
}

export const AffiliateModal: React.FC<AffiliateModalProps> = ({
  product,
  currency,
  onClose,
  onConfirm
}) => {
  if (!product) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-fade-in">
      <div className="bg-white dark:bg-[#111C30] border border-slate-200 dark:border-slate-800 rounded-3xl max-w-md w-full p-6 shadow-2xl relative">
        <button
          onClick={onClose}
          className="absolute top-4 right-4 p-2 text-slate-400 hover:text-slate-600 dark:hover:text-white"
        >
          <X className="w-5 h-5" />
        </button>

        <div className="flex items-center gap-3 mb-4">
          <div className="w-10 h-10 rounded-2xl bg-blue-50 dark:bg-slate-800 flex items-center justify-center text-[#0F3B5F] dark:text-blue-400">
            <ExternalLink className="w-5 h-5" />
          </div>
          <div>
            <h3 className="font-bold text-base text-slate-900 dark:text-white">
              Redirecting to {product.retailerName}
            </h3>
            <span className="text-xs text-blue-600 dark:text-blue-400 font-semibold">
              Official Affiliate Partner Link
            </span>
          </div>
        </div>

        {/* Product mini card */}
        <div className="bg-slate-50 dark:bg-slate-800/60 rounded-xl p-3 mb-4 border border-slate-100 dark:border-slate-700/60 flex items-center gap-3">
          <img
            src={product.imageUrls[0]}
            alt={product.nameEn}
            className="w-14 h-14 object-cover rounded-lg"
          />
          <div className="flex-1 min-w-0">
            <h4 className="text-xs font-bold text-slate-800 dark:text-slate-100 truncate">
              {product.nameEn}
            </h4>
            <p className="text-xs font-black text-[#0F3B5F] dark:text-blue-400 mt-0.5">
              Retailer Price: {product.currentPrice} {product.currency}
            </p>
          </div>
        </div>

        {/* FTC / EU Legal Disclosure */}
        <div className="bg-blue-50/60 dark:bg-slate-800/40 border border-blue-100 dark:border-slate-700 rounded-xl p-3.5 mb-5 flex gap-2.5">
          <Info className="w-4 h-4 text-blue-600 dark:text-blue-400 shrink-0 mt-0.5" />
          <div className="text-[11px] text-slate-600 dark:text-slate-300 leading-relaxed">
            <p className="font-bold text-slate-800 dark:text-slate-200 mb-1">
              Transparent Affiliate Disclosure
            </p>
            When you click &quot;Continue to Retailer&quot;, you will be securely redirected to {product.retailerName}&apos;s official website. If you complete an eligible purchase, ShopMed may receive a qualifying commission at absolutely <strong>NO additional cost</strong> to you.
          </div>
        </div>

        <div className="flex items-center gap-2 text-xs text-emerald-600 dark:text-emerald-400 font-semibold mb-5">
          <ShieldCheck className="w-4 h-4" />
          <span>Encrypted Referral via {product.affiliateNetwork}</span>
        </div>

        {/* Buttons */}
        <div className="flex gap-3">
          <button
            onClick={onClose}
            className="flex-1 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 text-xs font-bold text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800 transition"
          >
            Cancel
          </button>
          <button
            onClick={onConfirm}
            className="flex-1 py-2.5 rounded-xl bg-[#0F3B5F] hover:bg-[#0c2e4a] text-white text-xs font-bold transition flex items-center justify-center gap-1.5 shadow-md"
          >
            <span>Continue to {product.retailerName}</span>
            <ExternalLink className="w-3.5 h-3.5" />
          </button>
        </div>
      </div>
    </div>
  );
};
