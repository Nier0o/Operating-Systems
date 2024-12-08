import java.util.*;

public class NonPreemptiveSJFScheduler implements Scheduler {
    @Override
    public void schedule()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int processesNumber = scanner.nextInt();

        System.out.print("Enter context switch time: ");
        int contextSwitch = scanner.nextInt();

        System.out.print("Enter aging factor (handling starvation): ");
        int agingFactor = scanner.nextInt();

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

        processes.sort(Process.SJFComparator);

        List<Process> executionOrder = new ArrayList<>();
        int currentTime = 0;
        double totalWaitingTime = 0, totalTurnaroundTime = 0;
        boolean isFirst = true;
        System.out.println("\nExecution Order:");

        while (!processes.isEmpty()) {
            List<Process> availableProcesses = new ArrayList<>();
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime) {
                    availableProcesses.add(p);
                }
            }

            if (availableProcesses.isEmpty()) {
                currentTime++;
                continue;
            }

            if (agingFactor > 0) {
                for (Process p : availableProcesses) {
                    int waitingTime = currentTime - p.arrivalTime;
                    p.effectiveBurstTime = p.burstTime - ((double) waitingTime / agingFactor);
                    if (p.effectiveBurstTime < 1) p.effectiveBurstTime = 1; // Avoid zero or negative burst time
                }
            }

            availableProcesses.sort(Process.SJFComparator);

            Process currentProcess = availableProcesses.getFirst();

            processes.remove(currentProcess);

            if (!isFirst) {
                currentTime += contextSwitch;
                System.out.println("Context Switch");
            }

            System.out.println(currentProcess.processName + " executed from " + currentTime + " to " + (currentTime + currentProcess.burstTime));
            currentProcess.waitingTime = currentTime - currentProcess.arrivalTime;
            currentProcess.turnaroundTime = currentProcess.waitingTime + currentProcess.burstTime;

            totalWaitingTime += currentProcess.waitingTime;
            totalTurnaroundTime += currentProcess.turnaroundTime;

            currentTime += currentProcess.burstTime;
            executionOrder.add(currentProcess);
            isFirst = false;
        }

        System.out.println("\nMetrics:");
        for (Process p : executionOrder) {
            System.out.println(p.processName + " - Waiting Time: " + p.waitingTime + ", Turnaround Time: " + p.turnaroundTime);
        }
        System.out.println("Average Waiting Time: " + (totalWaitingTime / processesNumber));
        System.out.println("Average Turnaround Time: " + (totalTurnaroundTime / processesNumber));
    }
}
