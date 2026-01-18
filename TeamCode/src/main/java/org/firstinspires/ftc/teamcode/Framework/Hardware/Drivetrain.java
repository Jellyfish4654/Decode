package org.firstinspires.ftc.teamcode.Framework.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Drivetrain {

    private final DcMotor[] driveMotors;

    public Drivetrain(DcMotor[] dcMotors) {
        this.driveMotors = dcMotors;
    }

    public void setMotorDirections(DcMotor.Direction[] directions) {
        for (int i = 0; i < driveMotors.length; i++) {
            driveMotors[i].setDirection(directions[i]);
            driveMotors[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    private double findMaxPower(double[] powers) {
        double max = 0;
        for (double power : powers) {
            max = Math.max(max, Math.abs(power));
        }
        return max;
    }
    private void applyPrecisionAndScale(double multiplier, double[] powers) {
        // precision math
        for (int i = 0; i < powers.length; i++) {
            powers[i] *= multiplier;
        }
        
        // apply scale to prevent values over 1 while keeping proportionality
        double maxPower = findMaxPower(powers);
        double scale = 1.0;
        if (maxPower > 1) {
            scale = 1.0 / maxPower;
        }
        for (int i = 0; i < powers.length; i++) {
            powers[i] *= scale;
        }
    }

    public void setMotorSpeeds(double multiplier, double[] powers) {
        applyPrecisionAndScale(multiplier, powers);
        for (int i = 0; i < driveMotors.length; i++) {
            //applyVoltageCompensation(driveMotors[i], powers[i]);
            driveMotors[i].setPower(powers[i]);
        }
    }
    
//    public double applyVoltageCompensation(DcMotor motor, double power) {
//        double voltageCompensation = 13.2/voltageSensor.getVoltage();
//        power *= voltageCompensation;
//        return power;
//    }

}