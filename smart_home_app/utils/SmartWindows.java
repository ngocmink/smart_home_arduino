package utils;

import java.io.IOException;
import com.fazecast.jSerialComm.SerialPort;
import java.io.Serial;


public class SmartWindows extends SmartDoorLock {
    
    private boolean locked = false; 

    public void close_when_turn_on_AC(SerialPort sp) throws IOException {
        
    }
    
}