import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseHelperTest {
    @Test
    public void testInsert(){
        DatabaseHelper.insertStock("Dhananjay", ActionThread.EMA_EQU_EMA21);
    }

    @Test
    public void testIsStockPresent(){
        assertTrue(DatabaseHelper.isStockPresent("Idea"));
    }

    @Test
    public void testGetStockStatus(){
        assertEquals(0,DatabaseHelper.getStockStatus("Idea"));
    }
}