package net.mynameistmillo.experimentalmod.Enum;

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
