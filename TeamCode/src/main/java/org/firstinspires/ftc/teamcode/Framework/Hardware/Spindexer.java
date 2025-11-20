package org.firstinspires.ftc.teamcode.Framework.Hardware;

import com.qualcomm.robotcore.hardware.Servo;

public class Spindexer {
    private Servo spindexer;
    private double[] posIn = {0, 0.3, 0.6};
    private double[] posOut = {0.5, 0.2, 0.4};

    public Spindexer (Servo servo) {
        this.spindexer = servo;
    }

    public void setIn (int slot) {
        spindexer.setPosition(posIn[slot-1]);
    }

    public void setOut (int slot) {
        spindexer.setPosition(posOut[slot-1]);
    }

}
