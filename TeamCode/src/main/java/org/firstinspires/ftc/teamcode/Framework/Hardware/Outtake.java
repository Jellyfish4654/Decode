package org.firstinspires.ftc.teamcode.Framework.Hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
public class Outtake {
    private final DcMotorEx outtake;

    //can config via dashboard
    public static double DEFAULT_POWER = 1;

    public Outtake (DcMotorEx motor) {
        this.outtake = motor;
    }

    public void on() {
        outtake.setPower(DEFAULT_POWER);
    }
    
    // variable power depending on launch distance
    public void on(double power) {
        outtake.setPower(power);
    }

    public void off() {
        outtake.setPower(0);
    }
    
    public boolean isOn() {
        return outtake.getPower() != 0;
    }
    
    
    // ↓ -------------- ↓ -------------- ↓ AUTO ACTIONS ↓ -------------- ↓ -------------- ↓
    public class OuttakeOn implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            on();
            return isOn();
        }
    }
    public Action outtakeOn() {
        return new OuttakeOn();
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