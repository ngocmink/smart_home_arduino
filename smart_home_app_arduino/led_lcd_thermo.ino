#include <LiquidCrystal.h>
#include <dht11.h>
#define DHT11PIN 13

dht11 DHT11;

// -------------------------
// LCD (parallel, not I2C)
// -------------------------
#define LCD_RS 7
#define LCD_EN 6
#define LCD_D4 5
#define LCD_D5 4
#define LCD_D6 3
#define LCD_D7 2
LiquidCrystal lcd(LCD_RS, LCD_EN, LCD_D4, LCD_D5, LCD_D6, LCD_D7);

// -------------------------
#define BULB_PIN 13
#define BACKLIGHT_PIN 10   // set -1 if none

// -------------------------
// State variables
// -------------------------
bool blinkActive = false;
int  blinksTarget = 0, blinksDone = 0;
unsigned long lastToggleMs = 0;
bool bulbOnPhase = false;

unsigned long lastSensorSend = 0;
const unsigned long SENSOR_INTERVAL = 1000; // 1s

// Thermostat state
bool thermostatOn = false;
int currentTemp = 0;
int currentHumid = 0;

// -------------------------
void setup() {
  Serial.begin(9600);
  Serial.setTimeout(2000);

  pinMode(BULB_PIN, OUTPUT);
  digitalWrite(BULB_PIN, LOW);

  if (BACKLIGHT_PIN >= 0) {
    pinMode(BACKLIGHT_PIN, OUTPUT);
    digitalWrite(BACKLIGHT_PIN, HIGH);
  }

  lcd.begin(16, 2);
  lcd.print("SmartHome Ready");
}

// -------------------------
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

// -------------------------
void loop() {
  serviceBlinker();

  if (Serial.available()) {
    String command = Serial.readStringUntil('\n');
    command.trim();

    // ---------- LCD ----------
    if (command.equalsIgnoreCase("LCD_ON")) {
      lcd.display();
      if (BACKLIGHT_PIN >= 0) digitalWrite(BACKLIGHT_PIN, HIGH);
      lcd.clear(); lcd.print("LCD ON");
      Serial.println("LCD ON");
    }
    else if (command.equalsIgnoreCase("LCD_OFF")) {
      lcd.noDisplay();
      if (BACKLIGHT_PIN >= 0) digitalWrite(BACKLIGHT_PIN, LOW);
      Serial.println("LCD OFF");
    }
    else if (command.startsWith("LCD_PRINT:")) {
      String msg = command.substring(10); // remove prefix "LCD_PRINT:"
      lcd.clear();

      // Check if message contains newline ('\n') for multi-line display
      int newlineIndex = msg.indexOf('\n');
      if (newlineIndex != -1) {
        // Split into two lines
        String line1 = msg.substring(0, newlineIndex);
        String line2 = msg.substring(newlineIndex + 1);

        lcd.setCursor(0, 0);
        lcd.print(line1.substring(0, 16));  // limit to 16 chars
        lcd.setCursor(0, 1);
        lcd.print(line2.substring(0, 16));  // limit to 16 chars
      } else {
        // Single-line message, wrap if longer than 16 chars
        lcd.setCursor(0, 0);
        lcd.print(msg.substring(0, 16));
        if (msg.length() > 16) {
          lcd.setCursor(0, 1);
          lcd.print(msg.substring(16, 32));
        }
      }
    }

    // ---------- BULB ----------
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

    // ---------- THERMOSTAT ----------
    else if (command.equalsIgnoreCase("THERMO_ON")) {
      thermostatOn = true;
      Serial.println("OK: THERMO ON");
    }
    else if (command.equalsIgnoreCase("THERMO_OFF")) {
      thermostatOn = false;
      Serial.println("OK: THERMO OFF");
    }
    else if (command.equalsIgnoreCase("GET_THERMO")) {
      Serial.println();
      int chk = DHT11.read(DHT11PIN);
      Serial.println((float)DHT11.temperature, 2);
      delay(2000);
    }
    else if (command.equalsIgnoreCase("GET_HUMID")) {
      Serial.println();
      int chk = DHT11.read(DHT11PIN);
      Serial.println((float)DHT11.humidity, 2);
      delay(2000);
    }

    // ---------- UNKNOWN ----------
    else {
      Serial.println("ERR: UNKNOWN CMD");
    }
  }
}
