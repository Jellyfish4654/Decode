package org.firstinspires.ftc.teamcode.Tuners;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.teamcode.Framework.Hardware.SensorColor;

@TeleOp(name = "Color Tuner", group = "Tuner")
public class ColorTuner extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException {
        final RevColorSensorV3 sensor;
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        sensor = hardwareMap.get(RevColorSensorV3.class, "colorSensor");

        SensorColor color = new SensorColor(sensor);

        double power;

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("colors:", color.detectColors());
            telemetry.update();
        }
    }
}