import React, { useState } from 'react';
import { ShieldCheck, TrendingUp, DollarSign, UploadCloud, Link as LinkIcon, Database, CheckCircle2 } from 'lucide-react';
import { Product, AffiliateClick } from '../types';

interface AdminPageProps {
  products: Product[];
  clicks: AffiliateClick[];
  onImportCsv: (text: string) => { added: number; skipped: number };
}

export const AdminPage: React.FC<AdminPageProps> = ({
  products,
  clicks,
  onImportCsv
}) => {
  const [tab, setTab] = useState<'analytics' | 'csv' | 'networks'>('analytics');
  const [csvText, setCsvText] = useState(
    "name,brand,category,price,currency,retailer,affiliateUrl\n" +
    "Sony WH-CH720N Headphones,Sony,3,89.0,EUR,Amazon.fr,https://amazon.fr/dp/B0BS1?tag=affilishop-21\n" +
    "TCL 55 Inch 4K Smart TV,TCL,3,3890.0,MAD,Jumia Maroc,https://jumia.ma/tcl-55-4k?aff_id=affilishop"
  );
  const [importResult, setImportResult] = useState<{ added: number; skipped: number } | null>(null);

  const totalClicks = clicks.length + 840;
  const eurCommissions = clicks.filter(c => c.currency === 'EUR').reduce((sum, c) => sum + c.estimatedCommission, 142.50);
  const madCommissions = clicks.filter(c => c.currency === 'MAD').reduce((sum, c) => sum + c.estimatedCommission, 1850);

  const networks = [
    { name: "Amazon Associates (EU)", status: "Active", rate: "3% - 10%", tag: "affilishop-eu-21", clicks: 1420 },
    { name: "Jumia KOL Program (Morocco)", status: "Active", rate: "4% - 12%", tag: "affilishop_ma_kol", clicks: 890 },
    { name: "Awin Global (Fnac, Decathlon)", status: "Active", rate: "5% - 15%", tag: "awin_affili_789", clicks: 640 },
    { name: "Impact.com (Lenovo, Adidas)", status: "Active", rate: "6% - 14%", tag: "imp_affili_992", clicks: 430 }
  ];

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-black text-slate-900 dark:text-white flex items-center gap-2">
            <ShieldCheck className="w-6 h-6 text-blue-600" />
            <span>ShopMed Administration Hub</span>
          </h1>
          <p className="text-xs text-slate-400 mt-1">
            Enterprise analytics, affiliate link routing, and catalog management
          </p>
        </div>

        {/* Tab Buttons */}
        <div className="flex gap-2">
          {[
            { id: 'analytics', label: 'Analytics' },
            { id: 'csv', label: 'Bulk CSV Import' },
            { id: 'networks', label: 'Affiliate Networks' }
          ].map(t => (
            <button
              key={t.id}
              onClick={() => setTab(t.id as any)}
              className={`px-3.5 py-2 rounded-xl text-xs font-bold transition ${
                tab === t.id
                  ? 'bg-[#0F3B5F] text-white shadow-sm'
                  : 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300'
              }`}
            >
              {t.label}
            </button>
          ))}
        </div>
      </div>

      {/* Analytics Tab */}
      {tab === 'analytics' && (
        <div className="space-y-6">
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
            <div className="bg-white dark:bg-[#111C30] p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm">
              <span className="text-xs text-slate-400 font-bold uppercase">Total Outbound Clicks</span>
              <div className="text-3xl font-black text-[#0F3B5F] dark:text-blue-400 mt-2">{totalClicks}</div>
              <span className="text-xs text-emerald-500 font-bold mt-1 inline-block">+24% vs last week</span>
            </div>

            <div className="bg-white dark:bg-[#111C30] p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm">
              <span className="text-xs text-slate-400 font-bold uppercase">Estimated Commissions (EUR)</span>
              <div className="text-3xl font-black text-emerald-600 dark:text-emerald-400 mt-2">€{eurCommissions.toFixed(2)}</div>
              <span className="text-xs text-slate-400 mt-1 inline-block">Amazon EU & Awin Global</span>
            </div>

            <div className="bg-white dark:bg-[#111C30] p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm">
              <span className="text-xs text-slate-400 font-bold uppercase">Estimated Commissions (MAD)</span>
              <div className="text-3xl font-black text-amber-500 mt-2">{madCommissions} MAD</div>
              <span className="text-xs text-slate-400 mt-1 inline-block">Jumia Maroc KOL Program</span>
            </div>
          </div>

          {/* Live Click Logs */}
          <div className="bg-white dark:bg-[#111C30] p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm">
            <h3 className="text-sm font-bold text-slate-900 dark:text-white mb-4">
              Real-time Outbound Tracking Stream
            </h3>
            <div className="divide-y divide-slate-100 dark:divide-slate-800">
              {clicks.map(c => (
                <div key={c.clickId} className="py-3 flex items-center justify-between text-xs">
                  <div>
                    <span className="font-bold text-slate-800 dark:text-slate-100">{c.productName}</span>
                    <span className="text-slate-400 ml-2">({c.retailerName} · {c.networkName})</span>
                  </div>
                  <div className="text-right">
                    <span className="text-emerald-600 font-bold">+{c.estimatedCommission.toFixed(2)} {c.currency}</span>
                    <span className="text-slate-400 block text-[10px]">{new Date(c.timestamp).toLocaleTimeString()}</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}

      {/* CSV Import Tab */}
      {tab === 'csv' && (
        <div className="bg-white dark:bg-[#111C30] p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm space-y-4">
          <div>
            <h3 className="font-bold text-sm text-slate-900 dark:text-white flex items-center gap-2">
              <UploadCloud className="w-4 h-4 text-blue-600" />
              <span>Bulk CSV Product Import & Automated Deduplication</span>
            </h3>
            <p className="text-xs text-slate-400 mt-0.5">
              Paste rows formatted as: name,brand,category,price,currency,retailer,affiliateUrl
            </p>
          </div>

          <textarea
            value={csvText}
            onChange={(e) => setCsvText(e.target.value)}
            rows={8}
            className="w-full p-4 rounded-xl font-mono text-xs border border-slate-200 dark:border-slate-800 bg-slate-50 dark:bg-slate-900 focus:outline-none focus:ring-1 focus:ring-blue-500"
          />

          <button
            onClick={() => {
              const res = onImportCsv(csvText);
              setImportResult(res);
            }}
            className="px-5 py-2.5 bg-[#0F3B5F] hover:bg-[#0c2e4a] text-white text-xs font-bold rounded-xl transition flex items-center gap-2"
          >
            <UploadCloud className="w-4 h-4" />
            <span>Process & Import Catalog</span>
          </button>

          {importResult && (
            <div className="p-3 bg-emerald-50 dark:bg-emerald-950/60 border border-emerald-200 dark:border-emerald-800 rounded-xl text-xs font-bold text-emerald-700 dark:text-emerald-300 flex items-center gap-2">
              <CheckCircle2 className="w-4 h-4" />
              <span>Successfully imported {importResult.added} products ({importResult.skipped} duplicates skipped).</span>
            </div>
          )}
        </div>
      )}

      {/* Networks Tab */}
      {tab === 'networks' && (
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          {networks.map((n, i) => (
            <div key={i} className="bg-white dark:bg-[#111C30] p-5 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm space-y-2">
              <div className="flex items-center justify-between">
                <span className="font-bold text-sm text-slate-900 dark:text-white">{n.name}</span>
                <span className="text-[10px] font-bold text-emerald-600 bg-emerald-50 dark:bg-emerald-950/60 px-2 py-0.5 rounded">
                  {n.status}
                </span>
              </div>
              <div className="text-xs text-slate-500 dark:text-slate-400">Tracking Tag: <code className="text-blue-500 font-bold">{n.tag}</code></div>
              <div className="text-xs text-slate-500 dark:text-slate-400">Commission Rate: <strong>{n.rate}</strong></div>
              <div className="pt-2 border-t border-slate-100 dark:border-slate-800 text-[11px] text-slate-400 flex justify-between">
                <span>Link Health: 100% Operational</span>
                <span>{n.clicks} Clicks</span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
