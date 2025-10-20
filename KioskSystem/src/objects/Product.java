package objects;

public class Product {
    private int id;
    private String name;
    private double price;
    private String description;
    private String imageFilename;
    private boolean isAvailable;
    private boolean archived;
    
    public Product(int id, String name, double price, String description, 
                   String imageFilename, boolean isAvailable) {
        this(id, name, price, description, imageFilename, isAvailable, false);
    }
    
    public Product(int id, String name, double price, String description, 
                   String imageFilename, boolean isAvailable, boolean archived) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageFilename = imageFilename;
        this.isAvailable = isAvailable;
        this.archived = archived;
    }
    
    public Product(int id, String name, double price, String description, String imagePath) {
        this(id, name, price, description, imagePath, true, false);
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageFilename() { return imageFilename; }
    public String getImagePath() { return imageFilename; }
    public boolean isAvailable() { return isAvailable; }
    public boolean isArchived() { return archived; }
    
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setImageFilename(String imageFilename) { this.imageFilename = imageFilename; }
    public void setImagePath(String imagePath) { this.imageFilename = imagePath; }
    public void setAvailable(boolean available) { this.isAvailable = available; }
    public void setArchived(boolean archived) { this.archived = archived; }
}