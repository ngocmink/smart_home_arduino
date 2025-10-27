package utils;
import java.io.IOException;
import java.util.*;

import com.fazecast.jSerialComm.SerialPort;

// Smart bulb implementation
public class SmartBulb implements SmartDevice {
    private boolean on = false;
    Scanner input = new Scanner(System.in);

    @Override
    public void turnOn(SerialPort sp) throws IOException {
        on = true;
        sp.getOutputStream().write("BULB_ON".getBytes());
        System.out.println("SmartBulb: Bulb turned on.");
    }

    @Override
    public void turnOff(SerialPort sp) throws IOException {
        on = false;
        sp.getOutputStream().write("BULB_OFF".getBytes());
        System.out.println("SmartBulb: Bulb turned off.");
    }

    @Override
    public boolean isOn() {
        return on;
    }

    @Override
    public void name(String name) {
        System.out.println("Add device: " + name);
    }

    public int blinks(SerialPort sp) throws IOException {
        System.out.print("\nEnter number of blink: ");
        Integer blinks = input.nextInt();
        if(blinks <= 0){
            System.out.print("\nInvalid number.");
            return 0;
        }
        sp.getOutputStream().write(blinks.byteValue());
        return blinks;
    }
}

