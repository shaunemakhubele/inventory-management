
public class Product {
    private final String name;
    private final double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                "Product name cannot be empty"
            );
        }

        if (price < 0 || quantity < 0) {
            throw new IllegalArgumentException(
                "Price and quantity cannot be negative"
            );
        }

        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
    return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isLowStock() {
        return quantity < 10;
    }

    public double getStockValue() {
        return price * quantity;
    }

    public void restock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Restock amount must be positive"
            );
        }

        quantity += amount;
    }
}
