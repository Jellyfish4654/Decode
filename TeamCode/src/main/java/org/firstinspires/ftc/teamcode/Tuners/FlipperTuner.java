package org.firstinspires.ftc.teamcode.Tuners;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Framework.Hardware.Flipper;

@TeleOp(name = "Flipper Tuner", group = "Test")
public class FlipperTuner extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        final Servo flipperServo;
        flipperServo = hardwareMap.get(Servo.class, "flipperServo");

        Flipper flipper = new Flipper(flipperServo);

        double position = flipper.getCurrentPosition();

        waitForStart();

        while (opModeIsActive())
        {

            flipper.changePosition(position);

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
                flipper.setPosUp();
                position = flipper.getCurrentPosition();
            }
            else if (gamepad1.x)
            {
                flipper.setPosDown();
                position = flipper.getCurrentPosition();
            }
        }
    }
}