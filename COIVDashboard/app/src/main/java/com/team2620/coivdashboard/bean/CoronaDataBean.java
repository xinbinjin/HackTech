package com.team2620.coivdashboard.bean;

import com.google.gson.annotations.SerializedName;

public class CoronaDataBean {

    /**
     * _id : 5e638c927c3e8c6ce81bd205
     * Province/State : Hubei
     * Country/Region : Mainland China
     * Last Update : 2020-03-06T14:23:04
     * Confirmed : 67592
     * Deaths : 2931
     * Recovered : 42033
     * coords : {"lat":30.9756,"lon":112.2707}
     */

    private String _id;
    @SerializedName("Province/State")
    private String _$ProvinceState96; // FIXME check this code
    @SerializedName("Country/Region")
    private String _$CountryRegion165; // FIXME check this code
    @SerializedName("Last Update")
    private String _$LastUpdate131; // FIXME check this code
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

    public String get_$ProvinceState96() {
        return _$ProvinceState96;
    }

    public void set_$ProvinceState96(String _$ProvinceState96) {
        this._$ProvinceState96 = _$ProvinceState96;
    }

    public String get_$CountryRegion165() {
        return _$CountryRegion165;
    }

    public void set_$CountryRegion165(String _$CountryRegion165) {
        this._$CountryRegion165 = _$CountryRegion165;
    }

    public String get_$LastUpdate131() {
        return _$LastUpdate131;
    }

    public void set_$LastUpdate131(String _$LastUpdate131) {
        this._$LastUpdate131 = _$LastUpdate131;
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
         * lat : 30.9756
         * lon : 112.2707
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
