package org.firstinspires.ftc.teamcode.Tuners;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

@TeleOp(name = "Intake Tuner", group = "2-Tuner")
public class IntakeTuner extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException {
        final DcMotor intakeMotor;
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

        double offset = 0;
        boolean frozen = false;
        double power = 0;

        waitForStart();

        while (opModeIsActive()) {
            
            // dpad left / right for fine control
            if (gamepad1.dpadLeftWasPressed() && offset > -1) {
                offset -= 0.1;
            } else if (gamepad1.dpadRightWasPressed() && offset < 1) {
                offset += 0.1;
            }
            
            // triggers for quick control (left=negative, right=positive)
            double speed = Math.max(Math.min(offset + (gamepad1.right_trigger-gamepad1.left_trigger),1),-1);
            if (!frozen) {
                power = speed;
                intakeMotor.setPower(power);
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
            
            telemetry.addData("dpad offset", offset);
            telemetry.addData("(left) negative trigger", gamepad1.left_trigger);
            telemetry.addData("(right) positive trigger", gamepad1.right_trigger);
            telemetry.addData("combined speed", speed);
            telemetry.addData("applied power frozen", frozen);
            telemetry.addData("applied power", power);
            telemetry.update();
        }
    }
}