package net.mynameistmillo.experimentalmod.Enum;

public enum CastPosDef {
    DEFAULT(0),
    FORCE_AT_PLAYER(1),
    BLOCK_POS(2);

    private final int value;

    CastPosDef(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CastPosDef fromValue(int value){
        for (CastPosDef v : values()){
            if (v.value == value) return v;
        }
        return DEFAULT;
    }
}
