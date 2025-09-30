package net.mynameistmillo.experimentalmod.spellLogic.stats;

public enum StatsKey {
    GRAVITY("gravity", 0.03f),          //0->No 0.2->big
    DRAG("drag", 1.0f),                 //1->No 0.90->big
    SPEED("speed", 1.0f),               //1->normal >big >small
    LIFETIME("lifetime", 60.0f),        //life
    DAMAGE("damage", 1.0f),
    DISPLACEMENT("displacement", 0);    //how far from normal
    //akceleracja
    //kolor, ale to nie jest priorytetem





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
