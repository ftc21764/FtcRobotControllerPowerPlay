package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Test: SwingArm", group="Robot")
public class SwingArmTest extends LinearOpMode {
    protected SwingArm      swingArm = null;
    @Override
    public void runOpMode() {
        swingArm = new SwingArm(hardwareMap, telemetry, gamepad2);
        swingArm.setBrakeMode(false);
        while (opModeInInit()) {
            swingArm.initLoop();
            telemetry.update();
        }
        swingArm.setBrakeMode(true);

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                swingArm.loop();
                telemetry.update();
            }
        }
    }
}
