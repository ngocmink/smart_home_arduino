package utils;

import java.io.IOException;
import com.fazecast.jSerialComm.SerialPort;

public class SmartRain implements SmartDevice {
    private boolean status;
    
    public SmartRain() {
        this.status = false;
    }
    
    @Override
    public void turnOn(SerialPort sp) throws IOException {
        sp.writeBytes("SmartRain on".getBytes(), "SmartRain on".length());
        this.status = true;
    }
    
    @Override
    public void turnOff(SerialPort sp) throws IOException {
        sp.writeBytes("SmartRain off".getBytes(), "SmartRain off".length());
        this.status = false;
    }
    
    @Override
    public boolean isOn() {
        return this.status;
    }
    
    @Override
    public void name(String name) {
        // Implementation if needed
    }
}
