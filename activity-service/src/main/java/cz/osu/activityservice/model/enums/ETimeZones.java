package cz.osu.activityservice.model.enums;

public enum ETimeZones {
    EUROPE_PRAGUE("Europe/Prague");
    private final String value;

    ETimeZones(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
