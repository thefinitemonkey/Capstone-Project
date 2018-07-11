package com.finitemonkey.dougb.nflcrimewatch.utils;

import com.finitemonkey.dougb.nflcrimewatch.R;

public class Logos {

    // This function takes the teamId and returns the appropriate logo resource
    public static int lookupIdByTeam(String teamId) {
        int logoAssetId;

        switch (teamId) {
            case "ARI": {
                logoAssetId = R.drawable.ic_cardinals;
                break;
            }
            case "ATL": {
                logoAssetId = R.drawable.ic_falcons;
                break;
            }
            case "BAL": {
                logoAssetId = R.drawable.ic_ravens;
                break;
            }
            case "BUF": {
                logoAssetId = R.drawable.ic_bills;
                break;
            }
            case "CAR": {
                logoAssetId = R.drawable.ic_panthers;
                break;
            }
            case "CHI": {
                logoAssetId = R.drawable.ic_bears;
                break;
            }
            case "CIN": {
                logoAssetId = R.drawable.ic_bengals;
                break;
            }
            case "CLE": {
                logoAssetId = R.drawable.ic_browns;
                break;
            }
            case "DAL": {
                logoAssetId = R.drawable.ic_cowboys;
                break;
            }
            case "DEN": {
                logoAssetId = R.drawable.ic_broncos;
                break;
            }
            case "DET": {
                logoAssetId = R.drawable.ic_lions;
                break;
            }
            case "GB": {
                logoAssetId = R.drawable.ic_packers;
                break;
            }
            case "HOU": {
                logoAssetId = R.drawable.ic_texans;
                break;
            }
            case "IND": {
                logoAssetId = R.drawable.ic_colts;
                break;
            }
            case "JAC": {
                logoAssetId = R.drawable.ic_jaguars;
                break;
            }
            case "KC": {
                logoAssetId = R.drawable.ic_chiefs;
                break;
            }
            case "LA": {
                logoAssetId = R.drawable.ic_rams;
                break;
            }
            case "LAC": {
                logoAssetId = R.drawable.ic_chargers;
                break;
            }
            case "MIA": {
                logoAssetId = R.drawable.ic_dolphins;
                break;
            }
            case "MIN": {
                logoAssetId = R.drawable.ic_vikings;
                break;
            }
            case "NE": {
                logoAssetId = R.drawable.ic_patriots;
                break;
            }
            case "NO": {
                logoAssetId = R.drawable.ic_saints;
                break;
            }
            case "NYG": {
                logoAssetId = R.drawable.ic_giants;
                break;
            }
            case "NYJ": {
                logoAssetId = R.drawable.ic_jets;
                break;
            }
            case "OAK": {
                logoAssetId = R.drawable.ic_raiders;
                break;
            }
            case "PHI": {
                logoAssetId = R.drawable.ic_eagles;
                break;
            }
            case "PIT": {
                logoAssetId = R.drawable.ic_steelers;
                break;
            }
            case "SEA": {
                logoAssetId = R.drawable.ic_seahawks;
                break;
            }
            case "SF": {
                logoAssetId = R.drawable.ic_49ers;
                break;
            }
            case "TB": {
                logoAssetId = R.drawable.ic_buccaneers;
                break;
            }
            case "TEN": {
                logoAssetId = R.drawable.ic_titans;
                break;
            }
            case "WAS": {
                logoAssetId = R.drawable.ic_redskins;
                break;
            }
            default: {
                logoAssetId = R.drawable.ic_nfl;
                break;
            }
        }

        return logoAssetId;
    }
}
