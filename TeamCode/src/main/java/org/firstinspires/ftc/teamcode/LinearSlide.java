package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LinearSlide {

    protected DcMotor linearSlideMotor   = null;
    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;

    public LinearSlide(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
       // linearSlideMotor  = hardwareMap.get(DcMotor.class, "CHANGETHIS");
       // linearSlideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    /**
     * This function moves the linear slide to the height specified in 'position.'
     * It tells the motor to run to a position, then returns from the function.
     * @param position What position the linear slide will go to. There are three positions, low (1), medium (2) and high (3).
     */
    public void setPosition(int position) {

    }

    /**
     * Senses whether a button is being pushed, and whether it was being pushed last time you checked.
     * If a button is pushed, it will set the linear slide to a desired height. Push A for low,
     * B or X for medium, and Y for high.
     * @param gamepad What gamepad will be used
     */
    public void readGamepad(Gamepad gamepad) {

    }
}
