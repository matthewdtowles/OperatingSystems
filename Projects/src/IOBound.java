import java.io.FileWriter;
import java.io.IOException;

/**
 * IOBound
 * Thread class that runs IO intensive operation
 */
public class IOBound extends Thread {

    public void run() {
        String file = "C:/Users/matthew.towles/MyEdu/OperatingSystems/test/outputFile.txt";
        int loops = 100;
        try {
            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < loops; i++) {
                writer.write(i + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
