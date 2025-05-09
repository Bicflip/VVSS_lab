package inventory.service;
import static org.junit.jupiter.api.Assertions.*;
import inventory.model.Part;
import inventory.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventoryServiceTest {

    private InventoryRepository repo;
    private InventoryService service;

    @BeforeEach
    void setUp() {
        repo = new InventoryRepository(); // Un repo fals pentru teste
        service = new InventoryService(repo);
    }

    // Test ECP (Equivalence Class Partitioning) valid
    @Test
    void testAddInhousePart_Valid() {
        // Arrange: Creează date valide pentru piesă
        String name = "Screw";
        double price = 10.0;
        int inStock = 50;
        int min = 10;
        int max = 100;
        int partDynamicValue = 500;
        // Act: Adaugă piesa în inventar
        service.addInhousePart(name, price, inStock, min, max, partDynamicValue);
        // Assert: Verifică dacă piesa a fost adăugată corect
        Part part = service.lookupPart(name);
        assertNotNull(part, "Part should be added successfully.");
        assertEquals(name, part.getName());
        assertEquals(price, part.getPrice());
        assertEquals(inStock, part.getInStock());
        assertEquals(min, part.getMin());
        assertEquals(max, part.getMax());
    }


    // Teste ECP (Equivalence Class Partitioning) invalide
    @Test
    void testAddInhousePart_InvalidStock() {
        // Arrange: Creează date invalide pentru piesă (stoc mai mare decât maximul)
        String name = "Screw";
        double price = 10.0;
        int inStock = 200;  // Invalide: Stoc mai mare decât max
        int min = 10;
        int max = 100;
        int partDynamicValue = 500;
        // Act & Assert: Verifică că se aruncă o excepție pentru stoc invalid
        assertThrows(IllegalArgumentException.class, () -> {
            service.addInhousePart(name, price, inStock, min, max, partDynamicValue);
        }, "Should throw IllegalArgumentException when inStock is greater than max.");
    }

    @Test
    void testAddInhousePart_InvalidMinGTMax() {
        // Arrange: Creează date invalide pentru piesă (stoc mai mare decât maximul)
        String name = "Screw";
        double price = 10.0;
        int inStock = 200;  // Invalide: Stoc mai mare decât max
        int min = 100;
        int max = 10;
        int partDynamicValue = 500;
        // Act & Assert: Verifică că se aruncă o excepție pentru stoc invalid
        assertThrows(IllegalArgumentException.class, () -> {
            service.addInhousePart(name, price, inStock, min, max, partDynamicValue);
        }, "Should throw IllegalArgumentException when min is greater than max.");
    }


    // Teste BVA (Boundary Value Analysis) valide
    @Test
    void testAddInhousePart_BoundaryValue_Min() {
        // Testează valori la limita intervalului pentru inStock
        // Arrange: Creează date cu valori de limită pentru stoc
        String name = "Screw";
        double price = 10.0;
        int min = 10;
        int max = 100;
        int partDynamicValue = 500;
        // Test la limita minimă (inStock = min)
        int inStockMin = min;
        service.addInhousePart(name, price, inStockMin, min, max, partDynamicValue);
        Part partMin = repo.lookupPart(name);
        assertEquals(inStockMin, partMin.getInStock(), "inStock should be equal to min.");
    }

    @Test
    void testAddInhousePart_BoundaryValue_Max() {
        // Testează valori la limita intervalului pentru inStock
        // Arrange: Creează date cu valori de limită pentru stoc
        String name = "Screw";
        double price = 10.0;
        int min = 10;
        int max = 100;
        int partDynamicValue = 500;
        // Test la limita maximă (inStock = max)
        int inStockMax = max;
        service.addInhousePart(name, price, inStockMax, min, max, partDynamicValue);
        Part partMax = repo.lookupPart(name);
        assertEquals(inStockMax, partMax.getInStock(), "inStock should be equal to max.");
    }

    // Teste BVA (Boundary Value Analysis) invalide
    @Test
    void testAddInhousePart_BoundaryValue_MinGreaterThanMax() {
        // Testează valori la limita intervalului pentru inStock
        // Arrange: Creează date cu valori de limită pentru stoc
        String name = "Screw";
        double price = 10.0;
        int min = 1000;
        int max = 100;
        int partDynamicValue = 500;
        // Test la limita minimă (inStock = min)
        int inStockMin = min;
        assertThrows(IllegalArgumentException.class, () -> {
            service.addInhousePart(name, price, inStockMin, min, max, partDynamicValue);
        }, "Should throw IllegalArgumentException when Min is greater than Max.");
    }

    // Teste BVA (Boundary Value Analysis) invalide
    @Test
    void testAddInhousePart_BoundaryValue_MaxLessThanMin() {
        // Testează valori la limita intervalului pentru inStock
        // Arrange: Creează date cu valori de limită pentru stoc
        String name = "Screw";
        double price = 10.0;
        int min = 1000;
        int max = 100;
        int partDynamicValue = 500;
        // Test la limita maxima (inStock = max)
        int inStockMax = max;
        assertThrows(IllegalArgumentException.class, () -> {
            service.addInhousePart(name, price, inStockMax, min, max, partDynamicValue);
        }, "Should throw IllegalArgumentException when Min is greater than Max.");
    }
}