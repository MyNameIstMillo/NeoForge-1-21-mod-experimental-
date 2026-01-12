package net.mynameistmillo.experimentalmod.WandLogic.Types;

public enum TriggerType {
    TRIGGER("trigger",1),
    TIMER("timer",2),
    EXPIRE("expire",3);

    private final String name;
    private final int id;

    TriggerType(String name, int id){
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
