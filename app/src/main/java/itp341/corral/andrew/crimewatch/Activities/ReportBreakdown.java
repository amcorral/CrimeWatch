package itp341.corral.andrew.crimewatch.Activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import itp341.corral.andrew.crimewatch.Models.Neighborhood;
import itp341.corral.andrew.crimewatch.Models.PoliceReport;
import itp341.corral.andrew.crimewatch.Models.ReportCategory;
import itp341.corral.andrew.crimewatch.NeighborhoodSingleton;
import itp341.corral.andrew.crimewatch.R;

import static android.R.attr.category;


public class ReportBreakdown extends AppCompatActivity{

    private static final String TAG = ReportBreakdown.class.getSimpleName();
    private final String URL = "https://data.sfgov.org/resource/PdId.json";
    public final static String NEIGHBORHOOD_INTENT = "NEIGHBORHOOD_X";

    List<Neighborhood> neighborhoodList;
    List <ReportCategory> categoryList;
    ListView categoryListView;

    public Neighborhood n;
    int neighborhood_position;
    String categoryName;

    Context c;
    TextView header;
    Intent i;
    RequestQueue queue;
    CategoryAdapter adapter;
    ImageView mapIcon;
    ImageView homeIcon;

    private class CategoryAdapter extends ArrayAdapter<ReportCategory> {
        List<ReportCategory> categories;

        public CategoryAdapter(int content, List<ReportCategory> objects) {
            super(getApplicationContext(), content, objects);
            this.categories = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(
                        R.layout.layout_item2, null);
            }

            TextView categoryName = (TextView) convertView.findViewById(R.id.categoryTypeTV);

            ReportCategory crimeCategory= categories.get(position);
            categoryName.setText(crimeCategory.getCategoryName());

            return convertView;
        }

    }

    public void RequestJSONParse(String reqURL){
        JsonArrayRequest req = new JsonArrayRequest(reqURL, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++){
                        JSONObject object = (JSONObject) response.get(i);

                        categoryName = object.getString("category");
                        PoliceReport p = new PoliceReport();
                        Boolean foundMatch = false;

                        if (categoryList.size() ==0){
                            ReportCategory rc = new ReportCategory();
                            rc.setCategoryName(categoryName);
                            categoryList.add(rc);
                            foundMatch = true;
                        } else {
                            for (int i2 = 0; i2 < categoryList.size(); i2++) {
                                if (categoryName.equals(categoryList.get(i2).getCategoryName())) {
                                    categoryList.get(i2).setNumIncidents(categoryList.get(i2).getNumIncidents() + 1);
                                    foundMatch = true;
                                }
                            }
                        }
                        if (!foundMatch) {
                            ReportCategory rc = new ReportCategory();
                            rc.setCategoryName(categoryName);
                            rc.setNumIncidents(rc.getNumIncidents() + 1);
                            categoryList.add(rc);
                        }

                        adapter.notifyDataSetChanged();

                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(c,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(c,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(req);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighborhood_report_categories);


        i = getIntent();
        neighborhoodList = NeighborhoodSingleton.get(c).getNeighborhoodArrayList();
        neighborhood_position = i.getIntExtra(NEIGHBORHOOD_INTENT, -1);
        n = neighborhoodList.get(neighborhood_position);

        categoryList = n.getReportCategoryArrayList();
        categoryListView = (ListView) findViewById(R.id.reportCategoryList);
        adapter = new CategoryAdapter(android.R.layout.simple_list_item_1, categoryList);
        categoryListView.setAdapter(adapter);
        header = (TextView) findViewById(R.id.neighborhoodRC);
        mapIcon = (ImageView) findViewById(R.id.mapNavButton);
        homeIcon = (ImageView) findViewById(R.id.houseNavButton);

        header.setText("Reports for " + n.getName());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date result = calendar.getTime();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = mdformat.format(result);

        String apiCompliantDate = "'" + currentDate + "T00:00:00'";
        String newURL = URL + "?$where=date>" + apiCompliantDate + " AND pddistrict='" + n.getName() + "'&$limit=50000&$order=date DESC";
        queue = Volley.newRequestQueue(this);
        RequestJSONParse(newURL);

        mapIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DistrictMapActivity.class);
                startActivity(i);
            }
        });

        homeIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), YourNeighborhoodActivity.class);
                startActivityForResult(i, 1);
            }
        });

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ReportListActivity.class);
                i.putExtra(ReportListActivity.NEIGHBORHOOD_POSITION, neighborhood_position );
                i.putExtra(ReportListActivity.REPORT_CATEGORY, categoryList.get(position).getCategoryName());
                startActivityForResult(i, 0);
            }
        });
    }
}


