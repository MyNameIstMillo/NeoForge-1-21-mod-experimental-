package net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey;

public enum StatsKeyI {
    //something
    COLOUR("colour", 0),
    EFFECT_ON_HIT("effect_on_hit", 0),
    TOLERANCE("tolerance", 0),
    SPAGHETTI_TOLERANCE("spaghetti_tolerance", 0),

    //bollen stats
    TRIGGER_TYPE("trigger_type",0),
    PIERCING("piercing", 0),
    TICK_EVENT("tick_event", 40),
    FRIENDLY_FIRE("friendly_fire", 0);

    private final String id;
    private final Integer defaultValue;

    StatsKeyI(String id, Integer defaultValue){
        this.id = id;
        this.defaultValue = defaultValue;
    }

    public String getId() {
        return id;
    }

    public Integer getDefaultValue() {
        return defaultValue;
    }
}
