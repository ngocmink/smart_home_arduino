import utils.*;
import java.io.IOException;
import com.fazecast.jSerialComm.*;  
import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

public class DoorLockMenu {

    public static void show(SerialPort sp, Scanner input, SmartDoorLock lock, SmartHomeHub hub) throws IOException, InterruptedException {
        while (true) {
            System.out.println("\n--- Door Lock Menu ---");
            System.out.println("Current status: " + (lock.isOn() ? "LOCKED" : "UNLOCKED"));
            System.out.print("\n1. Lock Door");
            System.out.print("\n2. Unlock Door");
            System.out.print("\n3. Lock all door");
            System.out.print("\n4. Unlock all door");
            System.out.print("\nChoose action (0 to exit): ");

            Integer action = input.nextInt();
            if (action == 0) break;
            Thread.sleep(500); 

            switch (action) {
                case 1:
                    System.out.println("Locking door...");
                    lock.lock(sp);
                    break;
                case 2:
                    System.out.println("Unlocking door...");
                    lock.unlock(sp); 
                    break;
                case 3:
                    System.out.println("Lock all door...");
                    lock.lock_all(sp, hub); 
                    break;
                case 4:
                    System.out.println("Unlock all door...");
                    lock.unlock_all(sp, hub); 
                    break;
                default:
                    System.out.println("Invalid action.");
                    break;
            }
        }
    }
}