package org.firstinspires.ftc.teamcode.Framework.Hardware;

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
}