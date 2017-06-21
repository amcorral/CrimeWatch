package itp341.corral.andrew.crimewatch.Models;


import java.util.ArrayList;
import java.util.List;


public class Neighborhood {

    String neighborhoodName;
    int numIncidents = 0;
    String rating;

    private List <PoliceReport> policeReportArrayList;
    private List <ReportCategory> reportCategoryArrayList;

    public Neighborhood () {
        super();
        policeReportArrayList = new ArrayList<PoliceReport>();
        reportCategoryArrayList = new ArrayList<ReportCategory>();
    }

    public Neighborhood (String neighborhoodName, int numIncidents) {
        this.neighborhoodName = neighborhoodName;
        this.numIncidents = numIncidents;
    }

    public String getName (){
        return neighborhoodName;
    }

    public void setName (String newNN){
        this.neighborhoodName = newNN;
    }

    public int getNumIncidents (){
        return numIncidents;
    }

    public void setNumIncidents (int newNumIncidents){
        this.numIncidents = newNumIncidents;
    }

    public String getRating(){
        return rating;
    }

    public void setRating (String newRating){
        this.rating = newRating;
    }

    public List<PoliceReport> getPRArrayList (){
        return policeReportArrayList;
    }

    public List<ReportCategory> getReportCategoryArrayList () { return reportCategoryArrayList;}

    public void addPoliceReport (PoliceReport p){
        policeReportArrayList.add(p);
    }

}
