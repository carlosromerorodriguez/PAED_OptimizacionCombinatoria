import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {
    private static final Problema_3_2 problema_3_2 = new Problema_3_2();
    public static void main(String[] args) {
        ArrayList<Boat> boats = new ArrayList<>();
        ArrayList<Sailor> sailors = new ArrayList<>();
        ArrayList<Boat> boatSorted = new ArrayList<>();
        ArrayList<Sailor> sailorsSorted = new ArrayList<>();

        System.out.print("¡Bienvenido a CatTheHobie!\n\n");
        leerArchivo(boats, sailors, new Scanner(System.in));

        int end = 0;

        do {
            int option = mostrarMenuOrdenacion (new Scanner(System.in));

            switch (option) {
                case 1 -> {
                    problema_3_1(boats, sailors, boatSorted, sailorsSorted);
                }
                case 2 -> {
                    problema_3_2(boats);
                }
                case 3 -> {
                    System.out.println("\n\n¡Hasta pronto!");
                    end = 1;
                }
            }
        } while (end == 0);
    }

    public static void quickSort(float @NotNull [] barcos_id, int izq, int der) {
        float pivote = barcos_id[izq]; // tomamos primer elemento como pivote
        int i = izq;                   // i realiza la búsqueda de izquierda a derecha
        int j = der;                   // j realiza la búsqueda de derecha a izquierda
        float aux;

        while (i < j) {                                  // mientras no se crucen las búsquedas
            while (barcos_id[i] <= pivote && i < j) i++; // busca elemento mayor que pivote
            while (barcos_id[j] > pivote) j--;           // busca elemento menor que pivote
            if (i < j) {                                 // si no se han cruzado
                aux = barcos_id[i];                      // los intercambia
                barcos_id[i] = barcos_id[j];
                barcos_id[j] = aux;
            }
        }

        barcos_id[izq] = barcos_id[j]; // se coloca el pivote en su lugar de forma que tendremos los
        barcos_id[j]=pivote;           // números más pequeños a su izquierda y los más grandes su derecha

        if(izq < j-1) {
            quickSort(barcos_id,izq,j-1);           // ordenamos subarray izquierdo
        }
        if(j+1 < der)
            quickSort(barcos_id,j+1,der);           // ordenamos subarray derecho
    }


    private static void ordenarXPrestaciones (ArrayList<Boat> barcos, ArrayList<Boat> boatSorted) {
        float[] prestaciones = new float[barcos.size()];

        for (int i = 0; i < barcos.size(); i++) { // llenar el array de strings "prestaciones"
            Boat aux1 = barcos.get(i);           // con todos los valores de la division
            prestaciones[i] = (float) (aux1.getPeso()/(aux1.getSlore()+aux1.getCapacity()+aux1.getVelocity()));
        }
        long start = System.nanoTime();
        quickSort(prestaciones, 0, barcos.size() - 1); //función recursiva
        long end = System.nanoTime();
        // mostrar los barcos ordenados por mejores prestaciones
        System.out.println("\nBARCOS DISPONIBLES:\n");
        for (float p : prestaciones) {
            for (Boat aux1 : barcos) {
                //encuentro el barco cuya división de atributos coincide con la de la posición del array "prestaciones"
                if ((float) (aux1.getPeso() / (aux1.getSlore() + aux1.getCapacity() + aux1.getVelocity())) == p) {
                    System.out.println("\t> NOMBRE: " + aux1.getName() + " || PESO: " + aux1.getPeso() + "kg" + " || ESLORA: " + aux1.getSlore()
                            + "m" + " || CAPACIDAD: " + aux1.getCapacity() + " pers." + " || VEL: " + aux1.getVelocity() + "km/h");
                    boatSorted.add(aux1);
                }
            }
        }
        System.out.println("\n\tTiempo QuickSort: " + (end-start) + " miliseconds\n");
    }

    private static void ordenarNavegantes(@NotNull ArrayList<Sailor> sailors, ArrayList<Sailor> sailorsSorted){
        float[] prestaciones = new float[sailors.size()];
        float sumHab;

        for (int i = 0; i < sailors.size(); i++) { // llenar el array de strings "prestaciones"
            Sailor aux1 = sailors.get(i);
            sumHab = 0.0f;
            for(int z = 0; z < aux1.getHabilities().length; z++){
                sumHab += aux1.getHabilities()[z];
            }
            sumHab = sumHab / 6.0f;// con todos los valores de la division
            prestaciones[i] = aux1.getPeso()/(sumHab + aux1.getRatio_victories());
        }
        long start = System.nanoTime();
        quickSort(prestaciones, 0, sailors.size() - 1); //función recursiva
        long end = System.nanoTime();

        // mostrar los navegantes ordenados por mejores prestaciones
        System.out.println("NAVEGANTES DISPONIBLES:\n");
        for (float prestacion : prestaciones) {
            for (Sailor aux1 : sailors) {
                sumHab = 0.0f;
                for(int z = 0; z < aux1.getHabilities().length; z++){
                    sumHab += aux1.getHabilities()[z];
                }
                sumHab = sumHab / 6.0f;
                //encuentro el barco cuya división de atributos coincide con la de la posición del array "prestaciones"
                if ((aux1.getPeso()/(sumHab + aux1.getRatio_victories())) == prestacion) {
                    System.out.println("\t> NOMBRE: " + aux1.getName() + " || PESO: " + aux1.getPeso() + "kg");
                    sailorsSorted.add(aux1);
                }
            }
        }
        System.out.println("\n\tTiempo QuickSort: " + (end-start) + " miliseconds\n");
    }

    private static void problema_3_1(@NotNull ArrayList<Boat> boats, ArrayList<Sailor> sailors, ArrayList<Boat> boatsSorted, ArrayList<Sailor> sailorsSorted) {
        int max_cap = 0;
        for (Boat b : boats) {
            max_cap += b.getCapacity();
        }
        int[] config = new int[max_cap];
        ordenarXPrestaciones(boats, boatsSorted);
        ordenarNavegantes(sailors, sailorsSorted);
        Arrays.fill(config, -1);

        boolean[] used = new boolean[max_cap];
        Arrays.fill(used, false);

        switch (showMenuProblema_3_1()) {
            case 1 -> {
                long start1 = System.nanoTime();
                backTracking(config, 0, sailorsSorted, boatsSorted, 0);
                long end1 = System.nanoTime();
                System.out.println("\nTiempo backtracking: "+(end1-start1)+"ns -> "+((end1-start1)/Math.pow(10, 9))+"s.");
            }
            case 2 -> {
                long start3 = System.nanoTime();
                greedy(sailorsSorted, boatsSorted);
                long end3 = System.nanoTime();
                System.out.println("\nTiempo greedy: "+(end3-start3)+"ns -> "+((end3-start3)/Math.pow(10, 9))+"s.");
            }
        }
    }

    private static void greedy(@NotNull ArrayList<Sailor> sailors, ArrayList<Boat> boats) {
        double best = 0.0;
        for (Sailor sailor : sailors) {
            Boat bestBoat = null;
            double bestSpeed = 0.0;
            for (Boat boat : boats) {
                if (boat.getCapacity() > 0 && sailor.getPeso() <= boat.getPeso()) {
                    double impNav = calculateImpNav(sailor, boat);
                    double velocidad = boat.getVelocity() * impNav;
                    if (velocidad > bestSpeed) {
                        bestBoat = boat;
                        bestSpeed = velocidad;
                    }
                }
            }
            if (bestBoat != null) {
                sailor.setId(bestBoat.getId());
                best += bestSpeed;
                bestBoat.setCapacity(bestBoat.getCapacity() - 1);
            }
        }
        System.out.print("\n------------------------------\n");
        for (Boat boat : boats) {
            System.out.printf("\nBarco ID: %d\n", boat.getId());
            for (Sailor sailor : sailors) {
                if (sailor.getId() == boat.getId()) {
                    System.out.printf("\tNavegante: %s\n", sailor.getName());
                }
            }
        }
        System.out.printf("\nVelocidad Total: %f\n", best);
        System.out.print("\n------------------------------\n");
    }

    private static double calculateImpNav(@NotNull Sailor sailor, @NotNull Boat boat) {
        double impPes = ((100.0 - sailor.getPeso()) / boat.getPeso());
        double aux = 0.0;
        switch (boat.getType()) {
            case "Windsurf" -> aux = 1.0;
            case "Optimist" -> aux = 2.0;
            case "Laser" -> aux = 3.0;
            case "Patí Català" -> aux = 4.0;
            case "HobieDragoon" -> aux = 5.0;
            case "HobieCat" -> aux = 6.0;
        }
        double impHab = (((sailor.getHabilities()[(int) (aux - 1)] / 10.0) + (sailor.getRatio_victories() / 100.0)) / 20.0);
        return ((impPes + impHab) / 2.0) / 1000;
    }

    private static void backTracking(int @NotNull [] config, int k, ArrayList<Sailor> sailors, ArrayList<Boat> boats, double best){
        double aux1;
        config[k] = 0;

        while (config[k] <= 1) {
            if (k < config.length - 1) {
                for(int h = 0; h < boats.size(); h++){
                    sailors.get(k).setId(boats.get(h).getId());
                    if(checkSolution(sailors,boats)){
                        backTracking(config, k + 1, sailors, boats, best);
                    }
                }
            } else {
                // Solution case
                if(checkFinalSolution(sailors,boats)){
                    aux1 = printSolution(sailors, boats);
                    if(aux1 > best){
                        System.out.print("\nMejor Solución Encontrada!\n");
                        best = aux1;
                        System.out.print("\n------------------------------\n");
                        for(Boat b : boats){
                            System.out.printf("\nBarco ID: %d\n", b.getId());
                            for(Sailor s : sailors){
                                if(s.getId() == b.getId()){
                                    System.out.printf("\tNavegante: %s\n", s.getName());
                                }
                            }
                        }
                        System.out.printf("\nVelocidad Total: %f\n", best);
                        System.out.print("\n------------------------------\n");
                    }
                }
            }
            // Next successor
            config[k]++;
        }
    }

    private static void backTrackingMarcaje(int @NotNull [] config, int k, ArrayList<Sailor> sailors, ArrayList<Boat> boats, double best, boolean[] used){
        double aux1;
        config[k] = 0;

        while (config[k] <= 1) {
            if (k < config.length - 1) {
                for(int h = 0; h < boats.size(); h++){
                    Sailor currentSailor = sailors.get(k);
                    Boat currentBoat = boats.get(h);
                    if(!used[k] && currentSailor.getId() == currentBoat.getId()){
                        used[k] = true;
                        if(checkSolution(sailors,boats)){
                            backTrackingMarcaje(config, k + 1, sailors, boats, best, used);
                        }
                        used[k] = false;
                    }
                }
            } else {
                // Solution case
                if(checkFinalSolution(sailors,boats)){
                    aux1 = printSolution(sailors, boats);
                    if(aux1 > best){
                        System.out.print("\nMejor Solución Encontrada!\n");
                        best = aux1;
                        System.out.print("\n------------------------------\n");
                        for(Boat b : boats){
                            System.out.printf("\nBarco ID: %d\n", b.getId());
                            for(Sailor s : sailors){
                                if (s.getId() == b.getId()) {
                                    System.out.printf("\tNavegante: %s\n", s.getName());
                                }
                            }
                        }
                        System.out.printf("\nVelocidad Total: %f\n", best);
                        System.out.print("\n------------------------------\n");
                    }
                }
            }
            // Next successor
            config[k]++;
        }
    }

    private static boolean checkSolution(ArrayList<Sailor> sailors, @NotNull ArrayList<Boat> boats) {
        for (Boat boat : boats) {
            int quantity = 0;
            for (Sailor sailor : sailors) {
                if (sailor.getId() == boat.getId()) {
                    quantity++;
                }
            }
            if (quantity > boat.getCapacity()) {
                return false;
            }
        }
        return true;
    }
    private static boolean checkFinalSolution(ArrayList<Sailor> sailors, @NotNull ArrayList<Boat> boats) {
        for (Boat boat : boats) {
            int quantity = 0;
            for (Sailor sailor : sailors) {
                if (sailor.getId() == boat.getId()) {
                    quantity++;
                }
            }
            if (quantity > boat.getCapacity() || quantity == 0) {
                return false;
            }
        }
        return true;
    }

    private static double printSolution(ArrayList<Sailor> sailors, @NotNull ArrayList<Boat> boats){
        double total = 0.0, velocidad, impNav, impPes, impHab, aux = 0;

        for (Boat boat : boats) {
            impNav = 0.0;
            for (Sailor sailor : sailors) {
                if (sailor.getId() == boat.getId()) {
                    impPes = ((100.0 - sailor.getPeso()) / boat.getPeso());
                    switch (boat.getType()) {
                        case "Windsurf" -> aux = 1;
                        case "Optimist" -> aux = 2;
                        case "Laser" -> aux = 3;
                        case "Patí Català" -> aux = 4;
                        case "HobieDragoon" -> aux = 5;
                        case "HobieCat" -> aux = 6;
                    }
                    impHab = (((sailor.getHabilities()[(int) (aux - 1)] / 10.0) + (sailor.getRatio_victories() / 100.0)) / 2.0);
                    if (impNav == 0) {
                        impNav = ((impPes + impHab) / 2.0);
                    } else {
                        impNav = impNav * ((impPes + impHab) / 2.0);
                    }
                }
            }
            velocidad = boat.getVelocity() * impNav / 1000;
            total += velocidad;
        }
        return total;
    }

    public static int leerOpcion(@NotNull Scanner scanner) {
        int option = 0;
        do {
            System.out.print("¿Que fichero desea leer? \n");
            System.out.println("1- Large");
            System.out.println("2- Medium");
            System.out.println("3- Small");
            System.out.println("4- ExtraSmall\n");
            System.out.print("Opción? ");
            try {
                option = scanner.nextInt();
                if (option < 1  || option > 4) {
                    System.out.print("Please enter a number between 1 and 4.\n\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer.\n");
            } finally {
                scanner.nextLine();
            }
        } while (option < 1  || option > 4);
        return option;
    }

    public static int mostrarMenuOrdenacion (@NotNull Scanner scanner) {
        int option = 0;
        do {
            System.out.print("\nElija una de las 4 opciones: \n");
            System.out.println("1- Problema 3_1");
            System.out.println("2- Problema 3_2");
            System.out.println("3- Salir\n");
            System.out.print("Opción? ");
            try {
                option = scanner.nextInt();
                if (option < 1  || option > 3) {
                    System.out.print("Please enter a number between 1 and 3.\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer.\n");
            } finally {
                scanner.nextLine();
            }
        } while (option < 1  || option > 4);
        return option;
    }

    private static void problema_3_2(ArrayList<Boat> boats) {
        List<Center> centers = fillCenters(boats);
        Set<String> boatTypes = new HashSet<>(Arrays.asList("Windsurf", "Optimist", "Laser", "Patí Català", "HobieDragoon", "HobieCat"));
        Global.setCenters(centers);

        if (!checkCenters(centers)) {
            System.out.println("\u001B[31m(ERROR) No existe una solución posible!\u001B[0m");
        } else {
            System.out.println("\u001B[32mExiste una solución posible!\u001B[0m");

            switch (showMenuProblema_3_2()) {
                case 1 -> {
                    long start = System.nanoTime();
                    problema_3_2.backTrackingConMarcaje(centers, boatTypes, new ArrayList<>(), new ArrayList<>(), Integer.MAX_VALUE);
                    long end = System.nanoTime();
                    System.out.println("\u001B[34mTiempo BackTracking con Marcaje y PBMSC: "+(end - start)+" ns ("+ (double) TimeUnit.NANOSECONDS.toMillis(end-start)+"ms)\u001B[0m\n");
                    long start2 = System.nanoTime();
                    problema_3_2.backTrackingSinMarcaje(centers, boatTypes, new ArrayList<>(), new ArrayList<>(), Integer.MAX_VALUE);
                    long end2 = System.nanoTime();
                    System.out.println("\u001B[34mTiempo BackTracking sin Marcaje y PBMSC: "+(end2 - start2)+" ns ("+ (double) TimeUnit.NANOSECONDS.toMillis(end2-start2)+"ms)\u001B[0m");
                }
                case 2 -> {
                    long start = System.nanoTime();
                    problema_3_2.branchAndBound();
                    long end = System.nanoTime();
                    System.out.println("\u001B[34mTiempo Branch and Bound: "+(end - start)+" ns ("+ (double) TimeUnit.NANOSECONDS.toMillis(end-start)+"ms)\u001B[0m\n");
                }
                case 3 -> {
                    long start = System.nanoTime();
                    problema_3_2.greedy();
                    long end = System.nanoTime();
                    System.out.println("\u001B[34mTiempo Greedy: "+(end - start)+" ns ("+ (double) TimeUnit.NANOSECONDS.toMillis(end-start)+"ms)\u001B[0m\n");
                }
            }
        }
    }

    private static int showMenuProblema_3_1() {
        Scanner scanner = new Scanner(System.in);
        int option = 0;

        do {
            System.out.println("\nCon que método desea hacer 'Navegació d’alta velocitat'?\n");
            System.out.println("\t1- BackTracking");
            System.out.println("\t2- Greedy");
            System.out.println("\t3- Volver al menú principal\n");
            System.out.print("Opcion? ");
            try {
                option = scanner.nextInt();
                if (option < 1  || option > 3) {
                    System.out.print("Please enter a number between 1 and 4.\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer.\n");
            } finally {
                scanner.nextLine();
            }
        } while (option < 1  || option > 3);
        return option;
    }

    private static int showMenuProblema_3_2() {
        Scanner scanner = new Scanner(System.in);
        int option = 0;

        do {
            System.out.println("\nCon que método desea hacer 'Flota al complet'?\n");
            System.out.println("\t1- BackTracking");
            System.out.println("\t2- Branch & Bound");
            System.out.println("\t3- Greedy");
            System.out.println("\t4- Volver al menú principal\n");
            System.out.print("Opcion? ");
            try {
                option = scanner.nextInt();
                if (option < 1  || option > 4) {
                    System.out.print("Please enter a number between 1 and 4.\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer.\n");
            } finally {
                scanner.nextLine();
            }
        } while (option < 1  || option > 4);
        return option;
    }

    private static boolean checkCenters(@NotNull List<Center> centers) {
        TreeSet<String> dictionary = new TreeSet<>(Arrays.asList("Windsurf", "Optimist", "Laser", "Patí Català", "HobieDragoon", "HobieCat"));
        TreeSet<String> aux = new TreeSet<>();
        for (Center c : centers) { for (Boat boat : c.getBoats()) { aux.add(boat.getType()); } }
        return aux.equals(dictionary);
    }
    public static @NotNull List<Center> fillCenters(@NotNull ArrayList<Boat> boats) {
        List<Center> centers = new ArrayList<>();
        Set<String> centers_name = new HashSet<>();

        for (Boat b : boats) { centers_name.add(b.getCenter()); }
        for (String s : centers_name) {
            ArrayList<Boat> aux = new ArrayList<>();
            for (Boat boat : boats) {
                if (boat.getCenter().equals(s)) {
                    aux.add(boat);
                }
            }
            centers.add(new Center(aux, s));
        }
        return centers;
    }

    private static @NotNull List<String> leerFicheroTripulantes(String filename) throws IOException {
        List<String> input;
        try (Stream<String> lines = Files.lines(Paths.get("data/" + filename))) { input = lines.collect(Collectors.toList()); }
        input.remove(0);
        return input;
    }

    public static ArrayList<Boat> leerArchivo (ArrayList<Boat> barcos, ArrayList<Sailor> sailors, Scanner scanner) {

        File archivo;
        FileReader fr = null;
        BufferedReader br;
        String nombre_fichero_texto = "";
        String nombre_fichero_texto_sailors = "";
        int option;

        try {
            // Apertura del fichero y creacion de BufferedReader para
            // realizar la lectura de fichero.
            option = leerOpcion (scanner);

            switch (option) {
                case 1 -> nombre_fichero_texto = "boatsL.txt";
                case 2 -> nombre_fichero_texto = "boatsM.txt";
                case 3 -> nombre_fichero_texto = "boatsS.txt";
                case 4 -> nombre_fichero_texto = "boatsXS.txt";
            }
            switch (option) {
                case 1 -> nombre_fichero_texto_sailors = "sailorsL.txt";
                case 2 -> nombre_fichero_texto_sailors = "sailorsM.txt";
                case 3 -> nombre_fichero_texto_sailors = "sailorsS.txt";
                case 4 -> nombre_fichero_texto_sailors = "sailorsXS.txt";
            }
            List<String> tripulantes = leerFicheroTripulantes(nombre_fichero_texto_sailors);
            for (String s : tripulantes) {
                String[] parts = s.split(";");
                int[] abilities = new int[6];
                for (int i = 3; i < 9; i++) {
                    abilities[i-3] = Integer.parseInt(parts[i]);
                }
                Sailor sailor = new Sailor(Integer.parseInt(parts[0]), parts[1], Float.parseFloat(parts[2]), abilities, Float.parseFloat(parts[9]));
                sailors.add(sailor);
            }
            System.out.println("\n");
            //Apertura del fichero
            archivo = new File("data/" + nombre_fichero_texto);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String it = br.readLine();
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] parts = linea.split(";");
                Boat barco = new Boat(Integer.parseInt(parts[0]), parts[1], parts[2],
                        Double.parseDouble(parts[3]), Double.parseDouble(parts[4]),
                        Integer.parseInt(parts[5]), Integer.parseInt(parts[6]),
                        parts[7], Integer.parseInt(parts[8]), parts[9]);
                barcos.add(barco);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close(); //cerrar el fichero
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return (barcos);
    }
}
