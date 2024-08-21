package com.example.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.example.entity.Bike;
import com.example.entity.Driver;

public class EasyDriveTest {

	static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mit");
	static EntityManager entityManager = entityManagerFactory.createEntityManager();

	public static Bike insertBike(String bikeName) {
		entityManager.getTransaction().begin();
		Bike bike = new Bike();
		bike.setBikeName(bikeName);
		bike.setAvailability(true);
		entityManager.persist(bike);
		entityManager.getTransaction().commit();
		return bike;
	}

	// assignDriver is nothing but creating a driver details with bike
	public static Driver assignDriver(String driverName, String driverEmail, String sourceAddress,
			String destinationAddress, LocalDateTime dateTime, Bike bike) {
		entityManager.getTransaction().begin();
		Driver driver = new Driver();
		driver.setDriverName(driverName);
		driver.setDriverEmail(driverEmail);
		driver.setBike(bike);
		bike.setAvailability(false);
		driver.setSourceAddress(sourceAddress);
		driver.setDestinationAddress(destinationAddress);
		driver.setDateTime(dateTime);
		entityManager.persist(driver);
		entityManager.getTransaction().commit();
		return driver;
	}

	public static Driver findByDriverId(int driverId) {
		return entityManager.find(Driver.class, driverId);
	}

	public static Bike findByBikeId(int bikeId) {
		return entityManager.find(Bike.class, bikeId);
	}

	public static boolean findByBikeAvailability(int bikeId) {
		entityManager.getTransaction().begin();
		Bike bike = findByBikeId(bikeId);
		entityManager.getTransaction().commit();
		return bike.isAvailability();
	}

	public static void updateDriverData(Driver driver, String driverName, String driverEmail, String sourceAddress,
			String destinationAddress)// only if exists
	{
		entityManager.getTransaction().begin();
		driver.setDriverName(driverName);
		driver.setDriverEmail(driverEmail);
		driver.setSourceAddress(sourceAddress);
		driver.setDestinationAddress(destinationAddress);
		entityManager.getTransaction().commit();
	}

	public static void deleteDriver(Driver driver)// only if exists
	{
		entityManager.getTransaction().begin();
		driver.getBike().setAvailability(true);
		entityManager.remove(driver);
		entityManager.getTransaction().commit();
	}

	public static List<Driver> fetchAllRecords() {
		entityManager.getTransaction().begin();
		String query1 = "select d from Driver d ";
		Query query = entityManager.createQuery(query1, Driver.class);
		List<Driver> drivers = query.getResultList();
		entityManager.getTransaction().commit();
		return drivers;
	}

	public static List<Bike> fetchAllBikes() {
		entityManager.getTransaction().begin();
		String query1 = "select b from Bike b";
		Query query = entityManager.createQuery(query1, Bike.class);
		List<Bike> bikes = query.getResultList();
		entityManager.getTransaction().commit();
		return bikes;
	}

	public static void deleteBike(Bike bike) {
		entityManager.getTransaction().begin();
		entityManager.remove(bike);
		entityManager.getTransaction().commit();
	}

	public static void sortByDriverName() {
		entityManager.getTransaction().begin();
		String query1 = "select d from Driver d order by d.driverName ";
		Query query = entityManager.createQuery(query1, Driver.class);
		List<Driver> drivers = query.getResultList();
		for (Driver driver : drivers) {
			System.out.println(driver);
		}
		entityManager.getTransaction().commit();
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int choice;

		try {
			do {
				System.out.println("Enter your choice:");
				System.out.println("1. Create Bike");
				System.out.println("2. Assign Driver");
				System.out.println("3. Find Driver by ID");
				System.out.println("4. Update Driver");
				System.out.println("5. Delete Driver");
				System.out.println("6. Fetch All Drivers");
				System.out.println("7. Find Bike by ID");
				System.out.println("8. Sort Drivers by Name");
				System.out.println("9. Delete Bike if Available");
				System.out.println("10. Fetch All Bikes");
				System.out.println("11. Exit");

				choice = sc.nextInt();
				sc.nextLine();

				switch (choice) {
				case 1:
					// Create Bike
					System.out.println("Enter Bike Name:");
					String bikeName = sc.nextLine();
					Bike bike = insertBike(bikeName);
					System.out.println("Bike created successfully: " + bike);
					break;

				case 2:
					// Assign Driver
					System.out.println("Enter Bike ID:");
					int bikeId = sc.nextInt();
					sc.nextLine();
					Bike assignedBike = findByBikeId(bikeId);
					if (assignedBike != null) {
						if (assignedBike.isAvailability()) {
							System.out.println("Enter Driver Name:");
							String driverName = sc.nextLine();
							System.out.println("Enter Driver Email:");
							String driverEmail = sc.nextLine();
							System.out.println("Enter Source Address:");
							String sourceAddress = sc.nextLine();
							System.out.println("Enter Destination Address:");
							String destinationAddress = sc.nextLine();
							System.out.println("Enter Date and Time (YYYY-MM-DD HH:MM:SS):");
							String dateTimeString = sc.nextLine();
							LocalDateTime dateTime = LocalDateTime.parse(dateTimeString,
									DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
							Driver driver = assignDriver(driverName, driverEmail, sourceAddress, destinationAddress,
									dateTime, assignedBike);
							System.out.println("Driver assigned successfully: " + driver);
						} else {
							System.out.println("Bike is busy with other driver....");
						}
					} else {
						System.out.println("Bike not found.");
					}
					break;

				case 3:
					// Find Driver by ID

					System.out.println("Enter Driver ID:");
					int driverId = sc.nextInt();
					sc.nextLine();
					Driver driverFound = findByDriverId(driverId);
					if (driverFound != null) {
						System.out.println("Driver found: " + driverFound);
					} else {
						System.out.println("Driver not found.");
					}
					break;

				case 4:
					// Update Driver
					System.out.println("Enter Driver ID to update:");
					int updateId = sc.nextInt();
					sc.nextLine();
					Driver driverToUpdate = findByDriverId(updateId);
					if (driverToUpdate != null) {
						System.out.println("Enter new Driver Name:");
						String newDriverName = sc.nextLine();
						System.out.println("Enter new Driver Email:");
						String newDriverEmail = sc.nextLine();
						System.out.println("Enter new Source Address:");
						String newSourceAddress = sc.nextLine();
						System.out.println("Enter new Destination Address:");
						String newDestinationAddress = sc.nextLine();
						updateDriverData(driverToUpdate, newDriverName, newDriverEmail, newSourceAddress,
								newDestinationAddress);
						System.out.println("Driver updated successfully.");
					} else {
						System.out.println("Driver not found.");
					}
					break;

				case 5:
					// Delete Driver
					System.out.println("Enter Driver ID to delete:");
					int deleteId = sc.nextInt();
					sc.nextLine();
					Driver driverToDelete = findByDriverId(deleteId);
					if (driverToDelete != null) {
						deleteDriver(driverToDelete);
						System.out.println("Driver deleted successfully.");
					} else {
						System.out.println("Driver not found.");
					}
					break;

				case 6:
					// Fetch All Drivers
					List<Driver> drivers = fetchAllRecords();
					System.out.println("All Drivers:");
					for (Driver d : drivers) {
						System.out.println(d);
					}
					break;

				case 7:
					// Find Bike by ID
					System.out.println("Enter Bike ID:");
					int bikeIdToFind = sc.nextInt();
					sc.nextLine();
					Bike bikeFound = findByBikeId(bikeIdToFind);
					if (bikeFound != null) {
						System.out.println("Bike found: " + bikeFound);
					} else {
						System.out.println("Bike not found.");
					}
					break;

				case 8:
					// Sort Drivers by Name
					System.out.println("Drivers sorted by Name:");
					sortByDriverName();
					break;

				case 9:
					// Delete Bike if Available
					System.out.println("Enter Bike ID to delete if available:");
					int bikeIdToDelete = sc.nextInt();
					sc.nextLine();
					Bike bikeToDelete = findByBikeId(bikeIdToDelete);
					if (bikeToDelete != null) {
						if (bikeToDelete.isAvailability()) {
							deleteBike(bikeToDelete);
							System.out.println("Bike deleted successfully.");
						} else {
							System.out.println("Bike is not available for deletion.");
						}
					} else {
						System.out.println("Bike not found.");
					}
					break;
				case 10:
					// Fetch All Bikes
					List<Bike> bikes = fetchAllBikes();
					System.out.println("All Bikes:");
					for (Bike b : bikes) {
						System.out.println(b);
					}
					break;

				case 11:
					System.out.println("Exiting...");
					break;

				default:
					System.out.println("Invalid choice. Please try again.");
					break;
				}
			} while (choice != 11);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
			if (entityManagerFactory != null) {
				entityManagerFactory.close();
			}
		}
	}
}
