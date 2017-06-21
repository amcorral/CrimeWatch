package itp341.corral.andrew.crimewatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.util.List;

import itp341.corral.andrew.crimewatch.Activities.DistrictMapActivity;
import itp341.corral.andrew.crimewatch.Activities.ReportBreakdown;
import itp341.corral.andrew.crimewatch.Activities.YourNeighborhoodActivity;
import itp341.corral.andrew.crimewatch.Models.Neighborhood;
import itp341.corral.andrew.crimewatch.Models.PoliceReport;


public class NeighborhoodActivityFragment extends Fragment {

    public static final String ARGS_POSITION = "args_position";
    private static final String TAG = NeighborhoodActivityFragment.class.getSimpleName();
    private final String URL = "https://data.sfgov.org/resource/PdId.json";

    TextView neighborhoodName;
    TextView neighborhoodNumIncidents;
    TextView neighborhoodRating;
    ImageView neighborhoodImage;
    ImageView reports;
    TextView viewReportsTV;
    ImageView mapIcon;
    ImageView homeIcon;

    List<Neighborhood> neighborhoodList;

    public Neighborhood n;
    int neighborhoodPosition;
    RequestQueue queue;


    public NeighborhoodActivityFragment () { }

    public static NeighborhoodActivityFragment newInstance(int position){
        Bundle args = new Bundle();
        args.putInt(NeighborhoodActivityFragment.ARGS_POSITION, position);

        NeighborhoodActivityFragment naf = new NeighborhoodActivityFragment();
        naf.setArguments(args);

        return naf;
    }

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_neighborhood, container, false);

        neighborhoodList = NeighborhoodSingleton.get(getActivity()).getNeighborhoodArrayList();
        neighborhoodPosition = getArguments().getInt(ARGS_POSITION);
        n = neighborhoodList.get(neighborhoodPosition);

        neighborhoodName = (TextView) v.findViewById(R.id.neighborhoodNameTV);
        neighborhoodNumIncidents = (TextView) v.findViewById(R.id.numIncidentsTV);
        neighborhoodRating = (TextView) v.findViewById(R.id.ratingTV);
        neighborhoodImage = (ImageView) v.findViewById(R.id.neighborhoodImage);
        mapIcon = (ImageView) v.findViewById(R.id.mapNavButton);
        homeIcon = (ImageView) v.findViewById(R.id.houseNavButton);
        reports = (ImageView) v.findViewById(R.id.reportsImage);
        viewReportsTV = (TextView) v.findViewById(R.id.viewReportsTV);

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

        reports.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ReportBreakdown.class);
                i.putExtra(ReportBreakdown.NEIGHBORHOOD_INTENT, neighborhoodPosition);
                startActivityForResult(i, 0);
            }
        });

        mapIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DistrictMapActivity.class);
                startActivity(i);
            }
        });

        homeIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), YourNeighborhoodActivity.class);
                startActivityForResult(i, 1);
            }
        });


        return v;

    }
}
