<div id="top" align="center">
  <img src="res/images/banner_smarthome.png" alt="Banner" width="100%">
</div>

<div align="center">

[![CONTRIBUTORS](https://img.shields.io/github/contributors/ngocmink/smart_home_arduino?label=CONTRIBUTORS&color=2F80ED&style=for-the-badge)](https://github.com/ngocmink/smart_home_arduino/graphs/contributors)
[![FORKS](https://img.shields.io/github/forks/ngocmink/smart_home_arduino?label=FORKS&color=27AE60&style=for-the-badge)](https://github.com/ngocmink/smart_home_arduino/network/members)
[![STARS](https://img.shields.io/github/stars/ngocmink/smart_home_arduino?label=STARS&color=F2C94C&style=for-the-badge)](https://github.com/ngocmink/smart_home_arduino/stargazers)
[![ISSUES](https://img.shields.io/github/issues/ngocmink/smart_home_arduino?label=ISSUES&color=EB5757&style=for-the-badge)](https://github.com/ngocmink/smart_home_arduino/issues)

</div>

<br/>

<div align="center">
  <img src="https://upload.wikimedia.org/wikipedia/commons/8/87/Arduino_Logo.svg" alt="Arduino Logo" width="140"/>

  <h2>SMART HOME ARDUINO PROJECT</h2>
  <h4>Team 11</h4>

  <p align="center">
    An IoT-based Home Automation System using <b>Arduino, C++ and OOP Java</b>.
    <br/>
    Designed for monitoring environment and home security.
    <br/><br/>
    <a href="https://youtu.be/YourDemoVideoLink"><b>▶ View Demo</b></a>
  </p>
</div>

# Table of contents :round_pushpin:
1. [Introduction](#Introduction)
2. [Installation](#Installation)
3. [Circuit & Flowchart](#Circuit-Flowchart)
4. [UML](#UML)
5. [Acknowledgments](#Acknowledgments)
6. [References](#References)

## 1. Introduction <a name="Introduction"></a> :house:

<div align="center">
  <img src="screenshots/Intro_Model.gif" alt="Smart Home Model">
</div>

<div style="text-align:justify">
This is our capstone project for the OOP course. "Smart Home Arduino" is a system designed to simulate a modern home automation environment. Unlike complex industrial systems, our project focuses on accessibility and cost-effectiveness using the Arduino platform. It allows users to monitor environmental conditions, control lighting remotely, and ensure security—all through a user-friendly interface. Let's make our home smarter!
</div>

### Team Members :technologist:

| Order |          Name           |     ID      |           Role                |                        Github account                         |
| :---: | :---------------------: | :---------: | :---------------------------: | :-----------------------------------------------------------: |
|   1   |      Nguyen Ngoc Minh   |  202416562  | Coding (Java), Circuit Design |                  https://github.com/ngocmink                  |
|   2   |      Nguyen Thuy Linh   |  202416547  | Coding (C++), Circuit Design  |                  https://github.com/linhnt-1113               |
|   3   |      Nguyen Dang Minh   |  202416567  | Coding (Java), Slides         |                    https://github.com/minhnoluv1234           |
|   4   |      Koy Viraksak       |  202400164  | Coding (Java)                 |                    https://github.com/hustsak                 |

## 2. Installation <a name="Installation"></a> :hammer_and_wrench:

1. **Hardware Setup**: Assemble the circuit following the diagram in the `docs` folder.
2. **Software Prerequisites**:
   * Install [Arduino IDE](https://www.arduino.cc/en/software).
   * Install required libraries (DHT sensor library, LiquidCrystal I2C).
   * Install `jSerialComm-2.11.2.jar` library for SerialPort
3. **Clone the repo**:
   ```sh
   git clone https://github.com/ngocmink/smart_home_arduino
4. Connect the kit and run code.

## 3. Circuit & Flowchart <a name="Circuit-Flowchart"></a> :electric_plug:

### 🔧 Circuit Diagram

<div align="center">
  <img src="docs/circuit_diagram.png" alt="Circuit Diagram" width="80%">
</div>

**Circuit Description:**
- Arduino acts as the central controller.
- DHT sensor measures temperature and humidity.
- Relay modules control electrical devices such as lights and fans.
- LCD displays real-time sensor data.
- Serial communication enables data exchange between Arduino and Java application.

---

### 🔄 System Flowchart

<div align="center">
  <img src="docs/flowchart.png" alt="System Flowchart" width="80%">
</div>

**Flow Explanation:**
1. Arduino collects data from sensors.
2. Sensor data is transmitted to the Java application via Serial Port.
3. Users interact with the GUI to monitor data and control devices.
4. Control commands are sent back to Arduino.
5. Arduino updates actuators and LCD display accordingly.

---

## 🧩 4. UML <a name="UML"></a> :triangular_ruler:

<div align="center">
  <img src="docs/uml_diagram.png" alt="UML Diagram" width="80%">
</div>

**UML Description:**
- The system is designed following Object-Oriented Programming principles.
- Abstract classes are used for sensors and devices.
- Inheritance simplifies code reuse.
- Polymorphism allows flexible control of different devices.

**Main Classes:**
- `Device` (abstract)
- `Light`, `Fan`, `Relay`
- `Sensor` (abstract)
- `DHTSensor`
- `SmartHomeController`
- `SerialPortManager`

---

## 5. Acknowledgments <a name="Acknowledgments"></a> :handshake:

We would like to sincerely thank:

- Hanoi University of Science and Technology (HUST)
- Object-Oriented Programming course instructors
- Arduino open-source community
- Group members for their dedication and teamwork

---

## 6. References <a name="References"></a> :books:

- Arduino Documentation: https://www.arduino.cc/reference/en
- jSerialComm Library: https://fazecast.github.io/jSerialComm
- DHT Sensor Library Documentation
- Website `Banlinhkien.com`

