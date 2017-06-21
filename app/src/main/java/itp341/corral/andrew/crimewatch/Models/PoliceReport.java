package itp341.corral.andrew.crimewatch.Models;


public class PoliceReport {

    String date;
    String address;
    String resolution;
    Long incidentNum;
    String dayOfWeek;
    String time;

    String district;
    String category;
    String description;


    public PoliceReport () {
        super();
    }

    public PoliceReport (String date, String address, String resolution, Long incidentNum,
                         String dayOfWeek, String time, String district, String category, String description )
    {
        this.date = date;
        this.address = address;
        this.resolution = resolution;
        this.incidentNum = incidentNum;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.district = district;
        this.category = category;
        this.description = description;
    }

    public String getDate () { return date; }

    public void setDate( String newDate) { this.date = newDate; }

    public String getAddress() { return address; }

    public void setAddress(String newAddress) { this.address = newAddress; }

    public String getResolution() { return resolution; }

    public void setResolution(String newResolution ) { this.resolution = newResolution; }

    public Long getIncidentNum() { return incidentNum; }

    public void setIncidentNum(Long newIncidentNum) { this.incidentNum = newIncidentNum; }

    public String getDayOfWeek () { return dayOfWeek; }

    public void setDayOfWeek(String newDayOfWeek) { this.dayOfWeek = newDayOfWeek; }

    public String getTime () { return time; }

    public void setTime (String newTime ) { this.time = newTime; }

    public String getDistrict () { return district; }

    public void setDistrict (String newDistrict ) { this.district = newDistrict; }

    public String getCategory () { return category; }

    public void setCategory (String newCategory ) { this.category = newCategory; }

    public String getDescription () { return description; }

    public void setDescription (String newDescription ) { this.description = newDescription; }


}
