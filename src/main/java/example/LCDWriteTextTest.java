package example;

import ev3dev.hardware.display.SystemDisplay;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.Color;
import lejos.utility.Delay;

public class LCDWriteTextTest {

    public static GraphicsLCD lcd = SystemDisplay.initializeRealLCD();

    public static void main(final String[] args) {

        System.out.println("EV3 LCD Example");

        clear();
        writeMessage("Juanito");
        Delay.msDelay(5000);
        clear();
        writeMessage("Jorgito");
        Delay.msDelay(5000);
        clear();
        writeMessage("Pablito");
        Delay.msDelay(5000);
    }


    public static void writeMessage(final String message){
        //lcd.setColor(Color.BLACK);
        lcd.setColor(0,0,0);
        lcd.drawString(message, 50,50, 0);
        lcd.refresh();
    }

    public static void clear(){
        //lcd.setColor(Color.WHITE);
        lcd.setColor(255,255,255);
        lcd.fillRect(0,0, lcd.getWidth(), lcd.getHeight());

    }
}
