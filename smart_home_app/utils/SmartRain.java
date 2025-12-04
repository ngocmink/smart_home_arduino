package utils;

import java.io.IOException;
import java.util.Scanner;

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

    public boolean isRaining(SerialPort sp) throws IOException {
        if (!on) return false; 
        try {
            sp.getOutputStream().write("GET_RAIN\n".getBytes());
            sp.getOutputStream().flush();
            Thread.sleep(200); 
            Scanner data = new Scanner(sp.getInputStream());
            if (data.hasNextLine()) {
                String response = data.nextLine().trim();
                if (response.equals("RAINING")) {   
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Cannot detect rain status: " + e.getMessage());
        }
        return false;
    }

    public void close_when_rain(SerialPort sp, SmartDoorLock lock, SmartHomeHub hub) throws IOException {
        boolean raining = isRaining(sp);      
        if(raining){
            System.out.println("WARNING: It is raining! Closing window...");
            lock.lock_all(sp, hub);
        }
    }
}