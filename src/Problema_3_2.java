import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Problema_3_2 {

    public int bestSize = Integer.MAX_VALUE;
    public void backTrackingConMarcaje(List<Center> centers, @NotNull Set<String> boatTypes, List<Center> selectedCenters, List<List<Center>> bestSolutions, int minBoats) {
        if (boatTypes.isEmpty()) {
            int currentBoats = 0;
            for (Center center : selectedCenters) {
                currentBoats += center.getBoats().size();
            }

            // Optimización
            if (selectedCenters.size() < bestSize && currentBoats < minBoats) {
                bestSolutions.clear();
                bestSolutions.add(selectedCenters);
                bestSize = selectedCenters.size();
                minBoats = currentBoats;
                System.out.println("\u001B[33mNUEVA SOLUCIÓN ÓPTIMA " + "num.centers (" + bestSize + ") num.Boats (" + currentBoats + ")\u001B[0m");
                for (Center center : selectedCenters) {
                    System.out.print("Optimal Centers-> " + center.getName() + ": ");
                    for (int i = 0; i < center.getBoats().size(); i++) {
                        Boat boat = center.getBoats().get(i);
                        System.out.print(boat.getName() + " (" + boat.getType() + ") ");
                        System.out.print(i == center.getBoats().size() - 1 ? "" : "/ ");
                    }
                    System.out.println();
                }
                System.out.println("\n");
            }
        }

        for (Center center : centers) {
            Set<String> newBoatTypes = new HashSet<>(boatTypes);
            List<Center> newSelectedCenters = new ArrayList<>(selectedCenters);
            boolean centerContainsBoatType = false;

            // Marcaje
            for (Boat boat : center.getBoats()) {
                if (newBoatTypes.contains(boat.getType())) {
                    newBoatTypes.remove(boat.getType());
                    centerContainsBoatType = true; // marcaje
                }
            }

            // PBMSC
            if (centerContainsBoatType && newSelectedCenters.size() < bestSize) {
                newSelectedCenters.add(center);
                backTrackingConMarcaje(centers, newBoatTypes, newSelectedCenters, bestSolutions, minBoats);
                newSelectedCenters.remove(center);
            }
        }
    }

    private int bestSizeSinMarcaje = Integer.MAX_VALUE;

    public void backTrackingSinMarcaje(List<Center> centers, @NotNull Set<String> boatTypes, List<Center> selectedCenters, List<List<Center>> bestSolutions, int minBoats) {
        if (boatTypes.isEmpty()) {
            int currentBoats = 0;
            for (Center center : selectedCenters) {
                currentBoats += center.getBoats().size();
            }

            // Optimización
            if (selectedCenters.size() < bestSizeSinMarcaje && currentBoats < minBoats) {
                bestSolutions.clear();
                bestSolutions.add(selectedCenters);
                bestSizeSinMarcaje = selectedCenters.size();
                minBoats = currentBoats;
                System.out.println("\u001B[33mNUEVA SOLUCIÓN ÓPTIMA " + "num.centers (" + bestSizeSinMarcaje + ") num.Boats (" + currentBoats + ")\u001B[0m");
                for (Center center : selectedCenters) {
                    System.out.print("Optimal Centers-> " + center.getName() + ": ");
                    for (int i = 0; i < center.getBoats().size(); i++) {
                        Boat boat = center.getBoats().get(i);
                        System.out.print(boat.getName() + " (" + boat.getType() + ") ");
                        System.out.print(i == center.getBoats().size() - 1 ? "" : "/ ");
                    }
                    System.out.println();
                }
                System.out.println("\n");
            }
        }

        for (Center center : centers) {
            Set<String> newBoatTypes = new HashSet<>(boatTypes);
            List<Center> newSelectedCenters = new ArrayList<>(selectedCenters);

            for (Boat boat : center.getBoats()) {
                newBoatTypes.remove(boat.getType());
            }

            // PBMSC
            if (newSelectedCenters.size() < bestSize) {
                newSelectedCenters.add(center);
                backTrackingSinMarcaje(centers, newBoatTypes, newSelectedCenters, bestSolutions, minBoats);
                newSelectedCenters.remove(center);
            }
        }
    }

    public PriorityQueue<Config> priorityQueue = new PriorityQueue<>();
    public int minNumberOfCenters = Integer.MAX_VALUE;

    public void branchAndBound() {
        priorityQueue.add(new Config());

        while (!priorityQueue.isEmpty()) {
            for (Config successor : priorityQueue.poll().expand()) {
                if (successor.isFull()) {
                    //Optimización
                    if (successor.getCost() < minNumberOfCenters) {
                        minNumberOfCenters = successor.getCost();
                    }
                } else {
                    //PBMSC
                    if (successor.getCost() < minNumberOfCenters) {
                        priorityQueue.add(successor);
                    }
                }
            }
        }
    }

    public void greedy() {
        List<Center> selectedCenters = new ArrayList<>();
        Set<String> remainingBoatTypes = new HashSet<>(Global.boatTypes);

        while (!remainingBoatTypes.isEmpty()) {
            Map<Center, Set<String>> centerTypeCounts = new HashMap<>();
            for (Center center : Global.centers) {
                Set<String> typeSet = new HashSet<>();
                for (Boat boat : center.getBoats()) {
                    String type = boat.getType();
                    if (remainingBoatTypes.contains(type)) {
                        typeSet.add(type);
                    }
                }
                centerTypeCounts.put(center, typeSet);
            }

            // Ordena los centros en función de la cantidad de tipos de barcos que ofrecen para los tipos de barcos restantes
            Global.centers.sort((c1, c2) -> centerTypeCounts.get(c2).size() - centerTypeCounts.get(c1).size());

            // Selecciona el primer centro que cubra un tipo de barco restante
            boolean found = false;
            for (Center center : Global.centers) {
                if (selectedCenters.contains(center)) { continue; }
                for (Boat boat : center.getBoats()) {
                    String type = boat.getType();
                    if (remainingBoatTypes.contains(type)) {
                        selectedCenters.add(center);
                        remainingBoatTypes.remove(type);
                        found = true;
                        break;
                    }
                }
                if (found) { break; }
            }
            if (!found) { break; }
        }
        System.out.println("Solución óptima encontrada con " + selectedCenters.size() + " centros.");
        for (Center center : selectedCenters) {
            System.out.print("Centro seleccionado: " + center.getName() + " [ ");
            for (Boat boat : center.getBoats()) {
                System.out.print(boat.getType() + ", ");
            }
            System.out.println("]\n");
        }
    }
}