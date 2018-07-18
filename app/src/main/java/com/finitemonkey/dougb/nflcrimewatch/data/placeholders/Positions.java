package com.finitemonkey.dougb.nflcrimewatch.data.placeholders;


public class Positions {
    private String playerPosition;
    private String playerPositionName;
    private String playerPositionType;
    private int arrestCount;

    public Positions(String playerPosition, String playerPositionName, String playerPositionType,
                     int arrestCount) {
        this.playerPosition = playerPosition;
        this.playerPositionName = playerPositionName;
        this.playerPositionType = playerPositionType;
        this.arrestCount = arrestCount;
    }


    public String getPlayerPosition() {return playerPosition;}
    public void setPlayerPosition(String playerPosition) {this.playerPosition = playerPosition;}

    public String getPlayerPositionName() {return playerPositionName;}
    public void setPlayerPositionName(String playerPositionName) {this.playerPositionName = playerPositionName;}

    public String getPlayerPositionType() {return playerPositionType;}
    public void setPlayerPositionType(String playerPositionType) {this.playerPositionType = playerPositionType;}

    public int getArrestCount() {return arrestCount;}
    public void setArrestCount(int arrestCount) {this.arrestCount = arrestCount;}
}
