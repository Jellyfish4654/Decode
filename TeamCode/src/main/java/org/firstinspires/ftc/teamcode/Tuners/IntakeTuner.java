package org.firstinspires.ftc.teamcode.Tuners;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Paddle;

@TeleOp(name = "Intake Tuner", group = "Test")
public class IntakeTuner extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException {
        final DcMotor intakeMotor;
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

        double power;

        waitForStart();

        while (opModeIsActive()) {
            power = (gamepad1.left_stick_x+1)/2;
            if (gamepad1.a)
            {
                intakeMotor.setPower(0);
            }
            else if (gamepad1.x)
            {
                intakeMotor.setPower(power);
            }
        }
    }
}