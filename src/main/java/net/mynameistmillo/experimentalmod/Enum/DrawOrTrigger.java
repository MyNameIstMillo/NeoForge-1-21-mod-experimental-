package net.mynameistmillo.experimentalmod.Enum;

public enum DrawOrTrigger {
    DRAW("Draw"),
    TRIGGER("Trigger");

    private final String id;

    DrawOrTrigger(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
