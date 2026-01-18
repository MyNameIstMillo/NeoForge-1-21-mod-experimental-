package net.mynameistmillo.experimentalmod.Enum;

public enum TriggerType {
    TRIGGER(1),
    TIMER(2),
    EXPIRE(3);

    private final int id;

    TriggerType(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
