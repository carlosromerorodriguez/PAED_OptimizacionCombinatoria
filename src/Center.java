import java.util.ArrayList;

public class Center {
    private ArrayList<Boat> boats;
    private String name;

    public Center(ArrayList<Boat> boats, String name) {
        this.boats = boats;
        this.name = name;
    }

    public ArrayList<Boat> getBoats() {
        return boats;
    }

    public void setBoats(ArrayList<Boat> boats) {
        this.boats = boats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean hasBoatType(String boatType) {
        for (Boat boat : boats) {
            if (boat.getType().equals(boatType)) {
                return true;
            }
        }
        return false;
    }

}
