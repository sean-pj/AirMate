# WeatherStation
Indoor and Outdoor Weather Station. Monitors Indoor air quality, temperature and humidity using sensors. Also monitors outdoor weather data from the Open-Mateo API.
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
