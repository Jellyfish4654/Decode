package org.firstinspires.ftc.teamcode.Framework.Hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.Servo;

public class Spindexer {
    private final Servo spindexer;
    
    // Servo positions for intake and outtake of slots
    private final double[] POSITIONS_IN = {0.745, 0.382, 0};
    private final double[] POSITIONS_OUT = {0.19, 0.93, 0.56};
    
    private double position;
    
    // positive numbers for intake positions, negative for outtake positions, -3 to -1 and 1 to 3
    // absolute value and minus 1 for position indexes
    private int currentSlot;
    
    // artifacts in slots
    // 0=empty 1=green 2=purple
    private int[] contents = new int[3];

    public Spindexer (Servo servo) {
        this.spindexer = servo;
    }
    
    // combined, parameter directly matches slot variable above
    public void setSlot(int newSlot) {
        int posIndex = Math.abs(newSlot) - 1;
        if (posIndex >= 0 && posIndex <= 2) {
            if (newSlot > 0) {
                changePosition(POSITIONS_IN[posIndex]);
            } else if (newSlot < 0) {
                changePosition(POSITIONS_OUT[posIndex]);
            }
            this.currentSlot = newSlot;
        }
    }
    
    // separated methods with positive for both, 1 to 3
    public void setSlotIn (int newSlot) {
        if (newSlot >= 1 && newSlot <= 3) {
            changePosition(POSITIONS_IN[newSlot - 1]);
            this.currentSlot = newSlot;
        }
    }

    public void setSlotOut (int newSlot) {
        if (newSlot >= 1 && newSlot <= 3) {
            changePosition(POSITIONS_OUT[newSlot - 1]);
            this.currentSlot = -newSlot;
        }
    }
    
    
    public void changePosition(double newPosition) {
        spindexer.setPosition(newPosition);
        this.position = newPosition;
    }
    
    public double getCurrentPosition() {
        return position;
    }
    
    public int getSlot() {
        return currentSlot;
    }
    
    public void setContents(int slot, int newContents) {
        if (slot >= 1 && slot <= 3 && newContents >= 0 && newContents <= 2) {
            this.contents[slot - 1] = newContents;
        }
    }
    
    public int getContents(int slot) {
        if (slot >= 1 && slot <= 3) {
            return this.contents[slot - 1];
        } else {
            return 0;
        }
    }

    public int findSlot (int search) {
        int slot = 0;
        for (int i = 1; i <= 3; i++) {
            if (getContents(i) == search) {
                slot = i;
            }
        }
        return slot;
    }

    public class SlotIn implements Action {
        private SensorColor color;

        SlotIn (SensorColor color) {
            this.color = color;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            int slot = findSlot(0);
            setSlotIn(slot);
            return (getSlot() == slot);
        }
    }
    public Action slotIn(SensorColor color) {
        return new SlotIn(color);
    }

    public class GreenOut implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            int slot = findSlot(1);
            setSlotOut(slot);
            return (getSlot() == slot);
        }
    }
    public Action greenOut() {
        return new GreenOut();
    }

    public class PurpleOut implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            int slot = findSlot(1);
            setSlotOut(slot);
            return (getSlot() == slot);
        }
    }
    public Action purpleOut() {
        return new PurpleOut();
    }
}