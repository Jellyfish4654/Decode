package org.firstinspires.ftc.teamcode.Framework.Hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
public class PaddleMotor {
    private final DcMotorEx paddle;
    
    // TODO: tune PIDF, MAX_VEL, and POS constants
    public static double P = 0;
    public static double I = 0;
    public static double D = 0;
    public static double F = 0;
    public static double MAX_VEL = 200;
    public static int POS_UP = 1;
    public static int POS_DOWN = 0;
    
    private int position;
    private boolean isUp;

    public PaddleMotor(DcMotorEx motor) {
        this.paddle = motor;
        this.paddle.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void setUp() {
        changePosition(POS_UP);
        this.isUp = true;
    }

    public void setDown() {
        changePosition(POS_DOWN);
        this.isUp = false;
    }

    public void changePosition(int position) {
        this.paddle.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        this.paddle.setVelocityPIDFCoefficients(P, I, D, F);
        this.paddle.setVelocity(MAX_VEL);
        this.paddle.setTargetPosition(position);


        this.position = position;
    }

    public int getCurrentPosition() {
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