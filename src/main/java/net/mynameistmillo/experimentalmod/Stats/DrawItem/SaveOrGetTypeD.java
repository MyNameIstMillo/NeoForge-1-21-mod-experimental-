package net.mynameistmillo.experimentalmod.Stats.DrawItem;

public enum SaveOrGetTypeD {
    MOD("Modifiers"),
    PROJ("Projectile");

    private final String id;

    SaveOrGetTypeD(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
