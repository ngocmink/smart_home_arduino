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
        SmartDoorLock main_door = new SmartDoorLock();
        SmartWindows window_1 = new SmartWindows();
        SmartWindows window_2 = new SmartWindows();

        // Adding devices to the hub
        hub.addDevice(bulb, "Neon_Bulb");
        hub.addDevice(thermostat, "Cold_Bulb");
        hub.addDevice(LCD, "LCD");
        hub.addDevice(main_door, "Main_Door");
        hub.addDevice(window_1, "Window_1");
        hub.addDevice(window_2, "Window_2");

        // Activating all devices
        System.out.println("Activating all devices...");
        hub.activateAllDevices(sp);

        //-------------------------------------------------------------
        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.print("\n*************");
            System.out.print("\n1. Bulb");
            System.out.print("\n2. LCD");
            System.out.print("\n3. Thermostat");
            System.out.print("\n4. ");
            System.out.print("\n*************");
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
                ThermoMenu.show(sp, input, thermostat, LCD);
            }
            else if(device == 4){
                System.out.print("\n*************");
                System.out.print("\n1. Main Door");
                System.out.print("\n2. Window 1");
                System.out.print("\n3. Window 2");
                System.out.print("\n*************");
                System.out.print("\nChoose door/window (0 to exit): ");
                Integer dw = input.nextInt();
                switch (dw) {
                    case 1:
                        DoorLockMenu.show(sp, input, main_door, hub);
                    case 2: 
                        DoorLockMenu.show(sp, input, window_1, hub);
                    case 3: 
                        DoorLockMenu.show(sp, input, window_2, hub);
                }
            }
            else{
                System.out.println("Invalid device.");
            }
        }
        input.close();

        // Deactivating all devices
        System.out.println("Deactivating all devices...");
        hub.deactivateAllDevices(sp);
    }
}
