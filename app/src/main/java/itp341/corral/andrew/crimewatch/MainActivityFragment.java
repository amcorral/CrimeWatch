package itp341.corral.andrew.crimewatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

import itp341.corral.andrew.crimewatch.Activities.DistrictMapActivity;
import itp341.corral.andrew.crimewatch.Activities.NeighborhoodActivity;
import itp341.corral.andrew.crimewatch.Activities.YourNeighborhoodActivity;
import itp341.corral.andrew.crimewatch.Models.Neighborhood;


public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();
    private final String URL = "https://data.sfgov.org/resource/PdId.json";
    public static final String PREFERENCES =  "itp341.corral.andrew.crimewatch.app_prefs";
    public static final String PREF_NEIGHBORHOOD = "itp341.corral.andrew.crimewatch.neighborhood";

    private List<Neighborhood> neighborhoodList;

    ListView neighborhoodListview;
    RequestQueue queue;
    private MainActivityFragment.NeighborhoodAdapter adapter;

    TextView neighborhoodName;
    TextView numberIncidents;
    ImageView icon;
    TextView neighborhoodAppHeader;
    ImageView mapIcon;
    ImageView homeIcon;

    public MainActivityFragment() {
        // Required empty public constructor
    }


    public static MainActivityFragment newInstance() {
        Bundle args = new Bundle();

        MainActivityFragment mainFragment = new MainActivityFragment();
        mainFragment.setArguments(args);

        return mainFragment;
    }

    private class NeighborhoodAdapter extends ArrayAdapter<Neighborhood> {
        List<Neighborhood> neighborhoods;

        public NeighborhoodAdapter(int content, List<Neighborhood> objects) {
            super(getActivity(), content, objects);
            this.neighborhoods = objects;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.layout_item, null);
            }

            neighborhoodName = (TextView) convertView.findViewById(R.id.neighborhoodNameTV);
            numberIncidents = (TextView) convertView.findViewById(R.id.numIncidentsTV);
            icon = (ImageView) convertView.findViewById(R.id.neighborhoodImage);

            Neighborhood n = neighborhoods.get(position);
            neighborhoodName.setText(n.getName());
            numberIncidents.setText(Integer.toString(n.getNumIncidents()));
            icon.setImageResource(R.drawable.neighborhood);

            return convertView;
        }

    }

    public void RequestJSONParse(String reqURL) {
        JsonArrayRequest req = new JsonArrayRequest(reqURL, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = (JSONObject) response.get(i);

                        String neighborhoodName = object.getString("pddistrict");

                        Boolean foundMatch = false;

                        if (neighborhoodList.size() == 0) {
                            Neighborhood n = new Neighborhood();
                            n.setName(neighborhoodName);
                            n.setNumIncidents(n.getNumIncidents() + 1);
                            neighborhoodList.add(n);
                            foundMatch = true;
                        } else {
                            for (int i2 = 0; i2 < neighborhoodList.size(); i2++) {
                                if (neighborhoodName.equals(neighborhoodList.get(i2).getName())) {
                                    neighborhoodList.get(i2).setNumIncidents(neighborhoodList.get(i2).getNumIncidents() + 1);
                                    foundMatch = true;
                                }
                            }
                        }

                        if (!foundMatch) {
                            Neighborhood n = new Neighborhood();
                            n.setName(neighborhoodName);
                            n.setNumIncidents(n.getNumIncidents() + 1);
                            neighborhoodList.add(n);
                        }

                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(req);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        neighborhoodAppHeader = (TextView) v.findViewById(R.id.neighboorApp);
        neighborhoodListview = (ListView) v.findViewById(R.id.neighborhoodList);
        neighborhoodList = NeighborhoodSingleton.get(getActivity()).getNeighborhoodArrayList();
        adapter = new MainActivityFragment.NeighborhoodAdapter(android.R.layout.simple_list_item_1, neighborhoodList);
        neighborhoodListview.setAdapter(adapter);
        mapIcon = (ImageView) v.findViewById(R.id.mapNavButton);
        homeIcon = (ImageView) v.findViewById(R.id.houseNavButton);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date result = calendar.getTime();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = mdformat.format(result);
        String apiCompliantDate = "'" + currentDate + "T00:00:00'";
        String newURL = URL + "?$where=date>" + apiCompliantDate + "&$limit=50000&$order=date DESC";

        queue = Volley.newRequestQueue(getActivity());
        RequestJSONParse(newURL);

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

        neighborhoodListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), NeighborhoodActivity.class);
                i.putExtra(NeighborhoodActivity.NEIGHBORHOOD_INTENT, position);
                startActivityForResult(i, 0);
            }
        });

        return v;
    }
}
