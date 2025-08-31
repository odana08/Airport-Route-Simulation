import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The FlightPlannerSwing class provides a graphical user interface (GUI) for planning flights
 * between airports using a graph representation. It allows users to select start and destination
 * airports and displays available routes along with details such as distance, travel time, and cost.
 */
public class FlightPlannerSwing extends JFrame {

    // Declare the main Graph that holds airports and routes
    private Graph graph;
    // JComboBoxes for selecting start and destination airports
    private JComboBox<Integer> startComboBox;
    private JComboBox<Integer> destinationComboBox;
    // JTextArea to display the results of route searches
    private JTextArea resultArea;
    // JTable to display route details
    private JTable routesTable;
    // Panel to visually represent the graph of airports and routes
    private GraphPanel graphPanel;
    // Random object for generating random airports and routes
    private Random random;

    /**
     * Constructor to set up the GUI and initialize components.
     * This initializes the JFrame, generates random airports and routes,
     * and sets up the UI components including labels, combo boxes, buttons,
     * and the graph panel.
     */
    public FlightPlannerSwing() {
        // Set title, size, default close operation, and initial location for the JFrame
        setTitle("Flight Planner");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        random = new Random();
        graph = new Graph();

        // Generate random airports and routes (between 4 and 6)
        generateRandomAirportsAndRoutes();

        // Create UI components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        // Label and ComboBox for selecting the start airport
        JLabel startLabel = new JLabel("Select Start Airport:");
        startComboBox = new JComboBox<>(getAirportIds());

        // Label and ComboBox for selecting the destination airport
        JLabel destinationLabel = new JLabel("Select Destination Airport:");
        destinationComboBox = new JComboBox<>(getAirportIds());

        // Button to trigger route search
        JButton findRoutesButton = new JButton("Find Routes");

        // TextArea for displaying the search results, wrapped in a scroll pane
        resultArea = new JTextArea(15, 30);
        resultArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultArea); // Scrollable JTextArea

        // Create a JTable for displaying route information (start, destination, distance, travel time, and cost)
        routesTable = new JTable(new DefaultTableModel(new Object[]{"Start", "Destination", "Distance", "Travel Time", "Cost"}, 0));
        JScrollPane tableScrollPane = new JScrollPane(routesTable); // Scrollable JTable
        tableScrollPane.setPreferredSize(new Dimension(600, 200)); // Fixed height for the table scroll

        // Add the components to the panel
        panel.add(startLabel);
        panel.add(startComboBox);
        panel.add(destinationLabel);
        panel.add(destinationComboBox);
        panel.add(findRoutesButton);
        panel.add(resultScrollPane); // Add the scrollable text area to the panel

        // Create a GraphPanel for visualizing the airports and routes and set its size
        graphPanel = new GraphPanel(graph);
        graphPanel.setPreferredSize(new Dimension(600, 400));

        // Add all components to the JFrame in their respective layout positions
        add(panel, BorderLayout.EAST); // Panel with controls on the right (east)
        add(graphPanel, BorderLayout.CENTER); // Graph visualization in the center
        add(tableScrollPane, BorderLayout.SOUTH); // Table of routes at the bottom

        // Action Listener to handle button click for finding routes
        findRoutesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findAndDisplayRoutes(); // Find routes when the button is clicked
                graphPanel.repaint(); // Repaint the graph panel to update the visualization
            }
        });

        // Print all routes to the console
        printAllRoutes();
    }

    /**
     * Generates random airports and routes to populate the graph.
     * Creates between 4 to 6 airports and connects them with random routes,
     * ensuring no duplicate routes are added.
     */
    private void generateRandomAirportsAndRoutes() {
        int numAirports = random.nextInt(3) + 4; // Randomly choose between 4 to 6 airports

        // Add airports to the graph
        for (int i = 0; i < numAirports; i++) {
            graph.addAirport(i);
        }

        List<String> existingRoutes = new ArrayList<>(); // Track already added routes to avoid duplicates

        // Randomly add routes between airports
        for (int i = 0; i < numAirports; i++) {
            int numRoutes = random.nextInt(3) + 2; // Randomly generate between 2 to 4 routes per airport
            for (int j = 0; j < numRoutes; j++) {
                int destination;
                do {
                    destination = random.nextInt(numAirports);
                } while (destination == i); // Ensure the destination is not the same as the current airport

                String routeId = i + "-" + destination; // Create a unique route identifier

                // Add the route if it doesn't already exist
                if (!existingRoutes.contains(routeId)) {
                    int distance = random.nextInt(500) + 100; // Random distance between 100 and 600
                    int travelTime = random.nextInt(180) + 30; // Random travel time between 30 and 210 minutes
                    int cost = random.nextInt(500) + 100; // Random cost between 100 and 600

                    graph.addRoute(i, destination, distance, travelTime, cost); // Add the route to the graph
                    existingRoutes.add(routeId); // Mark this route as added
                }
            }
        }

        System.out.println("Generated " + numAirports + " airports and random routes.");
    }

    /**
     * Retrieves airport IDs as an array of Integers for use in JComboBox.
     *
     * @return an array of airport IDs
     */
    private Integer[] getAirportIds() {
        List<Airport> airports = graph.getAirports();
        Integer[] ids = new Integer[airports.size()];
        for (int i = 0; i < airports.size(); i++) {
            ids[i] = airports.get(i).getId();
        }
        return ids;
    }

    /**
     * Finds and displays routes between the selected start and destination airports.
     * Updates the JTable with the matching routes and provides additional details about
     * the best routes based on cost, distance, and travel time.
     */
    private void findAndDisplayRoutes() {
        int startId = (int) startComboBox.getSelectedItem(); // Get selected start airport
        int destinationId = (int) destinationComboBox.getSelectedItem(); // Get selected destination airport

        DefaultTableModel model = (DefaultTableModel) routesTable.getModel();
        model.setRowCount(0); // Clear previous data in the table

        // If start and destination are the same, display a message and set all criteria to zero
        if (startId == destinationId) {
            model.addRow(new Object[]{
                startId,
                destinationId,
                0, // Distance
                0, // Travel Time
                0  // Cost
            });
            resultArea.setText("Start and destination cannot be the same. All criteria set to zero.");
            return;
        }

        StringBuilder result = new StringBuilder();

        // Loop through all routes and add matching ones to the table
        List<Route> allRoutes = graph.getAllRoutes();
        for (Route route : allRoutes) {
            if (route.getStartId() == startId && route.getDestinationId() == destinationId) {
                model.addRow(new Object[]{
                    startId,
                    route.getDestinationId(),
                    route.getDistance(),
                    route.getTravelTime(),
                    route.getCost()
                });
            }
        }

        // Find and display the best routes based on cost, distance, and travel time
        List<Integer> bestRouteByCost = graph.dijkstra(startId, destinationId, "cost");
        if (bestRouteByCost.isEmpty()) {
            result.append("No valid route found based on cost.\n");
        } else {
            result.append("Best route based on cost: ").append(bestRouteByCost).append("\n");
        }

        List<Integer> bestRouteByDistance = graph.dijkstra(startId, destinationId, "distance");
        if (bestRouteByDistance.isEmpty()) {
            result.append("No valid route found based on distance.\n");
        } else {
            result.append("Best route based on distance: ").append(bestRouteByDistance).append("\n");
        }

        List<Integer> bestRouteByTime = graph.dijkstra(startId, destinationId, "time");
        if (bestRouteByTime.isEmpty()) {
            result.append("No valid route found based on travel time.\n");
        } else {
            result.append("Best route based on travel time: ").append(bestRouteByTime).append("\n");
        }

        resultArea.setText(result.toString()); // Display the results in the text area
    }

    /**
     * Prints all the generated routes to the console for debugging.
     */
    private void printAllRoutes() {
        List<Route> allRoutes = graph.getAllRoutes();
        System.out.println("All possible routes:");
        for (Route route : allRoutes) {
            System.out.println("From Airport " + route.getStartId() + " to Airport " + route.getDestinationId() +
                    " - Distance: " + route.getDistance() +
                    " - Travel Time: " + route.getTravelTime() +
                    " - Cost: " + route.getCost());
        }
    }

    /**
     * GraphPanel is a JPanel that visualizes the graph of airports and routes.
     * It handles the drawing of airports and their connections based on the graph data.
     */
    class GraphPanel extends JPanel {
        private Graph graph;
        private Random random;

        /**
         * Constructor for the GraphPanel.
         *
         * @param graph The graph containing airports and routes to visualize.
         */
        public GraphPanel(Graph graph) {
            this.graph = graph;
            this.random = new Random();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawGraph(g);
        }

        /**
         * Draws the graph of airports and routes on the panel.
         *
         * @param g The Graphics object used for drawing.
         */
        private void drawGraph(Graphics g) {
            List<Airport> airports = graph.getAirports();

            int panelWidth = getWidth();
            int panelHeight = getHeight();

            // Generate random positions for each airport node
            int[][] positions = new int[airports.size()][2];
            for (int i = 0; i < airports.size(); i++) {
                positions[i][0] = random.nextInt(panelWidth - 50) + 25; // X coordinate
                positions[i][1] = random.nextInt(panelHeight - 50) + 25; // Y coordinate
            }

            // Draw routes (edges) between airports
            g.setColor(Color.GRAY);
            for (Airport airport : airports) {
                for (Route route : airport.getRoutes()) {
                    int startX = positions[airport.getId()][0];
                    int startY = positions[airport.getId()][1];
                    int endX = positions[route.getDestinationId()][0];
                    int endY = positions[route.getDestinationId()][1];
                    g.drawLine(startX, startY, endX, endY); // Draw the route line
                }
            }

            // Draw airports (nodes)
            g.setColor(Color.RED);
            for (int i = 0; i < airports.size(); i++) {
                int x = positions[i][0];
                int y = positions[i][1];
                g.fillOval(x - 5, y - 5, 10, 10); // Draw the airport node
                g.drawString("Airport " + i, x - 10, y - 10); // Label the airport
            }
        }
    }

    /**
     * Main method to launch the FlightPlannerSwing application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FlightPlannerSwing().setVisible(true);
            }
        });
    }
}
