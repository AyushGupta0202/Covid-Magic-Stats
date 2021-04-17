package com.eggdevs.covidquerytester;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

// Custom array adapter class to populate views with covid data in the list view.
public class CovidAdapter extends ArrayAdapter<PeopleData> {

    public CovidAdapter(@NonNull Context context, List<PeopleData> peopleDataList) {
        super(context, 0, peopleDataList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);
        }

        // Get data for current state data.
        PeopleData currentPeopleData = getItem(position);

        // State Name.
        TextView stateView = listItemView.findViewById(R.id.state_name);
        stateView.setText(currentPeopleData.getStateName());

        // Confirmed cases.
        TextView confirmedView = listItemView.findViewById(R.id.confirmed_cases);
        confirmedView.setText(currentPeopleData.getConfirmedCases());

        // Active cases.
        TextView activeView = listItemView.findViewById(R.id.active_cases);
        activeView.setText(currentPeopleData.getActiveCases());

        // People recovered.
        TextView recoverView = listItemView.findViewById(R.id.people_recovered);
        recoverView.setText(currentPeopleData.getPeopleRecovered());

        // People died.
        TextView deathView = listItemView.findViewById(R.id.people_died);
        deathView.setText(currentPeopleData.getPeopleDied());

        // Last updated.
        TextView timeView = listItemView.findViewById(R.id.last_updated);
        timeView.setText(currentPeopleData.getLastUpdatedTime());

        return listItemView;
    }
}
