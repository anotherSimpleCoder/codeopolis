package de.htwsaar.esch.Codeopolis.Presentation;

import de.htwsaar.esch.Codeopolis.DomainModel.Game;
import de.htwsaar.esch.Codeopolis.DomainModel.Silo;
import de.htwsaar.esch.Codeopolis.DomainModel.Game.GrainType;

import java.util.Comparator;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * A class that provides a dialog for interacting with the depot management system.
 * It allows users to print details of silos with optional filtering and sorting criteria.
 */
public class DepotDetailsDialog {

    private Scanner scanner;

    private Game game;

    /**
     * Constructs a DepotDetailsDialog with the specified game.
     *
     * @param game The game instance containing the depot to manage.
     */
    public DepotDetailsDialog(Game game) {
        this.scanner = new Scanner(System.in);
        this.game = game;
    }

    /**
     * Starts the depot details dialog, providing options for printing silos with or without filtering and sorting.
     */
    public void start() {
        System.out.println("Welcome to the Depot Management System");
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Print all silos");
            System.out.println("2. Print filtered and sorted silos");
            System.out.println("3. Exit");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println(game.getDepotDetails(null, null));
                    break; // Added break statement
                case 2:
                    Predicate<Silo> filter = getFilterCriteria();
                    Comparator<Silo> comparator = getComparatorCriteria();
                    System.out.println(game.getDepotDetails(filter, comparator));
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Prompts the user to choose a filter criterion and returns the corresponding predicate.
     *
     * @return A predicate for filtering silos, or null if no filter is chosen.
     */
    private Predicate<Silo> getFilterCriteria() {
        System.out.println("Choose a filter criterion:");
        System.out.println("1. Grain Type");
        System.out.println("2. Minimum Fill Level");
        System.out.println("3. Maximum Fill Level");
        System.out.println("4. No filter");

        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                System.out.println("Enter the grain type (e.g., WHEAT, CORN):");
                String grainTypeInput = scanner.nextLine().toUpperCase();
                try {
                    GrainType grainType = GrainType.valueOf(grainTypeInput);
                    return silo -> silo.getGrainType() != null && silo.getGrainType() == grainType;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid grain type. No filter will be applied.");
                    return null;
                }
            case 2:
                System.out.println("Enter the minimum fill level:");
                int minFillLevel = Integer.parseInt(scanner.nextLine());
                return silo -> silo.getFillLevel() >= minFillLevel;
            case 3:
                System.out.println("Enter the maximum fill level:");
                int maxFillLevel = Integer.parseInt(scanner.nextLine());
                return silo -> silo.getFillLevel() <= maxFillLevel;
            case 4:
                return null;
            default:
                System.out.println("Invalid choice. No filter will be applied.");
                return null;
        }
    }

    /**
     * Prompts the user to choose a sorting criterion and returns the corresponding comparator.
     *
     * @return A comparator for sorting silos, or null if no sorting is chosen.
     */
    private Comparator<Silo> getComparatorCriteria() {
        System.out.println("Choose a sorting criterion:");
        System.out.println("1. Grain Type");
        System.out.println("2. Fill Level");
        System.out.println("3. Capacity");
        System.out.println("4. Random Sorting");
        System.out.println("5. No sorting");

        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                return (s1, s2) -> {
                    if (s1.getGrainType() == null && s2.getGrainType() == null) return 0;
                    if (s1.getGrainType() == null) return -1;
                    if (s2.getGrainType() == null) return 1;
                    return s1.getGrainType().compareTo(s2.getGrainType());
                };
            case 2:
                return (s1, s2) -> Integer.compare(s1.getFillLevel(), s2.getFillLevel());
            case 3:
                return (s1, s2) -> Integer.compare(s1.getCapacity(), s2.getCapacity());
            case 4:
                return (s1, s2) -> Math.random() > 0.5 ? 1 : -1; // Random sorting
            case 5:
                return null;
            default:
                System.out.println("Invalid choice. No sorting will be applied.");
                return null;
        }
    }
}
