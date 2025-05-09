package inventory.repository;

import inventory.model.Part;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class InventoryRepositoryTest extends InventoryRepository {

    @Test
    void testLookupPart() {
        // Arrange
        InventoryRepository repo = new InventoryRepository();
        repo.readParts();
        String search = "Screw"; // Caută o piesă existentă

        // Act
        Part part = repo.lookupPart(search);

        // Assert
        assertNotNull(part, "Part should be found.");
        assertEquals("Screw", part.getName());
    }

    @Test
    void testLookupPartNotFound() {
        // Arrange
        InventoryRepository repo = new InventoryRepository();
        repo.readParts();
        String search = "NonExistentPart"; // Caută o piesă inexistentă

        // Act
        Part part = repo.lookupPart(search);

        // Assert
        assertNull(part, "Part should not be found.");
    }
}