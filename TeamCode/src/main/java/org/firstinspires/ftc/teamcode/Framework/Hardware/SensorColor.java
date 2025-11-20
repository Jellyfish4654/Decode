package org.firstinspires.ftc.teamcode.Framework.Hardware;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorSensor;

public class SensorColor {
    private final RevColorSensorV3 colorSensor;

    public SensorColor (RevColorSensorV3 sensor) {
        colorSensor = sensor;
    }

    public void detectColor () {
        colorSensor.enableLed(true);
        int red = colorSensor.red();
        int blue = colorSensor.blue();
        int green = colorSensor.green();
        int alpha = colorSensor.alpha();

        int combined = colorSensor.argb();

    }

}
