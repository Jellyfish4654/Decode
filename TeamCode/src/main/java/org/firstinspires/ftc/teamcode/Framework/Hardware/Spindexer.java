package org.firstinspires.ftc.teamcode.Framework.Hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Spindexer {
    private final Servo spindexer;
    
    // Servo positions for intake and outtake of slots
    public static double[] POSITIONS_IN = {0.745, 0.382, 0};
    public static double[] POSITIONS_OUT = {0.19, 0.93, 0.56};
    
    private double position;
    
    // positive numbers for intake positions, negative for outtake positions, -3 to -1 and 1 to 3
    // absolute value and minus 1 for position indexes
    private int currentSlot;
    
    // artifacts in slots
    public enum Artifact {
        EMPTY,
        GREEN,
        PURPLE
    }
    private Artifact[] contents = {Artifact.EMPTY, Artifact.EMPTY, Artifact.EMPTY};

    public Spindexer (Servo servo) {
        this.spindexer = servo;
    }
    
    // DON'T USE -- unless its easier after getCurrentSlot()
    // combined, parameter directly matches slot variable above
    public void setSlot(int newSlotPlusMinus) {
        if (newSlotPlusMinus > 0) {
                setSlotIn(newSlotPlusMinus);
        } else if (newSlotPlusMinus < 0) {
                setSlotOut(newSlotPlusMinus);
        }
    }
    
    // separated methods with positive for both, 1 to 3
    // abs to minimize errors
    public void setSlotIn (int newSlot) {
        int newSlotAbs = Math.abs(newSlot);
        if (newSlotAbs >= 1 && newSlotAbs <= 3) {
            changePosition(POSITIONS_IN[newSlotAbs - 1]);
            this.currentSlot = newSlotAbs;
        }
    }

    public void setSlotOut (int newSlot) {
        int newSlotAbs = Math.abs(newSlot);
        if (newSlotAbs >= 1 && newSlotAbs <= 3) {
            changePosition(POSITIONS_OUT[newSlotAbs - 1]);
            this.currentSlot = -newSlotAbs;
        }
    }
    
    
    public void changePosition(double newPosition) {
        spindexer.setPosition(newPosition);
        this.position = newPosition;
    }
    
    public double getCurrentPosition() {
        return position;
    }
    
    public int getCurrentSlot() {
        return currentSlot;
    }
    
    public void setContents(Artifact newContents) {
        int slotAbs = Math.abs(currentSlot);
        this.contents[slotAbs - 1] = newContents;
    }
    
    public void setContents(int slot, Artifact newContents) {
        int slotAbs = Math.abs(slot);
        if (slotAbs >= 1 && slotAbs <= 3) {
            this.contents[slotAbs - 1] = newContents;
        }
    }
    
    public Artifact getContents(int slot) {
        if (slot >= 1 && slot <= 3) {
            return this.contents[slot - 1];
        } else {
            return Artifact.EMPTY;
        }
    }

    public int findSlot (Artifact content) {
        for (int slot = 1; slot <= 3; slot++) {
            if (getContents(slot) == content) {
                return slot;
            }
        }
        return 0;
    }
    
    
    // ↓ -------------- ↓ -------------- ↓ AUTO ACTIONS ↓ -------------- ↓ -------------- ↓
    public class SlotIn implements Action {
        private SensorColor color;

        SlotIn (SensorColor color) {
            this.color = color;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            int slot = findSlot(Artifact.EMPTY);
            setSlotIn(slot);
            return (getCurrentSlot() == slot);
        }
    }
    public Action slotIn(SensorColor color) {
        return new SlotIn(color);
    }

    public class GreenOut implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            int slot = findSlot(Artifact.GREEN);
            setSlotOut(slot);
            return (getCurrentSlot() == slot);
        }
    }
    public Action greenOut() {
        return new GreenOut();
    }

    public class PurpleOut implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            int slot = findSlot(Artifact.PURPLE);
            setSlotOut(slot);
            return (getCurrentSlot() == slot);
        }
    }
    public Action purpleOut() {
        return new PurpleOut();
    }
}