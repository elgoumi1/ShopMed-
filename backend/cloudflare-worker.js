/**
 * ShopMed Global API — Cloudflare Worker
 * Free-tier serverless router for affiliate click logging, redirect tracking,
 * price alert notifications, and catalog synchronization.
 */

export default {
  async fetch(request, env, ctx) {
    const url = new URL(request.url);
    const origin = request.headers.get("Origin") || "*";

    // Standard CORS headers
    const corsHeaders = {
      "Access-Control-Allow-Origin": origin,
      "Access-Control-Allow-Methods": "GET, POST, OPTIONS",
      "Access-Control-Allow-Headers": "Content-Type, Authorization",
    };

    if (request.method === "OPTIONS") {
      return new Response(null, { headers: corsHeaders });
    }

    // Health check
    if (url.pathname === "/api/health") {
      return new Response(JSON.stringify({ status: "ok", app: "ShopMed API", timestamp: Date.now() }), {
        headers: { ...corsHeaders, "Content-Type": "application/json" }
      });
    }

    // Record Outbound Affiliate Click
    if (url.pathname === "/api/clicks" && request.method === "POST") {
      try {
        const body = await request.json();
        const clickRecord = {
          clickId: body.clickId || "clk_" + Math.random().toString(36).substring(2, 9),
          productId: body.productId,
          productName: body.productName,
          retailerName: body.retailerName,
          networkName: body.networkName,
          targetCountry: body.targetCountry || "GLOBAL",
          trafficSource: body.trafficSource || "direct",
          estimatedCommission: body.estimatedCommission || 0,
          currency: body.currency || "EUR",
          ip: request.headers.get("CF-Connecting-IP") || "0.0.0.0",
          userAgent: request.headers.get("User-Agent") || "Unknown",
          timestamp: Date.now(),
          status: "Tracking Active"
        };

        // In production with KV binding: await env.SHOPMED_KV.put(`click:${clickRecord.clickId}`, JSON.stringify(clickRecord));
        return new Response(JSON.stringify({ success: true, click: clickRecord }), {
          headers: { ...corsHeaders, "Content-Type": "application/json" }
        });
      } catch (err) {
        return new Response(JSON.stringify({ error: "Invalid click payload" }), {
          status: 400,
          headers: { ...corsHeaders, "Content-Type": "application/json" }
        });
      }
    }

    // Create Price Alert
    if (url.pathname === "/api/alerts" && request.method === "POST") {
      try {
        const body = await request.json();
        const alertRecord = {
          id: "alt_" + Math.random().toString(36).substring(2, 8),
          productId: body.productId,
          targetPrice: body.targetPrice,
          currency: body.currency,
          email: body.email,
          createdAt: Date.now(),
          active: true
        };

        return new Response(JSON.stringify({ success: true, alert: alertRecord }), {
          headers: { ...corsHeaders, "Content-Type": "application/json" }
        });
      } catch (err) {
        return new Response(JSON.stringify({ error: "Failed to create alert" }), {
          status: 400,
          headers: { ...corsHeaders, "Content-Type": "application/json" }
        });
      }
    }

    // Fallback 404
    return new Response(JSON.stringify({ error: "Endpoint not found" }), {
      status: 404,
      headers: { ...corsHeaders, "Content-Type": "application/json" }
    });
  }
};
