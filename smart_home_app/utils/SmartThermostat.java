package utils;

import java.io.IOException;
import java.util.Scanner;
import com.fazecast.jSerialComm.SerialPort;

public class SmartThermostat implements SmartDevice {
    private boolean on = false;
    private String name;

    @Override
    public void turnOn(SerialPort sp) throws IOException {
        on = true;
        sp.getOutputStream().write("THERMO_ON\n".getBytes());
        sp.getOutputStream().flush();
        System.out.println("SmartThermostat: Thermostat turned on.");
    }

    @Override
    public void turnOff(SerialPort sp) throws IOException {
        on = false;
        sp.getOutputStream().write("THERMO_OFF\n".getBytes());
        sp.getOutputStream().flush();
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

    public float get_tmp(SerialPort port) throws IOException {
        String line = null;
        float tmp = -1;

        try (Scanner data = new Scanner(port.getInputStream())) {
            port.getOutputStream().write("GET_THERMO".getBytes());
            if (data.hasNextLine()) {
                line = data.nextLine().trim();
                System.out.println("Received: " + line);
                tmp = Float.parseFloat(line);
            }
        } catch (Exception e) {
            System.err.println("Error reading temperature: " + e.getMessage());
        }

        return tmp;
    }

    public float get_humid(SerialPort port) throws IOException {
        String line = null;
        float humid = -1;

        try (Scanner data = new Scanner(port.getInputStream())) {
            port.getOutputStream().write("GET_HUMID".getBytes());
            if (data.hasNextLine()) {
                line = data.nextLine().trim();
                System.out.println("Received: " + line);
                humid = Float.parseFloat(line);
            }
        } catch (Exception e) {
            System.err.println("Error reading Humidity: " + e.getMessage());
        }

        return humid;
    }

    public void printthLCD(SerialPort sp, SmartLCD lcd) throws IOException {
        float tmp = get_tmp(sp);
        float humid = get_humid(sp);
        String message = "Temperature: " + tmp + "\u00B0C\nHumidity: " + humid + "%";
        lcd.printLCD(message, sp);
    }
}
