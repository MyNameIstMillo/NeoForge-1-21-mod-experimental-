package net.mynameistmillo.experimentalmod.spells.stats;

public enum StatsKey {
    GRAVITY("gravity", 0.03f),
    DRAG("drag", 1.0f),
    SPEED("speed", 1.0f),
    LIFETIME("lifetime", 60.0f),
    DAMAGE("damage", 1.0f),
    DISPLACEMENT("displacement", 0);
    //przesunięcie?
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
