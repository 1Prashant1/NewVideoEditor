package com.app.videonewsmaker;

public class videoallow {
    int makevid;
    private static final videoallow ourInstance = new videoallow();
    public static videoallow getInstance() {
        return ourInstance;
    }
    private videoallow() { }
    public void setText(int editValue) {
        this.makevid = editValue;
    }
    public int getText() {
        return makevid;
    }
}
