package itp341.corral.andrew.crimewatch.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import itp341.corral.andrew.crimewatch.Models.Neighborhood;
import itp341.corral.andrew.crimewatch.NeighborhoodActivityFragment;
import itp341.corral.andrew.crimewatch.R;


public class NeighborhoodActivity extends AppCompatActivity {

    private static final String TAG = NeighborhoodActivity.class.getSimpleName();

    public final static String NEIGHBORHOOD_INTENT = "NEIGHBORHOOD_X";

    public Neighborhood n;
    int position;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighborhood);

        Intent i = getIntent();
        position = i.getIntExtra(NEIGHBORHOOD_INTENT, -1);

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        if (f == null ) {
            f = NeighborhoodActivityFragment.newInstance(position);
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();
    }



}
