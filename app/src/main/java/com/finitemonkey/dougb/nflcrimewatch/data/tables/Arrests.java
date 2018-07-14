package com.finitemonkey.dougb.nflcrimewatch.data.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "arrests")
public class Arrests {
    @PrimaryKey(autoGenerate = true) private int id;
    @ColumnInfo(name = "updated_at") private Date updatedAt;
    private int arrestStatsId;
    private Date date;
    private String team;
    private int logo;
    private String teamName;
    private String teamPreferredName;
    private String teamCity;
    private String teamConference;
    private String teamDivision;
    private String teamHexColor;
    private String teamHexAltColor;
    private String playerName;
    private String playerPosition;
    private String playerPositionName;
    private String playerPositionType;
    private String encounter;
    private String category;
    private String crimeCategoryColor;
    private String description;
    private String resolution;

    @Ignore
    public Arrests(int arrestStatsId, Date date, String team, int logo, String teamName,
                   String teamPreferredName, String teamCity, String teamConference,
                   String teamDivision, String teamHexColor, String teamHexAltColor,
                   String playerName, String playerPosition, String playerPositionName,
                   String playerPositionType, String encounter, String category,
                   String crimeCategoryColor, String description, String resolution,
                   Date updatedAt) {
        this.arrestStatsId = arrestStatsId;
        this.date = date;
        this.team = team;
        this.logo = logo;
        this.teamName = teamName;
        this.teamPreferredName = teamPreferredName;
        this.teamCity = teamCity;
        this.teamConference = teamConference;
        this.teamDivision = teamDivision;
        this.teamHexColor = teamHexColor;
        this.teamHexAltColor = teamHexAltColor;
        this.playerName = playerName;
        this.playerPosition = playerPosition;
        this.playerPositionName = playerPositionName;
        this.playerPositionType = playerPositionType;
        this.encounter = encounter;
        this.category = category;
        this.crimeCategoryColor = crimeCategoryColor;
        this.description = description;
        this.resolution = resolution;
        this.updatedAt = updatedAt;
    }

    public Arrests(int id, int arrestStatsId, Date date, String team, int logo, String teamName,
                   String teamPreferredName, String teamCity, String teamConference,
                   String teamDivision, String teamHexColor, String teamHexAltColor,
                   String playerName, String playerPosition, String playerPositionName,
                   String playerPositionType, String encounter, String category,
                   String crimeCategoryColor, String description, String resolution,
                   Date updatedAt) {
        this.id = id;
        this.arrestStatsId = arrestStatsId;
        this.date = date;
        this.team = team;
        this.logo = logo;
        this.teamName = teamName;
        this.teamPreferredName = teamPreferredName;
        this.teamCity = teamCity;
        this.teamConference = teamConference;
        this.teamDivision = teamDivision;
        this.teamHexColor = teamHexColor;
        this.teamHexAltColor = teamHexAltColor;
        this.playerName = playerName;
        this.playerPosition = playerPosition;
        this.playerPositionName = playerPositionName;
        this.playerPositionType = playerPositionType;
        this.encounter = encounter;
        this.category = category;
        this.crimeCategoryColor = crimeCategoryColor;
        this.description = description;
        this.resolution = resolution;
        this.updatedAt = updatedAt;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getArrestStatsId() {return arrestStatsId;}
    public void setArrestStatsId(int arrestStatsId) {this.arrestStatsId = arrestStatsId;}

    public Date getDate() {return date;}
    public void setDate(Date date) {this.date = date;}

    public String getTeam() {return team;}
    public void setTeam(String team) {this.team = team;}

    public int getLogo() {return logo;}
    public void setLogo(int logo) {this.logo = logo;}

    public String getTeamName() {return teamName;}
    public void setTeamName(String teamName) {this.teamName = teamName;}

    public String getTeamPreferredName() {return teamPreferredName;}
    public void setTeamPreferredName(String teamPreferredName) {this.teamPreferredName = teamPreferredName;}

    public String getTeamCity() {return teamCity;}
    public void setTeamCity(String teamCity) {this.teamCity = teamCity;}

    public String getTeamConference() {return teamConference;}
    public void setTeamConference(String teamConference) {this.teamConference = teamConference;}

    public String getTeamDivision() {return teamDivision;}
    public void setTeamDivision(String teamDivision) {this.teamDivision = teamDivision;}

    public String getTeamHexColor() {return teamHexColor;}
    public void setTeamHexColor(String teamHexColor) {this.teamHexColor = teamHexColor;}

    public String getTeamHexAltColor() {return teamHexAltColor;}
    public void setTeamHexAltColor(String teamHexAltColor) {this.teamHexAltColor = teamHexAltColor;}

    public String getPlayerName() {return playerName;}
    public void setPlayerName(String playerName) {this.playerName = playerName;}

    public String getPlayerPosition() {return playerPosition;}
    public void setPlayerPosition(String playerPosition) {this.playerPosition = playerPosition;}

    public String getPlayerPositionName() {return playerPositionName;}
    public void setPlayerPositionName(String playerPositionName) {this.playerPositionName = playerPositionName;}

    public String getPlayerPositionType() {return playerPositionType;}
    public void setPlayerPositionType(String playerPositionType) {this.playerPositionType = playerPositionType;}

    public String getEncounter() {return encounter;}
    public void setEncounter(String encounter) {this.encounter = encounter;}

    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}

    public String getCrimeCategoryColor() {return crimeCategoryColor;}
    public void setCrimeCategoryColor(String crimeCategoryColor) {this.crimeCategoryColor = crimeCategoryColor;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getResolution() {return resolution;}
    public void setResolution(String resolution) {this.resolution = resolution;}

    public Date getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(Date updatedAt) {this.updatedAt = updatedAt;}
}
