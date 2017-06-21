package itp341.corral.andrew.crimewatch.Models;

import java.util.ArrayList;
import java.util.List;

import itp341.corral.andrew.crimewatch.Models.PoliceReport;

import static itp341.corral.andrew.crimewatch.R.drawable.reports;

public class ReportCategory {

    String categoryName;
    private List <PoliceReport> reports;
    int numIncidents;

    public ReportCategory (){
        super();
        reports = new ArrayList<PoliceReport>();
    }

    public ReportCategory(String name, ArrayList <PoliceReport> reports) {
        this.categoryName = name;
        this.reports = reports;
    }

    public void setCategoryName (String categoryName){
        this.categoryName = categoryName;
    }

    public String getCategoryName (){
        return categoryName;
    }

    public List <PoliceReport> getReports (){
        return reports;
    }

    public PoliceReport getReport(int position){
        return reports.get(position);
    }

    public void addReport (PoliceReport pr){
        reports.add(pr);
    }

    public int getNumIncidents (){
        return numIncidents;
    }

    public void setNumIncidents (int newNumIncidents){
        this.numIncidents = newNumIncidents;
    }

}
