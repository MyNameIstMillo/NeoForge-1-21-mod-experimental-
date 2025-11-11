package net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey;

public enum StatsKeyF {
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

    //stats
    LIFETIME("lifetime", 60.0f),
    DAMAGE("damage", 1.0f);






    private final String id;
    private final float defaultValue;

    StatsKeyF(String id, float defaultValue){
        this.id = id;
        this.defaultValue = defaultValue;
    }

    public String getId(){return id;}
    public float getDefaultValue(){return defaultValue;}



}
