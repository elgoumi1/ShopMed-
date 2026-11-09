package com.example.model

data class Category(
    val id: Int,
    val nameEn: String,
    val nameFr: String,
    val nameAr: String,
    val iconKey: String,
    val subcategories: List<String>,
    val nameZh: String = ""
) {
    fun localizedName(language: AppLanguage): String = when (language) {
        AppLanguage.EN -> nameEn
        AppLanguage.FR -> nameFr
        AppLanguage.AR -> nameAr
        AppLanguage.ZH -> if (nameZh.isNotBlank()) nameZh else nameEn
    }
}

object CategoryCatalog {
    val allCategories: List<Category> = listOf(
        Category(1, "Smartphones & Mobile", "Smartphones & Mobiles", "الهواتف الذكية والأجهزة المحمولة", "phone", listOf("iPhones", "Samsung Galaxy", "Xiaomi & Poco", "Accessories", "Chargers"), "智能手机与移动设备"),
        Category(2, "Computers & Laptops", "Ordinateurs & Portables", "أجهزة الكمبيوتر والمحمولة", "laptop", listOf("MacBooks", "Gaming Laptops", "Ultrabooks", "Monitors", "Keyboards"), "电脑与笔记本"),
        Category(3, "Consumer Electronics", "Électronique Grand Public", "إلكترونيات استهلاكية", "tv", listOf("Audio & Soundbars", "Smart TVs", "Tablets", "Headphones", "Power Banks"), "消费电子产品"),
        Category(4, "Smartwatches & Accessories", "Montres Connectées & Accessoires", "الساعات الذكية وملحقاتها", "watch", listOf("Apple Watch", "Garmin GPS", "Fitness Bands", "Straps & Docks"), "智能手表与配件"),
        Category(5, "Men's Fashion", "Mode Homme", "أزياء رجالية", "man", listOf("Shirts & Polos", "Suits & Blazers", "Jeans & Trousers", "Jackets", "Activewear"), "男士时尚"),
        Category(6, "Women's Fashion", "Mode Femme", "أزياء نسائية", "woman", listOf("Dresses & Abayas", "Tops & Blouses", "Cardigans", "Pants & Skirts", "Traditional Moroccan Caftans"), "女士时尚"),
        Category(7, "Kids' Clothing", "Vêtements Enfants", "ملابس الأطفال", "child", listOf("Baby Sets", "Boys Casual", "Girls Dresses", "School Wear", "Pyjamas"), "童装与婴儿服"),
        Category(8, "Shoes & Sneakers", "Chaussures & Baskets", "الأحذية والأحذية الرياضية", "shoe", listOf("Running Sneakers", "Leather Loafers", "Boots", "Sandals", "Traditional Babouches"), "鞋履与运动鞋"),
        Category(9, "Bags & Accessories", "Sacs & Accessoires", "الحقائب والإكسسوارات", "bag", listOf("Backpacks", "Handbags", "Leather Wallets", "Belts", "Sunglasses"), "箱包与配饰"),
        Category(10, "Beauty & Skincare", "Beauté & Soins", "الجمال والعناية بالبشرة", "beauty", listOf("Argan Oil Serums", "Sunscreen SPF50", "Moisturizers", "Anti-Aging", "Organic Clay"), "美妆与护肤"),
        Category(11, "Perfumes & Fragrances", "Parfums & Fragrances", "العطور والبخور", "perfume", listOf("Luxury Eau de Parfum", "Oriental Oud & Amber", "Designer Cologne", "Body Mists"), "香水与香氛"),
        Category(12, "Home Appliances", "Électroménager", "الأجهزة المنزلية", "appliances", listOf("Air Conditioners", "Washing Machines", "Vacuum Cleaners", "Refrigerators", "Steam Irons"), "家用电器"),
        Category(13, "Furniture", "Mobilier & Salon", "الأثاث والديكور", "furniture", listOf("Modern Sofas", "Dining Tables", "Ergonomic Chairs", "Beds & Mattresses", "Moroccan Living Salons"), "家具与家居"),
        Category(14, "Kitchen Equipment", "Équipements de Cuisine", "معدات المطبخ", "kitchen", listOf("Air Fryers", "Espresso Machines", "Traditional Tagines", "Cookware Sets", "Blenders"), "厨房设备与餐具"),
        Category(15, "Home Decoration", "Décoration Intérieure", "ديكور المنزل", "decor", listOf("Moroccan Rugs & Zellige", "Ambient Lamps", "Wall Art", "Cushions & Throws", "Vases"), "家居装饰"),
        Category(16, "Sports & Fitness", "Sport & Fitness", "الرياضة واللياقة البدنية", "sports", listOf("Gym Dumbbells", "Yoga Mats", "Football Jerseys", "Bicycles", "Protein Shakers"), "运动与健身"),
        Category(17, "Gaming & Entertainment", "Jeux Vidéo & Divertissement", "الألعاب والترفيه", "gaming", listOf("PlayStation 5", "Xbox Series X", "Nintendo Switch", "Gaming Headsets", "Controllers"), "游戏与娱乐"),
        Category(18, "Baby Products", "Univers Bébé", "منتجات الأطفال والرضع", "baby", listOf("Strollers & Prams", "Car Seats", "Baby Monitors", "Feeding Bottles", "Diaper Bags"), "母婴用品"),
        Category(19, "Automotive Accessories", "Accessoires Auto", "ملحقات السيارات", "car", listOf("Dash Cams", "Phone Mounts", "Car Seat Covers", "Tire Inflators", "OBD2 Scanners"), "汽车配件与车载设备"),
        Category(20, "Pet Supplies", "Articles pour Animaux", "مستلزمات الحيوانات الأليفة", "pets", listOf("Cat & Dog Food", "Pet Beds", "Grooming Kits", "Collars & Leashes", "Scratchers"), "宠物用品"),
        Category(21, "Books & Stationery", "Livres & Papeterie", "الكتب والقرطاسية", "books", listOf("Best-Selling Fiction", "Business & Finance", "Academic Notebooks", "Luxury Pens", "Art Supplies"), "图书与文具"),
        Category(22, "Travel Accessories", "Accessoires de Voyage", "إكسسوارات السفر", "travel", listOf("Carry-On Luggage", "Passport Holders", "Travel Adapters", "Neck Pillows", "Packing Cubes"), "旅行用品与行李箱"),
        Category(23, "Photography Equipment", "Matériel Photo & Vidéo", "معدات التصوير", "camera", listOf("Mirrorless Cameras", "Gimbals & Stabilizers", "Tripods", "Studio Lights", "Microphones"), "摄影与摄像设备"),
        Category(24, "Garden & Outdoor", "Jardin & Extérieur", "الحديقة والمنتجات الخارجية", "garden", listOf("Solar Lights", "Garden Furniture", "Plant Pots", "Barbecues & Grills", "Pressure Washers"), "花园与户外用品"),
        Category(25, "Office Equipment", "Équipement de Bureau", "معدات المكاتب", "office", listOf("Standing Desks", "Laser Printers", "Paper Shredders", "Document Scanners", "Organizers"), "办公设备与耗材"),
        Category(26, "Smart Home Devices", "Maison Connectée", "أجهزة المنزل الذكي", "smarthome", listOf("Smart Wi-Fi Plugs", "Video Doorbells", "Smart Thermostats", "Security Cameras", "Zigbee Hubs"), "智能家居设备"),
        Category(27, "Outdoor & Camping", "Camping & Randonnée", "معدات التخييم والهواء الطلق", "camping", listOf("Tents & Sleeping Bags", "Portable Gas Stoves", "Trekking Backpacks", "Thermal Flasks", "Headlamps"), "户外露营与徒步"),
        Category(28, "Personal Care Products", "Soins Personnels", "منتجات العناية الشخصية", "personalcare", listOf("Electric Toothbrushes", "Hair Dryers & Stylers", "Shavers & Trimmers", "Massage Guns", "Skin Peelers"), "个人护理与个护健康")
    )
}
