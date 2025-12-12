package org.firstinspires.ftc.teamcode.Framework.Hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Intake {
    private final DcMotorEx intake;
    private final double DEFAULT_POWER = 1;
    
    public Intake (DcMotorEx motor) {
        this.intake = motor;
        intake.setDirection(DcMotorEx.Direction.REVERSE);
    }

    public void on() {
        intake.setPower(DEFAULT_POWER);
    }

    public void off() {
        intake.setPower(0);
    }
    
    public boolean isOn() {
        return intake.getPower() != 0;
    }
    
    
    // ↓ -------------- ↓ -------------- ↓ AUTO ACTIONS ↓ -------------- ↓ -------------- ↓
    public class IntakeOn implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            on();
            return isOn();
        }
    }
    public Action intakeOn() {
        return new IntakeOn();
    }

    public class IntakeOff implements Action {
        @Override
        public boolean run (@NonNull TelemetryPacket packet) {
            off();
            return !isOn();
        }
    }
    public Action intakeOff() {
        return new IntakeOff();
    }
}