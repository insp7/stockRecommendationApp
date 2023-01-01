import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActionThread {
    final static int EMA9_GRE_EMA21 = 1;
    final static int EM9_LES_EMA21 = 2;
    final static int EMA_EQU_EMA21 = 0;

    int currentStatus=0;
    public ActionThread(String stockName, String ema9, String ema21, String base64Image) {

        System.out.println("Starting New Thread");
        new Thread(() -> {
            try{


            currentStatus = ActionThread.getEMA9ToEMA21Status(Double.parseDouble(ema9),
                    Double.parseDouble(ema21));

            if (!DatabaseHelper.isStockPresent(stockName)){
                DatabaseHelper.insertStock(stockName,currentStatus);
            }else{
                int databaseStatus = DatabaseHelper.getStockStatus(stockName);
                if (databaseStatus != currentStatus){
                    try {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        String msg = stockName+" value updated "+dtf.format(now);
                        Files.write(Paths.get("statusUpdated.txt"), msg.getBytes(), StandardOpenOption.APPEND);
                    }catch (IOException e) {
                        //exception handling left as an exercise for the reader
                        e.printStackTrace();
                    }
                    MailHelper.sendMailWithImagesToTeam(
                            "Alert for "+stockName, "<h1>Chart Value updated</h1>", base64Image);
                    System.out.println("Set alert to DDD company for stock"+stockName);
                    DatabaseHelper.updateStockStatus(stockName,currentStatus);
                }

            }
            }catch (Exception e){
                e.printStackTrace();
            }


        }).start();

    }

    public static int getEMA9ToEMA21Status(double ema9, double ema21){
        if (ema9 > ema21){
            return  ActionThread.EMA9_GRE_EMA21;
        }else if(ema9 < ema21){
            return  ActionThread.EM9_LES_EMA21;
        }
        return EMA_EQU_EMA21;
    }
}
