package net.mynameistmillo.experimentalmod.Stats.DrawItem;

public enum ModOrProjType {
    MOD("Modifiers"),
    PROJ("Projectile");

    private final String id;

    ModOrProjType(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
