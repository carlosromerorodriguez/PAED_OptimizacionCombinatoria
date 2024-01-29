import org.jetbrains.annotations.NotNull;

import java.util.*;

// Classe auxiliar que representa una configuració per a facilitar la implementació de l'algorisme
public class Config implements Comparable<Config>{
    private final int[] config;
    private int level;
    private final boolean[] visitedCenters;
    private int minNumCenters;
    private int best;
    private final Set<String> availableBoatTypes;

    public Config() {
        config = new int[Global.centers.size()];
        level = 0;
        visitedCenters = new boolean[Global.centers.size()];
        minNumCenters = 1;
        availableBoatTypes = new HashSet<>(Global.boatTypes);
        best = Integer.MAX_VALUE;
    }
    public Config (@NotNull Config that) {
        config = that.config.clone();
        level = that.level;
        visitedCenters = that.visitedCenters.clone();
        minNumCenters = that.minNumCenters;
        availableBoatTypes = that.availableBoatTypes;
        best = that.best;
    }
    public List<Config> expand() {
        List<Config> successors = new ArrayList<>();
        for (int i = 0; i < Global.centers.size(); i++) {
            if (!visitedCenters[i]) {
                Config successor = new Config(this);

                //Añadir los tipos de barco que tiene ese centro
                for (int j = 0; j < Global.centers.get(i).getBoats().size(); j++) {
                    successor.availableBoatTypes.add(Global.centers.get(i).getBoats().get(j).getType());
                }

                if (successor.availableBoatTypes.size() == 6) {
                    successor.minNumCenters++;
                    if (successor.getCost() < best) {
                        best = successor.getCost();
                        System.out.println("MinNumberCenters: " + successor.minNumCenters+"\n");
                    }
                }
                successor.config[successor.level] = i;
                successor.level++;
                successor.visitedCenters[i] = true;

                successors.add(successor);
            }
        }
        return successors;
    }
    public boolean isFull() {
        return (availableBoatTypes.size() == 6);
    }
    public int getCost() {
        return minNumCenters;
    }
    public int estimate() {
        int estimation = 0;
        Map<String, Integer> missingBoatTypes = new HashMap<>();
        for (String type: Global.boatTypes) {
            if(!availableBoatTypes.contains(type)) {
                if (missingBoatTypes.containsKey(type)) {
                        missingBoatTypes.put(type, missingBoatTypes.get(type) + 1);
                } else {
                    missingBoatTypes.put(type, 1);
                }
            }
        }
        for (Integer count : missingBoatTypes.values()) {
            estimation += Math.ceil((double) count / 6);
        }
        return minNumCenters + estimation;
    }


    @Override
    public int compareTo(Config that) {
        return (this.estimate() - that.estimate());
    }
}

