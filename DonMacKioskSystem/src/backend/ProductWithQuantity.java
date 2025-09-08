// ProductWithQuantity.java
package backend;

public class ProductWithQuantity {
    private int id;
    private String name;
    private String category;
    private String description;
    private double price;
    private int quantity;
    private String imagePath;
    
    public ProductWithQuantity(int id, String name, String category, String description, 
                             double price, int quantity, String imagePath) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getImagePath() { return imagePath; }
}