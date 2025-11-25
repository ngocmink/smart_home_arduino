package utils;

import java.io.IOException;
import com.fazecast.jSerialComm.SerialPort;
import java.io.IOError;
import java.io.Serial;


public class SmartDoorLock implements SmartDevice {
    
    private boolean locked = false; 

  
    @Override
    public void turnOn(SerialPort sp) throws IOException {
        locked = true;
        sp.getOutputStream().write("DOOR_LOCK".getBytes());
        sp.getOutputStream().flush();
        System.out.println("SmartDoorLock: Door has been LOCKED.");
    }

    @Override
    public void turnOff(SerialPort sp) throws IOException {
        locked = false;
        sp.getOutputStream().write("DOOR_UNLOCK".getBytes());
        sp.getOutputStream().flush();
        System.out.println("SmartDoorLock: Door has been UNLOCKED.");
    }

    @Override
    public boolean isOn() {
        return locked;
    }


    @Override
    public void name(String name) {
        System.out.println("Add device: " + name);
    }
    
    public void lock(SerialPort sp) throws IOException {
        this.turnOn(sp);
    }

    public void unlock(SerialPort sp) throws IOException {
        this.turnOff(sp);
    }

    public void lock_all(SerialPort sp, SmartHomeHub hub) throws IOException {
        for (SmartDevice device : hub.get_devices()) {
            if (device instanceof SmartDoorLock) {
                ((SmartDoorLock) device).lock(sp);
            }
        }
    }

    public void unlock_all(SerialPort sp, SmartHomeHub hub) throws IOException {
        for (SmartDevice device : hub.get_devices()) {
            if (device instanceof SmartDoorLock) {
                ((SmartDoorLock) device).unlock(sp);
            }
        }
    }
}