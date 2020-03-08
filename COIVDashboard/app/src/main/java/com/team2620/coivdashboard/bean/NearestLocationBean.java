package com.team2620.coivdashboard.bean;

import com.google.gson.annotations.SerializedName;

public class NearestLocationBean {

    /**
     * _id : 5e638c927c3e8c6ce81bd228
     * Province/State : Liaoning
     * Country/Region : Mainland China
     * Last Update : 2020-03-03T14:33:03
     * Confirmed : 125
     * Deaths : 1
     * Recovered : 106
     * coords : {"lat":41.2956,"lon":122.6085}
     */

    private String _id;
    @SerializedName("Province/State")
    private String _$ProvinceState84; // FIXME check this code
    @SerializedName("Country/Region")
    private String _$CountryRegion101; // FIXME check this code
    @SerializedName("Last Update")
    private String _$LastUpdate43; // FIXME check this code
    private int Confirmed;
    private int Deaths;
    private int Recovered;
    private CoordsBean coords;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_$ProvinceState84() {
        return _$ProvinceState84;
    }

    public void set_$ProvinceState84(String _$ProvinceState84) {
        this._$ProvinceState84 = _$ProvinceState84;
    }

    public String get_$CountryRegion101() {
        return _$CountryRegion101;
    }

    public void set_$CountryRegion101(String _$CountryRegion101) {
        this._$CountryRegion101 = _$CountryRegion101;
    }

    public String get_$LastUpdate43() {
        return _$LastUpdate43;
    }

    public void set_$LastUpdate43(String _$LastUpdate43) {
        this._$LastUpdate43 = _$LastUpdate43;
    }

    public int getConfirmed() {
        return Confirmed;
    }

    public void setConfirmed(int Confirmed) {
        this.Confirmed = Confirmed;
    }

    public int getDeaths() {
        return Deaths;
    }

    public void setDeaths(int Deaths) {
        this.Deaths = Deaths;
    }

    public int getRecovered() {
        return Recovered;
    }

    public void setRecovered(int Recovered) {
        this.Recovered = Recovered;
    }

    public CoordsBean getCoords() {
        return coords;
    }

    public void setCoords(CoordsBean coords) {
        this.coords = coords;
    }

    public static class CoordsBean {
        /**
         * lat : 41.2956
         * lon : 122.6085
         */

        private double lat;
        private double lon;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }
    }
}
