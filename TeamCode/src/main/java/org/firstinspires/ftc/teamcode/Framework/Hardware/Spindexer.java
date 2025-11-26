package org.firstinspires.ftc.teamcode.Framework.Hardware;

import com.qualcomm.robotcore.hardware.Servo;

public class Spindexer {
    private final Servo spindexer;
    
    // Placeholder values
    private final double[] posIn = {0, 0.3, 0.6};
    private final double[] posOut = {0.5, 0.2, 0.4};
    
    private double position;
    
    // positive numbers for intake positions, negative for outtake positions, -3 to -1 and 1 to 3
    // absolute value and minus 1 for position indexes
    private int slot;

    public Spindexer (Servo servo) {
        this.spindexer = servo;
    }
    
    // combined, parameter directly matches slot variable above
    public void setSlot(int newSlot) {
        int posIndex = Math.abs(newSlot) - 1;
        if (newSlot > 0) {
            changePosition(posIn[posIndex]);
        } else if (newSlot < 0) {
            changePosition(posOut[posIndex]);
        }
        this.slot = newSlot;
    }
    
    // separated methods with positive for both, 1 to 3
    public void setSlotIn (int newSlot) {
        changePosition(posIn[newSlot-1]);
        this.slot = newSlot;
    }

    public void setSlotOut (int newSlot) {
        changePosition(posOut[newSlot-1]);
        this.slot = -newSlot;
    }
    
    
    public void changePosition(double newPosition) {
        spindexer.setPosition(newPosition);
        this.position = newPosition;
    }
    
    public double getCurrentPosition() {
        return position;
    }
    
    public int getSlot() {
        return slot;
    }

}