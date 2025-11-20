package org.firstinspires.ftc.teamcode.Framework.Hardware;

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
}
