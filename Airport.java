import java.util.ArrayList;
import java.util.List;

/**
 * The Airport class represents an airport in the flight planner graph.
 * Each airport has a unique identifier and can have multiple routes connected to it.
 */
class Airport {
    private int id;  // Unique identifier for each airport
    private List<Route> routes;  // List of routes (edges) connected to the airport

    /**
     * Constructor to initialize an airport with a unique id.
     *
     * @param id the unique identifier for this airport
     */
    public Airport(int id) {
        this.id = id;
        this.routes = new ArrayList<>();
    }

    /**
     * Adds a route (edge) to the airport.
     *
     * @param route the route to be added to this airport
     */
    public void addRoute(Route route) {
        routes.add(route);
    }

    /**
     * Gets the unique identifier of the airport.
     *
     * @return the id of the airport
     */
    public int getId() {
        return id;
    }

    /**
     * Gets all the routes associated with this airport.
     *
     * @return a list of routes connected to this airport
     */
    public List<Route> getRoutes() {
        return routes;
    }

    @Override
    public String toString() {
        return "Airport{" + "id=" + id + ", routes=" + routes + '}';
    }
}
