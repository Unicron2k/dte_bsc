import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


class MyHashMapTest {
    private MyHashMap<String, String> map;

    @BeforeEach
    void Prepare_And_Fill_Map_With_Initial_Values(){

        map = new MyHashMap<>();

        // "Siblings" and "Teheran" will produce a hash collision
        map.put("Siblings", "Cake");
        map.put("Siblings", "Ice");

        map.put("Teheran", "Doughnut");
        map.put("Teheran", "Cake");

        map.put("Siblings", "Soda");
        map.put("Siblings", "Jerky");

        map.put("Teheran", "Brandy");
        map.put("Teheran", "Nuts");

        // The rest won't
        map.put("John", "Vanilla-buns");
        map.put("John", "Vanilla-pie");
        map.put("John", "Vanilla-grapefruit");

        map.put("Kira", "Apple-pie");
        map.put("Kira", "Watermelon");
        map.put("Kira", "Cake");
    }

    @Test
    void check_put_CollidedKeysStillExists_ShouldReturnTrue() {

        assertTrue(map.containsKey("Siblings"));
        assertTrue(map.containsValue("Jerky"));

        assertTrue(map.containsKey("Teheran"));
        assertTrue(map.containsValue("Doughnut"));
    }

    @Test
    void check1_remove_should_return_success() {
        assertTrue(map.containsKey("Teheran"));
        map.remove("Teheran");
        assertFalse(map.containsKey("Teheran"));

    }

    @Test
    void check2_remove_should_return_success() {
        assertTrue(map.containsKey("Siblings"));
        map.remove("Siblings");
        assertFalse(map.containsKey("Siblings"));

    }

    @Test
    void check_removeSingleElement_should_return_success() {
        map.clear();
        map.put("Siblings", "Cake");
        assertTrue(map.containsKey("Siblings"));
        map.remove("Siblings");
        assertFalse(map.containsKey("Siblings"));

    }
}