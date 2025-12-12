package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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
    
    // TODO: remove this once intake is fully integrated
    private void updateIntake() {
        if (controller.intake()) {
            intake.on();
        } else if (intake.isOn()) {
            intake.off();
        }
    }

    // TODO: adjust order to prioritize outtake or intake buttons when both pressed
    private void updateAux() {
        if (!isSpinningIn && !isSpinningOut && !paddle.getState()) {
            if (controller.intakePressed()) {
                spinIntake();
            } else if (controller.outGreenPressed()) {
                spinOuttake(1);
            } else if (controller.outPurplePressed()) {
                spinOuttake(2);
            }
        } else if (isSpinningIn) {
            if(System.currentTimeMillis()-spindexerStartTime >= SPINDEXER_DELAY){
                intake.on();
                isSpinningIn = false;
            }
        } else if (isSpinningOut) {
            if(System.currentTimeMillis()-spindexerStartTime >= SPINDEXER_DELAY){
                outtake.on();
                paddle.setUp();

                isSpinningOut = false;
            }
        }
        
        telemetry.addLine();
        telemetry.addLine("Aux:");
        telemetry.addData("\tSpinningIn", isSpinningIn);
        telemetry.addData("\tSpinningOut", isSpinningOut);
        telemetry.addData("\tSpindexerPos", spindexer.getSlot());
        telemetry.addData("\tIntakeOn", intake.isOn());
        telemetry.addData("\tOuttakeOn", intake.isOn());
        telemetry.addData("\tPaddleUp", paddle.getState());
    }
    
    private void spinIntake() {
        for (int slot = 1; slot <= 3; slot++) {
            if (spindexer.getContents(slot)==0) {
                paddle.setDown(); // backup safety
                spindexer.setSlot(slot);
                spindexerStartTime = System.currentTimeMillis();
                isSpinningIn = true;
                return;
            }
        }
        controller.rumble(200);
    }

    private void spinOuttake(int artifactID) {
        for (int slot = 1; slot <= 3; slot++) {
            if (spindexer.getContents(slot) == artifactID) {
                paddle.setDown(); // backup safety
                spindexer.setSlot(-slot);
                spindexer.setContents(slot, 0);
                spindexerStartTime = System.currentTimeMillis();
                isSpinningOut = true;
                return;
            }
        }
        controller.rumble(200);
    }
    
    
    // ↓ -------------- ↓ -------------- ↓ DRIVETRAIN ↓ -------------- ↓ -------------- ↓
    private void updateDrive(double precisionMultiplier) {
        double[] motorSpeeds = calculateMotorSpeeds();
        drivetrain.setMotorSpeeds(precisionMultiplier, motorSpeeds);
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

    private double[] calculateMotorSpeeds() {
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
}