package cz.osu.authenticationservice.model.enums;

public enum EHeaders {
    AUTHORIZATION("Authorization");

    private final String value;

    EHeaders(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
