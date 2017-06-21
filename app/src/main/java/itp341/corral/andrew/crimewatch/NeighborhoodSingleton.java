package itp341.corral.andrew.crimewatch;

import android.content.Context;
import java.util.ArrayList;

import itp341.corral.andrew.crimewatch.Models.Neighborhood;

public class NeighborhoodSingleton {

    private ArrayList <Neighborhood> neighborhoodArrayList;
    private Context context;
    private static NeighborhoodSingleton singleton;

    private NeighborhoodSingleton (Context c){
        context = c;
        neighborhoodArrayList = new ArrayList<>();

    }

    public static NeighborhoodSingleton get(Context c){
        if (singleton == null) {
            singleton = new NeighborhoodSingleton(c);
        }
        return singleton;
    }

    public ArrayList<Neighborhood> getNeighborhoodArrayList () {
        return neighborhoodArrayList;
    }

    public void addNeighborhood (Neighborhood n){
        neighborhoodArrayList.add(n);
    }

    public void removeNeighborhood (int position){
        if (position >=0 && position < neighborhoodArrayList.size()) {
            neighborhoodArrayList.remove(position);
        }
    }

    public void updateNeighborhood (Neighborhood n, int position){
        if (position >= 0 && position < neighborhoodArrayList.size()) {
            neighborhoodArrayList.set(position, n);
        }
    }


}
