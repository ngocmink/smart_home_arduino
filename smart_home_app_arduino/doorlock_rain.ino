#include <RFID.h>
#include <SPI.h>
#include <LiquidCrystal_I2C.h>
#include <Servo.h>

#define RAIN_SENSOR A0
#define RAIN_BUZZER 8 
#define SS_PIN 10
#define RST_PIN 9
#define SERVO_PIN 7
#define RED_LED 6
#define GREEN_LED 5

const int RAIN_THRESHOLD = 1000;
long DOOR_OPEN_TIME = 5000; 

Servo servo;
RFID rfid(SS_PIN, RST_PIN);
LiquidCrystal_I2C lcd(0x27, 16, 2);

int masterKey[5] = {83, 192, 191, 42, 6}; 
unsigned long doorTimer = 0;              
bool isDoorOpen = false;
bool rainSystemActive = true; 

void setup() {
  Serial.begin(9600);
  SPI.begin();
  rfid.init();

  pinMode(RAIN_SENSOR, INPUT);
  pinMode(RAIN_BUZZER, OUTPUT);
  pinMode(RED_LED, OUTPUT);
  pinMode(GREEN_LED, OUTPUT);
  
  lcd.init();
  lcd.backlight();
  lcd.clear();
  lcd.setCursor(0,0);
  lcd.print("System Ready");
  
  servo.attach(SERVO_PIN);
  lockDoor(); 
  
  Serial.println("System Initialized...");
}

void loop() {
  // rain check
  monitorRainBackground();

  // rain -> door closes
  if (isDoorOpen && (millis() - doorTimer >= DOOR_OPEN_TIME)) {
    lockDoor();
    digitalWrite(GREEN_LED, LOW);
  }

  // rfid check
  if (rfid.isCard() && rfid.readCardSerial()) {
    handleRFID();
    rfid.halt(); 
  }

  if (Serial.available()) {
    String command = Serial.readStringUntil('\n');
    command.trim(); 

    if (command.equalsIgnoreCase("RAIN_ON")) {
      rainSystemActive = true;
      Serial.println("ACK: Rain System ON");
    }
    else if (command.equalsIgnoreCase("RAIN_OFF")) {
      rainSystemActive = false;
      digitalWrite(RAIN_BUZZER, LOW); 
      lcd.setCursor(0, 0);
      lcd.print("Rain Mon: OFF   ");
      lcd.setCursor(0, 1);
      lcd.print("                   ");
      Serial.println("ACK: Rain System OFF");
    }

    else if (command.equalsIgnoreCase("GET_RAIN")) {
       int val = analogRead(RAIN_SENSOR);
       if (val < RAIN_THRESHOLD) {
         Serial.println("RAINING"); 
       } else {
         Serial.println("NO RAIN");
       }
    }

    else if (command.equalsIgnoreCase("DOOR_LOCK")) {
      lockDoor();
      DOOR_OPEN_TIME = 5000;
      Serial.println("Door Locked.");
    }

    else if (command.equalsIgnoreCase("DOOR_UNLOCK")) {
      unlockDoor();
      DOOR_OPEN_TIME = 99999999;
      Serial.println("Door Unlocked.");
    }
    
    
    else if (command.length() > 0) {
      Serial.print("ERR: Unknown - ");
      Serial.println(command);
    }
  }
}

void monitorRainBackground() {
  if (!rainSystemActive) return; 

  int sensorValue = analogRead(RAIN_SENSOR);

  // lcd print
  static unsigned long lastLcdUpdate = 0;
  if (millis() - lastLcdUpdate > 500) {
    char bufNum[17];
    snprintf(bufNum, sizeof(bufNum), "Rain Val: %4d  ", sensorValue);
    lcd.setCursor(0, 1);
    lcd.print(bufNum);
    lastLcdUpdate = millis();
    
    // buzz if rain
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
    
    doorTimer = millis(); 
    isDoorOpen = true;
    
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