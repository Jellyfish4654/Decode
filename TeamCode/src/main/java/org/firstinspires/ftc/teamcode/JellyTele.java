package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Framework.BaseOpMode;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Spindexer.Artifact;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Vision.Alliance;

@TeleOp(name = "JellyTele", group = "1-OpMode")
public class JellyTele extends BaseOpMode {
    private final double PRECISION_MULTIPLIER_LOW = 0.35;
    private final double PRECISION_MULTIPLIER_HIGH = 0.2;
    private final double DEADBAND_VALUE = 0.02;
    private final double STRAFE_ADJUSTMENT_FACTOR = (14.0 / 13.0);

    private double OUTTAKE_DELAY = 0.5*1000;
    private final double SPINDEXER_DELAY = 0.55*1000; // in millis
    
    private double imuOffset = 0;
    private Alliance alliance = Alliance.RED;
    

    private enum SpinState {
        STANDBY,
        SPIN_INTAKE,
        SPIN_OUTTAKE,
        INTAKING,
        OUTTAKING
    }
    private SpinState spinState = SpinState.STANDBY;
    private long spindexerStartTime = 0;
    private long outtakeStartTime = 0;
    private double aimRotation = 0;

    
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        imuOffset = imuSensor.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        waitForStart();
        while (opModeIsActive()) {
            updateDrive(calculatePrecisionMultiplier());
            //updateIntake(); // testing only
            updateAux();
            updateParameters();
            telemetry.update();
        }
    }
    
    // testing only -- keep this commented for normal use
//    private void updateIntake() {
//        if (controller.intake()) {
//            intake.on();
//        } else if (intake.isOn()) {
//            intake.off();
//        }
//    }

    // TODO: add more components while managing/stopping components with more delays
    private void updateAux() {
        boolean spinCompleted = System.currentTimeMillis()-spindexerStartTime >= SPINDEXER_DELAY;
        boolean outtakeCompleted = System.currentTimeMillis()-outtakeStartTime >= OUTTAKE_DELAY;
        aimRotation = 0;
        if (spinState == SpinState.INTAKING) {
            if (colorSensor.isGreen()) {
                intake.off();
                spindexer.setContents(Artifact.GREEN);
                spinState = SpinState.STANDBY;
            } else if (colorSensor.isPurple()) {
                intake.off();
                spindexer.setContents(Artifact.PURPLE);
                spinState = SpinState.STANDBY;
            }
        } else if (spinState == SpinState.OUTTAKING && outtakeCompleted) {
            outtake.off();
            spindexer.setContents(Artifact.EMPTY);
            spinState = SpinState.STANDBY;
        } else if (spinState == SpinState.SPIN_INTAKE && spinCompleted) {
            intake.on();
            spinState = SpinState.INTAKING;
        } else if (spinState == SpinState.SPIN_OUTTAKE) {
            if (spinCompleted) {
                double distance = vision.getGoalDistance(alliance);
                if (distance > 10 && distance < 20) {
                    outtake.onFar();
                } else {
                    outtake.onNear();
                }
                paddle.setUp();
                outtakeStartTime = System.currentTimeMillis();
                spinState = SpinState.OUTTAKING;
            } else {
                aimRotation = vision.getGoalBearing(alliance);
            }
        } else if (spinState == SpinState.STANDBY) {
            if (controller.intakePressed()) {
                spinIntake();
            } else if (controller.outGreenPressed()) {
                spinOuttake(Artifact.GREEN);
            } else if (controller.outPurplePressed()) {
                spinOuttake(Artifact.PURPLE);
            }
        }
        
        telemetry.addLine();
        telemetry.addLine("Aux:");
        telemetry.addData("\tSpinState", spinState);
        telemetry.addData("\tSpindexerSlot", spindexer.getCurrentSlot());
        telemetry.addData("\tIntakeOn", intake.isOn());
        telemetry.addData("\tOuttakeOn", outtake.isOn());
        telemetry.addData("\tPaddleUp", paddle.isUp());
    }
    
    private void spinIntake() {
        int slot = spindexer.findSlot(Artifact.EMPTY);
        if (slot == 0) {
            controller.rumble(200);
            return;
        }
        paddle.setDown(); // backup safety
        spindexer.setSlotIn(slot);
        spindexerStartTime = System.currentTimeMillis();
        spinState = SpinState.SPIN_INTAKE;
    }

    private void spinOuttake(Artifact artifact) {
        int slot = spindexer.findSlot(artifact);
        if (slot == 0) {
            controller.rumble(200);
            return;
        }
        paddle.setDown(); // backup safety
        spindexer.setSlotOut(slot);
        spindexer.setContents(Artifact.EMPTY);
        spindexerStartTime = System.currentTimeMillis();
        spinState = SpinState.SPIN_OUTTAKE;
    }
    
    // updates parameters like current alliance from gamepad2
    // TODO: switch this to controller2 with more controller mapping?
    private void updateParameters() {
        if (gamepad2.square) {
            alliance = Alliance.RED;
        } else if (gamepad2.circle) {
            alliance = Alliance.BlUE;
        }
    }
    
    
    // ↓ -------------- ↓ -------------- ↓ DRIVETRAIN ↓ -------------- ↓ -------------- ↓
    private enum DriveMode
    {
        MECANUM,
        FIELDCENTRIC
    }
    private DriveMode driveMode = DriveMode.MECANUM;
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
        double r = applyDeadband(controller.turnStickX()) + aimRotation; // outtake aim rotation
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
        double botHeading = imuSensor.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) - imuOffset;

        double r = applyDeadband(controller.turnStickX()) + aimRotation; // outtake aim rotation
        double x = applyDeadband(controller.moveStickX()) * STRAFE_ADJUSTMENT_FACTOR;
        double y = applyDeadband(controller.moveStickY());

        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        telemetry.addLine("Drivetrain:");
        telemetry.addData("\tDriveR", r);
        telemetry.addData("\tDriveX", x);
        telemetry.addData("\tDriveY", y);
        telemetry.addData("\tBotHeading", (botHeading/Math.PI*180));
        telemetry.addData("\tDriveRotX", rotX);
        telemetry.addData("\tDriveRotY", rotY);

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
}