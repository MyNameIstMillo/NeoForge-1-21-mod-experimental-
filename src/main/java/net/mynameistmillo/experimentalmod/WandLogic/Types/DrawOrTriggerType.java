package net.mynameistmillo.experimentalmod.WandLogic.Types;

public enum DrawOrTriggerType {
    DRAW("Draw"),
    TRIGGER("Trigger");

    private final String id;

    DrawOrTriggerType(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
