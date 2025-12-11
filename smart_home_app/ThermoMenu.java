import utils.*;
import java.io.IOException;
import com.fazecast.jSerialComm.*;  
import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

public class ThermoMenu {
    public static void show(SerialPort sp, Scanner input, SmartThermostat thermo, SmartLCD lcd, SmartDoorLock door, SmartHomeHub hub) throws IOException, InterruptedException {
        while (true) {
            System.out.println("\n--- Thermo Menu ---");
            System.out.println("1. Turn ON");
            System.out.println("2. Turn OFF");
            System.out.println("3. Get Temperature");
            System.out.println("4. Get Humidity");
            System.out.println("5. Print both on LCD");
            System.out.print("Choose action (0 to exit): ");
            int action = input.nextInt();
            if (action == 0) break;

            switch (action) {
                case 1:
                    if (thermo != null) {
                        if (thermo instanceof SmartAC) {
                            ((SmartAC) thermo).turnOn(sp, door, hub);  
                        }
                        else thermo.turnOn(sp);
                        break;
                    }
                case 2:
                    if (thermo != null) {
                        thermo.turnOff(sp);
                    }
                    break;
                case 3:
                    float tmp = thermo.get_tmp(sp);
                    System.out.println("Temperature: " + tmp);
                    break;
                case 4:
                    float humid = thermo.get_humid(sp);
                    System.out.println("Humidity: " + humid);
                    break;
                case 5:
                    thermo.printthLCD(sp, lcd);
                    break;
                default: 
                    System.out.println("Invalid action.");
                    break;
            }
        }
    }
}
