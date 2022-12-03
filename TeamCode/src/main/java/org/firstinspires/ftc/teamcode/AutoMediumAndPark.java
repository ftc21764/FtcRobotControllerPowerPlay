package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="Auto Medium And Park 21764", group="Robot")
//@Disabled

public class AutoMediumAndPark extends PowerPlayAutonomous {

    public void runAutonomousProgram() {
        // Step through each leg of the path,
        // Notes:   Reverse movement is obtained by setting a negative distance (not speed)
        //          holdHeading() is used after turns to let the heading stabilize
        //          Add a sleep(2000) after any step to keep the telemetry data visible for review


        //BASE DRIVE PATH:
        //turn around
        // TODO: testing observed that the first driveStraight call doesn't work on the first run, only after a second run would this first call work.
        // TODO: adding this holdHeading call in the hope that it 'wakes up' the robot and the driveStraight command runs on first run.

        intake.pickUpCone();
        driveStraight(DRIVE_SPEED, 35.0, 0.0);
        driveStraight(DRIVE_SPEED, -5.0, 0.0);
        moveToMediumPosition();
        holdHeading(TURN_SPEED, 0.0,2.0);
        turnToHeading(TURN_SPEED, 45.0);
        driveStraight(DRIVE_SPEED, 11.0, 45.0);
        intake.dropCone();
        holdHeading(TURN_SPEED, 45.0, 2.0);
        driveStraight(DRIVE_SPEED, -11.0, 45.0);
        turnToHeading(TURN_SPEED, -90.0);
        moveToGroundPosition();

        //check whether recognition label is null, if not, drive to parking space
        if (recognizer.recognitionLabel == null) {
            //stay in current place
        } else if (recognizer.recognitionLabel.startsWith("1")) {
            //Drive to parking 1
            driveStraight(DRIVE_SPEED, -24.0, -90.0);
        } else if (recognizer.recognitionLabel.startsWith("2")) {
            //Stay in parking 2
        } else {
            //Drive to parking 3
            driveStraight(DRIVE_SPEED, 24.0, -90.0);
        }
    }
}
