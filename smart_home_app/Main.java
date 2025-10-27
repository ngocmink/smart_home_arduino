import utils.*;
import java.io.IOException;
import com.fazecast.jSerialComm.*;  
import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Serial Port
        SerialPort sp = SerialPort.getCommPort("COM5");
        sp.setComPortParameters(9600, 8, 1, 0);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
        //-------------------------------------------------------------
        if(!sp.openPort()) {
            System.out.println("\nCOM port NOT available\n"); return;
        }

        // Main hub
        SmartHomeHub hub = new SmartHomeHub();

        // Initializing devices
        SmartBulb bulb = new SmartBulb();
        SmartThermostat thermostat = new SmartThermostat();
        SmartLCD LCD = new SmartLCD();

        // Adding devices to the hub
        hub.addDevice(bulb, "Neon_Bulb");
        hub.addDevice(thermostat, "Cold_Bulb");
        hub.addDevice(LCD, "LCD");

        // Activating all devices
        System.out.println("Activating all devices...");
        hub.activateAllDevices(sp);

        // Deactivating all devices
        System.out.println("Deactivating all devices...");
        hub.deactivateAllDevices(sp);

        Integer b = bulb.blinks(sp);
        while(b == 0) b = bulb.blinks(sp);
        System.out.println("Blink " + b + " times.");

        //-------------------------------------------------------------
        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.print("\n****************************");
            System.out.print("\n1. Bulb");
            System.out.print("\n2. Thuy Linh cute");
            System.out.print("\n3. Thuy Linh cute vai ca lon");
            System.out.print("\n4. Thuy linh xinh quaaaaaaaaaa");
            System.out.print("\n****************************");
            System.out.print("\nChoose device (0 to exit): ");
            Integer device = input.nextInt();
            if(device == 0) break;
            Thread.sleep(1500);
            if(device == 1){
                BulbMenu.show(sp, input, bulb);
            }
            else if(device == 2){
                LCDMenu.show(sp, input, LCD);
            }
            else if(device == 3){

            }
            else{
                System.out.println("Invalid device.");
            }
        }
        input.close();
    }
}
