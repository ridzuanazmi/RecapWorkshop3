import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in); // Create a Scanner object
		String action = "";
		String user = null;
		ArrayList<String> shoppingCart = new ArrayList<String>(); // Create an ArrayList object										
		printMenu();

		while (action != "exit") {
			System.out.print("> ");
			action = scanner.next().trim().toLowerCase(); // Read user input
			switch (action) {
				case "login":
					String username = scanner.nextLine().trim(); // Read the username. If the username is empty, prompt the user to enter a username
					if (username.length() != 0) {
						user = username;
					} else {
						System.out.println("Please enter a username.");
						break;
					}
					System.out.printf("Welcome %s\n", user);
					getCartDBFile(user); // Retrieve the shopping cart from the database
					break;
				case "save":
					if (!ensureUserLoggedIn(user) || !ensureCartNotEmpty(shoppingCart)) {
						break;
					}
					System.out.println("User: " + user + "\nShopping Cart: " + shoppingCart);
					saveShoppingCartToFile(user, shoppingCart);
					shoppingCart.clear();
					break;
				case "users":
					if (!ensureUsersInCartDB()) {
						break;
					}
					checkUsersInDb();
					break;
				case "list":
					if (!ensureUserLoggedIn(user)) {
						break;
					}
					checkCurrentShoppingCart(shoppingCart);
					checkUserCartContents(user);
					break;
				case "add":
					String itemsToAdd = scanner.nextLine().trim();
					String[] items = itemsToAdd.split(",");
					for (String item : items) {
						shoppingCart.add(item.trim());
						System.out.printf("%s has been added to your shopping cart.\n", item.trim());
					}
					break;
				case "remove":
					String itemToRemove = scanner.nextLine().trim();
					removeItemFromCart(shoppingCart, itemToRemove);
					break;
				case "clear":
					shoppingCart.clear();
					System.out.println("Your shopping cart has been cleared.");
					break;
				case "help":
					printMenu();
					break;
				case "quit":
					System.out.println("Thank you for using the shopping cart.");
					System.exit(0);
					break;
				default:
					System.out.printf("Invalid action '%s'. Please try again. Type 'quit' to exit the program\n",
							action);
					break;
			}
		}
		scanner.close();
	}

	private static void getCartDBFile(String user) {
		ArrayList<String> shoppingCart = new ArrayList<String>();
		File cartDBFile = new File("cartDB\\" + user + ".txt");
		if (cartDBFile.exists()) {
			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(cartDBFile))) {
				String line;
				if (0 == cartDBFile.length()) {
					System.out.println("You are already registered. " + user + ", your shopping cart is empty.");
				}
				while ((line = bufferedReader.readLine()) != null) {
					shoppingCart.add(line);
				}
				if (!shoppingCart.isEmpty())
					System.out.println("Contents of your shopping cart: " + shoppingCart);					
				bufferedReader.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		} else {
			try {
				cartDBFile.createNewFile();
				System.out.println(user + ", your shopping cart is empty.");
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
	}

	private static boolean ensureUsersInCartDB() {
		File cartDBFolder = new File("cartDB");
		if (cartDBFolder.list().length == 0) {
			System.out.println("No users are registered.");
			return false;
		}
		return true;
	}

	private static void checkCurrentShoppingCart(ArrayList<String> shoppingCart) {
		if (shoppingCart.isEmpty()) {
			System.out.println("Your shopping cart is empty.");
		} else {
			System.out.printf("Your shopping cart contains: %s\n", shoppingCart);
		}
	}

	private static void checkUserCartContents(String user) {
		File cartDBFile = new File("cartDB\\" + user + ".txt");
		ArrayList<String> shoppingCart = new ArrayList<String>();
		if (cartDBFile.exists()) {
			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(cartDBFile))) {
				String line;
				if (0 == cartDBFile.length()) {
					System.out.println(user + ", your shopping cart in the DB is empty.");
				}
				while ((line = bufferedReader.readLine()) != null) {
					shoppingCart.add(line);
				}
				if (!shoppingCart.isEmpty())
					System.out.println("Current contents of your shopping cart: " + shoppingCart);					
				bufferedReader.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		} else {
			System.out.println("User: " + user + " does not exist.");
		}
	}

	private static void checkUsersInDb() {
		File cartDBFolder = new File("cartDB");
		String[] files = cartDBFolder.list();
		for (int i = 0; i < files.length; i++) {
			System.out.printf("%d. %s\n", i + 1, files[i].substring(0, files[i].indexOf(".txt")));
		}
	}

	private static boolean ensureUserLoggedIn(String user) {
		if (user == null) {
			System.out.println("Please login to save or see your shopping cart.");
			return false;
		}
		return true;
	}

	private static boolean ensureCartNotEmpty(ArrayList<String> shoppingCart) {
		if (shoppingCart.isEmpty()) {
			System.out.println("Your shopping cart is empty. Nothing to save.");
			return false;
		}
		return true;
	}

	private static void saveShoppingCartToFile(String user, ArrayList<String> shoppingCart) {
		File cartDBFile = new File("cartDB\\" + user + ".txt");
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(cartDBFile, true))) {
			for (String item : shoppingCart) {
				bufferedWriter.write(item);
				bufferedWriter.newLine();
			}
			bufferedWriter.flush();
			bufferedWriter.close();
			System.out.println(user + ", your shopping cart has been saved successfully.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private static void removeItemFromCart(ArrayList<String> shoppingCart, String itemToDelete) {
		if (itemToDelete.matches("\\d+")) {
			int itemNumber = Integer.parseInt(itemToDelete);
			if (itemNumber >= 1 && itemNumber <= shoppingCart.size()) {
				String removedItem = shoppingCart.remove(itemNumber - 1);
				System.out.printf("Item %d '%s' has been removed from your shopping cart.\n", itemNumber, removedItem);
			} else {
				System.out.printf("Invalid item number. Please try again.\n");
			}
		} else {
			int itemNumber = shoppingCart.indexOf(itemToDelete) + 1;
			if (itemNumber != 0) {
				shoppingCart.remove(itemToDelete);
				System.out.printf("Item %d (%s) has been removed from your shopping cart.\n", itemNumber, itemToDelete);
			} else {
				System.out.printf("%s is not in your shopping cart.\n", itemToDelete);
			}
		}
	}

	private static void printMenu() {
		System.out.print("""
				---------------Welcome to your shopping cartItems---------------
				Type in what you want to do
				login - Login to your account or create an account
				save - Save your shopping cart
				users - List all users in the database
				list - List all items in your shopping cart
				add - Add item(s) to your shopping cart
				remove - Remove item(s) from your shopping cart
				help - Display the menu
				quit - Exit the program
				""");
	}
}
