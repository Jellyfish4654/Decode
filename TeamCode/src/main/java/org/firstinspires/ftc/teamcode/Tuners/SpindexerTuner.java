package org.firstinspires.ftc.teamcode.Tuners;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.teamcode.Framework.Hardware.Spindexer;

@TeleOp(name = "Spindexer Tuner", group = "3-Tuner")
public class SpindexerTuner extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        final Servo spindexerServo;
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        spindexerServo = hardwareMap.get(Servo.class, "spindexerServo");
        
        Spindexer spindexer = new Spindexer(spindexerServo);
        
        double position = spindexer.getCurrentPosition();
        
        
        
        waitForStart();
        
        while (opModeIsActive())
        {
            telemetry.addData("position", spindexer.getCurrentPosition());
            telemetry.addData("last slot", spindexer.getCurrentSlot());
            telemetry.update();
            
            spindexer.changePosition(position);
            
            // dpad left / right for finding positions
            if (gamepad1.dpad_left && position > 0) {
                position -= 0.0005;
            }
            if (gamepad1.dpad_right && position < 1) {
                position += 0.0005;
            }
            
            // gamepad buttons for slots clockwise from bottom (1=cross, 2=square, 3=triangle)
            // hold right bumper for outtake positions, otherwise intake
            if (gamepad1.cross && gamepad1.right_bumper) {
                spindexer.setSlotOut(1);
                position = spindexer.getCurrentPosition();
            } else if (gamepad1.square && gamepad1.right_bumper) {
                spindexer.setSlotOut(2);
                position = spindexer.getCurrentPosition();
            } else if (gamepad1.triangle && gamepad1.right_bumper) {
                spindexer.setSlotOut(3);
                position = spindexer.getCurrentPosition();
            } else if (gamepad1.cross) {
                spindexer.setSlotIn(1);
                position = spindexer.getCurrentPosition();
            } else if (gamepad1.square) {
                spindexer.setSlotIn(2);
                position = spindexer.getCurrentPosition();
            } else if (gamepad1.triangle) {
                spindexer.setSlotIn(3);
                position = spindexer.getCurrentPosition();
            }
        }
    }
}