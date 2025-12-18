package org.firstinspires.ftc.teamcode.Framework.Hardware;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

@Config
public class SensorColor {
    private final RevColorSensorV3 colorSensor;
    public static float GAIN = 2.5F;

    private final float[] hsvValues = new float[3];

    public SensorColor (RevColorSensorV3 sensor) {
        colorSensor = sensor;

    }

    public NormalizedRGBA detectColors () {
        return colorSensor.getNormalizedColors();
    }

    public double[] scaleRGBA () {
        colorSensor.setGain(GAIN);
        NormalizedRGBA raw = detectColors();

        return new double[] {
                Math.min(255, (raw.red*255*1000)),
                Math.min(255, (raw.green*255*1000)),
                Math.min(255, (raw.blue*255*1000)),
                Math.min(255, (raw.alpha*255*1000))
        };
    }

}
