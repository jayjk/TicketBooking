package com.test.ticketbooking.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Results {


    @SerializedName("results")
    private List<MasterData> results;

    public List<MasterData> getResults() {
        return results;
    }

    public void setResults(List<MasterData> results) {
        this.results = results;
    }
}