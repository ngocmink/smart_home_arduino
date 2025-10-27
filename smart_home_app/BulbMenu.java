import utils.*;
import java.io.IOException;
import com.fazecast.jSerialComm.*;  
import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

public class BulbMenu {
    public static void show(SerialPort sp, Scanner input, SmartBulb bulb) throws IOException, InterruptedException {
        while (true) {
            System.out.println("\n--- Bulb Menu ---");
            System.out.println("1. Turn ON");
            System.out.println("2. Turn OFF");
            System.out.println("3. Blink");
            System.out.print("Choose action (0 to exit): ");
            int action = input.nextInt();
            if (action == 0) break;

            switch (action) {
                case 1:
                    bulb.turnOn(sp);
                    break;
                case 2:
                    bulb.turnOff(sp);
                    break;
                case 3:
                    Integer blinks = bulb.blinks(sp);
                    while(blinks == 0) blinks = bulb.blinks(sp);
                    System.out.println("Blink " + blinks + " times.");
                    break;
                default: 
                    System.out.println("Invalid action.");
                    break;
            }
        }
    }
}
