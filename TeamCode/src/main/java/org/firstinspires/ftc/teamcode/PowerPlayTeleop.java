package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="Power Play TeleOp 21764", group="Robot")
public class PowerPlayTeleop extends PowerPlayAutonomous {
    @Override
    public void runOpMode() {
        setupRobot(BNO055IMU.AngleUnit.RADIANS);
        leftDriveF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftDriveB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDriveF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDriveB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while (opModeInInit()) {
            telemetry.addData(">", "Teleop mode waiting for start");
            telemetry.update();
        }

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, this is reversed!
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Read inverse IMU heading, as the IMU heading is CW positive
            double botHeading = -imu.getAngularOrientation().firstAngle;

            double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
            double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            // scale the speed down so we don't go too fast (for testing)
            frontLeftPower *= 0.4;
            backLeftPower *= 0.4;
            frontRightPower *= 0.4;
            backRightPower *= 0.4;

            leftDriveF.setPower(frontLeftPower);
            leftDriveB.setPower(backLeftPower);
            rightDriveF.setPower(frontRightPower);
            rightDriveB.setPower(backRightPower);

            linearSlide.readGamepad(gamepad2);

            //sense if a colorful button is pressed, if it is then turn to a place and if not do nothing :)
            if (gamepad1.a) {
                turnToHeading(TURN_SPEED, 180);
            } else if (gamepad1.b) {
                turnToHeading(TURN_SPEED, -90);
            } else if (gamepad1.x) {
                turnToHeading(TURN_SPEED, 90);
            } else if (gamepad1.y) {
                turnToHeading(TURN_SPEED, 0);
            }



            telemetry.addData("Motor Powers FL:FR:BL:BR", "%7f:%7f:%7f:%7f",
                    frontLeftPower, frontRightPower, backLeftPower, backRightPower);
            telemetry.addData("Gamepad 1: y:x:rx", "%7f:%7f:%7f",
                    y, x, rx);
            telemetry.update();
        }
    }
}