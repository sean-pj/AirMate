# Air Mate
Automated air quality monitor and control system. Monitors indoor air quality, temperature and humidity using sensors. Also monitors outdoor weather data from the Open-Mateo API.
Reactively turns on a servo motor, to turn a fan on and off in response to this data.
# Technical Specifications
## Hardware
* Arduino grove board hardware to control MQ135 and main state machine.
* Arduino Nano used to control DHT22 and servo.
* MQ135 Air Quality Sensor
* DHT22 Temperature and Humidity Sensor
* Micro Continuous Servo
## Software
* Firmata4j (Java): Houses main state machine and controls servo logic and monitors sensor data.
* Arduino IDE (C++): Sends DHT22 data to Java program through jSerialPort. Also receives commands to control servo motor.
* Princeton STD: Graphs sensor data
* Google simple Json: Interpret API data from Open-Mateo
##Images
![Board](https://github.com/user-attachments/assets/0253c2ac-74a3-4943-af20-57b29ca865e0)
![Servo](https://github.com/user-attachments/assets/057b2415-bc35-4e9b-bab4-3ccf59bb52a4)
