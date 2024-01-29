public class Boat {

    private final int id;
    private final String name;
    private final String type;
    private final double peso;
    private final double slore;
    private int cap;
    private final int n_comp;
    private final String state;
    private final int v;
    private final String center;

    public Boat(int id, String name, String type, double peso, double slore, int cap, int n_comp, String state, int v, String center) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.peso = peso;
        this.slore = slore;
        this.cap = cap;
        this.n_comp = n_comp;
        this.state = state;
        this.v = v;
        this.center = center;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public double getPeso() { return peso;}
    public double getSlore() { return slore;}
    public int getCapacity() {
        return cap;
    }
    public int getCompetitions() {
        return n_comp;
    }
    public String getState() {
        return state;
    }
    public int getVelocity() {
        return v;
    }
    public String getCenter() {
        return center;
    }

    public void setCapacity(int capacity) {
        this.cap = capacity;
    }

}