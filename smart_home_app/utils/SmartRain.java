package utils;

import java.io.IOException;
import com.fazecast.jSerialComm.SerialPort;

public class SmartRain implements SmartDevice {
    private boolean on;
    private String name;
    
    @Override
    public void turnOn(SerialPort sp) throws IOException {
        on = true;
        sp.getOutputStream().write("RAIN_ON\n".getBytes());
        sp.getOutputStream().flush();
        System.out.println("SmartRain: RainDetector turned on.");
    }
    
    @Override
    public void turnOff(SerialPort sp) throws IOException {
        on = false;
        sp.getOutputStream().write("RAIN_OFF\n".getBytes());
        sp.getOutputStream().flush();
        System.out.println("SmartRain: RainDetector turned off.");
    }
    
    @Override
    public boolean isOn() {
        return on;
    }
    
    @Override
    public void name(String name) {
        this.name = name;
        System.out.println("Add device: " + this.name);
    }
}