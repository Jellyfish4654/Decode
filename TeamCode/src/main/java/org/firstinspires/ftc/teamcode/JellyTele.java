package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Framework.BaseOpMode;

@TeleOp(name = "JellyTele", group = "OpMode")
public class JellyTele extends BaseOpMode {
    private final double PRECISION_MULTIPLIER_LOW = 0.35;
    private final double PRECISION_MULTIPLIER_HIGH = 0.2;
    private final double DEADBAND_VALUE = 0.02;
    private final double STRAFE_ADJUSTMENT_FACTOR = (14.0 / 13.0);
    private double SPINDEXER_DELAY = 0.55*1000;

    private boolean isOuttakingGreen = false;
    private boolean isOuttakingPurple = false;
    private double spindexerStartTime = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        waitForStart();
        while (opModeIsActive()) {
            updateDrive(calculatePrecisionMultiplier());
            telemetry.update();
        }
    }

    private void updateOuttake(){
        if(!isOuttakingGreen && !isOuttakingPurple) {
            if (controller.outGreenPressed()) {
                outGreen();
            } else if (controller.outPurplePressed()) {
                outPurple();
            }
        }else{
            if(System.currentTimeMillis()-spindexerStartTime >= SPINDEXER_DELAY){
                outtake.outtakeOn();
                isOuttakingGreen = false;
                isOuttakingPurple = false;
            }
        }
    }

    private void outGreen(){
        for(int slot = 1; slot <= 3; slot++){
            if(spindexer.getContents(slot)==1){
                spindexer.setSlot(-slot);
                spindexerStartTime = System.currentTimeMillis();
                isOuttakingGreen = true;
                break;
            }
        }
    }

    private void outPurple(){
        for(int slot = 1; slot <= 3; slot++){
            if(spindexer.getContents(slot)==2){
                spindexer.setSlot(-slot);
                spindexerStartTime = System.currentTimeMillis();
                isOuttakingGreen = true;
                break;
            }
        }
    }

    private void updateDrive (double precisionMultiplier) {
        double[] motorSpeeds = calculateMotorSpeeds();
        drivetrain.setMotorSpeeds(precisionMultiplier, motorSpeeds);
    }
    private double applyDeadband(double stick) {
        double sign = Math.signum(stick);
        return stick + (-sign * DEADBAND_VALUE);
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

        double sum = ((Math.abs(y))+(Math.abs(x))+(Math.abs(r)));
        double denominator = Math.max(sum, 1);

        return new double[]{
                (y + x + r)/denominator,
                (y - x + r)/denominator,
                (y - x - r)/denominator,
                (y + x - r)/denominator
        };
    }
}