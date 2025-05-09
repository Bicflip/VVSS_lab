
package inventory.model;


public abstract class Part {

    // Declare fields
    private int partId;
    private String name;
    private double price;
    private int inStock;
    private int min;
    private int max;
    
    // Constructor
    protected Part(int partId, String name, double price, int inStock, int min, int max) {
        this.partId = partId;
        this.name = name;
        this.price = price;
        this.inStock = inStock;
        this.min = min;
        this.max = max;
    }

    public static String isValidPart(String name, double v, int i, int i1, int i2, String errorMessage) {
        if (name.isEmpty()) {
            errorMessage += "Name cannot be empty.\n";
        }
        if (v < 0) {
            errorMessage += "Price cannot be negative.\n";
        }
        if (i < 0) {
            errorMessage += "Inventory cannot be negative.\n";
        }
        if (i1 < 0) {
            errorMessage += "Min cannot be negative.\n";
        }
        if (i2 < 0) {
            errorMessage += "Max cannot be negative.\n";
        }
        return errorMessage;
    }

    // Getters
    public int getPartId() {
        return partId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getInStock() {
        return inStock;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
    
    // Setters
    public void setPartId(int partId) {
        this.partId = partId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return this.partId+","+this.name+","+this.price+","+this.inStock+","+
                this.min+","+this.max;
    }
}
