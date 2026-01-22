package org.firstinspires.ftc.teamcode.Framework;

import java.util.HashMap;
import java.util.Map;

public class Params {
    
    // set both in auto for use in auto and teleop
    public static Alliance alliance = Alliance.BLUE;
    public static Motif motif = Motif.GPP;
    
    public enum Alliance {
        RED,
        BLUE
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

    private static final HashMap<Motif,Artifact[]> getMotifArtifacts = new HashMap<Motif,Artifact[]>(Map.of(
            Motif.GPP, new Artifact[]{Artifact.GREEN,Artifact.PURPLE,Artifact.PURPLE},
            Motif.PGP, new Artifact[]{Artifact.PURPLE,Artifact.GREEN,Artifact.PURPLE},
            Motif.PPG, new Artifact[]{Artifact.PURPLE,Artifact.PURPLE,Artifact.GREEN}
    ));
    
    public static Artifact[] motifArtifacts() {
        return getMotifArtifacts.get(motif);
    }
}