package org.firstinspires.ftc.teamcode.Framework.Hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.VoltageSensor;

@Config
public class Outtake {
    private final DcMotor outtake;
    private final DcMotor guiding;
    private final VoltageSensor voltageSensor;
    
    // TODO: PID! (keep as backup)
    public static double NEAR_POWER = 0.89;
    public static double FAR_POWER = 1;
    public static double GUIDING_POWER = 1;
    public static double VOLTAGE_COMP_STRENGTH = 1.3; // not necessary at all with pid
    
    public Outtake (DcMotor outtake, DcMotor guiding, VoltageSensor voltSensor) {
        this.outtake = outtake; // TODO: second outtake motor (check direction)
        this.guiding = guiding;
        this.voltageSensor = voltSensor;
        
        outtake.setDirection(DcMotor.Direction.REVERSE);
        outtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        
        guiding.setDirection(DcMotor.Direction.FORWARD);
        guiding.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    
    public void onNear() {
        outtake.setPower(applyVoltageCompensation(NEAR_POWER));
        guiding.setPower(GUIDING_POWER);
    }
    
    public void onFar() {
        outtake.setPower(applyVoltageCompensation(FAR_POWER));
        guiding.setPower(GUIDING_POWER);
    }
    
    public void off() {
        outtake.setPower(0);
        guiding.setPower(0);
    }
    
    public boolean isOn() {
        return (outtake.getPower() != 0) || (guiding.getPower() != 0);
    }
    
    public double getPower() {
        return outtake.getPower();
    }
    
    public double getVoltage() {
        return voltageSensor.getVoltage();
    }
    
    public double getVoltageCompensation() {
        return Math.max(Math.pow((12.0 / voltageSensor.getVoltage()), VOLTAGE_COMP_STRENGTH), 1);
        // Math.pow allows 12/12 to still be 1, while 10/12 to be larger than 1.2
    }
    
    public double applyVoltageCompensation(double power) {
        double voltageCompensation = 12.0 / voltageSensor.getVoltage();
        return power * voltageCompensation;
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