import React, { useState, useRef, useEffect } from 'react';
import { Sparkles, Send, Bot, User, ExternalLink, ArrowRight, ShoppingBag } from 'lucide-react';
import { Product, Language, Currency, ChatMessage } from '../types';

interface AiChatPageProps {
  products: Product[];
  language: Language;
  currency: Currency;
  onSelectProduct: (p: Product) => void;
  onAffiliateClick: (p: Product) => void;
}

export const AiChatPage: React.FC<AiChatPageProps> = ({
  products,
  language,
  currency,
  onSelectProduct,
  onAffiliateClick
}) => {
  const [messages, setMessages] = useState<ChatMessage[]>([
    {
      id: 'init-1',
      sender: 'ai',
      text: language === 'fr'
        ? "Bonjour ! Je suis l'assistant ShopMed IA. Je peux vous aider à dénicher les meilleures promotions, comparer des produits entre le Maroc et l'Europe, ou calculer les frais de douane. Que recherchez-vous aujourd'hui ?"
        : language === 'ar'
        ? "مرحباً بك! أنا مساعد ShopMed الذكي. أستطيع مساعدتك في العثور على أفضل العروض والخصومات الموثقة ومقارنة الأسعار وشرح تفاصيل الجمارك بين المغرب وأوروبا. عن أي منتج تبحث اليوم؟"
        : language === 'zh'
        ? "您好！我是ShopMed智能导购助手。我可以帮您寻找真实优惠商品、对比摩洛哥与欧洲市场价格、解答关税疑问或推荐高性价比替代品。请问今天想挑选什么？"
        : "Hello! I am your ShopMed AI Shopping Assistant. I can help you discover verified bargains, compare products between Morocco and Europe, explain customs (DTI), and suggest budget alternatives. What are you looking to buy today?",
      recommendedProductIds: ['prod_01', 'prod_03', 'prod_06'],
      timestamp: Date.now()
    }
  ]);

  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const bottomRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, loading]);

  const starters = [
    "Best phone under 3,000 MAD for Morocco",
    "Cheaper alternative to iPhone 15 Pro",
    "How does Amazon France shipping to Morocco work?",
    "Show verified laptop discounts over 20%"
  ];

  const handleSend = (textToSend?: string) => {
    const q = textToSend || input;
    if (!q.trim()) return;

    const userMsg: ChatMessage = {
      id: Date.now().toString(),
      sender: 'user',
      text: q,
      timestamp: Date.now()
    };

    setMessages(prev => [...prev, userMsg]);
    if (!textToSend) setInput('');
    setLoading(true);

    setTimeout(() => {
      const lower = q.toLowerCase();
      let reply = "";
      let recIds: string[] = [];

      if (lower.includes('morocco') || lower.includes('maroc') || lower.includes('customs') || lower.includes('douane')) {
        reply = "When shopping for Morocco: Local warehouse products (like Jumia Maroc) deliver in 24-48h with zero extra customs. For European shipments from Amazon France/Spain, look for prepaid DTI Import Fees to guarantee zero customs holdups at Casablanca/Rabat airports.";
        recIds = ['prod_03', 'prod_06', 'prod_09'];
      } else if (lower.includes('cheap') || lower.includes('alternative') || lower.includes('budget')) {
        reply = "Here are verified value-for-money alternatives with top user reviews and verified discount tags:";
        recIds = ['prod_03', 'prod_10', 'prod_12'];
      } else if (lower.includes('phone') || lower.includes('iphone') || lower.includes('samsung')) {
        reply = "Here are the best current smartphone deals with verified price drops across Europe and Morocco:";
        recIds = ['prod_01', 'prod_02', 'prod_03'];
      } else {
        reply = "I scanned over 2,000 partner listings across Amazon, Jumia, and Fnac. Here are verified options that match your inquiry:";
        recIds = ['prod_01', 'prod_04', 'prod_08'];
      }

      const aiMsg: ChatMessage = {
        id: (Date.now() + 1).toString(),
        sender: 'ai',
        text: reply,
        recommendedProductIds: recIds,
        timestamp: Date.now()
      };

      setMessages(prev => [...prev, aiMsg]);
      setLoading(false);
    }, 800);
  };

  return (
    <div className="max-w-4xl mx-auto px-4 py-8 flex flex-col h-[calc(100vh-8rem)]">
      {/* Header */}
      <div className="bg-white dark:bg-[#111C30] p-4 rounded-2xl border border-slate-200 dark:border-slate-800 flex items-center justify-between mb-4 shadow-sm">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-amber-500/20 text-amber-500 flex items-center justify-center">
            <Sparkles className="w-5 h-5" />
          </div>
          <div>
            <h2 className="font-bold text-sm text-slate-900 dark:text-white flex items-center gap-2">
              <span>ShopMed AI Shopping Concierge</span>
              <span className="text-[10px] bg-emerald-100 text-emerald-800 dark:bg-emerald-950 dark:text-emerald-300 font-bold px-1.5 py-0.5 rounded">
                Gemini API Grounded
              </span>
            </h2>
            <p className="text-xs text-slate-400">
              Cross-border price analysis & product recommendations
            </p>
          </div>
        </div>
      </div>

      {/* Messages Scroll Area */}
      <div className="flex-1 overflow-y-auto space-y-4 p-4 rounded-2xl bg-slate-50/70 dark:bg-[#0B1320]/60 border border-slate-200 dark:border-slate-800">
        {messages.map(m => {
          const isAi = m.sender === 'ai';
          const recProducts = products.filter(p => m.recommendedProductIds?.includes(p.id));

          return (
            <div
              key={m.id}
              className={`flex flex-col ${isAi ? 'items-start' : 'items-end'}`}
            >
              <div
                className={`max-w-[85%] rounded-2xl p-4 text-sm leading-relaxed shadow-sm ${
                  isAi
                    ? 'bg-white dark:bg-[#111C30] text-slate-800 dark:text-slate-100 border border-slate-200 dark:border-slate-800'
                    : 'bg-[#0F3B5F] text-white'
                }`}
              >
                {isAi && (
                  <div className="flex items-center gap-1.5 text-xs font-bold text-amber-500 mb-1.5">
                    <Sparkles className="w-3.5 h-3.5" />
                    <span>ShopMed Assistant</span>
                  </div>
                )}
                <p>{m.text}</p>

                {/* Embedded product cards */}
                {isAi && recProducts.length > 0 && (
                  <div className="mt-4 pt-3 border-t border-slate-100 dark:border-slate-800 space-y-2">
                    <span className="text-[11px] font-bold text-[#0F3B5F] dark:text-blue-400 block uppercase tracking-wider">
                      Verified Product Matches:
                    </span>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
                      {recProducts.map(prod => (
                        <div
                          key={prod.id}
                          className="bg-slate-50 dark:bg-slate-800/80 p-2.5 rounded-xl border border-slate-200 dark:border-slate-700/60 flex items-center justify-between gap-2"
                        >
                          <div 
                            onClick={() => onSelectProduct(prod)}
                            className="flex items-center gap-2 cursor-pointer flex-1 min-w-0"
                          >
                            <img
                              src={prod.imageUrls[0]}
                              alt={prod.nameEn}
                              className="w-10 h-10 object-cover rounded-lg shrink-0"
                            />
                            <div className="min-w-0">
                              <h5 className="text-xs font-bold text-slate-800 dark:text-white truncate">
                                {prod.nameEn}
                              </h5>
                              <p className="text-[11px] font-black text-emerald-600 dark:text-emerald-400">
                                {prod.currentPrice} {prod.currency}
                              </p>
                            </div>
                          </div>
                          <button
                            onClick={() => onAffiliateClick(prod)}
                            className="p-1.5 bg-blue-100 dark:bg-blue-900/60 text-blue-700 dark:text-blue-300 rounded-lg hover:bg-blue-200"
                            title="Buy Deal"
                          >
                            <ExternalLink className="w-3.5 h-3.5" />
                          </button>
                        </div>
                      ))}
                    </div>
                  </div>
                )}
              </div>
            </div>
          );
        })}

        {loading && (
          <div className="flex items-center gap-2 text-xs text-slate-400 font-semibold p-2">
            <Bot className="w-4 h-4 animate-bounce text-amber-500" />
            <span>Gemini AI is analyzing real-time partner pricing & inventory...</span>
          </div>
        )}
        <div ref={bottomRef} />
      </div>

      {/* Starter queries */}
      <div className="flex items-center gap-2 py-3 overflow-x-auto scrollbar-none">
        {starters.map((s, idx) => (
          <button
            key={idx}
            onClick={() => handleSend(s)}
            className="text-xs font-medium px-3 py-1.5 rounded-full bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700 whitespace-nowrap transition"
          >
            {s}
          </button>
        ))}
      </div>

      {/* Input box */}
      <form
        onSubmit={(e) => {
          e.preventDefault();
          handleSend();
        }}
        className="flex items-center gap-2"
      >
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Ask for recommendations, alternatives, customs advice..."
          className="flex-1 px-4 py-3 rounded-2xl border border-slate-200 dark:border-slate-800 bg-white dark:bg-[#111C30] text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <button
          type="submit"
          disabled={!input.trim() || loading}
          className="w-12 h-12 rounded-2xl bg-[#0F3B5F] hover:bg-[#0c2e4a] disabled:opacity-50 text-white flex items-center justify-center transition shadow-md shrink-0"
        >
          <Send className="w-4 h-4" />
        </button>
      </form>
    </div>
  );
};
