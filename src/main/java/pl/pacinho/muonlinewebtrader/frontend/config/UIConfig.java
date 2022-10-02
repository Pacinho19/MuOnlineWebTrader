package pl.pacinho.muonlinewebtrader.frontend.config;

public class UIConfig {
    public static final String HOME_URL = "/mu-online/web-trader";
    public static final String ITEM_URL = HOME_URL + "/item";
    public static final String PUT_FOR_SALE_ITEM_URL = HOME_URL + "/item/put-for-sale";
    public static final String DECODE_ITEM_URL = ITEM_URL + "/decode";
    public static final String ITEM_LIST_URL = ITEM_URL + "/list";
    public static final String WAREHOUSE_URL = HOME_URL + "/ware";
    public static final String TRANSFER_TO_WEB_WAREHOUSE_URL = HOME_URL + "/ware/transfer";
    public static final String WEB_WAREHOUSE_URL = WAREHOUSE_URL + "/web";
    public static final String TRANSFER_TO_GAME_WAREHOUSE_URL = WAREHOUSE_URL + "/web/transfer";
    public static final String SHOP_URL = HOME_URL + "/shop";
    public static final String TRANSFER_ZEN_TO_WEB_WAREHOUSE_URL = WAREHOUSE_URL + "/zen-transfer";
    public static final String TRANSFER_ZEN_TO_GAME_WAREHOUSE_URL = WEB_WAREHOUSE_URL + "/zen-transfer";
    public static final String PUT_FOR_SALE_ITEM_PAGE_URL = PUT_FOR_SALE_ITEM_URL + "-page";
    public static final String BUY_ITEM_URL = ITEM_URL + "/buy";
    public static final String ITEM_FOR_SALE = SHOP_URL + "/item/";
    public static final String WEB_WALLET_URL = HOME_URL + "/web-wallet";
    public static final String BLESS_TRANSFER = WEB_WALLET_URL + "/bless-transfer";
    public static final String SOUL_TRANSFER = WEB_WALLET_URL + "/soul-transfer";
    public static final String ZEN_TRANSFER = WEB_WALLET_URL + "/zen-transfer";
    public static final String BLESS_DISBURSEMENT = WEB_WALLET_URL + "/bless-disbursement";
    public static final String SOUL_DISBURSEMENT = WEB_WALLET_URL + "/soul-disbursement";
    public static final String ZEN_DISBURSEMENT = WEB_WALLET_URL + "/zen-disbursement";
    public static final String SHOP_CLEAR_FILTERS = SHOP_URL + "/clear-filters";
    public static final String READ_NOTIFICATION = HOME_URL + "/notification/{id}";
    public static final String READ_ALL_NOTIFICATIONS = HOME_URL + "/notification/read-all";
    public static final String CANCEL_ITEM_OFFER = ITEM_FOR_SALE + "{code}/cancel-offer";
    public static final String LOGIN_URL = "/login";
    public static final String TRADE_HOME_URL = HOME_URL + "/trade";
    public static final String TRADE_PUT_ITEM = TRADE_HOME_URL + "/put-item";
    public static final String TRADE_SEND_OFFER = TRADE_HOME_URL + "/send-offer";
}