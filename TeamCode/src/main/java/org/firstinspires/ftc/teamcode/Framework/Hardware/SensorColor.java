package org.firstinspires.ftc.teamcode.Framework.Hardware;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

@Config
public class SensorColor {
    private final RevColorSensorV3 colorSensor;
    public static float GAIN = 2.5F;

    public static float[] minPurple = {81, 50, 55};
    public static float[] maxPurple = {255, 234, 255};
    public static float[] minGreen = {50, 106, 60};
    public static float[] maxGreen = {80, 140, 100};

    public SensorColor (RevColorSensorV3 sensor) {
        colorSensor = sensor;

    }

    public NormalizedRGBA detectColors () {
        return colorSensor.getNormalizedColors();
    }

    public boolean isGreen(){
        double[] color = scaleRGBA();
        return color[0] >= minGreen[0] && color[1] >= minGreen[1] && color[2] >= minGreen[2] &&
                color[0] <= maxGreen[0] && color[1] <= maxGreen[1] && color[2] <= maxGreen[2];
    }

    public boolean isPurple(){
        double[] color = scaleRGBA();
        return color[0] >= minPurple[0] && color[1] >= minPurple[1] && color[2] >= minPurple[2] &&
                color[0] <= maxPurple[0] && color[1] <= maxPurple[1] && color[2] <= maxPurple[2];
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