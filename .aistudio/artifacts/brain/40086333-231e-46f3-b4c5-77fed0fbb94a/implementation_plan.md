# ShopMed — Global AI Affiliate Shopping Platform & Monorepo Ecosystem

An enterprise, multi-platform affiliate shopping and price comparison ecosystem under the unified brand **ShopMed**, featuring a native Android application with Jetpack Compose, a Gemini-powered conversational ChatScreen UI, an interactive React/TypeScript/Vite web application, and Cloudflare Workers backend integration.

---

### User Review & Critical Decisions

> [!NOTE]
> **Status: All Phases Successfully Implemented & Verified (Build Succeeded, All Tests Passing).**
> - **Brand Identity**: Unified under the name **ShopMed**, supporting Arabic, English, French, Chinese, and regional European languages.
> - **Codebase Structure**: Monorepo organization retaining the complete native Android Jetpack Compose codebase in `/app`, with responsive web platform in `/web` (React 19 + TypeScript + Vite + Tailwind CSS + PWA).
> - **AI Integration**: Native Gemini API integration in `ChatScreen.kt` with catalog grounding, deal discovery, and interactive product recommendation cards.

---

### 1. Overview & Core Concept

- **What It Does**: ShopMed is an affiliate discovery and intelligence marketplace. Users browse, search, and compare verified deals across major retailers (Amazon EU/Global, AliExpress Affiliate, Awin, CJ Affiliate, Impact, Jumia, and eBay Partner Network). When users click out to buy, they are safely redirected with FTC/EU-compliant disclosures and tracked affiliate links.
- **AI Shopping Assistant**: A dedicated `ChatScreen` powered by the Gemini API that acts as a personal shopping concierge. It analyzes user budget constraints, answers technical specification inquiries, suggests cheaper or higher-value alternatives, and grounds responses in real-time verified catalog data.
- **Target Audience**: Cross-border shoppers in Morocco, Europe, and global international markets seeking verified price history, authentic terroir & tech products, transparent currency conversions (EUR, MAD, USD), and personalized deal recommendations.
- **Preservation Principle**: 100% preservation of all existing Android screens (Catalog, Details, Side-by-Side Comparison Matrix, Wishlist, Price Drop Alerts, Admin Operations Hub, Local Room Database, and Custom Adaptive Icons).

---

### 2. User Experience & Visual Design

- **Visual Identity**:
  - **Dominant Theme**: Deep Royal Navy (`#0A192F` / `#0F2A4A`) paired with Emerald Green (`#059669` / `#10B981`) for verified savings, Amber Gold (`#D97706`) for affiliate badges, and Slate Neutral surfaces.
  - **Mode Support**: Full Light and Dark modes with automatic contrast adjustment.
  - **Multilingual & RTL**: Dynamic layout direction support for Arabic (RTL) alongside English, French, and Chinese (LTR).
- **Core User Journeys**:
  1. **Discovery & Deal Hunting**: Browse 28 standardized categories, apply multi-criteria filters (price range, verified discount %, country availability, brand, retailer), and view estimated currency conversions.
  2. **AI Shopping Consultation**: Tap the AI Concierge tab to enter `ChatScreen`. Inquire with queries like *"Find me a high-performance gaming laptop under €1200 with European warranty"* or *"What is the best running shoe with a verified discount over 30%?"*. The Gemini agent responds with actionable reasoning and interactive product cards.
  3. **Direct Affiliate Checkout**: Tapping "Buy Deal" opens the transparent Affiliate Disclosure Dialog explaining the commission model before safely opening the partner retailer website.
  4. **Side-by-Side Comparison**: Select up to 4 products to compare full technical specifications, verified rating metrics, and price differentials side-by-side.
  5. **Admin Operations Hub**: Manage catalog items, test link health, view live outbound click logs with estimated commissions, and execute bulk CSV imports.

---

### 3. Key Product Decisions & Trade-Offs

- **Decision 1: Native Android ChatScreen with Gemini API Grounding**
  - *Chosen Approach*: Implement a dedicated `ChatScreen` composable in the Android application utilizing `BuildConfig.GEMINI_API_KEY` (injected securely via Secrets Gradle Plugin and AI Studio Secrets panel). Ground all prompts with current catalog items, prices, and specifications to prevent hallucinations.
  - *Why*: Provides sub-second conversational latency, offline fallback to rule-based deal matching, and direct UI component rendering (clickable product recommendation cards with affiliate buttons).
- **Decision 2: Monorepo Organization (`/app` and `/web`)**
  - *Chosen Approach*: Keep the existing native Android project in root and `/app`, and add the responsive web application in `/web` with shared data contracts and types.
  - *Why*: Allows simultaneous deployment of the native Android APK and the standalone PWA/web app to Cloudflare Pages without breaking Gradle or the Android build toolchain.
- **Decision 3: Tiered Persistence (Room DB + Cloudflare API / Firebase Spark)**
  - *Chosen Approach*: On Android, use Room Database for persistent local state (favorites, comparison queue, price alerts, custom added products, and click logs). In the web app, connect to Firebase Spark (Auth & Cloud Firestore) with Cloudflare Workers API endpoints.
  - *Why*: Zero startup cost, no credit card required, offline-first reliability on device, and scalable cloud synchronization.

---

### 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        ShopMed Monorepo Core                           │
├───────────────────────────────────┬────────────────────────────────────┤
│      Native Android App (/app)    │        Web Platform (/web)         │
│  - Kotlin + Jetpack Compose (M3)  │  - React 19 + TypeScript + Vite    │
│  - Navigation BackHandler Stack   │  - Tailwind CSS + Lucide Icons     │
│  - Room Database (Local Entities) │  - PWA Service Worker & Manifest   │
│  - Coil Image Loading             │  - Responsive Mobile/Desktop UI    │
├───────────────────────────────────┴────────────────────────────────────┤
│                       AI & Commerce Services                           │
│  - Gemini API (AI Shopping Assistant with Catalog Grounding)           │
│  - Affiliate Engine (Amazon EU, AliExpress, Awin, CJ, Jumia, Impact)   │
│  - Currency Engine (Live/Benchmark EUR & MAD with rate override)       │
│  - Cloudflare Workers API & Firebase Spark (Auth, Firestore, Rules)    │
└────────────────────────────────────────────────────────────────────────┘
```

#### Entity & Data Flow Model:
1. **Product Catalog**: `id`, `nameEn/Fr/Ar/Zh`, `description`, `categoryId`, `brand`, `currentPrice`, `originalPrice`, `currency`, `discountPercent`, `verifiedDiscount`, `rating`, `reviewsCount`, `stockStatus`, `retailerName`, `affiliateNetwork`, `affiliateUrl`, `imageUrls`, `specifications`, `shippingNote`.
2. **Affiliate Clicks**: `clickId`, `productId`, `productName`, `retailerName`, `networkName`, `targetCountry`, `trafficSource`, `estimatedCommission`, `currency`, `timestamp`, `status`.
3. **AI Chat Session**: `messageId`, `sender` ("user" | "ai"), `text`, `recommendedProductIds`, `timestamp`, `groundingTokens`.

---

### Implementation Phases

1. **Phase 1: Android ChatScreen & Gemini API Integration**
   - Create `ChatScreen.kt` with modern messaging bubbles, typing indicator, quick prompt chips, and embedded clickable recommendation cards.
   - Connect the AI assistant to the Gemini model using `BuildConfig.GEMINI_API_KEY` with structured catalog prompt grounding.
   - Update navigation and `MainActivity.kt` to present the enhanced `ChatScreen` with full RTL and dark/light support.
2. **Phase 2: Multilingual Expansion & International Catalog**
   - Add Chinese (zh) localization support alongside Arabic, English, and French in `Category.kt`, `AppSettings.kt`, and `Product.kt`.
   - Update branding and launcher strings to **ShopMed**.
3. **Phase 3: Web Platform Construction (`/web`)**
   - Initialize `/web` with `package.json`, `vite.config.ts`, `tsconfig.json`, `index.html`, and `tailwind.config.js`.
   - Implement complete pages: Home, Catalog, Details, Comparison, Wishlist, AI Assistant, Deals, and Admin Dashboard.
   - Add PWA manifest and service worker.
4. **Phase 4: Cloudflare Workers & Firebase API Contracts**
   - Add Cloudflare Worker script and `firestore.rules` for affiliate link click logging, price alerts, and product synchronization.
5. **Phase 5: Verification & End-to-End Build**
   - Run `compile_applet` to ensure Android project compiles with zero errors.
   - Validate unit and Robolectric tests.
