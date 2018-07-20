package com.finitemonkey.dougb.nflcrimewatch.data.proxies;

public class Crimes {
    private String encounter;
    private String category;
    private String crimeCategoryColor;
    private int arrestCount;

    public Crimes(String encounter, String category, String crimeCategoryColor, int arrestCount) {
        this.encounter = encounter;
        this.category = category;
        this.crimeCategoryColor = crimeCategoryColor;
        this.arrestCount = arrestCount;
    }

    public String getEncounter() {return encounter;}
    public void setEncounter(String encounter) {this.encounter = encounter;}

    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}

    public String getCrimeCategoryColor() {return crimeCategoryColor;}
    public void setCrimeCategoryColor(String crimeCategoryColor) {this.crimeCategoryColor = crimeCategoryColor;}

    public int getArrestCount() {return arrestCount;}
    public void setArrestCount(int arrestCount) {this.arrestCount = arrestCount;}
}
