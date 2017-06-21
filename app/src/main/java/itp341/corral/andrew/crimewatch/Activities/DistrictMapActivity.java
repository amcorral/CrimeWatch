package itp341.corral.andrew.crimewatch.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import itp341.corral.andrew.crimewatch.R;


public class DistrictMapActivity extends AppCompatActivity{
    ImageView homeIcon;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_map);

        ImageView imageContainer = (ImageView) findViewById(R.id.imageContainer);
        homeIcon = (ImageView) findViewById(R.id.houseNavButton);
        imageContainer.setImageResource(R.drawable.districtmap);

        homeIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), YourNeighborhoodActivity.class);
                startActivityForResult(i, 1);
            }
        });
    }
}
