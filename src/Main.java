
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<Product> products = new ArrayList<>();
        loadProducts(products);

        boolean running = true;

        while (running) {
            System.out.println("\n=== Inventory Management System ===");
            System.out.println("1. Add product");
            System.out.println("2. View products");
            System.out.println("3. Restock product");
            System.out.println("4. Search product");
            System.out.println("5. Calculate total inventory value");
            System.out.println("6. Remove product");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Product name: ");
                    String name = input.nextLine();

                    double price = readDouble(input, "Product price (R): ");

                    int quantity = readInt(input, "Quantity: ");

                    if (name.isBlank()) {
                       System.out.println("Product name cannot be empty.");
                       break;
                    }

                    if (price < 0 || quantity < 0) {
                        System.out.println("Price and quantity cannot be negative.");
                        break;
                    }

                    try {
                        products.add(new Product(name, price, quantity));
                        System.out.println("Product added successfully!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case "2":
                    if (products.isEmpty()) {
                        System.out.println("No products in inventory.");
                    } else {
                        for (int i = 0; i < products.size(); i++) {
                            Product product = products.get(i);

                            System.out.println("\nProduct #" + (i + 1));
                            System.out.println("Name: " + product.getName());
                            System.out.println("Quantity: " + product.getQuantity());
                            System.out.println("Low stock: " + product.isLowStock());
                            System.out.println(
                                "Stock value: R" + product.getStockValue()
                            );
                        }
                    }
                    break;

                case "3":
                    if (products.isEmpty()) {
                        System.out.println("Add a product first.");
                        break;
                    }

                    for (int i = 0; i < products.size(); i++) {
                        System.out.println(
                            (i + 1) + ". " + products.get(i).getName()
                        );
                    }

                    int productNumber = readInt(input, "Choose product number: ");

                    if (productNumber < 1 ||
                        productNumber > products.size()) {
                        System.out.println("Invalid product number.");
                        break;
                    }

                    int amount = readInt(input, "Units to add: ");
                    try {
                        products.get(productNumber - 1).restock(amount);
                        System.out.println("Stock updated successfully!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                
                
                case "4":
                    System.out.print("Enter product name to search: ");
                    String searchName = input.nextLine();

                    boolean found = false;

                    for (Product product : products) {
                        if (product.getName().equalsIgnoreCase(searchName)) {
                            System.out.println("Product found!");
                            System.out.println("Name: " + product.getName());
                            System.out.println("Quantity: " + product.getQuantity());
                            System.out.println("Low stock: " + product.isLowStock());
                            System.out.println("Stock value: R" + product.getStockValue());

                            found = true;
                        }
                    }

                    if (!found) {
                        System.out.println("Product not found.");
                    }
                    break;

                
                case "5":
                    double totalValue = 0;

                    for (Product product : products) {
                        totalValue += product.getStockValue();
                    }

                    System.out.printf("Total inventory value: R%.2f%n", totalValue);
                    break;

                                  
                case "6":
                    if (products.isEmpty()) {
                        System.out.println("No products available to remove.");
                        break;
                    }

                    for (int i = 0; i < products.size(); i++) {
                        System.out.println(
                            (i + 1) + ". " + products.get(i).getName()
                        );
                    }

                    int removeNumber = readInt(
                        input, "Enter the product number to remove: "
                    );

                    if (removeNumber < 1 || removeNumber > products.size()) {
                        System.out.println("Invalid product number.");
                        break;
                    }

                    Product removed = products.remove(removeNumber - 1);

                    System.out.println(
                        removed.getName() + " was removed successfully."
                    );
                    break;

                case "7":
                    saveProducts(products);
                    running = false;
                    System.out.println("Goodbye!");
                    break;

            }
        }

        input.close();
    }

    
private static double readDouble(Scanner input, String prompt) {
    while (true) {
        System.out.print(prompt);
        try {
            double value = Double.parseDouble(input.nextLine());

            if (Double.isFinite(value)) {
                return value;
            }

            System.out.println("Please enter a valid number.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }
}

private static int readInt(Scanner input, String prompt) {
    while (true) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a whole number.");
        }
    }
}


private static final Path INVENTORY_FILE =
        Path.of("inventory.csv");

private static void saveProducts(ArrayList<Product> products) {
    try (BufferedWriter writer =
            Files.newBufferedWriter(INVENTORY_FILE)) {

        for (Product product : products) {
            writer.write(
                product.getName() + ","
                + product.getPrice() + ","
                + product.getQuantity()
            );
            writer.newLine();
        }

        System.out.println("Inventory saved successfully.");

    } catch (IOException e) {
        System.out.println("Could not save inventory: " + e.getMessage());
    }
}

private static void loadProducts(ArrayList<Product> products) {
    if (!Files.exists(INVENTORY_FILE)) {
        System.out.println("No saved inventory found. Starting fresh.");
        return;
    }

    try (BufferedReader reader =
            Files.newBufferedReader(INVENTORY_FILE)) {

        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",", -1);

            if (parts.length != 3) {
                System.out.println("Skipping invalid inventory record.");
                continue;
            }

            try {
                String name = parts[0];
                double price = Double.parseDouble(parts[1]);
                int quantity = Integer.parseInt(parts[2]);

                products.add(new Product(name, price, quantity));

            } catch (IllegalArgumentException e) {
                System.out.println("Skipping invalid inventory record.");
            }
        }

        System.out.println("Inventory loaded successfully.");

    } catch (IOException e) {
        System.out.println("Could not load inventory: " + e.getMessage());
    }
}


}
