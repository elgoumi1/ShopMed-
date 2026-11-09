export type Language = 'en' | 'fr' | 'ar' | 'zh';
export type Currency = 'EUR' | 'MAD' | 'USD';
export type Country = 'ALL' | 'MA' | 'FR' | 'ES' | 'DE';

export interface Product {
  id: string;
  nameEn: string;
  nameFr: string;
  nameAr: string;
  nameZh: string;
  descriptionEn: string;
  descriptionFr: string;
  descriptionAr: string;
  descriptionZh: string;
  categoryId: number;
  brand: string;
  currentPrice: number;
  originalPrice?: number;
  currency: string;
  discountPercent: number;
  verifiedDiscount: boolean;
  rating: number;
  reviewsCount: number;
  stockStatus: string;
  retailerName: string;
  retailerCountry: string;
  affiliateNetwork: string;
  affiliateUrl: string;
  originalRetailerUrl: string;
  imageUrls: string[];
  countryAvailability: string[];
  shippingNote: string;
  specifications: Record<string, string>;
  isFeatured?: boolean;
  isTrending?: boolean;
  isBestSeller?: boolean;
  clicksCount: number;
}

export interface Category {
  id: number;
  nameEn: string;
  nameFr: string;
  nameAr: string;
  nameZh: string;
  icon: string;
}

export interface AffiliateClick {
  clickId: string;
  productId: string;
  productName: string;
  retailerName: string;
  networkName: string;
  targetCountry: string;
  trafficSource: string;
  estimatedCommission: number;
  currency: string;
  timestamp: number;
  status: string;
}

export interface ChatMessage {
  id: string;
  sender: 'user' | 'ai';
  text: string;
  recommendedProductIds?: string[];
  timestamp: number;
}
