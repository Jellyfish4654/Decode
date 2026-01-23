package org.firstinspires.ftc.teamcode.Tuners;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import org.firstinspires.ftc.teamcode.Framework.Hardware.PaddleMotor;

@TeleOp(name = "Paddle Motor Tuner", group = "3-Tuner")
public class PaddleMotorTuner extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        final DcMotorEx paddleMotor;
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        paddleMotor = hardwareMap.get(DcMotorEx.class, "paddleMotor");

        PaddleMotor paddle = new PaddleMotor(paddleMotor);

        paddle.setDown();
        double position = paddle.getTargetPosition();



        waitForStart();

        while (opModeIsActive())
        {
            telemetry.addData("target position", (int)position);
            telemetry.addData("energized", paddle.isEnergized());
            telemetry.addData("real position", paddle.getRealPosition());
            telemetry.update();
            position = Math.max(0,position);


            paddle.changePosition((int)position);

            if (gamepad1.dpad_left) {
                position -= 0.05;
            }
            if (gamepad1.dpad_right)  {
                position += 0.05;
            }

            if (gamepad1.triangle) {
                paddle.setUp();
                position = paddle.getTargetPosition();
            } else if (gamepad1.cross) {
                paddle.setDown();
                position = paddle.getTargetPosition();
            }
            
            if (gamepad1.psWasPressed()) {
                if (paddle.isEnergized()) {
                    paddle.deenergize();
                } else {
                    paddle.energize();
                }
            }
        }
    }
}