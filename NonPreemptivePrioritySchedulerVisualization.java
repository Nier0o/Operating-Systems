import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NonPreemptivePrioritySchedulerVisualization extends JFrame {
    private final List<Process> processes;
    private final int contextSwitch;

    public NonPreemptivePrioritySchedulerVisualization(List<Process> processes, int contextSwitch) {
        this.processes = processes;
        this.contextSwitch = contextSwitch;

        setTitle("Process Scheduling Visualization");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new TimelinePanel());
    }

    class TimelinePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int x = 50;  // Start drawing from x=50
            int y = 100; // Y-coordinate for the timeline
            int barHeight = 30; // Height of each process bar
            int scale = 10; // Time scale (pixels per time unit)

            g.drawString("Time Scale: 1 unit = " + scale + " pixels", 50, 50);

            for (Process p : processes) {
                if (x < p.arrivalTime * scale) {
                    // Draw idle time (if any)
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(x, y, (p.arrivalTime - x / scale) * scale, barHeight);
                    x = p.arrivalTime * scale;
                }

                g.setColor(Color.decode(p.color)); // Use process color
                g.fillRect(x, y, p.burstTime * scale, barHeight); // Draw process execution
                g.setColor(Color.BLACK);
                g.drawRect(x, y, p.burstTime * scale, barHeight); // Outline the rectangle
                g.drawString(p.processName, x + 5, y + 20); // Label process name inside bar

                x += p.burstTime * scale;

                // Draw context switch (if not last process)
                if (!p.equals(processes.get(processes.size() - 1))) {
                    g.setColor(Color.RED);
                    g.fillRect(x, y, contextSwitch * scale, barHeight / 2);
                    x += contextSwitch * scale;
                }
            }
        }
    }

    public static void main(String[] args) {
        // Example processes (replace with your inputs)
        List<Process> processes = List.of(
                new Process("P1", 0, 4, 2, "#FF5733"),
                new Process("P2", 2, 6, 1, "#33FF57"),
                new Process("P3", 5, 2, 3, "#3357FF"),
                new Process("P4", 7, 3, 2, "#F3FF33")
        );

        int contextSwitch = 2;

        SwingUtilities.invokeLater(() -> new NonPreemptivePrioritySchedulerVisualization(processes, contextSwitch).setVisible(true));
    }
}

