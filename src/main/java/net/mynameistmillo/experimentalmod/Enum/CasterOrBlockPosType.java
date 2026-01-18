package net.mynameistmillo.experimentalmod.Enum;

public enum CasterOrBlockPosType {
    CASTER("Caster"),
    BLOCK_POS("Block_Pos");

    private final String name;

    CasterOrBlockPosType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
//zamiast tego to w stats proj to powinno się znajdować