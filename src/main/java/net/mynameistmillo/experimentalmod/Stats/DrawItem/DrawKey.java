package net.mynameistmillo.experimentalmod.Stats.DrawItem;

public enum DrawKey {
    DRAW("draw_size",0),
    FREE_SPACE("free_space",0);

    private final String id;
    private final int size;

    DrawKey(String id, int size){
        this.id = id;
        this.size = size;
    }

    public String getId(){return id;}
    public int getDefaultValue(){return size;}


}
