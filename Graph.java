import java.util.*;

/**
 * The Graph class represents a network of airports and routes between them.
 * It allows for the addition of airports and routes, as well as searching for the shortest path
 * between airports using Dijkstra's algorithm based on various criteria (cost, distance, or time).
 */
class Graph {
    private Map<Integer, Airport> airports;  // Map of airports by their ID
    private List<Route> routes;  // List of all routes (edges) between airports

    /**
     * Constructor to initialize the Graph with empty airports and routes.
     */
    public Graph() {
        airports = new HashMap<>();
        routes = new ArrayList<>();
    }

    /**
     * Adds an airport to the graph.
     *
     * @param id The unique identifier for the airport.
     */
    public void addAirport(int id) {
        airports.put(id, new Airport(id));
    }

    /**
     * Adds a route between two airports in the graph.
     *
     * @param startId      The ID of the starting airport.
     * @param destinationId The ID of the destination airport.
     * @param distance      The distance of the route.
     * @param travelTime    The travel time of the route.
     * @param cost          The cost of the route.
     */
    public void addRoute(int startId, int destinationId, int distance, int travelTime, int cost) {
        Route route = new Route(startId, destinationId, distance, travelTime, cost);
        airports.get(startId).addRoute(route);  // Add route to start airport
        routes.add(route);  // Add route to the overall list of routes
    }

    /**
     * Gets a list of all airports in the graph.
     *
     * @return A list of all airports.
     */
    public List<Airport> getAirports() {
        return new ArrayList<>(airports.values());
    }

    /**
     * Gets a list of all routes in the graph.
     *
     * @return A list of all routes.
     */
    public List<Route> getAllRoutes() {
        return routes;
    }

    /**
     * Implements Dijkstra's algorithm to find the shortest path between two airports
     * based on a specified criterion (cost, distance, or time).
     *
     * @param startId      The ID of the starting airport.
     * @param destinationId The ID of the destination airport.
     * @param criterion     The criterion for finding the shortest path ("cost", "distance", or "time").
     * @return A list of airport IDs representing the shortest path from start to destination.
     */
    public List<Integer> dijkstra(int startId, int destinationId, String criterion) {
        Map<Integer, Integer> dist = new HashMap<>();  // Map to store the shortest distances
        Map<Integer, Integer> prev = new HashMap<>();  // Map to store the previous node in the path
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));  // Priority queue for exploring nodes
        Set<Integer> visited = new HashSet<>();

        // Initialize distances to infinity for all airports
        for (int id : airports.keySet()) {
            dist.put(id, Integer.MAX_VALUE);
        }

        dist.put(startId, 0);  // Distance to start airport is zero
        pq.add(new int[]{startId, 0});  // Add start airport to the queue

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int airportId = current[0];
            int currentDist = current[1];

            if (!visited.add(airportId)) continue;  // Skip if already visited

            // Check each route (neighbor) from the current airport
            for (Route route : airports.get(airportId).getRoutes()) {
                int neighborId = route.getDestinationId();
                int newDist;

                // Choose the weight (distance, time, or cost) based on the criterion
                if (criterion.equals("distance")) {
                    newDist = currentDist + route.getDistance();
                } else if (criterion.equals("time")) {
                    newDist = currentDist + route.getTravelTime();
                } else {
                    newDist = currentDist + route.getCost();
                }

                // If the new distance is shorter, update the distance and the path
                if (newDist < dist.get(neighborId)) {
                    dist.put(neighborId, newDist);
                    prev.put(neighborId, airportId);
                    pq.add(new int[]{neighborId, newDist});
                }
            }
        }

        // Backtrack to get the shortest path
        List<Integer> path = new ArrayList<>();
        if (dist.get(destinationId) == Integer.MAX_VALUE) {
            return path;  // No path found
        }

        for (Integer at = destinationId; at != null; at = prev.get(at)) {
            path.add(at);
        }

        Collections.reverse(path);  // Reverse to get the path from start to destination
        return path;
    }
}
