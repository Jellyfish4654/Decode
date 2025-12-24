package org.firstinspires.ftc.teamcode.Framework.Hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
public class Outtake {
    private final DcMotor outtake;
    
    // can config via dashboard
    public static double NEAR_POWER = 1;
    public static double FAR_POWER = 1;

    public Outtake (DcMotor motor) {
        this.outtake = motor;
        outtake.setDirection(DcMotorSimple.Direction.FORWARD);
        outtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void onNear() {
        outtake.setPower(NEAR_POWER);
    }
    
    public void onFar() {
        outtake.setPower(FAR_POWER);
    }

    public void off() {
        outtake.setPower(0);
    }
    
    public boolean isOn() {
        return outtake.getPower() != 0;
    }
    
    
    // ↓ -------------- ↓ -------------- ↓ AUTO ACTIONS ↓ -------------- ↓ -------------- ↓
    public class OuttakeOnNear implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            onNear();
            return isOn();
        }
    }
    public Action outtakeOnNear() {
        return new OuttakeOnNear();
    }
    
    public class OuttakeOnFar implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            onFar();
            return isOn();
        }
    }
    public Action outtakeOnFar() {
        return new OuttakeOnFar();
    }

    public class OuttakeOff implements Action {
        @Override
        public boolean run (@NonNull TelemetryPacket packet) {
            off();
            return !isOn();
        }
    }
    public Action outtakeOff() {
        return new OuttakeOff();
    }
}