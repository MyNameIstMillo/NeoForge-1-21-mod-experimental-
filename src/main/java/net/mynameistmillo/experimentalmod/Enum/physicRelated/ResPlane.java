package net.mynameistmillo.experimentalmod.Enum.physicRelated;

public enum ResPlane {
    NOPE(0),
    XY(1 ),
    XZ(2 ),
    ZY(3 );

    private final int value;

    ResPlane(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ResPlane fromValue(int value){
        for (ResPlane v : values()){
            if (v.value == value) return v;
        }
        return NOPE;
    }
}
