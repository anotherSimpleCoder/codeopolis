package de.htwsaar.esch.Codeopolis.Presentation;

import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Predicate;

import de.htwsaar.esch.Codeopolis.DomainModel.Game;
import de.htwsaar.esch.Codeopolis.DomainModel.Silo;
import de.htwsaar.esch.Codeopolis.Utils.LinkedList;
import de.htwsaar.esch.Codeopolis.DomainModel.Game.GrainType;

public class DepotDetailsDialog {
	private Scanner scanner;
	private Game game;
	
	public DepotDetailsDialog(Game game) {
		this.scanner = new Scanner(System.in);
		this.game = game;
	}
	
	public void start() {
		System.out.println("Welcome to the Depot Management System");
		while(true) {
			System.out.println("Choose an option:");
			System.out.println("1. Print all silos");
			System.out.println("2. Print filtered and sorted silos");
			System.out.println("3. Exit");
		
			LinkedList<Silo> silos = game.getCityState().getSilos();
			int choice = scanner.nextInt();
			
			switch(choice) {
				case 1:
					System.out.println(game.getDepotDetails(null, null));
					break;
				case 2:
					System.out.println(game.getDepotDetails(this.getFilterCriteria(), this.getComparator()));
					break;
				case 3: 
					System.out.println("Exiting...");
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
			}
		}
	}
	
	private Predicate<Silo> getFilterCriteria() {
		System.out.println("Choose a filter criterion:");
		System.out.println("1. Grain Type");
		System.out.println("2. Minimum fill level");
		System.out.println("3. Maximum fill level");
		System.out.println("4. No filter");
		
		int choice = scanner.nextInt();
		
		switch(choice) {
			case 1:
				System.out.println("Enter your grain type (e.g., WHEAT, CORN):");
				String grainTypeInput = scanner.nextLine().toUpperCase();
				
				try {
					GrainType grainType = GrainType.valueOf(grainTypeInput);
					
					return (silo) -> silo.getGrainType().equals(grainType);
				} catch(IllegalArgumentException e) {
					System.out.println("Invalid grain type. No filter will be applied.");
					return null;
				}
			case 2:
				System.out.println("Enter the minimum fill level");
				int minFillLevel = scanner.nextInt();
				return (silo) -> silo.getFillLevel() > minFillLevel;
			case 3:
				System.out.println("Enter the maximum fill level");
				int maxFillLevel = scanner.nextInt();
				return (silo) -> silo.getFillLevel() < maxFillLevel;
			case 4:
				return null;
			default:
				System.out.println("Invalid choice. No filter will be applied.");
				return null;
		}
	}

	
	private Comparator<Silo> getComparator() {
		System.out.println("Choose a sorting criterion:");
		System.out.println("1. Grain Type");
		System.out.println("2. Fill Level");
		System.out.println("3. Capacity");
		System.out.println("4. Random Sorting");
		System.out.println("5. No sorting");
		
		int choice = scanner.nextInt();
		
		switch(choice) {
			case 1:
				return (silo1, silo2) -> silo1.getGrainType().equals(silo2.getGrainType()) ? 0 : 1;
			case 2:
				return (silo1, silo2) -> Integer.compare(silo1.getFillLevel(), silo2.getFillLevel());
			case 3:
				return (silo1, silo2) -> Integer.compare(silo1.getCapacity(), silo2.getCapacity());
			case 4:
				return (silo1, silo2) -> {
					Random random = new Random();
					return random.nextInt(-1,1);
				};
			case 5:
				return null;
			default:
                System.out.println("Invalid choice. No sorting will be applied.");
                return null;
		}
	}

}
