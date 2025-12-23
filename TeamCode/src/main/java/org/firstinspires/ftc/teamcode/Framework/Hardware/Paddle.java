package org.firstinspires.ftc.teamcode.Framework.Hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.Servo;

// TODO: create separate PaddleMotor class with PIDF
public class Paddle {
    private final Servo paddle;
    private final double POS_UP = 0.0245;
    private final double POS_DOWN = 0.219;
    private double position;
    private boolean isUp;

    public Paddle(Servo servo) {
        this.paddle = servo;
    }

    public void setUp() {
        changePosition(POS_UP);
        this.isUp = true;
    }

    public void setDown() {
        changePosition(POS_DOWN);
        this.isUp = false;
    }

    public void changePosition(double position) {
        paddle.setPosition(position);
        this.position = position;
    }

    public double getCurrentPosition() {
        return position;
    }
    
    public boolean isUp() {
        return isUp;
    }
    
    
    // ↓ -------------- ↓ -------------- ↓ AUTO ACTIONS ↓ -------------- ↓ -------------- ↓
    public class PaddleUp implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            setUp();
            return isUp();
        }
    }
    public Action paddleUp() {
        return new PaddleUp();
    }

    public class PaddleDown implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            setDown();
            return !isUp();
        }
    }
    public Action paddleDown() {
        return new PaddleDown();
    }
}