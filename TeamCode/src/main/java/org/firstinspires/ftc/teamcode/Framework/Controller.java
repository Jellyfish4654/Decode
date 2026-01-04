package org.firstinspires.ftc.teamcode.Framework;


import com.qualcomm.robotcore.hardware.Gamepad;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Controller {
    enum Button {
        PRIMARY_TRIANGLE,
        PRIMARY_CIRCLE,
        PRIMARY_SQUARE,
        PRIMARY_CROSS,
        PRIMARY_DPAD_UP,
        PRIMARY_DPAD_DOWN,
        PRIMARY_DPAD_LEFT,
        PRIMARY_DPAD_RIGHT,
        PRIMARY_LEFT_BUMPER,
        PRIMARY_RIGHT_BUMPER,
        PRIMARY_PLAYSTATION_LOGO,
        SECONDARY_TRIANGLE,
        SECONDARY_CIRCLE,
        SECONDARY_SQUARE,
        SECONDARY_CROSS,
        SECONDARY_DPAD_UP,
        SECONDARY_DPAD_DOWN,
        SECONDARY_DPAD_LEFT,
        SECONDARY_DPAD_RIGHT,
        SECONDARY_LEFT_BUMPER,
        SECONDARY_RIGHT_BUMPER,
        SECONDARY_PLAYSTATION_LOGO
    }

    enum Joystick {
        PRIMARY_LEFT_JOYSTICK,
        PRIMARY_RIGHT_JOYSTICK,
        SECONDARY_LEFT_JOYSTICK,
        SECONDARY_RIGHT_JOYSTICK
    }

    private final Gamepad gamepad1;
    private final Gamepad gamepad2;
    Button lowPrecisionBtn;
    Button highPrecisionBtn;
    Button outPurpleBtn;
    Button outGreenBtn;
    Button outMotifBtn;
    Button intakeBtn;
    Button driveModeBtn;
    Button allianceRedBtn;
    Button allianceBlueBtn;
    Button motifPGPBtn;
    Button motifPPGBtn;
    Button motifGPPBtn;

    Joystick turnStk;
    Joystick moveStk;
    public Controller(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        //CHANGE BELOW TO CHANGE CONTROLLER CONFIG

        //Primary functions (intake, outtake, drivetrain)
        this.outGreenBtn = Button.PRIMARY_CIRCLE;
        this.outPurpleBtn = Button.PRIMARY_SQUARE;
        this.outMotifBtn = Button.PRIMARY_TRIANGLE;
        this.intakeBtn = Button.PRIMARY_CROSS;

        this.lowPrecisionBtn = Button.PRIMARY_LEFT_BUMPER;
        this.highPrecisionBtn = Button.PRIMARY_RIGHT_BUMPER;
        this.driveModeBtn = Button.PRIMARY_PLAYSTATION_LOGO;

        this.turnStk = Joystick.PRIMARY_RIGHT_JOYSTICK;
        this.moveStk = Joystick.PRIMARY_LEFT_JOYSTICK;

        //Secondary config functions (alliance and motif)
        this.allianceRedBtn = Button.SECONDARY_SQUARE;
        this.allianceBlueBtn = Button.SECONDARY_CIRCLE;

        //this is actually EASY to remember: left dpad when the g is on the left, up when g is in middle, etc
        this.motifGPPBtn = Button.SECONDARY_DPAD_LEFT;
        this.motifPGPBtn = Button.SECONDARY_DPAD_UP;
        this.motifPPGBtn = Button.SECONDARY_DPAD_RIGHT;

    }

    private Field getField(Button btn) throws NoSuchFieldException{

        switch (btn) {
            case PRIMARY_CROSS:
            case SECONDARY_CROSS:
                return Gamepad.class.getField("cross");
            case PRIMARY_SQUARE:
            case SECONDARY_SQUARE:
                return Gamepad.class.getField("square");
            case PRIMARY_TRIANGLE:
            case SECONDARY_TRIANGLE:
                return Gamepad.class.getField("triangle");
            case PRIMARY_CIRCLE:
            case SECONDARY_CIRCLE:
                return Gamepad.class.getField("circle");
            case PRIMARY_DPAD_UP:
            case SECONDARY_DPAD_UP:
                return Gamepad.class.getField("dpad_up");
            case PRIMARY_DPAD_DOWN:
            case SECONDARY_DPAD_DOWN:
                return Gamepad.class.getField("dpad_down");
            case PRIMARY_DPAD_LEFT:
            case SECONDARY_DPAD_LEFT:
                return Gamepad.class.getField("dpad_left");
            case PRIMARY_DPAD_RIGHT:
            case SECONDARY_DPAD_RIGHT:
                return Gamepad.class.getField("dpad_right");
            case PRIMARY_LEFT_BUMPER:
            case SECONDARY_LEFT_BUMPER:
                return Gamepad.class.getField("left_bumper");
            case PRIMARY_RIGHT_BUMPER:
            case SECONDARY_RIGHT_BUMPER:
                return Gamepad.class.getField("right_bumper");
            case PRIMARY_PLAYSTATION_LOGO:
            case SECONDARY_PLAYSTATION_LOGO:
                return Gamepad.class.getField("ps");
        }

        return null;
    }

    private Method getMethod(Button btn) throws NoSuchMethodException{

        switch (btn) {
            case PRIMARY_CROSS:
            case SECONDARY_CROSS:
                return Gamepad.class.getMethod("crossWasPressed");
            case PRIMARY_SQUARE:
            case SECONDARY_SQUARE:
                return Gamepad.class.getMethod("squareWasPressed");
            case PRIMARY_TRIANGLE:
            case SECONDARY_TRIANGLE:
                return Gamepad.class.getMethod("triangleWasPressed");
            case PRIMARY_CIRCLE:
            case SECONDARY_CIRCLE:
                return Gamepad.class.getMethod("circleWasPressed");
            case PRIMARY_DPAD_UP:
            case SECONDARY_DPAD_UP:
                return Gamepad.class.getMethod("dpadUpWasPressed");
            case PRIMARY_DPAD_DOWN:
            case SECONDARY_DPAD_DOWN:
                return Gamepad.class.getMethod("dpadDownWasPressed");
            case PRIMARY_DPAD_LEFT:
            case SECONDARY_DPAD_LEFT:
                return Gamepad.class.getMethod("dpadLeftWasPressed");
            case PRIMARY_DPAD_RIGHT:
            case SECONDARY_DPAD_RIGHT:
                return Gamepad.class.getMethod("dpadRightWasPressed");
            case PRIMARY_LEFT_BUMPER:
            case SECONDARY_LEFT_BUMPER:
                return Gamepad.class.getMethod("leftBumperWasPressed");
            case PRIMARY_RIGHT_BUMPER:
            case SECONDARY_RIGHT_BUMPER:
                return Gamepad.class.getMethod("rightBumperWasPressed");
            case PRIMARY_PLAYSTATION_LOGO:
            case SECONDARY_PLAYSTATION_LOGO:
                return Gamepad.class.getMethod("psWasPressed");

        }

        return null;
    }

    private Gamepad chooseGamepad(Button btn) {
        switch (btn) {
            case SECONDARY_CROSS:
            case SECONDARY_SQUARE:
            case SECONDARY_TRIANGLE:
            case SECONDARY_CIRCLE:
            case SECONDARY_DPAD_UP:
            case SECONDARY_DPAD_DOWN:
            case SECONDARY_DPAD_LEFT:
            case SECONDARY_DPAD_RIGHT:
            case SECONDARY_LEFT_BUMPER:
            case SECONDARY_RIGHT_BUMPER:
            case SECONDARY_PLAYSTATION_LOGO:
                return this.gamepad2;
            default:
                return this.gamepad1;
        }
    }
    public double turnStickX(){
        switch (this.turnStk){
            case PRIMARY_LEFT_JOYSTICK:
                return this.gamepad1.left_stick_x;
            case PRIMARY_RIGHT_JOYSTICK:
                return this.gamepad1.right_stick_x;
        }
        return 0.0;
    }

    public double turnStickY(){
        switch (this.turnStk){
            case PRIMARY_LEFT_JOYSTICK:
                return this.gamepad1.left_stick_y;
            case PRIMARY_RIGHT_JOYSTICK:
                return this.gamepad1.right_stick_y;
        }
        return 0.0;
    }

    public double moveStickX(){
        switch (this.moveStk){
            case PRIMARY_LEFT_JOYSTICK:
                return this.gamepad1.left_stick_x;
            case PRIMARY_RIGHT_JOYSTICK:
                return this.gamepad1.right_stick_x;
        }
        return 0.0;
    }

    public double moveStickY(){
        switch (this.moveStk){
            case PRIMARY_LEFT_JOYSTICK:
                return -this.gamepad1.left_stick_y;
            case PRIMARY_RIGHT_JOYSTICK:
                return -this.gamepad1.right_stick_y;
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
            return this.getField(this.intakeBtn).getBoolean(chooseGamepad(this.intakeBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean outPurple() {
        try {
            return this.getField(this.outPurpleBtn).getBoolean(chooseGamepad(this.outPurpleBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean outGreen() {
        try {
            return this.getField(this.outGreenBtn).getBoolean(chooseGamepad(this.outGreenBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean outMotif(){
        try {
            return this.getField(this.outMotifBtn).getBoolean(chooseGamepad(this.outMotifBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean driveMode() {
        try {
            return this.getField(this.driveModeBtn).getBoolean(chooseGamepad(this.driveModeBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean lowPrecision() {
        try {
            return this.getField(this.lowPrecisionBtn).getBoolean(chooseGamepad(this.lowPrecisionBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean highPrecision() {
        try {
            return this.getField(this.highPrecisionBtn).getBoolean(chooseGamepad(this.highPrecisionBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean allianceRed() {
        try {
            return this.getField(this.allianceRedBtn).getBoolean(chooseGamepad(this.allianceRedBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean allianceBlue() {
        try {
            return this.getField(this.allianceBlueBtn).getBoolean(chooseGamepad(this.allianceBlueBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean motifGPP() {
        try {
            return this.getField(this.motifGPPBtn).getBoolean(chooseGamepad(this.motifGPPBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean motifPGP() {
        try {
            return this.getField(this.motifPGPBtn).getBoolean(chooseGamepad(this.motifPGPBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean motifPPG() {
        try {
            return this.getField(this.motifPPGBtn).getBoolean(chooseGamepad(this.motifPPGBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean intakePressed() {
        try {
            return (boolean)this.getMethod(this.intakeBtn).invoke(chooseGamepad(this.intakeBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean outPurplePressed() {
        try {
            return (boolean)this.getMethod(this.outPurpleBtn).invoke(chooseGamepad(this.outPurpleBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean outGreenPressed() {
        try {
            return (boolean)this.getMethod(this.outGreenBtn).invoke(chooseGamepad(this.outGreenBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean outMotifPressed(){
        try {
            return (boolean) this.getMethod(this.outMotifBtn).invoke(chooseGamepad(this.outMotifBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean driveModePressed() {
        try {
            return (boolean)this.getMethod(this.driveModeBtn).invoke(chooseGamepad(this.driveModeBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean lowPrecisionPressed() {
        try {
            return (boolean)this.getMethod(this.lowPrecisionBtn).invoke(chooseGamepad(this.lowPrecisionBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean highPrecisionPressed() {
        try {
            return (boolean)this.getMethod(this.highPrecisionBtn).invoke(chooseGamepad(this.highPrecisionBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean allianceRedPressed() {
        try {
            return (boolean)this.getMethod(this.allianceRedBtn).invoke(chooseGamepad(this.allianceRedBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean allianceBluePressed() {
        try {
            return (boolean)this.getMethod(this.allianceBlueBtn).invoke(chooseGamepad(this.allianceBlueBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean motifGPPPressed() {
        try {
            return (boolean)this.getMethod(this.motifGPPBtn).invoke(chooseGamepad(this.motifGPPBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean motifPGPPressed() {
        try {
            return (boolean)this.getMethod(this.motifPGPBtn).invoke(chooseGamepad(this.motifPGPBtn));
        }catch (Exception e){
            return false;
        }
    }

    public boolean motifPPGPressed() {
        try {
            return (boolean)this.getMethod(this.motifPPGBtn).invoke(chooseGamepad(this.motifPPGBtn));
        }catch (Exception e){
            return false;
        }
    }

    public void rumble(int durationMs, boolean rumbleSecondary){
        if(rumbleSecondary){
            this.gamepad2.rumble(durationMs);
        }else{
            this.gamepad1.rumble(durationMs);
        }

    }
    public void rumble(double rumble1, double rumble2, int durationMs, boolean rumbleSecondary){
        if(rumbleSecondary){
            this.gamepad2.rumble(rumble1,rumble2,durationMs);
        }else{
            this.gamepad1.rumble(rumble1,rumble2,durationMs);
        }

    }
}