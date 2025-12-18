package org.firstinspires.ftc.teamcode.Framework.Hardware;


import com.qualcomm.robotcore.hardware.Gamepad;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

enum Buttons {
    TRIANGLE,
    CIRCLE,
    SQUARE,
    CROSS,
    DPAD_UP,
    DPAD_DOWN,
    DPAD_LEFT,
    DPAD_RIGHT,
    LEFT_BUMPER,
    RIGHT_BUMPER,
    PLAYSTATION_LOGO
}

enum Joysticks {
    LEFT_JOYSTICK,
    RIGHT_JOYSTICK
}
public class Controller {
    private final Gamepad gamepad;
    Buttons lowPrecisionBtn;
    Buttons highPrecisionBtn;
    Buttons outPurpleBtn;
    Buttons outGreenBtn;
    Buttons intakeBtn;
    Buttons driveModeBtn;

    Joysticks turnStk;
    Joysticks moveStk;
    public Controller(Gamepad gamepad) {
        this.gamepad = gamepad;
        //CHANGE BELOW TO CHANGE CONTROLLER CONFIG
        this.lowPrecisionBtn = Buttons.LEFT_BUMPER;
        this.highPrecisionBtn = Buttons.RIGHT_BUMPER;
        this.outGreenBtn = Buttons.CIRCLE;
        this.outPurpleBtn = Buttons.SQUARE;
        this.intakeBtn = Buttons.CROSS;
        this.driveModeBtn = Buttons.PLAYSTATION_LOGO;

        this.turnStk = Joysticks.RIGHT_JOYSTICK;
        this.moveStk = Joysticks.LEFT_JOYSTICK;

    }

    private Field getField(Buttons btn) throws NoSuchFieldException{

        switch (btn) {
            case CROSS:
                return Gamepad.class.getField("cross");
            case SQUARE:
                return Gamepad.class.getField("square");
            case TRIANGLE:
                return Gamepad.class.getField("triangle");
            case CIRCLE:
                return Gamepad.class.getField("circle");
            case DPAD_UP:
                return Gamepad.class.getField("dpad_up");
            case DPAD_DOWN:
                return Gamepad.class.getField("dpad_down");
            case DPAD_LEFT:
                return Gamepad.class.getField("dpad_left");
            case DPAD_RIGHT:
                return Gamepad.class.getField("dpad_right");
            case LEFT_BUMPER:
                return Gamepad.class.getField("left_bumper");
            case RIGHT_BUMPER:
                return Gamepad.class.getField("right_bumper");
            case PLAYSTATION_LOGO:
                return Gamepad.class.getField("ps");
        }

        return null;
    }

    private Method getMethod(Buttons btn) throws NoSuchMethodException{

        switch (btn) {
            case CROSS:
                return Gamepad.class.getMethod("crossWasPressed");
            case SQUARE:
                return Gamepad.class.getMethod("squareWasPressed");
            case TRIANGLE:
                return Gamepad.class.getMethod("triangleWasPressed");
            case CIRCLE:
                return Gamepad.class.getMethod("circleWasPressed");
            case DPAD_UP:
                return Gamepad.class.getMethod("dpadUpWasPressed");
            case DPAD_DOWN:
                return Gamepad.class.getMethod("dpadDownWasPressed");
            case DPAD_LEFT:
                return Gamepad.class.getMethod("dpadLeftWasPressed");
            case DPAD_RIGHT:
                return Gamepad.class.getMethod("dpadRightWasPressed");
            case LEFT_BUMPER:
                return Gamepad.class.getMethod("leftBumperWasPressed");
            case RIGHT_BUMPER:
                return Gamepad.class.getMethod("rightBumperWasPressed");
            case PLAYSTATION_LOGO:
                return Gamepad.class.getMethod("psWasPressed");

        }

        return null;
    }

    public double turnStickX(){
        switch (this.turnStk){
            case LEFT_JOYSTICK:
                return this.gamepad.left_stick_x;
            case RIGHT_JOYSTICK:
                return this.gamepad.right_stick_x;
        }
        return 0.0;
    }

    public double turnStickY(){
        switch (this.turnStk){
            case LEFT_JOYSTICK:
                return this.gamepad.left_stick_y;
            case RIGHT_JOYSTICK:
                return this.gamepad.right_stick_y;
        }
        return 0.0;
    }

    public double moveStickX(){
        switch (this.moveStk){
            case LEFT_JOYSTICK:
                return this.gamepad.left_stick_x;
            case RIGHT_JOYSTICK:
                return this.gamepad.right_stick_x;
        }
        return 0.0;
    }

    public double moveStickY(){
        switch (this.moveStk){
            case LEFT_JOYSTICK:
                return -this.gamepad.left_stick_y;
            case RIGHT_JOYSTICK:
                return -this.gamepad.right_stick_y;
        }
        return 0.0;
    }

    public double[] moveStick(){
        return new double[] {this.moveStickX(),this.moveStickY()};
    }
    public double[] turnStick(){
        return new double[] {this.turnStickX(),this.turnStickY()};
    }

    public boolean intake() {
        try {
            return this.getField(this.intakeBtn).getBoolean(this.gamepad);
        }catch (Exception e){
            return false;
        }
    }

    public boolean outPurple() {
        try {
            return this.getField(this.outPurpleBtn).getBoolean(this.gamepad);
        }catch (Exception e){
            return false;
        }
    }

    public boolean outGreen() {
        try {
            return this.getField(this.outGreenBtn).getBoolean(this.gamepad);
        }catch (Exception e){
            return false;
        }
    }

    public boolean driveMode() {
        try {
            return this.getField(this.driveModeBtn).getBoolean(this.gamepad);
        }catch (Exception e){
            return false;
        }
    }

    public boolean lowPrecision() {
        try {
            return this.getField(this.lowPrecisionBtn).getBoolean(this.gamepad);
        }catch (Exception e){
            return false;
        }
    }

    public boolean highPrecision() {
        try {
            return this.getField(this.highPrecisionBtn).getBoolean(this.gamepad);
        }catch (Exception e){
            return false;
        }
    }

    public boolean intakePressed() {
        try {
            return (boolean)this.getMethod(this.intakeBtn).invoke(this.gamepad);
        }catch (Exception e){
            return false;
        }
    }

    public boolean outPurplePressed() {
        try {
            return (boolean)this.getMethod(this.outPurpleBtn).invoke(this.gamepad);
        }catch (Exception e){
            return false;
        }
    }

    public boolean outGreenPressed() {
        try {
            return (boolean)this.getMethod(this.outGreenBtn).invoke(this.gamepad);
        }catch (Exception e){
            return false;
        }
    }

    public boolean driveModePressed() {
        try {
            return (boolean)this.getMethod(this.driveModeBtn).invoke(this.gamepad);
        }catch (Exception e){
            return false;
        }
    }

    public boolean lowPrecisionPressed() {
        try {
            return (boolean)this.getMethod(this.lowPrecisionBtn).invoke(this.gamepad);
        }catch (Exception e){
            return false;
        }
    }

    public boolean highPrecisionPressed() {
        try {
            return (boolean)this.getMethod(this.highPrecisionBtn).invoke(this.gamepad);
        }catch (Exception e){
            return false;
        }
    }

    public void rumble(int durationMs){
        this.gamepad.rumble(durationMs);
    }
    public void rumble(double rumble1, double rumble2, int durationMs){
        this.gamepad.rumble(rumble1,rumble2,durationMs);
    }
}