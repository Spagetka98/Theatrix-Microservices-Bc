package cz.osu.authenticationservice.model.enums;

public enum EDateFormats {
    DD_MM_YYYY_HH_MM_SS_HYPHEN("dd-MM-yyyy HH:mm:ss");

    private final String value;

    EDateFormats(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
