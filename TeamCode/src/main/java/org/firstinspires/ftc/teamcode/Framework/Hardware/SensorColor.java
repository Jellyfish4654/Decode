package org.firstinspires.ftc.teamcode.Framework.Hardware;

import android.graphics.Color;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.Framework.Params.Artifact;

@Config
public class SensorColor {
    private final RevColorSensorV3 colorSensor;
    public static float GAIN = 0.1F;
    public static boolean USE_HSV = true;
    
    public static int[] HSV_PURPLE_RANGE = {210, 280};
    public static int[] HSV_GREEN_RANGE = {120, 180};
    public static int HSV_MIN_VALUE = 55;
    
    
    // current graph: https://www.desmos.com/3d/g1f8te9yqm
    public static double[][][] RGB_PURPLE_RANGES = {{{0,0,0},{80,40,100}},{{70,10,60},{150,80,180}},{{170,80,150},{255,180,230}},{{100,80,60},{170,120,150}}};
    public static double[][][] RGB_GREEN_RANGES = {{{25,48,15},{65,100,100}},{{20,90,40},{90,150,110}},{{20,130,20},{110,255,140}}};

    public SensorColor (RevColorSensorV3 sensor) {
        colorSensor = sensor;
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

    public double[] detectHSV() {
        colorSensor.setGain(GAIN);
        float [] hsvValues = new float[3];
        Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);
        return new double[] { //may need to add scaling like for rgba
                hsvValues[0],
                hsvValues[1]*100,
                hsvValues[2]*100
        };
    }

    
    private boolean isInRange(double[] color, double[] low, double[] high){
        return color[0] >= low[0] && color[1] >= low[1] && color[2] >= low[2] &&
                color[0] <= high[0] && color[1] <= high[1] && color[2] <= high[2];
    }

    public boolean isGreen() {
        if (USE_HSV) {
            double[] hsv = detectHSV();
            return hsv[0] > HSV_GREEN_RANGE[0] && hsv[0] < HSV_GREEN_RANGE[1] && hsv[2] > HSV_MIN_VALUE;
        } else {
            double[] rgba = detectRGBA();
            for (double[][] range : RGB_GREEN_RANGES) {
                if (isInRange(rgba, range[0], range[1])) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isPurple() {
        if (USE_HSV) {
            double[] hsv = detectHSV();
            return hsv[0] > HSV_PURPLE_RANGE[0] && hsv[0] < HSV_PURPLE_RANGE[1] && hsv[2] > HSV_MIN_VALUE;
        } else {
            double[] rgba = detectRGBA();
            for (double[][] range : RGB_PURPLE_RANGES) {
                if (isInRange(rgba, range[0], range[1])) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public Artifact getArtifact() {
        if (isGreen()) {
            return Artifact.GREEN;
        } else if (isPurple()) {
            return Artifact.PURPLE;
        } else {
            return Artifact.NONE;
        }
    }
}