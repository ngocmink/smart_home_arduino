package utils;
import java.io.IOException;
import com.fazecast.jSerialComm.SerialPort;
import java.io.Serial;

public class SmartAC extends SmartThermostat {

    private boolean on = false;

    public void turnOn(SerialPort sp, SmartDoorLock door, SmartHomeHub hub) throws IOException {
        on = true;
        sp.getOutputStream().write("AC_ON\n".getBytes());
        sp.getOutputStream().flush();
        door.lock_all(sp, hub);
        System.out.println("Turning on AC...");
    }

    @Override
    public void turnOff(SerialPort sp) throws IOException {
        on = false;
        sp.getOutputStream().write("AC_OFF\n".getBytes());
        sp.getOutputStream().flush();
        System.out.println("Turning off AC");
    }

    public void auto_turn_on_AC(SerialPort sp) throws IOException {
        float temp = get_tmp(sp);
        float humid = get_humid(sp);

        if(temp < 20 || temp > 30) turnOn(sp);
        else if(temp >= 20 && temp <= 30 && humid > 30 && humid < 50) turnOff(sp);
    }
}
