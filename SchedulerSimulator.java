public class SchedulerSimulator {
    public static void main(String[] args)
    {
        Scheduler nonPreemptivePriorityScheduler = new NonPreemptivePriorityScheduler();

//        nonPreemptivePriorityScheduler.schedule();

        Scheduler nonPreemptiveSJFScheduler = new NonPreemptiveSJFScheduler();

//        nonPreemptiveSJFScheduler.schedule();

        Scheduler SRTFScheduler = new ShortestRemainingTimeFirstScheduler();

        SRTFScheduler.schedule();
    }
}
