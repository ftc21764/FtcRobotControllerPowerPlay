package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.FakeDrivetrain;

public class TekerzBaseOpmode extends OpMode {
    protected ElapsedTime runtime = new ElapsedTime();
    //public FakeWebcam webcam = new FakeWebcam();
    public FakeDrivetrain drivetrain = new FakeDrivetrain();

    /**
     * User defined init method
     * <p>
     * This method will be called once when the INIT button is pressed.
     */
    @Override
    public void init() {
        //webcam.init(this);
        drivetrain.init(this);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        //webcam.init_loop();
        drivetrain.init_loop();

        // This is the only place where telemetry.update should be called for all mechanisms
        telemetry.update();
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();

        //webcam.start();
        drivetrain.start();
    }

    /**
     * User defined loop method
     * <p>
     * This method will be called repeatedly in a loop while this op mode is running
     */
    @Override
    public void loop() {
        //webcam.loop();
        drivetrain.loop();

        // This is the only place where telemetry.update should be called for all mechanisms
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        //webcam.stop();
        drivetrain.stop();
    }

    public boolean isAutonomous() {
        return this.getClass().getName().matches("Autonomous");
    }

    public boolean isTeleop() {
        return this.getClass().getName().matches("Teleop");
    }

}
