package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Drivetrain {
    enum State {
        IDLE,
        DRIVE,
        TURN,
        HOLD
    }

    State state = State.IDLE;

    public void init(HardwareMap hardwareMap) {

    }

    public void loop() {

    }
}
