import java.util.ArrayList;
import java.util.Scanner;

public class App {
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in); // Create a Scanner object
		String action = "";
		printMenu();
		ArrayList<String> shoppingCart = new ArrayList<String>(); // Create an ArrayList object
		
		while (action != "exit") {
			System.out.print("> ");
			action = scanner.next().trim().toLowerCase(); // Read user input
			switch (action) {
				case "list":
					if (shoppingCart.isEmpty()) {
						System.out.println("Your shopping cart is empty.");
					} else {
						System.out.println("Here are the items in your shopping cart: ");
						listItem(shoppingCart);
					}
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

	private static void listItem(ArrayList<String> shoppingCart) {
		for (int i = 0; i < shoppingCart.size(); i++) {
			System.out.printf("%d. %s\n", i + 1, shoppingCart.get(i));
		}
	}

	private static void printMenu() {
		System.out.print("""
				---------------Welcome to your shopping cartItems---------------
				Type in what you want to do
				list - List all items in your shopping cart
				add - Add item(s) to your shopping cart
				remove - Remove item(s) from your shopping cart
				help - Display the menu
				quit - Exit the program
				""");
	}
}
