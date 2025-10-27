package utils;

import java.io.IOException;
import com.fazecast.jSerialComm.SerialPort;

// Interface for device control
public interface SmartDevice {
    void turnOn(SerialPort sp) throws IOException;
    void turnOff(SerialPort sp) throws IOException;
    boolean isOn();
    void name(String name);
}
