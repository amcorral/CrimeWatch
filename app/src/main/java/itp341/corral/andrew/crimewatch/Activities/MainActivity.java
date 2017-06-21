package itp341.corral.andrew.crimewatch.Activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import itp341.corral.andrew.crimewatch.MainActivityFragment;
import itp341.corral.andrew.crimewatch.R;


public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCES =  "itp341.corral.andrew.crimewatch.app_prefs";
    public static final String PREF_NEIGHBORHOOD = "itp341.corral.andrew.crimewatch.neighborhood";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container2);

        if (f == null ) {
            f = MainActivityFragment.newInstance();
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container2, f);
        fragmentTransaction.commit();
    }
}

