import java.util.*;

public class NonPreemptivePriorityScheduler implements Scheduler {
    @Override
    public void schedule()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int processesNumber = scanner.nextInt();

        System.out.print("Enter context switch time: ");
        int contextSwitch = scanner.nextInt();

        List<Process> processes = new ArrayList<>();
        for(int i = 0;i < processesNumber;i++)
        {
            System.out.println("Enter details for Process " + (i + 1) + ":");
            System.out.print("Name: ");
            String name = scanner.next();
            System.out.print("Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Burst Time: ");
            int burstTime = scanner.nextInt();
            System.out.print("Priority: ");
            int priority = scanner.nextInt();
            System.out.print("Color: ");
            String color = scanner.next();

            processes.add(new Process(name, arrivalTime, burstTime, priority, color));
        }

        Collections.sort(processes);

        int currentTime = 0;
        double totalWaitingTime = 0, totalTurnaroundTime = 0;
        boolean isFirst = true;
        System.out.println("\nExecution Order:");
        for(Process p : processes)
        {
            if(currentTime < p.arrivalTime)
            {
                currentTime = p.arrivalTime;
            }
            else if(!isFirst)
            {
                currentTime += contextSwitch;
                System.out.println("Context Switch");
            }

            System.out.println(p.processName + " executed from " + currentTime + " to " + (currentTime + p.burstTime));
            p.waitingTime = currentTime - p.arrivalTime;
            p.turnaroundTime = p.waitingTime + p.burstTime;

            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnaroundTime;

            currentTime += p.burstTime;
            isFirst = false;
        }
        System.out.println("\nMetrics:");
        for (Process p : processes) {
            System.out.println(p.processName + " - Waiting Time: " + p.waitingTime + ", Turnaround Time: " + p.turnaroundTime);
        }

        System.out.println("Average Waiting Time: " + (totalWaitingTime / processesNumber));
        System.out.println("Average Turnaround Time: " + (totalTurnaroundTime / processesNumber));
    }
}
