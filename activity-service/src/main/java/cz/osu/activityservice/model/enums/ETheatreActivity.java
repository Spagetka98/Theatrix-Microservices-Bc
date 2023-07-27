package cz.osu.activityservice.model.enums;

public enum ETheatreActivity {

    ID("id"), NAME("name"), STAGE("stage"), DIVISION("division"),
    DATE("date"),START("start");

    private final String attribute;

    ETheatreActivity(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }
}
