package utils;
import java.util.*;
import java.io.IOException;
import com.fazecast.jSerialComm.*;  
import com.fazecast.jSerialComm.SerialPort;

public class SmartHomeHub {
    private List<SmartDevice> devices;

    public SmartHomeHub() {
        devices = new ArrayList<>();
    }

    public void addDevice(SmartDevice device, String name) {
        devices.add(device);
        device.name(name);
        System.out.println("Device added to the hub.");
    }

    // Method to turn on all devices
    public void activateAllDevices(SerialPort sp) throws IOException{
        for (SmartDevice device : devices) {
            device.turnOn(sp);
        }
    }

    // Method to turn off all devices
    public void deactivateAllDevices(SerialPort sp) throws IOException{
        for (SmartDevice device : devices) {
            device.turnOff(sp);
        }
    }

    public List<SmartDevice> get_devices(){
        return devices;
    }
}
