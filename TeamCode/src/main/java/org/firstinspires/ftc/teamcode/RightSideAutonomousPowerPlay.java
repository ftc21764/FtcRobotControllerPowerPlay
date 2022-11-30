package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Right Side Autonomous", group="Robot")
//@Disabled

/**
 * RightSideAutonomousPowerPlay
 * Select this opmode if your robot starts on the right of the field relative to your drivers, in position A2 or F5
 **/
public class RightSideAutonomousPowerPlay extends PowerPlayAutonomous {

    public RightSideAutonomousPowerPlay() {
        reverseTurnsForLeftSide = 1;
    }
}
