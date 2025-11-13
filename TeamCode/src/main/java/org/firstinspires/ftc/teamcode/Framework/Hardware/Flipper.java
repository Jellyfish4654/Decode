package org.firstinspires.ftc.teamcode.Framework.Hardware;

import com.qualcomm.robotcore.hardware.Servo;

public class Flipper {
    private final Servo servo;
    private final double up = 0.77;
    private final double down = 0.1875;
    private int position;

    public Flipper(Servo servo) {
        this.servo = servo;
    }

    public void setPosUp() {
        changePosition(up);
    }

    public void setPosDown() {
        changePosition(down);
    }

    public void changePosition(double position) {
        servo.setPosition(position);
    }

    public double getCurrentPosition() {
        return position;
    }
}
