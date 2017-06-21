package itp341.corral.andrew.crimewatch.Activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.List;

import itp341.corral.andrew.crimewatch.MainActivityFragment;
import itp341.corral.andrew.crimewatch.Models.Neighborhood;
import itp341.corral.andrew.crimewatch.NeighborhoodActivityFragment;
import itp341.corral.andrew.crimewatch.NeighborhoodSingleton;
import itp341.corral.andrew.crimewatch.R;


public class YourNeighborhoodActivity extends AppCompatActivity {

    public static final String PREFERENCES =  "itp341.corral.andrew.crimewatch.app_prefs";
    public static final String PREF_NEIGHBORHOOD = "itp341.corral.andrew.crimewatch.neighborhood";
    public static final String PREF_NEIGHBORHOOD_POSITION = "itp341.corral.andrew.crimewatch.neighborhood_position";
    private List<Neighborhood> neighborhoodList;
    Spinner neighborhoods;

    SharedPreferences prefs;
    String neighborhoodPref;
    SharedPreferences.Editor prefEditor;

    int spinPosition;
    Neighborhood n;

    ImageView mapIcon;
    TextView neighborhoodName;
    TextView neighborhoodNumIncidents;
    TextView neighborhoodRating;
    ImageView neighborhoodImage;
    ImageView reports;
    TextView viewReportsTV;

    public void getNeighborhoodName(){
        if (n.getName().equals("SOUTHERN")){
            neighborhoodImage.setImageResource(R.drawable.southern);
        }
        else if (n.getName().equals("CENTRAL")) {
            neighborhoodImage.setImageResource(R.drawable.central);
        }
        else if (n.getName().equals("NORTHERN")){
            neighborhoodImage.setImageResource(R.drawable.northern);
        }
        else if (n.getName().equals("TENDERLOIN")){
            neighborhoodImage.setImageResource(R.drawable.tenderloin);
        }
        else if (n.getName().equals("RICHMOND")){
            neighborhoodImage.setImageResource(R.drawable.richmond);
        }
        else if (n.getName().equals("PARK")){
            neighborhoodImage.setImageResource(R.drawable.haight);
        }
        else if (n.getName().equals("MISSION")){
            neighborhoodImage.setImageResource(R.drawable.mission);
        }
        else if (n.getName().equals("TARAVAL")){
            neighborhoodImage.setImageResource(R.drawable.sunset);
        }
        else if (n.getName().equals("BAYVIEW")){
            neighborhoodImage.setImageResource(R.drawable.bayview);
        }
        else if (n.getName().equals("INGLESIDE")){
            neighborhoodImage.setImageResource(R.drawable.ingleside);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_neighborhood);

        prefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        neighborhoodPref = prefs.getString(PREF_NEIGHBORHOOD, "default");
        spinPosition = prefs.getInt(PREF_NEIGHBORHOOD_POSITION, 0);

        neighborhoodList = NeighborhoodSingleton.get(getApplicationContext()).getNeighborhoodArrayList();
        neighborhoods = (Spinner) findViewById(R.id.neighborhoodSelect);
        neighborhoods.setSelection(spinPosition);
        mapIcon = (ImageView) findViewById(R.id.mapNavButton);
        neighborhoodName = (TextView) findViewById(R.id.neighborhoodNameTV);
        neighborhoodNumIncidents = (TextView) findViewById(R.id.numIncidentsTV);
        neighborhoodRating = (TextView) findViewById(R.id.ratingTV);
        neighborhoodImage = (ImageView) findViewById(R.id.neighborhoodImage);
        reports = (ImageView) findViewById(R.id.reportsImage);
        viewReportsTV = (TextView) findViewById(R.id.viewReportsTV);

        neighborhoods.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinPosition = position;
                n = neighborhoodList.get(spinPosition);
                prefEditor = prefs.edit();
                neighborhoodPref = neighborhoods.getSelectedItem().toString();
                prefEditor.putInt(PREF_NEIGHBORHOOD_POSITION, spinPosition);
                prefEditor.putString(PREF_NEIGHBORHOOD, neighborhoodPref);
                prefEditor.commit();

                if (!prefs.getString(PREF_NEIGHBORHOOD, "default").equals("default")){

                    if (n.getNumIncidents() > 1500 ){
                        n.setRating("D");
                    }
                    else if (n.getNumIncidents() < 1500 & n.getNumIncidents() > 1000) {
                        n.setRating("C");
                    }
                    else if (n.getNumIncidents() < 1000 & n.getNumIncidents() > 500) {
                        n.setRating("B");
                    }
                    else {
                        n.setRating("A");
                    }

                    neighborhoodName.setText(n.getName());
                    neighborhoodNumIncidents.setText("Number of incidents in this neighborhood: " + n.getNumIncidents());
                    neighborhoodRating.setText("Safety rating: " + n.getRating());
                    getNeighborhoodName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ReportBreakdown.class);
                i.putExtra(ReportBreakdown.NEIGHBORHOOD_INTENT, spinPosition);
                startActivityForResult(i, 0);
            }
        });

        mapIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DistrictMapActivity.class);
                startActivity(i);
            }
        });


    }
}


