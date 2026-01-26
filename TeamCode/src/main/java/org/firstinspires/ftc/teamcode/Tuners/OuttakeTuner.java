package org.firstinspires.ftc.teamcode.Tuners;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;

@TeleOp(name = "Outtake Tuner", group = "3-Tuner")
public class OuttakeTuner extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        final DcMotorEx outtakeMotor = hardwareMap.get(DcMotorEx.class, "outtakeMotor");
        final DcMotorEx guidingMotor = hardwareMap.get(DcMotorEx.class, "guidingMotor");
        
        outtakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        guidingMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        outtakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        outtakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double offset = 0;
        boolean frozen = false;
        double power = 0;
        VoltageSensor voltSensor = hardwareMap.get(VoltageSensor.class, ("Control Hub"));

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
            double vel = 1;
            if (!frozen) {
                vel = Math.abs(outtakeMotor.getVelocity() * 60 / 28);
                power = speed;
                outtakeMotor.setPower(power);
                if (power != 0) {
                    guidingMotor.setPower(1);
                } else {
                    guidingMotor.setPower(0);
                }
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
            telemetry.addData("volts", power * voltSensor.getVoltage());
            telemetry.addData("rpm", vel);
            telemetry.addData("kV", power*voltSensor.getVoltage()/vel);

            telemetry.update();
        }
    }
}