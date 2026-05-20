package net.mynameistmillo.experimentalmod.Enum;

public enum CasterOrBlockPos {
    CASTER("Caster"),
    BLOCK_POS("Block_Pos");

    private final String name;

    CasterOrBlockPos(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}