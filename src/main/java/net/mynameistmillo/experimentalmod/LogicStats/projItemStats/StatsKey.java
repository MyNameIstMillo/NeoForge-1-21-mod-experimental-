package net.mynameistmillo.experimentalmod.LogicStats.projItemStats;

public enum StatsKey {
    //movement
    SPEED("speed", 1.0f),
    DRAG("drag", 1.0f),
    GRAVITY("gravity", 0.03f),
    ACCELERATION_L_R("acceleration_l_r", 0.0f),
    ACCELERATION_U_D("acceleration_u_d", 0.0f),
    ACCELERATION_F_B("acceleration_f_b", 0.0f),

    //on spawn stats
    VERTICAL_SPREAD("vertical_spread", 0.0f),
    HORIZONTAL_SPREAD("horizontal_spread", 0.0f),
    RECOIL("recoil", 0.0f),
    DISPLACEMENT_L_R("displacement_l_r", 0),
    DISPLACEMENT_U_D("displacement_u_d", 0),
    DISPLACEMENT_F_B("displacement_f_b", 0),

    //one time use
    COLOUR("colour", 0.0f),
    EFFECT_ON_HIT("effect_on_hit", 0.0f),
    TOLERANCE("tolerance", 0.0f),
    SPAGHETTI_TOLERANCE("spaghetti_tolerance", 0.0f),

    //bollen stats
    TRIGGER_TYPE("trigger_type",0.0F),
    PIERCING("piercing", 0.0f),
    TICK_EVENT("tick_event", 40.0f),
    FRIENDLY_FIRE("friendly_fire", 0.0f),

    //stats
    LIFETIME("lifetime", 60.0f),
    DAMAGE("damage", 1.0f);






    private final String id;
    private final float defaultValue;

    StatsKey(String id, float defaultValue){
        this.id = id;
        this.defaultValue = defaultValue;
    }

    public String getId(){return id;}
    public float getDefaultValue(){return defaultValue;}

    public static StatsKey byName(String name){
        for (StatsKey s : values()) if (s.id.equals(name) || s.name().equalsIgnoreCase(name)) return s;
        return null;
    }


}
