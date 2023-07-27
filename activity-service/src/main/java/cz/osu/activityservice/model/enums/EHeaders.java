package cz.osu.activityservice.model.enums;

public enum EHeaders {
    USERNAME("Username"),
    USER_ID("UserID"),
    ROLES("Roles");
    private final String value;

    EHeaders(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
