package org.firstinspires.ftc.teamcode.Framework.Hardware;

import com.qualcomm.robotcore.hardware.Servo;

public class Paddle {
    private final Servo paddle;
    private final double posUp = 0.2735;
    private final double posDown = 0.142;
    private double position;
    private boolean isUp;

    public Paddle(Servo servo) {
        this.paddle = servo;
    }

    public void setUp() {
        changePosition(posUp);
        this.isUp = true;
    }

    public void setDown() {
        changePosition(posDown);
        this.isUp = false;
    }

    public void changePosition(double position) {
        paddle.setPosition(position);
        this.position = position;
    }

    public double getCurrentPosition() {
        return position;
    }
    
    public boolean getState() {
        return isUp;
    }
}