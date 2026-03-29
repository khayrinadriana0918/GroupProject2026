import java.util.*;
import java.io.*;

/**
 * CyberIncidentSystem - Main System for Phase 3
 * SWC3684 Project - Cybersecurity Incident Response System
 */
public class CyberIncidentSystem
{
    // Data structures required by project
    private LinkedList<AnalystInfo> analystList;
    private Queue<AnalystInfo> internalQueue;
    private Queue<AnalystInfo> externalQueue;
    private Queue<AnalystInfo> criticalQueue;
    private Stack<AnalystInfo> resolvedStack;
    
    // Constructor
    public CyberIncidentSystem()
    {
        analystList = new LinkedList<>();
        internalQueue = new LinkedList<>();
        externalQueue = new LinkedList<>();
        criticalQueue = new LinkedList<>();
        resolvedStack = new Stack<>();
    }
    
    // ========== PHASE 1: READ FILE ==========
    
    public void readFile(String filename)
    {
        try {
            Scanner scanner = new Scanner(new File(filename));
            int count = 0;
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    parseLine(line);
                    count++;
                }
            }
            scanner.close();
            System.out.println("Loaded " + count + " incidents. Total analysts: " + analystList.size());
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    
    private void parseLine(String line)
    {
        String[] parts = line.split("\\|");
        // Format: analystId|analystName|expertiseArea|incidentId|incidentType|severity|date|time|cost
        
        String analystId = parts[0];
        String analystName = parts[1];
        String expertise = parts[2];
        String incId = parts[3];
        String incType = parts[4];
        int severity = Integer.parseInt(parts[5]);
        String date = parts[6];
        int time = Integer.parseInt(parts[7]);
        double cost = Double.parseDouble(parts[8]);
        
        // Create incident
        IncidentInfo incident = new IncidentInfo(incId, incType, severity, date, time, cost);
        
        // Find or create analyst
        AnalystInfo analyst = findAnalyst(analystId);
        if (analyst == null) {
            analyst = new AnalystInfo(analystId, analystName, expertise);
            analystList.add(analyst);
        }
        
        // Link incident to analyst (HAS-A relationship)
        analyst.addIncident(incident);
    }
    
    private AnalystInfo findAnalyst(String id)
    {
        for (AnalystInfo a : analystList) {
            if (a.getAnalystId().equals(id)) return a;
        }
        return null;
    }
    
    // ========== PHASE 2: ASSIGN TO QUEUES ==========
    
    public void assignToQueues()
    {
        boolean alternate = true;
        
        for (AnalystInfo analyst : analystList) {
            int incidentCount = analyst.getIncidentCount();
            
            if (incidentCount > 3) {
                // More than 3 incidents -> Critical Queue
                criticalQueue.add(analyst);
                System.out.println("Critical Queue: " + analyst.getAnalystName() + " (" + incidentCount + " incidents)");
            } else {
                // 3 or fewer incidents -> Alternate between Internal and External
                if (alternate) {
                    internalQueue.add(analyst);
                    System.out.println("Internal Queue: " + analyst.getAnalystName() + " (" + incidentCount + " incidents)");
                } else {
                    externalQueue.add(analyst);
                    System.out.println("External Queue: " + analyst.getAnalystName() + " (" + incidentCount + " incidents)");
                }
                alternate = !alternate;
            }
        }
        
        System.out.println("\nQueue Summary:");
        System.out.println("  Internal Queue: " + internalQueue.size() + " analysts");
        System.out.println("  External Queue: " + externalQueue.size() + " analysts");
        System.out.println("  Critical Queue: " + criticalQueue.size() + " analysts");
    }
    
    // ========== PHASE 3: PROCESS WITH STACK ==========
    
    public void processIncidents()
    {
        System.out.println("\n========== PHASE 3: PROCESSING INCIDENTS ==========");
        int batchNum = 1;
        
        while (!internalQueue.isEmpty() || !externalQueue.isEmpty() || !criticalQueue.isEmpty()) {
            
            System.out.println("\n--- BATCH " + batchNum + " ---");
            
            // Process 5 from Internal Queue
            processQueue(internalQueue, "Internal");
            
            // Process 5 from External Queue
            processQueue(externalQueue, "External");
            
            // Process 5 from Critical Queue
            processQueue(criticalQueue, "Critical");
            
            batchNum++;
        }
        
        // Display resolved stack (LIFO order)
        displayStack();
    }
    
    private void processQueue(Queue<AnalystInfo> queue, String queueName)
    {
        System.out.println("\nProcessing " + queueName + " Queue:");
        int processed = 0;
        
        while (!queue.isEmpty() && processed < 5) {
            // DEQUEUE in FIFO order
            AnalystInfo analyst = queue.poll();
            
            System.out.println("  Analyst: " + analyst.getAnalystName());
            System.out.println("    Expertise: " + analyst.getExpertiseArea());
            System.out.println("    Incidents to resolve: " + analyst.getIncidentCount());
            
            // Calculate total impact
            double total = 0;
            for (IncidentInfo inc : analyst.getIncidents()) {
                total += inc.getImpactCost();
                System.out.println("      - " + inc.getIncidentId() + ": RM " + inc.getImpactCost());
            }
            analyst.setTotalResolvedImpact(total);
            System.out.println("    Total Impact: RM " + total);
            
            // PUSH to STACK (LIFO)
            resolvedStack.push(analyst);
            System.out.println("    >> PUSHED to Stack (Stack size: " + resolvedStack.size() + ")");
            
            processed++;
        }
        
        if (processed == 0) {
            System.out.println("  No analysts in this queue");
        } else {
            System.out.println("  Processed " + processed + " analyst(s) from " + queueName + " Queue");
        }
    }
    
    private void displayStack()
    {
        System.out.println("\n========== RESOLVED STACK (LIFO ORDER) ==========");
        System.out.println("Most recent resolved analysts shown FIRST:\n");
        
        // Temporary stack to preserve order
        Stack<AnalystInfo> temp = new Stack<>();
        int num = 1;
        double grandTotal = 0;
        
        // Pop from stack to display (LIFO order)
        while (!resolvedStack.isEmpty()) {
            AnalystInfo a = resolvedStack.pop();
            temp.push(a);
            
            System.out.println(num + ". " + a.getAnalystName() + " (" + a.getAnalystId() + ")");
            System.out.println("   Expertise: " + a.getExpertiseArea());
            System.out.println("   Incidents resolved: " + a.getIncidentCount());
            System.out.printf("   Total Impact Cost: RM %,.2f\n", a.getTotalResolvedImpact());
            System.out.println();
            
            grandTotal += a.getTotalResolvedImpact();
            num++;
        }
        
        // Restore stack
        while (!temp.isEmpty()) {
            resolvedStack.push(temp.pop());
        }
        
        System.out.println("==========================================");
        System.out.printf("GRAND TOTAL IMPACT COST: RM %,.2f\n", grandTotal);
        System.out.println("==========================================");
    }
    
    // ========== MAIN METHOD ==========
    
    public static void main(String[] args)
    {
        CyberIncidentSystem system = new CyberIncidentSystem();
        
        // Phase 1: Read file
        System.out.println("PHASE 1: Loading data...");
        system.readFile("cyber_incidents.txt");
        
        // Phase 2: Assign to queues
        System.out.println("\nPHASE 2: Assigning to queues...");
        system.assignToQueues();
        
        // Phase 3: Process with stack
        system.processIncidents();
    }
}