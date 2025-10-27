package utils;

import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;

public class SmartThermostat implements SmartDevice {
    private boolean on = false;
    private String name;

    @Override
    public void turnOn(SerialPort sp) throws IOException {
        on = true;
        System.out.println("SmartThermostat: Thermostat turned on.");
    }

    @Override
    public void turnOff(SerialPort sp) throws IOException{
        on = false;
        System.out.println("SmartThermostat: Thermostat turned off.");
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