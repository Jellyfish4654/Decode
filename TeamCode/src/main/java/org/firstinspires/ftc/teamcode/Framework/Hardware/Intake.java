package org.firstinspires.ftc.teamcode.Framework.Hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;

@Config
public class Intake {
    private final DcMotor intake;
    public static double POWER = 1; // can config via dashboard
    
    public Intake (DcMotor motor) {
        this.intake = motor;
        intake.setDirection(DcMotor.Direction.REVERSE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void on() {
        intake.setPower(POWER);
    }

    public void off() {
        intake.setPower(0);
    }

    public void reverse() {
        intake.setPower(-POWER);
    }
    
    public boolean isOn() {
        return intake.getPower() != 0;
    }
    
    
    // ↓ -------------- ↓ -------------- ↓ AUTO ACTIONS ↓ -------------- ↓ -------------- ↓
    public class IntakeOn implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            on();
            return !isOn();
        }
    }
    public Action intakeOn() {
        return new IntakeOn();
    }

    public class IntakeOff implements Action {
        @Override
        public boolean run (@NonNull TelemetryPacket packet) {
            off();
            return isOn();
        }
    }
    public Action intakeOff() {
        return new IntakeOff();
    }
}