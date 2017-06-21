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
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.List;

import itp341.corral.andrew.crimewatch.Models.Neighborhood;
import itp341.corral.andrew.crimewatch.Models.PoliceReport;
import itp341.corral.andrew.crimewatch.Models.ReportCategory;
import itp341.corral.andrew.crimewatch.NeighborhoodSingleton;
import itp341.corral.andrew.crimewatch.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


public class ReportListActivity extends AppCompatActivity {

    public static final String NEIGHBORHOOD_POSITION = "neighborhood_position";
    public static final String REPORT_CATEGORY = "report_category";

    private static final String TAG = ReportListActivity.class.getSimpleName();
    private final String URL = "https://data.sfgov.org/resource/PdId.json";

    Intent i;
    public Neighborhood n;
    public ReportCategory rc;
    int position;
    String categoryName;

    ImageView homeIcon;
    ImageView mapIcon;
    TextView incidentCategoryBreakdown;
    TextView incidentNumberText;
    TextView incidentNumberTV;
    TextView incidentAddressText;
    TextView incidentAddressTV;
    TextView incidentResText;
    TextView incidentResTV;
    TextView incidentDescriptionText;
    TextView incidentDescriptionTV;
    TextView incidentDateText;
    TextView incidentDateTV;

    Context c;
    List<Neighborhood> neighborhoodList;
    List <PoliceReport> policeReportsList;
    ListView policeReportsListView;
    PoliceReportAdapter adapter;
    RequestQueue queue;

    private class PoliceReportAdapter extends ArrayAdapter<PoliceReport> {
        List<PoliceReport> reports;

        public PoliceReportAdapter(int content, List<PoliceReport> objects) {
            super(getApplicationContext(), content, objects);
            this.reports = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(
                        R.layout.layout_item3, null);

            }
                PoliceReport p = reports.get(position);
                TextView incidentNum = (TextView) convertView.findViewById(R.id.incidentNumTV);
                TextView incidentDate = (TextView) convertView.findViewById(R.id.incidentDateTV);
                incidentNum.setText("Incident number: " + String.valueOf(p.getIncidentNum()));
                incidentDate.setText(p.getDate());
                return convertView;
        }

    }

        public void RequestJSONParse(String reqURL){
        JsonArrayRequest req = new JsonArrayRequest(reqURL, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++){
                        JSONObject object = (JSONObject) response.get(i);

                        PoliceReport p = new PoliceReport();

                        String date = object.getString("date");
                        String fixedDate = date.substring(0, date.indexOf("T"));
                        p.setDate(fixedDate);
                        p.setDayOfWeek(object.getString("dayofweek"));
                        p.setCategory(object.getString("category"));
                        p.setAddress(object.getString("address"));
                        p.setDescription(object.getString("descript"));
                        p.setResolution(object.getString("resolution"));
                        p.setIncidentNum(Long.valueOf(object.getString("incidntnum")));
                        policeReportsList.add(p);

                        adapter.notifyDataSetChanged();

                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(req);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        i = getIntent();
        neighborhoodList = NeighborhoodSingleton.get(c).getNeighborhoodArrayList();
        position = i.getIntExtra(NEIGHBORHOOD_POSITION, -1);
        n = neighborhoodList.get(position);

        categoryName = i.getStringExtra(REPORT_CATEGORY);
        rc = new ReportCategory();
        rc.setCategoryName(categoryName);
        incidentCategoryBreakdown = (TextView) findViewById(R.id.incidentCategoryBreakdown);
        incidentCategoryBreakdown.setText("Looking at " + categoryName + " for " + n.getName());
        incidentNumberText = (TextView) findViewById(R.id.incidentNumberText);
        incidentNumberTV = (TextView) findViewById(R.id.incidentNumberTV);
        incidentAddressText = (TextView) findViewById(R.id.incidentAddressText);
        incidentAddressTV = (TextView) findViewById(R.id.incidentAddressTV);
        incidentResText = (TextView) findViewById(R.id.incidentResText);
        incidentResTV = (TextView) findViewById(R.id.incidentResTV);
        incidentDescriptionText = (TextView) findViewById(R.id.incidentDescriptionText);
        incidentDescriptionTV = (TextView) findViewById(R.id.incidentDescriptionTV);
        incidentDateText = (TextView) findViewById(R.id.incidentDateText);
        incidentDateTV = (TextView) findViewById(R.id.incidentDateTV);
        mapIcon = (ImageView) findViewById(R.id.mapNavButton);
        homeIcon = (ImageView) findViewById(R.id.houseNavButton);

        policeReportsList = rc.getReports();
        policeReportsListView = (ListView) findViewById(R.id.policeReportListView);
        adapter = new ReportListActivity.PoliceReportAdapter(android.R.layout.simple_list_item_1, policeReportsList);
        policeReportsListView.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date result = calendar.getTime();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = mdformat.format(result);

        String apiCompliantDate = "'" + currentDate + "T00:00:00'";
        String newURL = URL + "?$where=date>" + apiCompliantDate + " AND pddistrict='" + n.getName() + "' AND category='" + categoryName + "'&$limit=50000&$order=date DESC";
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

        policeReportsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                incidentNumberTV.setText(String.valueOf(policeReportsList.get(position).getIncidentNum()));
                incidentAddressTV.setText(policeReportsList.get(position).getAddress());
                incidentResTV.setText(policeReportsList.get(position).getResolution());
                incidentDescriptionTV.setText(policeReportsList.get(position).getDescription());
                incidentDateTV.setText(policeReportsList.get(position).getDate());
            }
        });

    }
}
