import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ActionThreadTest {
    @Test
    public void testGetEMA9ToEMA12Status(){
        assertEquals(ActionThread.EM9_LES_EMA21,ActionThread.getEMA9ToEMA21Status(9,12));
    }

    @Test
    public void testActionThread(){
        new ActionThread("Idea","9","9","");
    }

    @Test
    public void writeResults() throws IOException {
        String stockName = "";
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String msg = stockName+" value updated"+dtf.format(now);
            Files.write(Paths.get("statusUpdated.txt"), msg.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
            e.printStackTrace();
        }
    }

}