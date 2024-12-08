import java.util.*;

public class Process implements Comparable<Process> {
    String processName;
    int arrivalTime;
    int burstTime;
    int priority;
    String color;
    int waitingTime = 0;
    int turnaroundTime = 0;
    int waitingSince;
    double effectiveBurstTime;

    public Process(String processName, int arrivalTime, int burstTime, int priority, String color)
    {
        this.processName = processName;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.color = color;
        this.waitingSince = arrivalTime;
        this.effectiveBurstTime = burstTime;
    }

    // SJF Comparator
    public static final Comparator<Process> SJFComparator = new Comparator<Process>() {
        @Override
        public int compare(Process p1, Process p2) {
            if (p1.effectiveBurstTime == p2.effectiveBurstTime) {
                return Integer.compare(p1.arrivalTime, p2.arrivalTime);
            }
            return Double.compare(p1.effectiveBurstTime, p2.effectiveBurstTime);
        }
    };

    // Default Comparator
    @Override
    public int compareTo(Process other)
    {
        if(this.arrivalTime == other.arrivalTime)
        {
            return Integer.compare(this.priority, other.priority);
        }
        return Integer.compare(this.arrivalTime, other.arrivalTime);
    }
}
