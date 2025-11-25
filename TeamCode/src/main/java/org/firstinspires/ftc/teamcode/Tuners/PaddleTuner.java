package org.firstinspires.ftc.teamcode.Tuners;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Paddle;

@TeleOp(name = "Paddle Tuner", group = "Test")
public class PaddleTuner extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        final Servo flipperServo;
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        flipperServo = hardwareMap.get(Servo.class, "paddleServo");

        Paddle paddle = new Paddle(flipperServo);

        double position = paddle.getCurrentPosition();

        waitForStart();

        while (opModeIsActive())
        {
            telemetry.addData("target position", position);
            telemetry.addData("position", paddle.getCurrentPosition());
            telemetry.update();

            paddle.changePosition(position);

            if (gamepad1.dpad_left)
            {
                position -= 0.0005;
            }
            if (gamepad1.dpad_right)
            {
                position += 0.0005;
            }

            if (gamepad1.a)
            {
                paddle.setPosUp();
                position = paddle.getCurrentPosition();
            }
            else if (gamepad1.x)
            {
                paddle.setPosDown();
                position = paddle.getCurrentPosition();
            }
        }
    }
}