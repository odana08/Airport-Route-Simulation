/**
 * The Route class represents a flight route between two airports.
 * It encapsulates the start and destination airport IDs, distance, travel time, and cost of the route.
 */
class Route {
    private int startId;  // Start airport ID
    private int destinationId;  // Destination airport ID
    private int distance;  // Distance of the route
    private int travelTime;  // Travel time in minutes
    private int cost;  // Cost of the route

    /**
     * Constructor to initialize a route with start airport ID, destination airport ID, distance, travel time, and cost.
     *
     * @param startId        The ID of the starting airport.
     * @param destinationId  The ID of the destination airport.
     * @param distance       The distance of the route in kilometers.
     * @param travelTime     The travel time of the route in minutes.
     * @param cost           The cost of the route.
     */
    public Route(int startId, int destinationId, int distance, int travelTime, int cost) {
        this.startId = startId;
        this.destinationId = destinationId;
        this.distance = distance;
        this.travelTime = travelTime;
        this.cost = cost;
    }

    /**
     * Gets the start airport ID.
     *
     * @return The ID of the starting airport.
     */
    public int getStartId() {
        return startId;
    }

    /**
     * Gets the destination airport ID.
     *
     * @return The ID of the destination airport.
     */
    public int getDestinationId() {
        return destinationId;
    }

    /**
     * Gets the distance of the route.
     *
     * @return The distance of the route in kilometers.
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Gets the travel time of the route.
     *
     * @return The travel time of the route in minutes.
     */
    public int getTravelTime() {
        return travelTime;
    }

    /**
     * Gets the cost of the route.
     *
     * @return The cost of the route.
     */
    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Route{" +
                "startId=" + startId +
                ", destinationId=" + destinationId +
                ", distance=" + distance +
                ", travelTime=" + travelTime +
                ", cost=" + cost +
                '}';
    }
}
