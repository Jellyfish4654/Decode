package org.firstinspires.ftc.teamcode.Framework.Hardware;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

@Config
public class SensorColor {
    private final RevColorSensorV3 colorSensor;
    public static float GAIN = 2.7F;

    // TODO: tune color sensor at mounted position
    public static double[][][] purpleRanges = {{{0,0,0},{80,40,100}},{{70,30,60},{150,80,150}},{{170,80,200},{255,180,230}},{{100,80,60},{170,120,150}}};
    public static double[][][] greenRanges = {{{50,70,40},{65,100,100}},{{50,90,40},{90,150,110}},{{20,130,20},{110,255,140}}};

    public SensorColor (RevColorSensorV3 sensor) {
        colorSensor = sensor;

    }

    private boolean isInRange(double[] color, double[] low, double[] high){
        return color[0] >= low[0] && color[1] >= low[1] && color[2] >= low[2] &&
                color[0] <= high[0] && color[1] <= high[1] && color[2] <= high[2];
    }

    public boolean isGreen() {
        double[] color = detectRGBA();
        for (double[][] range : greenRanges){
            if(isInRange(color,range[0],range[1])){
                return true;
            }
        }
        return false;
    }

    public boolean isPurple() {
        double[] color = detectRGBA();
        for (double[][] range : purpleRanges){
            if(isInRange(color,range[0],range[1])){
                return true;
            }
        }
        return false;
    }
    
    public double[] detectRGBA() {
        colorSensor.setGain(GAIN);
        NormalizedRGBA raw = colorSensor.getNormalizedColors();

        return new double[] {
                Math.min(255, (raw.red*255*1000)),
                Math.min(255, (raw.green*255*1000)),
                Math.min(255, (raw.blue*255*1000)),
                Math.min(255, (raw.alpha*255*1000))
        };
    }

}