package org.firstinspires.ftc.teamcode.Framework.Hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;

@Config
public class Outtake {
    private final DcMotor outtake;
    private final DcMotor guiding;
    private final VoltageSensor voltageSensor;
    
    // BEWARE: numerous hours were wasted trying to create a PIDF controller for this flywheel...
    // we were terribly unsuccessful. don't get any ideas. this is your warning.
    public static double NEAR_POWER = 0.763;
    public static double FAR_POWER = 1;
    public static double GUIDING_POWER = 1;
    
    // voltage compensation -- TODO: tune this now that it actually works
    public static double VOLTAGE_COMP_STRENGTH = 1.031;
    public static double MIN_VOLTAGE_COMP = 0; // only use if its dropping unnecessarily
    
    public Outtake (DcMotor outtake, DcMotor guiding, VoltageSensor voltSensor) {
        this.outtake = outtake; // TODO: second outtake motor (check direction)
        this.guiding = guiding;
        this.voltageSensor = voltSensor;
        
        outtake.setDirection(DcMotor.Direction.REVERSE); // TODO: fix wiring and flip this
        outtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        
        guiding.setDirection(DcMotor.Direction.FORWARD);
        guiding.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    
    public void onNear() {
        outtake.setPower(NEAR_POWER * calcVoltageCompensation());
        guiding.setPower(GUIDING_POWER);
    }
    
    public void onFar() {
        outtake.setPower(FAR_POWER * calcVoltageCompensation());
        guiding.setPower(GUIDING_POWER);
    }
    
    public void off() {
        outtake.setPower(0);
        guiding.setPower(0);
    }
    
    public double getPower() {
        return outtake.getPower();
    }
    
    public boolean isOn() {
        return (getPower() != 0) || (guiding.getPower() != 0);
    }
    
    public double getVoltage() {
        return voltageSensor.getVoltage();
    }
    
    public double calcVoltageCompensation() {
        double ratio = 13 / getVoltage();
        double scaledRatio = Math.pow(ratio, VOLTAGE_COMP_STRENGTH);
        // Math.pow allows 12/12 to still be 1, while 12/10 to be larger than 1.2
        return Math.max(scaledRatio, MIN_VOLTAGE_COMP);
    }
    
    public double getVelocity() {
        DcMotorEx outtakeEx = (DcMotorEx) outtake;
        return outtakeEx.getVelocity();
    }
    
    
    // ↓ -------------- ↓ -------------- ↓ AUTO ACTIONS ↓ -------------- ↓ -------------- ↓
    public class OuttakeOnNear implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            onNear();
            return !isOn();
        }
    }
    public Action outtakeOnNear() {
        return new OuttakeOnNear();
    }
    
    public class OuttakeOnFar implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            onFar();
            return !isOn();
        }
    }
    public Action outtakeOnFar() {
        return new OuttakeOnFar();
    }
    
    public class OuttakeOff implements Action {
        @Override
        public boolean run (@NonNull TelemetryPacket packet) {
            off();
            return isOn();
        }
    }
    public Action outtakeOff() {
        return new OuttakeOff();
    }
}