import java.util.*;

public class Global {
    public static List<Center> centers;
    public static Set<String> boatTypes = new HashSet<>(Arrays.asList("Windsurf", "Optimist", "Laser", "Patí Català", "HobieDragoon", "HobieCat"));

    public Global() {
        Global.centers = new ArrayList<>();
    }

    public static void setCenters(List<Center> centers) {
        Global.centers = centers;
    }
}
