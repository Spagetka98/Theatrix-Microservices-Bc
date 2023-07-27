package cz.osu.ratingservice.model.enums;

public enum EDateFormats {
    DD_MM_YYYY_HH_MM_DOT("dd.MM.yyyy HH:mm");

    private final String value;

    EDateFormats(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
