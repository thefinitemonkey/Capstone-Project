package com.finitemonkey.dougb.nflcrimewatch.data.tables;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "stadiums")
public class Stadiums {
    @PrimaryKey(autoGenerate = true) private int id;
    private String teamID;
    private String teamName;
    private String stadium;
    private Double lat;
    private Double lon;

    @Ignore
    public Stadiums(String teamID, String teamName, String stadium, Double lat, Double lon) {
        this.teamID = teamID;
        this.teamName = teamName;
        this.stadium = stadium;
        this.lat = lat;
        this.lon = lon;
    }

    public Stadiums(int id, String teamName, String stadium, Double lat, Double lon) {
        this.id = id;
        this.teamID = teamID;
        this.teamName = teamName;
        this.stadium = stadium;
        this.lat = lat;
        this.lon = lon;
    }

    public int getId() {return id;}
    public void setId(int id){this.id = id;}

    public String getTeamID() {return teamID;}
    public void setTeamID(String teamID) {this.teamID = teamID;}

    public String getTeamName() {return teamName;}
    public void setTeamName(String teamName) {this.teamName = teamName;}

    public String getStadium() {return stadium;}
    public void setStadium(String stadium) {this.stadium = stadium;}

    public Double getLat() {return lat;}
    public void setLat(Double lat) {this.lat = lat;}

    public Double getLon() {return lon;}
    public void setLon(Double lon) {this.lon = lon;}

    public static Stadiums[] populateStadiums() {
        return new Stadiums[] {
          new Stadiums("ARI", "Cardinals", "University of Phoenix", 33.5276247, -112.2647533),
          new Stadiums("ATL", "Falcons", "Mercedes-Benz Stadium", 35.3317873, -74.3281432),
          new Stadiums("BAL", "Ravens", "M&T Bank", 39.2779876, -76.6248984),
          new Stadiums("BUF", "Bills", "NewEra", 42.7736971, -78.7892101),
          new Stadiums("CAR", "Panthers", "Bank of America", 35.2258296, -80.8550314),
          new Stadiums("CHI", "Bears", "Soldier Field", 41.8623132, -87.6188824),
          new Stadiums("CIN", "Bengals", "Paul Brown", 39.0954576, -84.5182517),
          new Stadiums("CLE", "Browns", "FirstEnergy", 41.5060535, -81.7017421),
          new Stadiums("DAL", "Cowboys", "AT&T", 32.7472844,-97.0966879),
          new Stadiums("DEN", "Broncos", "Mile High", 39.7438895, -105.0223034),
          new Stadiums("DET", "Lions", "Ford", 42.3400064, -83.047797),
          new Stadiums("GB", "Packers", "Lambeau", 44.5013406, -88.0644023),
          new Stadiums("HOU", "Texans", "NRG", 29.6847219, -95.4129014),
          new Stadiums("IND", "Colts", "Lucas Oil", 39.7601007, -86.1660817),
          new Stadiums("JAC", "Jaguars", "TIAA Bank", 30.323972, -81.6394833),
          new Stadiums("KC", "Chiefs", "Arrowhead", 39.0489391, -94.4861097),
          new Stadiums("LA", "Rams", "Memorial Coliseum", 34.0140526, -118.2900694),
          new Stadiums("LAC", "Chargers", "StubHub Center", 33.8643777, -118.2633366),
          new Stadiums("MIA", "Dolphins", "Hard Rock", 25.9579665, -80.2410544),
          new Stadiums("MIN", "Vikings", "U.S. Bank", 44.9737514, -93.2600094),
          new Stadiums("NE", "Patriots", "Gillette", 42.0909458, -71.2665405),
          new Stadiums("NO", "Saints", "Mercedes-Benz Superdome", 29.951061, -90.0834382),
          new Stadiums("NYG", "Giants", "MetLife", 40.8128397, -74.0764031),
          new Stadiums("NYJ", "Jets", "MetLife", 40.8128397, -74.0764031),
          new Stadiums("OAK", "Raiders", "Oakland-Alameda County", 37.7515946, -122.2027398),
          new Stadiums("PHI", "Eagles", "Lincoln Financial", 39.9007674, -75.1696575),
          new Stadiums("PIT", "Steelers", "Heinz", 40.4467648, -80.0179543),
          new Stadiums("SEA", "Seahawks", "CenturyLink", 47.5951518, -122.3338334),
          new Stadiums("SF", "49ers", "Levi's", 37.4031966, -121.972001),
          new Stadiums("TB", "Buccaneers", "Raymond James", 27.9758691, -82.5055284),
          new Stadiums("TEN", "Titans", "Nissan", 36.166479, -86.7734838),
          new Stadiums("WAS", "Redskins", "FedEx", 38.9076433, -76.8667394)
        };
    }
}
