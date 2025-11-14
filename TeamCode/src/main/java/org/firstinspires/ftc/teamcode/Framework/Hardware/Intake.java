package org.firstinspires.ftc.teamcode.Framework.Hardware;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Intake {
    private DcMotorEx intake;

    public void on () {
        intake.setPower(1);
    }

    public void off () {
        intake.setPower(0);
    }
}
