package org.firstinspires.ftc.teamcode.Framework;


import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.JellyTele;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Controller {
    enum Button {
        PRIMARY_RIGHT_TRIGGER_BUTTON,
        PRIMARY_LEFT_TRIGGER_BUTTON,
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
        SECONDARY_RIGHT_TRIGGER_BUTTON,
        SECONDARY_LEFT_TRIGGER_BUTTON,
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
    double TRIGGER_BUTTON_THRESHOLD;
    Button lowPrecisionBtn;
    Button highPrecisionBtn;
    Button outPurpleBtn;
    Button outGreenBtn;
    Button outMotifBtn;
    Button intakeBtn;
    Button outPrespinBtn;
    Button unjamBtn;
    Button clearSpindexerBtn;
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
        
        TRIGGER_BUTTON_THRESHOLD = 0.5;

        //Primary functions (intake, outtake, drivetrain)
        this.outGreenBtn = Button.PRIMARY_CIRCLE;
        this.outPurpleBtn = Button.PRIMARY_SQUARE;
        this.outMotifBtn = Button.PRIMARY_CROSS;
        this.intakeBtn = Button.PRIMARY_RIGHT_TRIGGER_BUTTON;
        this.outPrespinBtn = Button.PRIMARY_TRIANGLE; // TODO: swap this and motif button?

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
        
        //emergency buttons
        this.unjamBtn = Button.SECONDARY_LEFT_TRIGGER_BUTTON;
        this.clearSpindexerBtn = Button.SECONDARY_RIGHT_TRIGGER_BUTTON;
        
        this.gamepad1.setTriggerThreshold((float) TRIGGER_BUTTON_THRESHOLD);
        this.gamepad2.setTriggerThreshold((float) TRIGGER_BUTTON_THRESHOLD);
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
            case PRIMARY_LEFT_TRIGGER_BUTTON:
            case SECONDARY_LEFT_TRIGGER_BUTTON:
                return Gamepad.class.getField("left_trigger_pressed");
            case PRIMARY_RIGHT_TRIGGER_BUTTON:
            case SECONDARY_RIGHT_TRIGGER_BUTTON:
                return Gamepad.class.getField("right_trigger_pressed");
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
            case PRIMARY_LEFT_TRIGGER_BUTTON:
            case SECONDARY_LEFT_TRIGGER_BUTTON:
                return Gamepad.class.getMethod("leftTriggerWasPressed");
            case PRIMARY_RIGHT_TRIGGER_BUTTON:
            case SECONDARY_RIGHT_TRIGGER_BUTTON:
                return Gamepad.class.getMethod("rightTriggerWasPressed");
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
            case SECONDARY_RIGHT_TRIGGER_BUTTON:
            case SECONDARY_LEFT_TRIGGER_BUTTON:
                return this.gamepad2;
            default:
                return this.gamepad1;
        }
    }
    
    private boolean buttonIsOn(Button btn){
        try {
            return this.getField(btn).getBoolean(chooseGamepad(btn));
        }catch (Exception e){
            return false;
        }
    }

    private boolean buttonPressed(Button btn){
        try {
            return (boolean)this.getMethod(btn).invoke(chooseGamepad(btn));
        }catch (Exception e){
            return false;
        }
    }
    
    public double turnStickX(){
        switch (this.turnStk){
            case PRIMARY_LEFT_JOYSTICK:
                return this.gamepad1.left_stick_x;
            case PRIMARY_RIGHT_JOYSTICK:
                return this.gamepad1.right_stick_x;
            case SECONDARY_LEFT_JOYSTICK:
                return this.gamepad2.left_stick_x;
            case SECONDARY_RIGHT_JOYSTICK:
                return this.gamepad2.right_stick_x;
        }
        return 0.0;
    }

    public double turnStickY(){  // y-values are inverted here so up is positive
        switch (this.turnStk){
            case PRIMARY_LEFT_JOYSTICK:
                return -this.gamepad1.left_stick_y;
            case PRIMARY_RIGHT_JOYSTICK:
                return -this.gamepad1.right_stick_y;
            case SECONDARY_LEFT_JOYSTICK:
                return -this.gamepad2.left_stick_y;
            case SECONDARY_RIGHT_JOYSTICK:
                return -this.gamepad2.right_stick_y;
        }
        return 0.0;
    }

    public double moveStickX(){
        switch (this.moveStk){
            case PRIMARY_LEFT_JOYSTICK:
                return this.gamepad1.left_stick_x;
            case PRIMARY_RIGHT_JOYSTICK:
                return this.gamepad1.right_stick_x;
            case SECONDARY_LEFT_JOYSTICK:
                return this.gamepad2.left_stick_x;
            case SECONDARY_RIGHT_JOYSTICK:
                return this.gamepad2.right_stick_x;
        }
        return 0.0;
    }

    public double moveStickY(){ // y-values are inverted here so up is positive
        switch (this.moveStk){
            case PRIMARY_LEFT_JOYSTICK:
                return -this.gamepad1.left_stick_y;
            case PRIMARY_RIGHT_JOYSTICK:
                return -this.gamepad1.right_stick_y;
            case SECONDARY_LEFT_JOYSTICK:
                return -this.gamepad2.left_stick_y;
            case SECONDARY_RIGHT_JOYSTICK:
                return -this.gamepad2.right_stick_y;
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
        return buttonIsOn(this.intakeBtn);
    }

    public boolean outPrespin() {
        return buttonIsOn(this.outPrespinBtn);
    }
    
    public boolean unjam() {
        return buttonIsOn(this.unjamBtn);
    }
    
    public boolean clearSpindexer() {
        return buttonIsOn(this.clearSpindexerBtn);
    }

    public boolean outPurple() {
        return buttonIsOn(this.outPurpleBtn);
    }

    public boolean outGreen() {
        return buttonIsOn(this.outGreenBtn);
    }

    public boolean outMotif(){
        return buttonIsOn(this.outMotifBtn);
    }

    public boolean driveMode() {
        return buttonIsOn(this.driveModeBtn);
    }

    public boolean lowPrecision() {
        return buttonIsOn(this.lowPrecisionBtn);
    }

    public boolean highPrecision() {
        return buttonIsOn(this.highPrecisionBtn);
    }

    public boolean allianceRed() {
        return buttonIsOn(this.allianceRedBtn);
    }

    public boolean allianceBlue() {
        return buttonIsOn(this.allianceBlueBtn);
    }

    public boolean motifGPP() {
        return buttonIsOn(this.motifGPPBtn);
    }

    public boolean motifPGP() {
        return buttonIsOn(this.motifPGPBtn);
    }

    public boolean motifPPG() {
        return buttonIsOn(this.motifPPGBtn);
    }

    public boolean intakePressed() {
        return buttonPressed(this.intakeBtn);
    }
    
    public boolean outPrespinPressed() {
        return buttonPressed(this.outPrespinBtn);
    }
    
    public boolean unjamPressed() {
        return buttonPressed(this.unjamBtn);
    }
    
    public boolean clearSpindexerPressed() {
        return buttonPressed(this.clearSpindexerBtn);
    }

    public boolean outPurplePressed() {
        return buttonPressed(this.outPurpleBtn);
    }

    public boolean outGreenPressed() {
        return buttonPressed(this.outGreenBtn);
    }

    public boolean outMotifPressed(){
        return buttonPressed(this.outMotifBtn);
    }

    public boolean driveModePressed() {
        return buttonPressed(this.driveModeBtn);
    }

    public boolean lowPrecisionPressed() {
        return buttonPressed(this.lowPrecisionBtn);
    }

    public boolean highPrecisionPressed() {
        return buttonPressed(this.highPrecisionBtn);
    }

    public boolean allianceRedPressed() {
        return buttonPressed(this.allianceRedBtn);
    }

    public boolean allianceBluePressed() {
        return buttonPressed(this.allianceBlueBtn);
    }

    public boolean motifGPPPressed() {
        return buttonPressed(this.motifGPPBtn);
    }

    public boolean motifPGPPressed() {
        return buttonPressed(this.motifPGPBtn);
    }

    public boolean motifPPGPressed() {
        return buttonPressed(this.motifPPGBtn);
    }

    public void rumble(int durationMs, boolean rumbleSecondary){
        if(rumbleSecondary){
            this.gamepad2.rumble(durationMs);
        }else{
            this.gamepad1.rumble(durationMs);
        }

    }
//    public void rumble(double rumble1, double rumble2, int durationMs, boolean rumbleSecondary){
//        if(rumbleSecondary){
//            this.gamepad2.rumble(rumble1,rumble2,durationMs);
//        }else{
//            this.gamepad1.rumble(rumble1,rumble2,durationMs);
//        }
//
//    }
    
    public void megaRumble() {
        Gamepad.RumbleEffect megaEffect = new Gamepad.RumbleEffect.Builder()
                .addStep(1, 0, 250)
                .addStep(0, 1, 250)
                .addStep(1, 0, 250)
                .addStep(0, 1, 250)
                .addStep(1, 1, 500)
                .build();
        gamepad1.runRumbleEffect(megaEffect);
        gamepad2.runRumbleEffect(megaEffect);
    }
    
    public void setLEDs(JellyTele.SpinState state) {
        double[] rgb = {0, 0, 0};
        if (state != null) {
            switch (state) {
                case STANDBY:
                    rgb = new double[] {0.9, 1, 0.9}; // white
                    break;
                case PRESPIN_OUTTAKE:
                    rgb = new double[] {1, 0, 1}; // purple
                    break;
                case SPIN_INTAKE:
                    rgb = new double[] {1, 1, 0}; // yellow
                    break;
                case SPIN_OUTTAKE:
                    rgb = new double[] {0, 0.7, 1}; // cyan
                    break;
                case INTAKING:
                    rgb = new double[] {0, 1, 0}; // green
                    break;
                case OUTTAKING:
                    rgb = new double[] {0, 0.2, 1}; // blue
                    break;
                case UNJAM:
                    rgb = new double[] {1, 0, 0}; // red
                    break;
            }
        }
        this.gamepad1.setLedColor(rgb[0], rgb[1], rgb[2], Gamepad.LED_DURATION_CONTINUOUS); // TODO: keep LED for main driver?
        this.gamepad2.setLedColor(rgb[0], rgb[1], rgb[2], Gamepad.LED_DURATION_CONTINUOUS);
    }
}