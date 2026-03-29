import java.util.ArrayList;
import java.util.List;

/**
 * AnalystInfo Class - Stores analyst information and their assigned incidents
 * SWC3684 Project - Cybersecurity Incident Response System
 * HAS-A relationship with IncidentInfo
 */
public class AnalystInfo
{
    private String analystId;
    private String analystName;
    private String expertiseArea;
    private List<IncidentInfo> incidents;  // HAS-A relationship
    private double totalResolvedImpact;
    
    // Constructor
    public AnalystInfo(String analystId, String analystName, String expertiseArea)
    {
        this.analystId = analystId;
        this.analystName = analystName;
        this.expertiseArea = expertiseArea;
        this.incidents = new ArrayList<>();
        this.totalResolvedImpact = 0.0;
    }
    
    // Getter methods
    public String getAnalystId() { return analystId; }
    public String getAnalystName() { return analystName; }
    public String getExpertiseArea() { return expertiseArea; }
    public List<IncidentInfo> getIncidents() { return incidents; }
    public double getTotalResolvedImpact() { return totalResolvedImpact; }
    
    // Setter for total resolved impact
    public void setTotalResolvedImpact(double total)
    {
        this.totalResolvedImpact = total;
    }
    
    // Add incident to analyst
    public void addIncident(IncidentInfo incident)
    {
        incidents.add(incident);
    }
    
    // Get number of incidents assigned
    public int getIncidentCount()
    {
        return incidents.size();
    }
    
    // Display analyst info
    public String toString()
    {
        return analystName + " (" + analystId + ") - " + incidents.size() + " incidents";
    }
}