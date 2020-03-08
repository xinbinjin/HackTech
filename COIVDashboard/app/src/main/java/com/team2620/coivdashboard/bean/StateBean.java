package com.team2620.coivdashboard.bean;

public class StateBean {

    /**
     * _id : Tibet
     * totalConfirmed : 1
     * totalDeaths : 0
     * totalRecovered : 1
     */

    private String _id;
    private int totalConfirmed;
    private int totalDeaths;
    private int totalRecovered;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getTotalConfirmed() {
        return totalConfirmed;
    }

    public void setTotalConfirmed(int totalConfirmed) {
        this.totalConfirmed = totalConfirmed;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(int totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public int getTotalRecovered() {
        return totalRecovered;
    }

    public void setTotalRecovered(int totalRecovered) {
        this.totalRecovered = totalRecovered;
    }
}
