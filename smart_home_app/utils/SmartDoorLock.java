package utils;

import java.io.IOException;
import com.fazecast.jSerialComm.SerialPort;
import java.io.IOError;
import java.io.Serial;


public class SmartDoorLock implements SmartDevice {
    
    private boolean locked = false; 
    private String name = "";
  
    @Override
    public void turnOn(SerialPort sp) throws IOException {
        locked = true;
        sp.getOutputStream().write("DOOR_LOCK\n".getBytes());
        sp.getOutputStream().flush();
        System.out.println(name + ": Door has been LOCKED.");
    }

    @Override
    public void turnOff(SerialPort sp) throws IOException {
        locked = false;
        sp.getOutputStream().write("DOOR_UNLOCK\n".getBytes());
        sp.getOutputStream().flush();
        System.out.println(name + ": Door has been UNLOCKED.");
    }

    @Override
    public boolean isOn() {
        return locked;
    }


    @Override
    public void name(String name) {
        System.out.println("Add device: " + name);
        this.name = name;
    }
    
    public void lock(SerialPort sp) throws IOException {
        this.turnOn(sp);
    }

    public void unlock(SerialPort sp) throws IOException {
        this.turnOff(sp);
    }

    public void lock_all(SerialPort sp, SmartHomeHub hub) throws IOException {
        setLockState(sp, hub, true);
    }

    public void unlock_all(SerialPort sp, SmartHomeHub hub) throws IOException {
        setLockState(sp, hub, false);
    }

    private void setLockState(SerialPort sp, SmartHomeHub hub, boolean shouldLock) throws IOException {
        for (SmartDevice device : hub.get_devices()) {
            if (device instanceof SmartDoorLock) {
                SmartDoorLock lock = (SmartDoorLock) device;
                if (shouldLock) {
                    lock.lock(sp);
                } else {
                    lock.unlock(sp);
                }
            }
        }
    }
}