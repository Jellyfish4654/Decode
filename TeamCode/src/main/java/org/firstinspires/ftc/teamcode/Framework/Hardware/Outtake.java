package org.firstinspires.ftc.teamcode.Framework.Hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Outtake {
    private final DcMotorEx outtake;

    public Outtake (DcMotorEx motor) {
        this.outtake = motor;
    }

    public void on () {
        outtake.setPower(1);
    }

    public void off () {
        outtake.setPower(0);
    }
    public class OuttakeOn implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            outtake.setPower(1);
            return true;
        }
    }
    public Action outtakeOn() {
        return new OuttakeOn();
    }

    public class OuttakeOff implements Action {
        @Override
        public boolean run (@NonNull TelemetryPacket packet) {
            outtake.setPower(0);
            return true;
        }
    }
    public Action outtakeOff() {
        return new OuttakeOff();
    }
}