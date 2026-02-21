package net.mynameistmillo.experimentalmod.Enum;

public enum ModOrProj {
    MOD("Modifiers"),
    PROJ("Projectile");

    private final String id;

    ModOrProj(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
