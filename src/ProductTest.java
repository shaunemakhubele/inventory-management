
public class ProductTest {

    private static int testsPassed = 0;

    private static void check(
            String testName, boolean condition) {

        if (condition) {
            System.out.println("PASS: " + testName);
            testsPassed++;
        } else {
            throw new AssertionError(
                "FAIL: " + testName
            );
        }
    }

    private static void expectError(
            String testName, Runnable action) {

        try {
            action.run();
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: " + testName);
            testsPassed++;
            return;
        }

        throw new AssertionError(
            "FAIL: " + testName + " - expected an error"
        );
    }

    public static void main(String[] args) {

        Product keyboard =
            new Product("Keyboard", 450.00, 4);

        check(
            "Product name is correct",
            keyboard.getName().equals("Keyboard")
        );

        check(
            "Initial quantity is correct",
            keyboard.getQuantity() == 4
        );

        check(
            "Low-stock warning works",
            keyboard.isLowStock()
        );

        check(
            "Stock value is correct",
            keyboard.getStockValue() == 1800.00
        );

        keyboard.restock(10);

        check(
            "Restocking updates quantity",
            keyboard.getQuantity() == 14
        );

        check(
            "Low-stock warning clears",
            !keyboard.isLowStock()
        );

        expectError(
            "Negative price is rejected",
            () -> new Product("Mouse", -10, 5)
        );

        expectError(
            "Negative quantity is rejected",
            () -> new Product("Mouse", 10, -5)
        );

        expectError(
            "Zero restock amount is rejected",
            () -> keyboard.restock(0)
        );

        expectError(
            "Negative restock amount is rejected",
            () -> keyboard.restock(-2)
        );

        System.out.println(
            "\nAll " + testsPassed + " tests passed!"
        );

        
        try {
            new Product("   ", 10.0, 5);
            System.out.println("FAIL: Blank product name accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: Blank product name rejected");
        }

    }
}
