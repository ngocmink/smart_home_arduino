import utils.*;
import java.io.IOException;
import com.fazecast.jSerialComm.*;  
import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

public class LCDMenu {
    public static void show(SerialPort sp, Scanner input, SmartLCD lcd) throws IOException, InterruptedException {
        while (true) {
            System.out.println("\n--- LCD Menu ---");
            System.out.println("1. Turn ON");
            System.out.println("2. Turn OFF");
            System.out.println("3. Print message");
            System.out.print("Choose action (0 to exit): ");
            int action = input.nextInt();
            if (action == 0) break;

            switch (action) {
                case 1:
                    lcd.turnOn(sp);
                    break;
                case 2:
                    lcd.turnOff(sp);
                    break;
                case 3:
                    input.nextLine();
                    System.out.println("Enter message: ");
                    String message = input.nextLine();
                    lcd.printLCD(message, sp);
                    break;
                default: 
                    System.out.println("Invalid action.");
                    break;
            }
        }
    }
}
