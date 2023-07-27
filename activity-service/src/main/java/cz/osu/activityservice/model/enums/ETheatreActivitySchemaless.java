package cz.osu.activityservice.model.enums;

public enum ETheatreActivitySchemaless {
    AUTHOR("author"), END("end"), DESCRIPTION("description"), URL("url");

    private final String attribute;

    ETheatreActivitySchemaless(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }
}
