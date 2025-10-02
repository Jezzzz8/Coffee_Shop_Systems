package objects;

public class Product {
    private int id;
    private String name;
    private double price;
    private String description;
    private String imageFilename;
    private boolean isAvailable;
    
    public Product(int id, String name, double price, String description, 
                   String imageFilename, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageFilename = imageFilename;
        this.isAvailable = isAvailable;
    }
    
    
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageFilename() {return imageFilename; }
    public boolean isAvailable() { return isAvailable; }
    
    
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setImagePath(String imageFilename) { this.imageFilename = imageFilename; }
    public void setAvailable(boolean available) { this.isAvailable = available; }
}