package net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey;

public enum StatsF {
    //movement
    SPEED("S", 0.0f),
    DRAG("D", 0.0f),

    FORCE_Y("FY", 0.0f),
    FORCE_X("FX", 0.0f),
    FORCE_Z("FZ", 0.0f),

    //on spawn stats
    VERTICAL_SPREAD("SV", 0.0f),
    HORIZONTAL_SPREAD("SH", 0.0f),

    SHIFT_LR("HLR", 0.0f),
    SHIFT_UD("HUD", 0.0f),
    SHIFT_FB("HFB", 0.0f),

    //damage
    NORMAL_DAMAGE("ND", 0.0f);






    private final String id;
    private final float defaultValue;

    StatsF(String id, float defaultValue){
        this.id = id;
        this.defaultValue = defaultValue;
    }

    public String getId(){return id;}
    public float getDefaultValue(){return defaultValue;}



}
