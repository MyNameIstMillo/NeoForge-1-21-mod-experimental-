package net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey;

public enum StatsI {
    //bollen stats,
    TRIGGER_TYPE("TT",0),
    FRIENDLY_FIRE("FF", 0),

    //something
    DRAW_TRIGGER("DT",0),
    LIFETIME("L", 0),
    CAST_POS("CP", 0),

    RES_PLANE("RP", 0);

    private final String id;
    private final Integer defaultValue;

    StatsI(String id, Integer defaultValue){
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
