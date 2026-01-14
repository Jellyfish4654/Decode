package org.firstinspires.ftc.teamcode.Framework.Hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Paddle {
    private final Servo paddle;
    public static double POS_UP = -1;
    public static double POS_DOWN = 0.219;
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
    
    public void deenergize() {
        paddle.getController().pwmDisable();
    }
    
    // ↓ -------------- ↓ -------------- ↓ AUTO ACTIONS ↓ -------------- ↓ -------------- ↓
    public class PaddleUp implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            setUp();
            return !isUp();
        }
    }
    public Action paddleUp() {
        return new PaddleUp();
    }

    public class PaddleDown implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            setDown();
            return isUp();
        }
    }
    public Action paddleDown() {
        return new PaddleDown();
    }
}