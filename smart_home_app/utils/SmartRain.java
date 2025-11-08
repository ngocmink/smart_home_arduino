package utils;

public class SmartRain {
    private boolean status;
    
    public SmartRain() {
        this.status = false;
    }
    
    public void turnOn() {
        SerialCommunication.sendCommand("SmartRain on");
        this.status = true;
    }
    
    public void turnOff() {
        SerialCommunication.sendCommand("SmartRain off");
        this.status = false;
    }
    
    public boolean isOn() {
        return this.status;
    }
}
