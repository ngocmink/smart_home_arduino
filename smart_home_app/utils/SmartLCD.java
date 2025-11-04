package utils;

import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;

public class SmartLCD implements SmartDevice{
    private boolean on = false;
    private String name;
    private String message;

    @Override
    public void turnOn(SerialPort sp) throws IOException {
        on = true;
        sp.getOutputStream().write("LCD_ON".getBytes());
        System.out.println("LCD: LCD turned on.");
    }

    @Override
    public void turnOff(SerialPort sp) throws IOException {
        on = false;
        sp.getOutputStream().write("LCD_OFF".getBytes());
        System.out.println("LCD: LCD turned off.");
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
    
    public String printLCD(String message, SerialPort sp) throws IOException {
        this.message = message;
        System.out.println("Printing <" + this.message + "> on LCD.");
        sp.getOutputStream().write(("LCD_PRINT:" + message).getBytes());
        return this.message;
    }   
}
