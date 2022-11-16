package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Test: LinearSlide", group="Robot")
public class LinearSlideTest extends LinearOpMode {
    protected LinearSlide         linearSlide = null;
    @Override
    public void runOpMode() {
        linearSlide = new LinearSlide(hardwareMap, telemetry, gamepad2);
        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                linearSlide.loop();
            }
        }

    }
}
