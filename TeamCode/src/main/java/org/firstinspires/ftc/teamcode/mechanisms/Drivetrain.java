package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Drivetrain {
    enum State {
        IDLE,
        DRIVE,
        TURN,
        HOLD
    }

    State state = State.IDLE;
    DcMotor leftMotor;

    public void init(HardwareMap hardwareMap) {
        leftMotor = hardwareMap.get(DcMotor.class, "left_motor");
    }

    public void loop() {

    }
}
