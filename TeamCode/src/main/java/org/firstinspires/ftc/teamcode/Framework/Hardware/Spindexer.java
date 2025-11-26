package org.firstinspires.ftc.teamcode.Framework.Hardware;

import com.qualcomm.robotcore.hardware.Servo;

public class Spindexer {
    private final Servo spindexer;
    
    // Placeholder values
    private final double[] posIn = {0, 0.3, 0.6};
    private final double[] posOut = {0.5, 0.2, 0.4};
    
    private double position;
    
    // way to keep track of state or slot with enum or int?

    public Spindexer (Servo servo) {
        this.spindexer = servo;
    }

    public void setPosIn (int slot) {
        changePosition(posIn[slot-1]);
    }

    public void setPosOut (int slot) {
        changePosition(posOut[slot-1]);
    }
    
    public void changePosition(double position) {
        spindexer.setPosition(position);
        this.position = position;
    }
    
    public double getCurrentPosition() {
        return position;
    }

}