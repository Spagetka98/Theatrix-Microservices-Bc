package cz.osu.cloudgateway.model.enums;

public enum EHeaders {
    AUTHORIZATION("Authorization"),
    USERNAME("Username"),
    USER_ID("UserID"),
    ROLES("Roles"),
    CONTENT_TYPE("Content-Type");

    private final String value;

    EHeaders(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
