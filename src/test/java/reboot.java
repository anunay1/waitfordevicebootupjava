import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class reboot {

    @Test
    public void reboot() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("adb", "reboot");
        Process pc = null;
        try {
            pc = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            pc.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("done");

        ProcessBuilder pb1 = new ProcessBuilder("adb","wait-for-device");
        pc = pb1.start();
        pc.waitFor();
        System.out.println("Wait completed adb deamon started successfully");
        String str = "";
        int counter = 300;
        do {
            Thread.sleep(1000);
            String command [] = {"adb", "shell","getprop","dev.bootcomplete"};
            Process logcatprocess = Runtime.getRuntime().exec(command);
            InputStream is = logcatprocess.getInputStream();
            InputStreamReader isReader = new InputStreamReader(is);
            BufferedReader bf = new BufferedReader(isReader);
            str = bf.readLine();
            if(str.contentEquals("1"))
            {
                System.out.println("Device booted up successfully");
                break;
            }

            counter--;
        }while (counter > 0);

        if (counter == 0)
        {
            System.out.println("Device did not boot up successfully, please check the logs");

        }

    }
}
