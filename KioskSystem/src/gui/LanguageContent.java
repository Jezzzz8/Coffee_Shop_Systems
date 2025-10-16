package gui;

public class LanguageContent {
    public static final String ENGLISH = "English";
    public static final String TAGALOG = "Tagalog";
    public static final String CEBUANO = "Cebuano";
    
    private static final String ENGLISH_HELP = 
        """
        HELP & FREQUENTLY ASKED QUESTIONS
        
        HOW TO PLACE AN ORDER:
        1. Click 'NEW ORDER' on the main screen
        2. Browse our MENU or BEST SELLING items
        3. Select quantity and click 'ADD TO CART'
        4. Review your cart and click 'CHECKOUT'
        5. Choose your payment method
        6. Receive your order slip
        
        PAYMENT METHODS:
        \u2022 CASH - Pay with cash at the counter
        \u2022 GCASH - Digital payment via QR code
        
        PRODUCT AVAILABILITY:
        \u2022 Green 'Available' = Product is in stock
        \u2022 Red 'Not Available' = Temporarily out of stock
        
        FREQUENTLY ASKED QUESTIONS:
        Q: Can I customize my drink (extra sugar, less ice, etc.)?
        A: We're sorry, but our kiosk does not support drink customizations.
        All our beverages are prepared as signature drinks with carefully crafted recipes to ensure consistent quality and taste.
        
        Q: Can I modify my order after payment?
        A: Please speak with our staff for modifications.
        
        Q: What if a product is out of stock?
        A: Unavailable items cannot be added to cart.
        
        NEED MORE HELP?
        Please approach our staff for assistance.
        We're here to make your experience great!""";

    private static final String TAGALOG_HELP = 
        """
        TULONG AT MGA KARANIWANG TANONG
        
        PAANO MAG-ORDER:
        1. I-click ang 'NEW ORDER' sa pangunahing screen
        2. I-browse ang aming MENU o BEST SELLING na mga item
        3. Piliin ang dami at i-click ang 'ADD TO CART'
        4. Suriin ang iyong cart at i-click ang 'CHECKOUT'
        5. Piliin ang iyong paraan ng pagbabayad
        6. Tanggapin ang iyong order slip
        
        MGA PARAAN NG PAGBAYAD:
        \u2022 CASH - Magbayad gamit ang cash sa counter
        \u2022 GCASH - Digital na pagbabayad sa pamamagitan ng QR code
        
        AVAILABILITY NG PRODUKTO:
        \u2022 Berde na 'Available' = May stock ang produkto
        \u2022 Pula na 'Not Available' = Pansamantalang wala sa stock
        
        MGA KARANIWANG TANONG:
        Q: Maaari ko bang i-customize ang aking inumin (dagdag na asukal, mas kaunting yelo, atbp.)?
        A: Paumanhin, ngunit hindi sinusuportahan ng aming kiosk ang mga pag-customize ng inumin.
        Ang lahat ng aming inumin ay inihanda bilang mga signature na inumin na may maingat na ginawang mga recipe upang matiyak ang pare-parehong kalidad at lasa.
        
        Q: Maaari ko bang baguhin ang aking order pagkatapos ng pagbabayad?
        A: Mangyaring makipag-usap sa aming mga crew para sa mga pagbabago.
        
        Q: Paano kung ang isang produkto ay out of stock?
        A: Ang mga hindi available na item ay hindi maaaring idagdag sa cart.
        
        KAILANGAN NG HIGIT PANG TULONG?
        Mangyaring lumapit sa aming mga crew para sa tulong.
        Nandito kami para gawing maganda ang iyong karanasan!""";

    private static final String CEBUANO_HELP = 
        """
        TABANG UG KASAGARAN NGA MGA PANGUTANA
        
        UNSAON PAG-ORDER:
        1. I-klik ang 'NEW ORDER' sa main screen
        2. I-browse ang among MENU o BEST SELLING nga mga butang
        3. Pilia ang gidaghanon ug i-klik ang 'ADD TO CART'
        4. Ribyuha ang imong cart ug i-klik ang 'CHECKOUT'
        5. Pilia ang imong paagi sa pagbayad
        6. Dawata ang imong order slip
        
        MGA PAMAAGI SA PAGBAYAD:
        \u2022 CASH - Bayad gamit ang cash sa counter
        \u2022 GCASH - Digital nga pagbayad pinaagi sa QR code
        
        PAGKABATI SA PRODUKTO:
        \u2022 Berde nga 'Available' = Ang produkto anaa sa stock
        \u2022 Pula nga 'Not Available' = Temporaryo nga wala sa stock
        
        KASAGARAN NGA MGA PANGUTANA:
        Q: Mahimo ba nako ipasibo ang akong ilimnon (dugang nga asukal, gamay nga yelo, ug uban pa)?
        A: Pasayloa kami, apan ang among kiosk wala mosuporta sa pag-customize sa ilimnon.
        Ang tanan namong mga ilimnon giandam isip signature nga mga ilimnon nga adunay maayong pagkabuhat nga mga resipe aron masiguro ang makanunayon nga kalidad ug lami.
        
        Q: Mahimo ba nako usbon ang akong order pagkahuman sa pagbayad?
        A: Palihug pakigsulti sa among crew alang sa mga pagbag-o.
        
        Q: Unsa man kung ang usa ka produkto wala'y stock?
        A: Dili magamit nga mga butang dili madugang sa cart.
        
        NAGKINAHANGLAN UG DUGANG TABANG?
        Palihog duola ang among mga crew alang sa tabang.
        Ania kami aron mahimo ang imong kasinatian nga maayo!""";
    
    public static String getHelpContent(String language) {
        switch (language) {
            case TAGALOG:
                return TAGALOG_HELP;
            case CEBUANO:
                return CEBUANO_HELP;
            case ENGLISH:
            default:
                return ENGLISH_HELP;
        }
    }
    
    public static String[] getAvailableLanguages() {
        return new String[]{ENGLISH, TAGALOG, CEBUANO};
    }
}
