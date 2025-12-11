import utils.*;
import java.io.IOException;
import com.fazecast.jSerialComm.*;  
import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

public class RainMenu {

    public static void show(SerialPort sp, Scanner input, SmartRain rain, SmartWindows window, SmartHomeHub hub) throws IOException, InterruptedException {
        while(true){
            System.out.println("\n--- Rain Menu ---");
            System.out.println("1. Turn ON");
            System.out.println("2. Turn OFF");
            System.out.println("3. Check Status");
            System.out.println("4. Check Rain");
            System.out.print("Choose action (0 to exit): ");

            Integer action = input.nextInt();
            if (action == 0) break;
            Thread.sleep(500); 
            
            switch(action){
                case 1:
                    System.out.println("Turning on Rain Detector...");
                    rain.turnOn(sp);
                    break;
                case 2:
                    System.out.println("Turning off Rain Detector...");
                    rain.turnOff(sp);
                    break;
                case 3:
                    boolean isActive = rain.isOn();
                    System.out.println("\n[STATUS] Rain Sensor is: " + (isActive ? "ON" : "OFF"));
                    break;
                case 4:
                    System.out.println("Checking rain...");
                    boolean raining = rain.isRaining(sp);
                    System.out.println("The weather is " + (raining ? "raining" : "not raining"));
                    rain.close_when_rain(sp, window, hub);
                    break;
                default: 
                    System.out.println("\nInvalid selection");

            }
             
            Thread.sleep(800);
        }
    }
}