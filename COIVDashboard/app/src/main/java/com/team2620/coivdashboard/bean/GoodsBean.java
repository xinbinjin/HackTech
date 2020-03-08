package com.team2620.coivdashboard.bean;

import java.util.List;

public class GoodsBean {

    /**
     * keyword : surgical mask
     * std : 66.60364309469001
     * mean : 50.9665
     * min : 0.99
     * max : 505
     * time : ["2020-03-08T11:54:50.845Z"]
     */

    private String keyword;
    private double std;
    private double mean;
    private double min;
    private double max;
    private List<String> time;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public double getStd() {
        return std;
    }

    public void setStd(double std) {
        this.std = std;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }
}
