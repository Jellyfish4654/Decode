package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Framework.BaseOpMode;

@TeleOp(name = "JellyTele", group = "1-OpMode")
public class JellyTele extends BaseOpMode {
    private final double PRECISION_MULTIPLIER_LOW = 0.35;
    private final double PRECISION_MULTIPLIER_HIGH = 0.2;
    private final double DEADBAND_VALUE = 0.02;
    private final double STRAFE_ADJUSTMENT_FACTOR = (14.0 / 13.0);
    private double SPINDEXER_DELAY = 0.55*1000; // in millis

    private boolean isSpinningOut = false;
    private boolean isSpinningIn = false;
    private long spindexerStartTime = 0;

    
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        waitForStart();
        while (opModeIsActive()) {
            updateDrive(calculatePrecisionMultiplier());
            updateIntake(); // temporary
            //updateAux();
            telemetry.update();
        }
    }
    
    // TODO: temp -- remove this once intake is fully integrated and no longer needed
    private void updateIntake() {
        if (controller.intake()) {
            intake.on();
        } else if (intake.isOn()) {
            intake.off();
        }
    }

    // TODO: add more components while managing/stopping components with more delays
    private void updateAux() {
        boolean ready = System.currentTimeMillis()-spindexerStartTime >= SPINDEXER_DELAY;
        if (isSpinningIn && ready) {
            intake.on();
            isSpinningIn = false;
        } else if (isSpinningOut && ready) {
            outtake.on();
            paddle.setUp();
            isSpinningOut = false;
        } else if (!paddle.getState()) { // check that paddle is down for redundancy
            if (controller.intakePressed()) {
                spinIntake();
            } else if (controller.outGreenPressed()) {
                spinOuttake(1);
            } else if (controller.outPurplePressed()) {
                spinOuttake(2);
            }
        }
        
        telemetry.addLine();
        telemetry.addLine("Aux:");
        telemetry.addData("\tSpinningIn", isSpinningIn);
        telemetry.addData("\tSpinningOut", isSpinningOut);
        telemetry.addData("\tSpindexerPos", spindexer.getCurrentSlot());
        telemetry.addData("\tIntakeOn", intake.isOn());
        telemetry.addData("\tOuttakeOn", intake.isOn());
        telemetry.addData("\tPaddleUp", paddle.getState());
    }
    
    private void spinIntake() {
        int slot = spindexer.findSlot(0);
        if (slot == 0) {
            controller.rumble(200);
            return;
        }
        paddle.setDown(); // backup safety
        spindexer.setSlotIn(slot);
        spindexerStartTime = System.currentTimeMillis();
        isSpinningIn = true;
    }

    private void spinOuttake(int artifactID) {
        int slot = spindexer.findSlot(artifactID);
        if (slot == 0) {
            controller.rumble(200);
            return;
        }
        paddle.setDown(); // backup safety
        spindexer.setSlotOut(slot);
        spindexer.setContents(slot, 0);
        spindexerStartTime = System.currentTimeMillis();
        isSpinningOut = true;
    }
    
    
    // ↓ -------------- ↓ -------------- ↓ DRIVETRAIN ↓ -------------- ↓ -------------- ↓
    private enum DriveMode
    {
        MECANUM,
        FIELDCENTRIC
    }
    protected DriveMode driveMode = DriveMode.MECANUM;
    private void updateDrive(double precisionMultiplier) {
        if (controller.driveModePressed()) {
            if (driveMode == DriveMode.MECANUM) {
                driveMode = DriveMode.FIELDCENTRIC;
            } else {
                driveMode = DriveMode.MECANUM;
            }
        }
        updateDriveMode(precisionMultiplier);
    }

    private void updateDriveMode(double precisionMultiplier)
    {
        double[] motorSpeeds = null;
        switch (driveMode)
        {
            case MECANUM:
                motorSpeeds = MecanumDrive();
                break;
            case FIELDCENTRIC:
                motorSpeeds = FieldCentricDrive();
                break;
        }
        drivetrain.setMotorSpeeds(precisionMultiplier, motorSpeeds);
    }

    private double[] MecanumDrive() {
        double r = applyDeadband(controller.turnStickX());
        double x = applyDeadband(controller.moveStickX()) * STRAFE_ADJUSTMENT_FACTOR;
        double y = applyDeadband(controller.moveStickY());

        telemetry.addLine("Drivetrain:");
        telemetry.addData("\tDriveR", r);
        telemetry.addData("\tDriveX", x);
        telemetry.addData("\tDriveY", y);

        double sum = ((Math.abs(y))+(Math.abs(x))+(Math.abs(r)));
        double denominator = Math.max(sum, 1);

        return new double[] {
                (y + x + r)/denominator,
                (y - x + r)/denominator,
                (y - x - r)/denominator,
                (y + x - r)/denominator
        };
    }

    private double[] FieldCentricDrive() {
        double botHeading = imuSensor.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double r = applyDeadband(controller.turnStickX());
        double x = applyDeadband(controller.moveStickX()) * STRAFE_ADJUSTMENT_FACTOR;
        double y = applyDeadband(controller.moveStickY());

        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        telemetry.addLine("Drivetrain:");
        telemetry.addData("\tDriveX", x);
        telemetry.addData("\tDriveY", y);
        telemetry.addData("\tDriveRotX", rotX);
        telemetry.addData("\tDriveRotY", rotY);
        telemetry.addData("\tDriveR", r);

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(r), 1);

        return new double[] {
                (rotY + rotX + r) / denominator,
                (rotY - rotX + r) / denominator,
                (rotY - rotX - r) / denominator,
                (rotY + rotX - r) / denominator
        };
    }

    private double applyDeadband(double stick) {
        if (Math.abs(stick) > DEADBAND_VALUE) {
            return stick;
        } else {
            return 0;
        }
    }

    private double calculatePrecisionMultiplier() {
        if (controller.lowPrecision()) {
            return PRECISION_MULTIPLIER_LOW;
        } else if (controller.highPrecision()) {
            return PRECISION_MULTIPLIER_HIGH;
        }
        return 1;
    }

    //merge conflict for noah
}