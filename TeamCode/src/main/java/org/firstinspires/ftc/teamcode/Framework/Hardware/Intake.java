package org.firstinspires.ftc.teamcode.Framework.Hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Intake {
    private final DcMotorEx intake;

    public Intake (DcMotorEx motor) {
        this.intake = motor;
    }

    public void on () {
        intake.setPower(1);
    }

    public void off () {
        intake.setPower(0);
    }
    public class IntakeOn implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intake.setPower(1);
            return true;
        }
    }
    public Action intakeOn() {
        return new IntakeOn();
    }

    public class IntakeOff implements Action {
        @Override
        public boolean run (@NonNull TelemetryPacket packet) {
            intake.setPower(0);
            return true;
        }
    }
    public Action intakeOff() {
        return new IntakeOff();
    }
}
