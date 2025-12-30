package org.firstinspires.ftc.teamcode.Framework;

public class Params {
    
    // set both in auto for use in auto and teleop
    public static Alliance alliance = Alliance.RED;
    public static Motif motif = Motif.GPP;
    
    public enum Alliance {
        RED,
        BlUE
    }
    
    public enum Motif {
        GPP,
        PGP,
        PPG
    }
    
    public enum Artifact {
        NONE,
        GREEN,
        PURPLE
    }
}