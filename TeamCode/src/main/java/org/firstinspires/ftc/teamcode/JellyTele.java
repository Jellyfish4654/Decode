package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Framework.BaseOpMode;

import org.firstinspires.ftc.teamcode.Framework.Params;
import org.firstinspires.ftc.teamcode.Framework.Params.Alliance;
import org.firstinspires.ftc.teamcode.Framework.Params.Motif;
import org.firstinspires.ftc.teamcode.Framework.Params.Artifact;

@TeleOp(name = "JellyTele", group = "1-OpMode")
@Config
public class JellyTele extends BaseOpMode {
    public static double PRECISION_MULTIPLIER_LOW = 0.35;
    public static double PRECISION_MULTIPLIER_HIGH = 0.2;
    public static double DEADBAND_VALUE = 0.02;
    public static double STRAFE_ADJUSTMENT_FACTOR = (14.0 / 13.0);

    public static long OUTTAKE_DELAY = 500; // in millis -- TODO: adjust outtake delay (maybe spindexer also)
    public static long SPINDEXER_DELAY = 550; // in millis
    
    private double imuOffset = 0;
    
    private long loopTime = 0;

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
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        initHardware();
        imuOffset = imuSensor.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        waitForStart();
        while (opModeIsActive()) {
            updateDrive();
            updateAux();
            updateParameters();
            telemetry.update();
            //idle(); // potential fix for not stopping (or after loop), BUT could slow loop
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

    // main auxiliary logic for intake, spindexer, outtake, and vision integrations
    private void updateAux() {
        boolean spinCompleted = System.currentTimeMillis()-spindexerStartTime >= SPINDEXER_DELAY;
        boolean outtakeCompleted = System.currentTimeMillis()-outtakeStartTime >= OUTTAKE_DELAY;
        aimRotation = 0;
        if (spinState == SpinState.INTAKING) {
            Artifact detectedArtifact = colorSensor.getArtifact();
            if (detectedArtifact != Artifact.NONE) {
                intake.off();
                spindexer.setContents(detectedArtifact);
                spinIntake();
            }
        } else if (spinState == SpinState.OUTTAKING && outtakeCompleted) {
            outtake.off();
            paddleDown();
            spindexer.setContents(Artifact.NONE);
            spinState = SpinState.STANDBY;
        } else if (spinState == SpinState.SPIN_INTAKE && spinCompleted) {
            spinState = SpinState.STANDBY;
        } else if (spinState == SpinState.SPIN_OUTTAKE) {
            if (spinCompleted) {
                paddleUp();
                outtakeStartTime = System.currentTimeMillis();
                spinState = SpinState.OUTTAKING;
            } else {
                //aimRotation = vision.getGoalBearing(Params.alliance);
                // power up outtake early
                double distance = vision.getGoalDistance(Params.alliance);
                if (distance > 10 && distance < 20) { // TODO: adjust near and far distances and test, test aim rotation
                    outtake.onFar();
                } else { // near is default if goal isn't recognized or distance is unrealistic
                    outtake.onNear();
                }
            }
        } else if (spinState == SpinState.STANDBY) {
            if (controller.intake()/*Pressed()*/) {
                if(spindexer.getContents(spindexer.getCurrentSlot()) == Artifact.NONE) {
                    intake.on();
                    spinState = SpinState.INTAKING;
                }
                else{
                    controller.rumble(200);
                }
            } else if (controller.outGreen()/*Pressed()*/) {
                spinOuttake(Artifact.GREEN);
            } else if (controller.outPurple()/*Pressed()*/) {
                spinOuttake(Artifact.PURPLE);
            }
        }
        
        telemetry.addLine();
        telemetry.addLine("Aux:");
        telemetry.addData("\tSpinState", spinState);
        telemetry.addData("\tIntakeOn", intake.isOn());
        telemetry.addData("\tOuttakeOn", outtake.isOn());
        telemetry.addData("\tPaddleUp", paddleIsUp());

        telemetry.addLine();
        telemetry.addLine("Spindexer:");
        telemetry.addData("\tSpindexerSlot", spindexer.getCurrentSlot());
        // TODO: check if telemetry is showing contents correctly
        telemetry.addData("\tSlot 1", spindexer.getContents(1));
        telemetry.addData("\tSlot 2", spindexer.getContents(2));
        telemetry.addData("\tSlot 3", spindexer.getContents(3));
        
        telemetry.addLine();
        telemetry.addLine("Vision & Color:");
        telemetry.addData("\tGoalBearing", vision.getGoalBearing(Params.alliance)); // potentially heavy
        telemetry.addData("\tGoalDistance", vision.getGoalDistance(Params.alliance)); // potentially heavy
        telemetry.addData("\tColorSensor", colorSensor.getArtifact());
    }
    
    private void spinIntake() {
        int slot = spindexer.getContents(spindexer.getCurrentSlot()) == Artifact.NONE ? spindexer.getCurrentSlot() : spindexer.findSlot(Artifact.NONE);
        if (slot == 0) {
            spinState = SpinState.STANDBY;
            return;
        }
        paddleDown(); // backup safety
        spindexer.setSlotIn(slot);
        spindexerStartTime = System.currentTimeMillis();
        spinState = SpinState.SPIN_INTAKE;
    }

    private void spinOuttake(Artifact artifact) {
        int slot = spindexer.findSlot(artifact);
        if (slot == 0) {
            controller.rumble(200);
            spinState = SpinState.STANDBY;
            return;
        }
        paddleDown(); // backup safety
        spindexer.setSlotOut(slot);
        spindexer.setContents(Artifact.NONE);
        spindexerStartTime = System.currentTimeMillis();
        spinState = SpinState.SPIN_OUTTAKE;
    }
    
    // updates parameters lik e current alliance from gamepad2
    private void updateParameters() {
        if (gamepad2.square) {
            Params.alliance = Alliance.RED;
        } else if (gamepad2.circle) {
            Params.alliance = Alliance.BlUE;
        }
        
        telemetry.addLine();
        telemetry.addLine("Match:");
        telemetry.addData("\tAlliance", Params.alliance);
        telemetry.addData("\tMotif", Params.motif);
        
        // loops per sec experiment
        long currentTime = System.nanoTime();
        long nanoPerLoop = currentTime - loopTime;
        
        double loopsPerSec = 0;
        if (nanoPerLoop > 0) {
            loopsPerSec = 1e9 / nanoPerLoop;
        }
        
        telemetry.addLine();
        telemetry.addData("Millis Per Loop", (nanoPerLoop / 1e6));
        telemetry.addData("Loops Per Sec", loopsPerSec);
        loopTime = currentTime;
    }
    
    
    // ↓ -------------- ↓ -------------- ↓ DRIVETRAIN ↓ -------------- ↓ -------------- ↓
    private enum DriveMode
    {
        MECANUM,
        FIELDCENTRIC
    }
    private DriveMode driveMode = DriveMode.MECANUM;
    private void updateDrive() {
        if (controller.driveModePressed()) {
            if (driveMode == DriveMode.MECANUM) {
                driveMode = DriveMode.FIELDCENTRIC;
            } else {
                driveMode = DriveMode.MECANUM;
            }
        }
        
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
        drivetrain.setMotorSpeeds(getPrecisionMultiplier(), motorSpeeds);
    }

    private double[] MecanumDrive() {
        double r = applyDeadband(controller.turnStickX()) + aimRotation/90; // outtake aim rotation
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

        double r = applyDeadband(controller.turnStickX()) + aimRotation/90; // outtake aim rotation
        double x = applyDeadband(controller.moveStickX()) * STRAFE_ADJUSTMENT_FACTOR;
        double y = applyDeadband(controller.moveStickY());

        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        telemetry.addLine("Drivetrain (Field Centric):");
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
    
    // Old deadband: output jumps from 0 to DEADBAND_VALUE rather than increasing linearly from 0
//    private double applyDeadband(double stick) {
//        if (Math.abs(stick) > DEADBAND_VALUE) {
//            return stick;
//        } else {
//            return 0;
//        }
//    }
    
    // linear rescaled deadband: lowers inputs to start at 0 and scales up to reach 1
    private double applyDeadband(double stick) {
        if (Math.abs(stick) > DEADBAND_VALUE) {
            double loweredStick = Math.abs(stick) - DEADBAND_VALUE;
            double rangeAfterDeadband = 1.0 - DEADBAND_VALUE;
            // divide the lowered stick by the range remaining to stretch it back to 0 - 1
            return Math.copySign((loweredStick / rangeAfterDeadband), stick); // finish by copying the sign
        } else {
            return 0;
        }
    }

    private double getPrecisionMultiplier() {
        if (controller.lowPrecision()) {
            return PRECISION_MULTIPLIER_LOW;
        } else if (controller.highPrecision()) {
            return PRECISION_MULTIPLIER_HIGH;
        }
        return 1;
    }


    // ↓ Wrappers depending on which paddle type is used ↓ ------------------------------ ↓
    private void paddleUp(){
        if(PADDLE_MOTOR){
            paddleMotor.setUp();
        }else{
            paddle.setUp();
        }
    }

    private void paddleDown(){
        if(PADDLE_MOTOR){
            paddleMotor.setDown();
        }else{
            paddle.setDown();
        }
    }

    private boolean paddleIsUp(){
        if(PADDLE_MOTOR){
            return paddleMotor.isUp();
        }else{
            return paddle.isUp();
        }
    }
}