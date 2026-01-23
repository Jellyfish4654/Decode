package org.firstinspires.ftc.teamcode.Tuners;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

@TeleOp(name = "Gamepad Effects Tuner", group = "3-Tuner")
@Config
public class GamepadEffectsTuner extends LinearOpMode {
    
    public static double[] GAMEPAD_LED_RGB = {1, 1, 1};
    public static int GAMEPAD_LED_DURATION_MS = 10;
    
    
    public static double[] RUMBLE_POWERS = {1,1};
    public static int RUMBLE_DURATION_MS = 2000;
    public static int RUMBLE_BLIP_COUNT = 1;
    
    private long prevLoopNanoTime = 0;
    
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        
        waitForStart();
        
        while (opModeIsActive()) {
            if (gamepad1.circleWasPressed()) {
                gamepad1.setLedColor(GAMEPAD_LED_RGB[0], GAMEPAD_LED_RGB[1], GAMEPAD_LED_RGB[2], GAMEPAD_LED_DURATION_MS);
            }
            
            if (gamepad1.triangleWasPressed()) {
                gamepad1.rumble(RUMBLE_DURATION_MS);
            } else if (gamepad1.triangleWasReleased()) {
                gamepad1.stopRumble();
            }
            
            if (gamepad1.squareWasPressed()) {
                gamepad1.rumble(RUMBLE_POWERS[0], RUMBLE_POWERS[1], RUMBLE_DURATION_MS);
            } else if (gamepad1.squareWasReleased()) {
                gamepad1.stopRumble();
            }
            
            if (gamepad1.crossWasPressed()) {
                gamepad1.rumbleBlips(RUMBLE_BLIP_COUNT);
            } else if (gamepad1.crossWasReleased()) {
                gamepad1.stopRumble();
            }
            
            telemetry.addData("Rumbling", gamepad1.isRumbling());
            telemetry.addData("LED Button (Circle) Pressed", gamepad1.circle);
            telemetry.addData("Rumble Simple Button (Triangle) Pressed", gamepad1.triangle);
            telemetry.addData("Rumble Advanced Button (Square) Pressed", gamepad1.square);
            telemetry.addData("Rumble Blips Button (Cross) Pressed", gamepad1.cross);
            
            
            long currentNanoTime = System.nanoTime();
            long nanoPerLoop = currentNanoTime - prevLoopNanoTime;
            
            double loopsPerSec = 0;
            if (nanoPerLoop > 0) {
                loopsPerSec = 1e9 / nanoPerLoop;
            }
            
            telemetry.addLine();
            telemetry.addData("Millis Per Loop", (nanoPerLoop / 1e6));
            telemetry.addData("Loops Per Sec", loopsPerSec);
            telemetry.update();
            prevLoopNanoTime = currentNanoTime;
        }
    }
}