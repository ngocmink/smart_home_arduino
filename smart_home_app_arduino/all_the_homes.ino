#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <dht11.h>
#include <RFID.h>
#include <SPI.h>
#include <Servo.h>

#define DHT11PIN 4       
#define BULB_PIN 3       
#define RAIN_SENSOR A0
#define RAIN_BUZZER 8 
#define SS_PIN 10
#define RST_PIN 9
#define SERVO_PIN 7
#define RED_LED 6
#define GREEN_LED 5

dht11 DHT11;
RFID rfid(SS_PIN, RST_PIN);
LiquidCrystal_I2C lcd(0x27, 16, 2);
Servo servo;

// bulb 
bool blinkActive = false;
int blinksTarget = 0, blinksDone = 0;
unsigned long lastToggleMs = 0;
bool bulbOnPhase = false;

// thermo, rain
bool thermostatOn = false;
const unsigned long SENSOR_INTERVAL = 1000;
const int RAIN_THRESHOLD = 1000;

// rfid
int masterKey[5] = {83, 192, 191, 42, 6}; 
unsigned long doorTimer = 0;
long DOOR_OPEN_TIME = 5000;
bool isDoorOpen = false;
bool rainSystemActive = true;

unsigned long lcdOverrideTimer = 0;      
const unsigned long OVERRIDE_TIME = 5000; 
bool isDisplayingMessage = false;        

void setup() {
  Serial.begin(9600);
  SPI.begin();
  rfid.init();
  
  pinMode(BULB_PIN, OUTPUT);
  digitalWrite(BULB_PIN, LOW);
  pinMode(RAIN_SENSOR, INPUT);
  pinMode(RAIN_BUZZER, OUTPUT);
  pinMode(RED_LED, OUTPUT);
  pinMode(GREEN_LED, OUTPUT);

  lcd.init();
  lcd.backlight();
  lcd.clear();
  lcd.print("SmartHome Ready");

  servo.attach(SERVO_PIN);
  lockDoor();

  Serial.println("System Initialized...");
}

// bulb blink
void serviceBlinker() {
  if (!blinkActive) return;
  unsigned long now = millis();
  if (now - lastToggleMs >= 300) {
    lastToggleMs = now;
    if (!bulbOnPhase) {
      digitalWrite(BULB_PIN, HIGH);
      bulbOnPhase = true;
    } else {
      digitalWrite(BULB_PIN, LOW);
      bulbOnPhase = false;
      blinksDone++;
      if (blinksDone >= blinksTarget) {
        blinkActive = false;
        Serial.print("BLINKS:");
        Serial.println(blinksDone);
      }
    }
  }
}

// rain check
void monitorRainBackground() {
  if (!rainSystemActive) return; 

  if (isDisplayingMessage) {
    if (millis() - lcdOverrideTimer < OVERRIDE_TIME) {
      return; 
    } else {
      isDisplayingMessage = false; 
    }
  }

  int sensorValue = analogRead(RAIN_SENSOR);
  static unsigned long lastLcdUpdate = 0;
  
  if (millis() - lastLcdUpdate > 500) {
    lastLcdUpdate = millis();
    
    char bufNum[17];
    snprintf(bufNum, sizeof(bufNum), "Rain Val: %4d  ", sensorValue);
    lcd.setCursor(0, 1);
    lcd.print(bufNum);
    
    if (sensorValue < RAIN_THRESHOLD) {
      lcd.setCursor(0, 0);
      lcd.print("Status: RAINING "); 
      digitalWrite(RAIN_BUZZER, HIGH);
    } else {
      lcd.setCursor(0, 0);
      lcd.print("Status: NO RAIN ");
      digitalWrite(RAIN_BUZZER, LOW);
    }
  }
}

// servo
void lockDoor() {
  servo.write(0); 
  isDoorOpen = false;
  digitalWrite(GREEN_LED, LOW);
}

void unlockDoor() {
  servo.write(135); 
  isDoorOpen = true;
  doorTimer = millis(); 
  digitalWrite(GREEN_LED, HIGH);
}

// rfid
void handleRFID() {
  bool valid = true;
  for (int i = 0; i < 5; i++) {
    if (masterKey[i] != rfid.serNum[i]) {
      valid = false;
      break;
    }
  }

  if (valid) {
    Serial.println("Access Granted");
    digitalWrite(GREEN_LED, HIGH);
    digitalWrite(RED_LED, LOW);
    unlockDoor();
    lcd.setCursor(0, 0);
    lcd.print("Access Granted  ");
    delay(1000); 
  } else {
    Serial.println("Access Denied");
    digitalWrite(RED_LED, HIGH);
    lcd.setCursor(0, 0);
    lcd.print("Access Denied   ");
    delay(1000);
    digitalWrite(RED_LED, LOW); 
  }
}

void loop() {
  serviceBlinker();
  monitorRainBackground();

  if (isDoorOpen && (millis() - doorTimer >= DOOR_OPEN_TIME)) {
    lockDoor();
  }

  if (rfid.isCard() && rfid.readCardSerial()) {
    handleRFID();
    rfid.halt(); 
  }

  if (Serial.available()) {
    String command = Serial.readStringUntil('\n');
    command.trim();

    if (command.equalsIgnoreCase("LCD_ON")) {
      lcd.backlight();
      lcd.display();
      lcd.clear(); lcd.print("LCD ON");
      Serial.println("LCD ON");
    }
    else if (command.equalsIgnoreCase("LCD_OFF")) {
      lcd.noBacklight();
      lcd.noDisplay();
      Serial.println("LCD OFF");
    }
    else if (command.startsWith("LCD_PRINT:")) {
      String msg = command.substring(10);
      lcd.clear();

      isDisplayingMessage = true;
      lcdOverrideTimer = millis();
      int newlineIndex = msg.indexOf('\\');
      if (newlineIndex != -1) {
        lcd.setCursor(0, 0);
        lcd.print(msg.substring(0, newlineIndex).substring(0, 16));
        lcd.setCursor(0, 1);
        lcd.print(msg.substring(newlineIndex + 1).substring(0, 16));
      } else {
        lcd.setCursor(0, 0);
        lcd.print(msg.substring(0, 16));
        if (msg.length() > 16) {
          lcd.setCursor(0, 1);
          lcd.print(msg.substring(16, 32));
        }
      }
    }

// bulb
    else if (command.equalsIgnoreCase("BULB_ON")) {
      blinkActive = false;
      digitalWrite(BULB_PIN, HIGH);
      Serial.println("OK: BULB ON");
    }
    else if (command.equalsIgnoreCase("BULB_OFF")) {
      blinkActive = false;
      digitalWrite(BULB_PIN, LOW);
      Serial.println("OK: BULB OFF");
    }
    else if (command.startsWith("BULB_BLINK:")) {
      int n = command.substring(11).toInt();
      if (n <= 0) n = 3;
      blinksTarget = n; blinksDone = 0;
      blinkActive = true; bulbOnPhase = false;
      lastToggleMs = millis();
      Serial.println("OK: BLINK STARTED");
    }

// thermo
    else if (command.equalsIgnoreCase("GET_THERMO")) {
      DHT11.read(DHT11PIN);
      Serial.println((float)DHT11.temperature, 2);
    }
    else if (command.equalsIgnoreCase("GET_HUMID")) {
      DHT11.read(DHT11PIN);
      Serial.println((float)DHT11.humidity, 2);
    }
    else if (command.equalsIgnoreCase("GET_RAIN")) {
       int val = analogRead(RAIN_SENSOR);
       Serial.println(val < RAIN_THRESHOLD ? "RAINING" : "NO RAIN");
    }
    else if (command.equalsIgnoreCase("RAIN_ON")) {
      rainSystemActive = true;
      Serial.println("ACK: Rain System ON");
    }
    else if (command.equalsIgnoreCase("RAIN_OFF")) {
      rainSystemActive = false;
      digitalWrite(RAIN_BUZZER, LOW);
      Serial.println("ACK: Rain System OFF");
    }

// door
    else if (command.equalsIgnoreCase("DOOR_LOCK")) {
      lockDoor();
      DOOR_OPEN_TIME = 5000;
      Serial.println("Door Locked.");
    }
    else if (command.equalsIgnoreCase("DOOR_UNLOCK")) {
      unlockDoor();
      DOOR_OPEN_TIME = 999999;
      Serial.println("Door Unlocked.");
    }
    else {
      Serial.print("ERR: Unknown - ");
      Serial.println(command);
    }
  }
}