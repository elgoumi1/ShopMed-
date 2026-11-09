import React from 'react';
import { ShieldCheck, Globe } from 'lucide-react';
import { Language } from '../types';

interface FooterProps {
  language: Language;
  onNavigate: (tab: string) => void;
}

export const Footer: React.FC<FooterProps> = ({ language, onNavigate }) => {
  return (
    <footer className="bg-white dark:bg-[#0A192F] border-t border-slate-200 dark:border-slate-800 py-12 mt-20">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          <div className="space-y-3">
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 rounded-lg bg-[#0F3B5F] flex items-center justify-center text-white font-black text-sm">
                SM
              </div>
              <span className="font-black text-lg text-[#0F3B5F] dark:text-blue-400">ShopMed</span>
            </div>
            <p className="text-xs text-slate-500 dark:text-slate-400 leading-relaxed">
              Global AI affiliate shopping marketplace connecting consumers in Morocco, Europe, and international markets with verified deals.
            </p>
          </div>

          <div>
            <h4 className="text-xs font-bold text-slate-900 dark:text-white uppercase tracking-wider mb-3">
              Marketplaces
            </h4>
            <ul className="text-xs text-slate-500 space-y-2">
              <li>🇲🇦 Jumia Maroc & Local Stocks</li>
              <li>🇫🇷 Amazon France & Fnac</li>
              <li>🇪🇸 Amazon Spain & MediaMarkt</li>
              <li>🇪🇺 Decathlon & Sephora Europe</li>
            </ul>
          </div>

          <div>
            <h4 className="text-xs font-bold text-slate-900 dark:text-white uppercase tracking-wider mb-3">
              Ecosystem
            </h4>
            <ul className="text-xs text-slate-500 space-y-2">
              <li onClick={() => onNavigate('catalog')} className="cursor-pointer hover:text-blue-500">28 Categories</li>
              <li onClick={() => onNavigate('ai')} className="cursor-pointer hover:text-blue-500">Gemini AI Concierge</li>
              <li onClick={() => onNavigate('compare')} className="cursor-pointer hover:text-blue-500">Side-by-Side Comparison</li>
              <li onClick={() => onNavigate('admin')} className="cursor-pointer hover:text-blue-500">Admin Operations Hub</li>
            </ul>
          </div>

          <div>
            <h4 className="text-xs font-bold text-slate-900 dark:text-white uppercase tracking-wider mb-3">
              Compliance & Legal
            </h4>
            <div className="flex items-center gap-1.5 text-xs text-emerald-600 font-semibold mb-2">
              <ShieldCheck className="w-4 h-4" />
              <span>FTC & EU Compliant</span>
            </div>
            <p className="text-[11px] text-slate-400 leading-relaxed">
              ShopMed participates in affiliate programs with Amazon, Jumia, Awin, and others. We may earn a qualifying commission when you purchase through our links at no extra cost to you.
            </p>
          </div>
        </div>

        <div className="pt-8 border-t border-slate-100 dark:border-slate-800 text-center text-xs text-slate-400 flex flex-col sm:flex-row items-center justify-between gap-4">
          <p>© {new Date().getFullYear()} ShopMed Global Ecosystem. All rights reserved.</p>
          <div className="flex items-center gap-4">
            <span>Privacy Policy</span>
            <span>Terms of Service</span>
            <span>Cookie Notice</span>
            <span>Affiliate Disclosure</span>
          </div>
        </div>
      </div>
    </footer>
  );
};
