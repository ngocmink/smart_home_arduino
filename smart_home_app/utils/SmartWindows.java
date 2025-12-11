package utils;

import java.io.IOException;
import com.fazecast.jSerialComm.SerialPort;
import java.io.Serial;


public class SmartWindows extends SmartDoorLock {

    public void close_when_rain(SerialPort sp, SmartRain rain, SmartHomeHub hub) throws IOException {
        if(rain.isRaining(sp) == true){
            lock_all(sp, hub);
        }
    }
    
    @Override
    public void lock_all(SerialPort sp, SmartHomeHub hub) throws IOException {
        for (SmartDevice device : hub.get_devices()) {
            if (device instanceof SmartWindows) {
                ((SmartWindows) device).lock(sp);
            }
        }
    }

    @Override
    public void unlock_all(SerialPort sp, SmartHomeHub hub) throws IOException {
        for (SmartDevice device : hub.get_devices()) {
            if (device instanceof SmartWindows) {
                ((SmartWindows) device).unlock(sp);
            }
        }
    }

}