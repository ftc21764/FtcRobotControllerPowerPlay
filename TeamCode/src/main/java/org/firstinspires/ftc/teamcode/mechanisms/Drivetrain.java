package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.opmodes.TekerzBaseOpmode;

public class Drivetrain extends TekerzBaseMechanism {
    enum State {
        DRIVER_CONTROL,
        IDLE,
        DRIVE,
        TURN,
        HOLD
    }

    State state = State.IDLE;
    DcMotor leftMotor;

    public void init(TekerzBaseOpmode robot) {
        leftMotor = robot.hardwareMap.get(DcMotor.class, "left_motor");
        if (robot.isTeleop()) {
            state = State.DRIVER_CONTROL;
        }

    }

    public void loop(TekerzBaseOpmode robot) {

    }
}
