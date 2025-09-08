package backend;

public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private String description;
    private String imagePath;
    private boolean isActive;
    private String createdAt;
    
    
    public Product(int id, String name, String category, double price, String description,  String imagePath) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
    }
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }
    public boolean getIsActive() { return isActive; }
    public String getCreatedAt() { return createdAt; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    
    public String getImagePathOrDefault() {
        if (imagePath == null || imagePath.isEmpty()) {
            return "/images/products/default.png"; // Default image path
        }
        return imagePath;
    }
}