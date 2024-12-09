import java.util.*;

public class ShortestRemainingTimeFirstScheduler implements Scheduler{
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

        List<Process> executionOrder = new ArrayList<>();
        int currentTime = 0;
        double totalWaitingTime = 0, totalTurnaroundTime = 0;
        boolean isFirst = true;
        Process lastProcess = null;

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Process.SJFComparator);

        while(!processes.isEmpty() || !readyQueue.isEmpty())
        {
            for(Process p : new ArrayList<>(processes))
            {
                if(p.arrivalTime <= currentTime)
                {
                    readyQueue.add(p);
                    processes.remove(p);
                }
            }

            if(readyQueue.isEmpty())
            {
                currentTime++;
                continue;
            }

            if(agingFactor > 0)
            {
                for(Process p : readyQueue)
                {
                    int waitingTime = currentTime - p.waitingSince;
                    p.effectiveBurstTime = Math.max(1, p.burstTime - ((double) waitingTime / (double) agingFactor));
                }
            }

            Process currentProcess = readyQueue.poll();

            if(!isFirst && currentProcess != lastProcess)
            {
                currentTime += contextSwitch;
                for(Process p : readyQueue)
                {
                    p.waitingSince = Math.max(p.waitingSince, currentTime);
                }
                System.out.println("Context Switch");
            }

            System.out.println(currentProcess.processName + " executed at time " + currentTime);
            currentProcess.burstTime -= 1;
            currentTime += 1;
            lastProcess = currentProcess;

            if (currentProcess.burstTime == 0)
            {
                currentProcess.turnaroundTime = currentTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.originalBurstTime;
                totalWaitingTime += currentProcess.waitingTime;
                totalTurnaroundTime += currentProcess.turnaroundTime;
                executionOrder.add(currentProcess);
            }
            else
            {
                readyQueue.add(currentProcess);
            }

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
