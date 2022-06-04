package utils;

public enum CannedSearch {

    FAVORITES, RECENTLY_VIEWED, ON_SALE, STYLE_WE_LOVE, BRANDS_YOU_LOVE, GONE_FOREVER;

    public String friendlyString() {
        switch (this) {
            case FAVORITES:
                return "Favorites";
            case RECENTLY_VIEWED:
                return "Recently viewed";
            case ON_SALE:
                return "On sale";
            case STYLE_WE_LOVE:
                return "Style we love";
            case BRANDS_YOU_LOVE:
                return "Brands you love";
            case GONE_FOREVER:
                return "Gone forever";
            default:
                return "Canned search";
        }
    }
}
