package org.firstinspires.ftc.teamcode.Framework.Hardware;

import com.qualcomm.robotcore.hardware.Servo;

public class Flipper {
    private final Servo flipper;
    private final double up = 0.77;
    private final double down = 0.1875;
    private double position;

    public Flipper(Servo servo) {
        this.flipper = servo;
    }

    public void setPosUp() {
        changePosition(up);
    }

    public void setPosDown() {
        changePosition(down);
    }

    public void changePosition(double position) {
        flipper.setPosition(position);
        this.position = position;
    }

    public double getCurrentPosition() {
        return position;
    }
}
