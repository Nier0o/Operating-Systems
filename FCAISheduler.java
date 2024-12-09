import java.util.*;

public class FCAISheduler implements Scheduler{
    @Override
    public void schedule()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int processesNumber = scanner.nextInt();

        int lastArrivalTime = 0;
        int maxBurstTime = 0;

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
            System.out.print("Quantum: ");
            int quantum = scanner.nextInt();
            System.out.print("Color: ");
            String color = scanner.next();

            lastArrivalTime = Math.max(lastArrivalTime, arrivalTime);
            maxBurstTime = Math.max(maxBurstTime, burstTime);
            Process p = new Process(name, arrivalTime, burstTime, priority, color);
            p.setQuantum(quantum);
            processes.add(p);
        }

        double V1 = (double) lastArrivalTime / 10;
        double V2 = (double) maxBurstTime / 10;

        for(Process p : processes)
        {
            p.updateFcaiFactor(V1, V2);
        }

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingDouble((Process p) -> p.fcaiFactor)
                                                                        .thenComparingInt(p -> p.arrivalTime)
                                                                        .thenComparingInt(p -> p.priority));
        List<Process> executionOrder = new ArrayList<>();
        int currentTime = 0;
        double totalWaitingTime = 0, totalTurnaroundTime = 0;
        Process lastProcess = null;

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

            Process currentProcess = readyQueue.poll();

            if(lastProcess != null && currentProcess != lastProcess)
            {
                readyQueue.remove(lastProcess);
                System.out.printf("Time %d - %d %s preempts %s", currentTime - lastProcess.executionTime, currentTime, lastProcess.processName, currentProcess.processName);
                if(lastProcess.burstTime == 0)
                {
                    lastProcess.quantum += 2;
                }
                else
                {
                    lastProcess.quantum += (lastProcess.quantum - lastProcess.executionTime);
                }
                readyQueue.add(lastProcess);
            }

            if(!currentProcess.quantum40Done)
            {
                int quantum40Percent = (int) Math.ceil(currentProcess.quantum * 0.4);
                int executionTime = Math.min(quantum40Percent, currentProcess.burstTime);
                currentTime += executionTime;
                currentProcess.executionTime += executionTime;
                currentProcess.burstTime -= executionTime;
                currentProcess.quantum40Done = true;
                lastProcess = currentProcess;
            }
            else
            {
                currentTime++;
                currentProcess.executionTime++;
                currentProcess.burstTime--;
                currentProcess.updateFcaiFactor(V1, V2);
                lastProcess = currentProcess;
            }

            if(currentProcess.burstTime == 0)
            {
                currentProcess.waitingTime = currentTime - currentProcess.arrivalTime - currentProcess.originalBurstTime;
                currentProcess.turnaroundTime = currentTime - currentProcess.arrivalTime;
                totalWaitingTime += currentProcess.waitingTime;
                totalTurnaroundTime += currentProcess.turnaroundTime;

                executionOrder.add(currentProcess);
                System.out.printf("Time %d: %s finishes execution%n", currentTime, currentProcess.processName);
            }
            else
            {
                currentProcess.updateFcaiFactor(V1, V2);
                readyQueue.add(currentProcess);
                System.out.printf("Time %d: %s preempted, remaining burst time: %d%n", currentTime, currentProcess.processName, currentProcess.burstTime);
            }
        }

        System.out.println("\nMetrics:");
        for (Process p : executionOrder) {
            System.out.println(p.processName + " - Waiting Time: " + p.waitingTime + ", Turnaround Time: " + p.turnaroundTime);
        }
        System.out.println("Average Waiting Time: " + (totalWaitingTime / processesNumber));
        System.out.println("Average Turnaround Time: " + (totalTurnaroundTime / processesNumber));
    }
}
