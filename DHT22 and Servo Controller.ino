//Beginning of referenced code
//How to use DHT-22 Sensor - Arduino Tutorial, codebender_cc, approx 2015
//https://www.instructables.com/How-to-use-DHT-22-sensor-Arduino-Tutorial/
//Arduino Servo Motors, cornelam, approx 2015
//https://www.instructables.com/Arduino-Servo-Motors/
#include <Arduino.h>
#include <DHT.h>;
#include <Servo.h>
#define DHTPIN 2     // what pin we're connected to
#define DHTTYPE DHT22
DHT dht(DHTPIN, DHTTYPE); 
int chk;
float hum;  //Stores humidity value
float temp; //Stores temperature value
Servo myservo; 

int pos = 0;
int center = 87;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  dht.begin();

  // The servo control wire is connected to Arduino D2 pin.
  myservo.attach(8);
  // Servo is stationary.
  myservo.write(center);
}

void loop() {
delay(1000);
//Read data and store it to variables hum and temp
    hum = dht.readHumidity();
    temp= dht.readTemperature();
    //Print temp and humidity values to serial monitor
    Serial.print(hum);
    Serial.print(",");
    Serial.print(temp - 3);
    Serial.println();
//0 Reverse center stationary 180 forward
  auto data = Serial.read();
  if(data ==(byte) 1){
  myservo.write(100);
  delay(450);
  myservo.write(center);
  delay(1000);
  myservo.write(75);
  delay(380);
  myservo.write(center);

  } else if(data ==(byte) -1){
    //Servo spins backward for 1 second
  myservo.write(70);
    delay(550);
    myservo.write(center);
    delay(1000);
    myservo.write(97);
    delay(345);
    myservo.write(center);
  }
  else{
    myservo.write(center);
  }
}
