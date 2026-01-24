package org.firstinspires.ftc.teamcode.Tuners;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

@TeleOp(name = "Outtake PID Tuner", group = "3-Tuner")
@Config
public class OuttakePIDTuner extends LinearOpMode {
    
    public static double P = 0;
    public static double I = 0;
    public static double D = 0;
    public static double F = 0;
    public static DcMotorEx.ZeroPowerBehavior ZERO_POWER_BEHAVIOR = DcMotor.ZeroPowerBehavior.FLOAT; // not sure what this should be for pid
    public static DcMotorEx.Direction DIRECTION = DcMotorEx.Direction.REVERSE; // power wires must stay flipped...
    // then change this so right trigger goes the normal outtake direction
    public static double TRIGGER_MULTIPLIER = 10_000; // max +/- value achieved by triggers
    public static double OFFSET_BUTTON_STEP = 100; // +/- steps of offset for each press of left/right dpad
    
    
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        final DcMotorEx outtakeMotor = hardwareMap.get(DcMotorEx.class, "outtakeMotor");
        
        outtakeMotor.setZeroPowerBehavior(ZERO_POWER_BEHAVIOR);
        outtakeMotor.setDirection(DIRECTION);
        outtakeMotor.setVelocityPIDFCoefficients(P, I, D, F);
        
        outtakeMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        outtakeMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        
        double offset = 0;
        boolean frozen = false;
        double velocity = 0;
        
        waitForStart();
        
        while (opModeIsActive()) {
            
            // dpad left / right for fine control (no bounds!)
            if (gamepad1.dpadLeftWasPressed()) {
                offset -= OFFSET_BUTTON_STEP;
            } else if (gamepad1.dpadRightWasPressed()) {
                offset += OFFSET_BUTTON_STEP;
            }
            
            // triggers for quick control (left=negative, right=positive)
            double speed = offset + ((gamepad1.right_trigger-gamepad1.left_trigger)*TRIGGER_MULTIPLIER);
            if (!frozen) {
                velocity = speed;
                outtakeMotor.setVelocity(velocity);
            }
            
            // Circle button to freeze / unfreeze (imagine circle as "hold")
            if (gamepad1.circleWasPressed()) {
                frozen = !frozen;
            }
            
            // Cross button to reset offset to 0 (imagine cross as "stop")
            if (gamepad1.cross) {
                frozen = false;
                offset = 0;
            }
            
            // Triangle button to update DcMotorEx variables (image triangle as "send")
            // This is needed cuz constantly setting these parameters prob messes up the controller
            if (gamepad1.triangleWasPressed()) {
                outtakeMotor.setZeroPowerBehavior(ZERO_POWER_BEHAVIOR);
                outtakeMotor.setDirection(DIRECTION);
                outtakeMotor.setVelocityPIDFCoefficients(P, I, D, F);
            }
            
            telemetry.addLine("Controller Input:");
            telemetry.addData("\tDpad Offset", offset);
            telemetry.addData("\t(Left) Negative Trigger", gamepad1.left_trigger);
            telemetry.addData("\t(Right) Positive Trigger", gamepad1.right_trigger);
            telemetry.addData("\tCombined Speed (Triggers x"+TRIGGER_MULTIPLIER+")", speed);
            
            telemetry.addLine("\nDcMotorEx Set Values:");
            telemetry.addData("\tSet Velocity is Frozen", frozen);
            telemetry.addData("\tSet Velocity (ticks/sec)", velocity);
            telemetry.addLine("\tNOTE: to update the values below, use FTC Dash, \nthen press the Triangle button.");
            telemetry.addData("\tDirection", outtakeMotor.getDirection());
            telemetry.addData("\tZero Power Behavior", outtakeMotor.getZeroPowerBehavior());
            telemetry.addData("\tP", outtakeMotor.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER).p);
            telemetry.addData("\tI", outtakeMotor.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER).i);
            telemetry.addData("\tD", outtakeMotor.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER).d);
            telemetry.addData("\tF", outtakeMotor.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER).f);
            
            
            telemetry.addLine("\nActual Motor State:");
            telemetry.addData("\tEncoder Position", outtakeMotor.getCurrentPosition());
            telemetry.addData("\tEncoder Velocity (ticks/sec)", -outtakeMotor.getVelocity());
            telemetry.addData("\tApplied Power", outtakeMotor.getPower()); // idk if this works
            
            telemetry.update();
        }
    }
}