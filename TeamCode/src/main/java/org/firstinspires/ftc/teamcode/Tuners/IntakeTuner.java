package org.firstinspires.ftc.teamcode.Tuners;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

@TeleOp(name = "Intake Tuner", group = "Test")
public class IntakeTuner extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException {
        final DcMotor intakeMotor;
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

        double offset = 0;
        double power = 0;

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.dpadLeftWasPressed() && offset > 0) {
                offset -= 0.05;
            } else if (gamepad1.dpadRightWasPressed() && offset < 1) {
                offset += 0.05;
            }
            
            power = offset+gamepad1.right_trigger;
            
            intakeMotor.setPower(power);
            telemetry.addData("button offset", offset);
            telemetry.addData("trigger", gamepad1.right_trigger);
            telemetry.addData("final power", power);
            telemetry.update();
        }
    }
}