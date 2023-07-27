package cz.osu.activityservice.model.enums;

public enum EDateFormats {
    DD_MM_YYYY_HH_MM_SS_HYPHEN("dd-MM-yyyy HH:mm:ss"),
    DD_MM_YYYY_HYPHEN("dd-MM-yyyy"),
    YYYY_MM_DD_HYPHEN("yyyy-MM-dd");

    private final String value;

    EDateFormats(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
