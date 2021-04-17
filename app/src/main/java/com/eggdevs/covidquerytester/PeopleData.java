package com.eggdevs.covidquerytester;

// Model class.
public class PeopleData {
    private String activeCases, lastUpdatedTime, stateName, peopleRecovered, peopleDied, confirmedCases;

    public PeopleData(String activeCases, String lastUpdatedTime, String stateName, String peopleRecovered, String peopleDied, String confirmedCases) {
        this.activeCases = activeCases;
        this.lastUpdatedTime = lastUpdatedTime;
        this.stateName = stateName;
        this.peopleRecovered = peopleRecovered;
        this.peopleDied = peopleDied;
        this.confirmedCases = confirmedCases;
    }

    public String getActiveCases() {
        return activeCases;
    }

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public String getStateName() {
        return stateName;
    }

    public String getPeopleRecovered() {
        return peopleRecovered;
    }

    public String getPeopleDied() {
        return peopleDied;
    }

    public String getConfirmedCases() {
        return confirmedCases;
    }
}
