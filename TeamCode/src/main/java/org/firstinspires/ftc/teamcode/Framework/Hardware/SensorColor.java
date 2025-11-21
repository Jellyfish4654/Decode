package org.firstinspires.ftc.teamcode.Framework.Hardware;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class SensorColor {
    private final RevColorSensorV3 colorSensor;

    public SensorColor (RevColorSensorV3 sensor) {
        colorSensor = sensor;
    }

    public NormalizedRGBA detectColors () {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        return colors;
    }

}
