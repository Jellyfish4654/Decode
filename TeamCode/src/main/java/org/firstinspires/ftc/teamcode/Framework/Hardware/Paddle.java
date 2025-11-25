package org.firstinspires.ftc.teamcode.Framework.Hardware;

import com.qualcomm.robotcore.hardware.Servo;

public class Paddle {
    private final Servo paddle;
    private final double up = 0.77;
    private final double down = 0.1875;
    private double position;

    public Paddle(Servo servo) {
        this.paddle = servo;
    }

    public void setPosUp() {
        changePosition(up);
    }

    public void setPosDown() {
        changePosition(down);
    }

    public void changePosition(double position) {
        paddle.setPosition(position);
        this.position = position;
    }

    public double getCurrentPosition() {
        return position;
    }
}
