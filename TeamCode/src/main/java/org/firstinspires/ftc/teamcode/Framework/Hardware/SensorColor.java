package org.firstinspires.ftc.teamcode.Framework.Hardware;

import android.graphics.Color;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class SensorColor {
    private final RevColorSensorV3 colorSensor;

    private final float[] hsvValues = new float[3];

    public SensorColor (RevColorSensorV3 sensor) {
        colorSensor = sensor;
    }

    public NormalizedRGBA detectColors () {
        return colorSensor.getNormalizedColors();
    }

}
