package org.firstinspires.ftc.teamcode.Framework.Hardware;

import static android.icu.util.UniversalTimeScale.MAX_SCALE;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class Drivetrain {

    DcMotor[] driveMotors;
    private VoltageSensor voltageSensor;

    public Drivetrain(DcMotor[] dcMotors) {
        this.driveMotors = dcMotors;
    }

    public void setMotorDirections(DcMotorSimple.Direction[] directions) {
        for (int i = 0; i < driveMotors.length; i++) {
            driveMotors[i].setDirection(directions[i]);
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
        for (int i = 0; i < powers.length; i++) {
            powers[i] *= multiplier;
        }

        double maxPower = findMaxPower(powers);
        double scale = maxPower > MAX_SCALE ? MAX_SCALE / maxPower : 1.0;

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