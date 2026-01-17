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
    public static double STRAFE_ADJUSTMENT_FACTOR = 1.08;
    
    public static long SPIN_INTAKE_DELAY = 550; // in millis
    public static long SPIN_OUTTAKE_DELAY_LONG = 2700; // in millis -- TODO: still good?
    public static long SPIN_OUTTAKE_DELAY_SHORT = 1000; // in millis -- for holding, motif, and auto sequences
    public static long OUTTAKE_DELAY = 500; // in millis -- for full spin-up and assumed full spin-up
    
    public static double DISTANCE_TOO_FAR = 200; // TODO: Adjust near/far distances?
    public static double DISTANCE_FAR = 70;
    public static double AIM_ROTATION_SPEED = 0.02;
    
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
    private boolean motifOuttakeLock = false;
    private boolean reversingIntake = false;
    private Artifact[] currentMotifArtifacts;
    private int motifOuttakeIndex = 0;
    private long spindexerStartTime = 0;
    private long outtakeStartTime = 0;
    private double aimRotation = 0;
    private long spinOuttakeDelaySkip = SPIN_OUTTAKE_DELAY_LONG;

    
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        initHardware(false);
        imuOffset = imuSensor.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        initFinishedTelemetry();
        waitForStart();
        while (opModeIsActive()) {
            updateDrive();
            updateAux();
            updateParameters();
            telemetry.update();
        }
        stopHardware();
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
        boolean spinInCompleted = System.currentTimeMillis() - spindexerStartTime >= SPIN_INTAKE_DELAY;
        boolean spinOutCompleted = System.currentTimeMillis() - spindexerStartTime >= spinOuttakeDelaySkip;
        boolean outtakeCompleted = System.currentTimeMillis() - outtakeStartTime >= OUTTAKE_DELAY;
        aimRotation = 0;
        if (controller.revIntake() && !reversingIntake) { // TODO: this could prob be improved
            spindexer.deenergize();
            intake.reverse();
            reversingIntake = true;
        } else if (reversingIntake && !controller.revIntake()) { // reset everything and go to standby
            intake.off();
            paddleDown();
            outtake.off();
            spindexer.energize();
            spinPostIntake();
            motifOuttakeLock = false;
            spinOuttakeDelaySkip = SPIN_OUTTAKE_DELAY_LONG;
            reversingIntake = false;
            spinState = SpinState.STANDBY;
        } else if (spinState == SpinState.INTAKING) {
            Artifact detectedArtifact = colorSensor.getArtifact();
            if (detectedArtifact != Artifact.NONE) {
                intake.off();
                spindexer.setContents(detectedArtifact);
                spinState = SpinState.STANDBY;
                spinPostIntake(); // TODO: consider if necessary, doesn't currently save time
            }
        } else if (spinState == SpinState.OUTTAKING && outtakeCompleted) {
            outtake.off();
            paddleDown();
            spindexer.setContents(Artifact.NONE);
            spinState = SpinState.STANDBY;
            spinOuttakeDelaySkip = SPIN_OUTTAKE_DELAY_SHORT;
        } else if (spinState == SpinState.SPIN_INTAKE && spinInCompleted) {
            intake.on();
            spinState = SpinState.INTAKING;
        } else if (spinState == SpinState.SPIN_OUTTAKE) {
            if (spinOutCompleted) {
                paddleUp();
                outtakeStartTime = System.currentTimeMillis();
                spinState = SpinState.OUTTAKING;
            } else {
                aimRotation = vision.getGoalBearing(Params.alliance) * AIM_ROTATION_SPEED;
                // power up outtake early
                double distance = vision.getGoalDistance(Params.alliance);
                if (distance > DISTANCE_FAR && distance < DISTANCE_TOO_FAR) {
                    outtake.onFar();
                } else { // near is default
                    outtake.onNear();
                }
            }
        } else if (spinState == SpinState.STANDBY) {
            if (!motifOuttakeLock) {
                if (controller.intake()) {
                    spinIntake();
                } else if (controller.outGreen()) {
                    spinOuttake(Artifact.GREEN);
                } else if (controller.outPurple()) {
                    spinOuttake(Artifact.PURPLE);
                } else if (controller.outMotif()) {
                    motifOuttakeLock = true;
                    currentMotifArtifacts = Params.motifArtifacts.get(Params.motif);
                    motifOuttakeIndex = 0;
                } else {
                    spinOuttakeDelaySkip = SPIN_OUTTAKE_DELAY_LONG;
                }
            } else { // Motif Outtake Logic ↓
                if(motifOuttakeIndex >= 3){
                    motifOuttakeLock = false;
                    spinOuttakeDelaySkip = SPIN_OUTTAKE_DELAY_LONG;
                } else {
                    spinOuttake(currentMotifArtifacts[motifOuttakeIndex]);
                    motifOuttakeIndex += 1;
                }
            }
        }
        
        telemetry.addLine();
        telemetry.addLine("Aux:");
        telemetry.addData("\tSpinState", spinState);
        telemetry.addData("\tVoltage", outtake.getVoltage());
        telemetry.addData("\tVoltage Compensation", outtake.getVoltageCompensation());
        telemetry.addData("\tIntakeOn", intake.isOn());
        telemetry.addData("\tOuttakePower", outtake.getPower());
        telemetry.addData("\tPaddleUp", paddleIsUp());
        telemetry.addData("\tMotif Lock",motifOuttakeLock);

        telemetry.addLine();
        telemetry.addLine("Spindexer:");
        telemetry.addData("\tSpindexerSlot", spindexer.getCurrentSlot());
        telemetry.addData("\tSlot 1", spindexer.getContents(1));
        telemetry.addData("\tSlot 2", spindexer.getContents(2));
        telemetry.addData("\tSlot 3", spindexer.getContents(3));
        telemetry.addData("\tOuttakeSpinDelay", spinOuttakeDelaySkip);
        
        telemetry.addLine();
        telemetry.addLine("Vision & Color:");
        telemetry.addData("\tGoalBearing", vision.getGoalBearing(Params.alliance)); // potentially heavy
        telemetry.addData("\tGoalDistance", vision.getGoalDistance(Params.alliance)); // potentially heavy
        telemetry.addData("\tColorSensor", colorSensor.getArtifact());
    }
    
    private void spinIntake() {
        int slot = spindexer.findSlot(Artifact.NONE);
        if (slot == 0) {
            controller.rumble(200,false);
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
            controller.rumble(200,false);
            motifOuttakeLock = false;
            return;
        }
        paddleDown(); // backup safety
        spindexer.setSlotOut(slot);
        spindexer.setContents(Artifact.NONE);
        spindexerStartTime = System.currentTimeMillis();
        spinState = SpinState.SPIN_OUTTAKE;
    }
    
    private void spinPostIntake() {
        paddleDown(); // backup safety
        int slot = spindexer.findSlot(Artifact.NONE);
        if (slot == 0) {
            spindexer.setSlotOut(1);
        } else {
            spindexer.setSlotIn(slot);
        }
    }

    private void updateParameters() {
        if (controller.allianceRedPressed()) {
            Params.alliance = Alliance.RED;
        } else if (controller.allianceBluePressed()) {
            Params.alliance = Alliance.BLUE;
        }

        if (controller.motifGPPPressed()){
            Params.motif = Motif.GPP;
        }else if(controller.motifPGPPressed()){
            Params.motif = Motif.PGP;
        }else if(controller.motifPPGPressed()){
            Params.motif = Motif.PPG;
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
    
    private void initFinishedTelemetry() {
        telemetry.addLine("Status: Init Finished ------------------------------------------");
        for (int i=0; i<16; i++) {
            telemetry.addLine("------------------------------------------------------------------------");
        }
        telemetry.update();
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