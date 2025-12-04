package org.firstinspires.ftc.teamcode.Tuners;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.teamcode.Framework.Hardware.Spindexer;

@TeleOp(name = "Spindexer Tuner", group = "Test")
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
            telemetry.addData("target position", position);
            telemetry.addData("position", spindexer.getCurrentPosition());
            telemetry.update();
            
            spindexer.changePosition(position);
            
            if (gamepad1.dpad_left && position > 0) {
                position -= 0.0005;
            }
            if (gamepad1.dpad_right && position < 1) {
                position += 0.0005;
            }
            
            if (gamepad1.a && gamepad1.x) {
                spindexer.setSlot(-1);
                position = spindexer.getCurrentPosition();
            } else if (gamepad1.b && gamepad1.x) {
                spindexer.setSlot(-2);
                position = spindexer.getCurrentPosition();
            } else if (gamepad1.y && gamepad1.x) {
                spindexer.setSlot(-3);
                position = spindexer.getCurrentPosition();
            } else if (gamepad1.a) {
                spindexer.setSlot(1);
                position = spindexer.getCurrentPosition();
            } else if (gamepad1.b) {
                spindexer.setSlot(2);
                position = spindexer.getCurrentPosition();
            } else if (gamepad1.y) {
                spindexer.setSlot(3);
                position = spindexer.getCurrentPosition();
            }
        }
    }
}