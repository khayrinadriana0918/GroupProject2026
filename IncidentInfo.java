/**
 * IncidentInfo Class - Stores incident details
 * SWC3684 Project - Cybersecurity Incident Response System
 */
public class IncidentInfo
{
    private String incidentId;
    private String incidentType;
    private int severityLevel;
    private String reportDate;
    private int estimatedResolutionTime;
    private double impactCost;
    
    // Constructor
    public IncidentInfo(String incidentId, String incidentType, int severityLevel, 
                        String reportDate, int estimatedResolutionTime, double impactCost)
    {
        this.incidentId = incidentId;
        this.incidentType = incidentType;
        this.severityLevel = severityLevel;
        this.reportDate = reportDate;
        this.estimatedResolutionTime = estimatedResolutionTime;
        this.impactCost = impactCost;
    }
    
    // Getter methods
    public String getIncidentId() { return incidentId; }
    public String getIncidentType() { return incidentType; }
    public int getSeverityLevel() { return severityLevel; }
    public String getReportDate() { return reportDate; }
    public int getEstimatedResolutionTime() { return estimatedResolutionTime; }
    public double getImpactCost() { return impactCost; }
    
    // Display incident details
    public String toString()
    {
        return incidentId + " | " + incidentType + " | Severity: " + severityLevel + 
               " | RM " + String.format("%,.2f", impactCost);
    }
}
