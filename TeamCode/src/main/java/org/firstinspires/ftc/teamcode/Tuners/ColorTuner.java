package org.firstinspires.ftc.teamcode.Tuners;

import android.graphics.Color;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.Framework.Hardware.SensorColor;

@TeleOp(name = "Color Tuner", group = "2-Tuner")
public class ColorTuner extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException {
        final RevColorSensorV3 sensor;
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        sensor = hardwareMap.get(RevColorSensorV3.class, "colorSensor");

        SensorColor color = new SensorColor(sensor);
        double[] detectedColors;
        double[] hsvValues;

        waitForStart();

        while (opModeIsActive()) {
            detectedColors = color.detectRGBA();
            hsvValues = color.detectHSV();

            telemetry.addData("R", detectedColors[0]);
            telemetry.addData("G", detectedColors[1]);
            telemetry.addData("B", detectedColors[2]);
            telemetry.addData("A", detectedColors[3]);

            telemetry.addData("H", hsvValues[0]);
            telemetry.addData("S", hsvValues[1]);
            telemetry.addData("V", hsvValues[2]);

            telemetry.addData("Is green?",color.isGreen());
            telemetry.addData("Is purple?",color.isPurple());
            telemetry.addData("Artifact", color.getArtifact());
            telemetry.update();
        }
    }
}