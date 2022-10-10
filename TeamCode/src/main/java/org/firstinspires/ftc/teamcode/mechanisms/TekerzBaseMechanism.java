package org.firstinspires.ftc.teamcode.mechanisms;

import org.firstinspires.ftc.teamcode.opmodes.TekerzBaseOpmode;

abstract public class TekerzBaseMechanism {

    enum GameMode {
        TELEOP_INIT,
        TELEOP_START,
        AUTO_INIT,
        AUTO_START,
        STOP
    }

    protected GameMode gameMode;
    protected TekerzBaseOpmode robot;
    private String currentState;
    private double runtimeCurrentStateStart;

    // Subclasses can use this to get the number of seconds
    // since the last state transition occurred
    protected double runtimeInCurrentState;

    public void init(TekerzBaseOpmode robot) {
        this.robot = robot;
        if (robot.isTeleop()) {
            gameMode = GameMode.TELEOP_INIT;
        } else {
            gameMode = GameMode.AUTO_INIT;
        }
    }

    // Mechanism subclasses must define init_loop with 0 arguments.
    // However we want them to pass their internal state to the superclass init_loop.
    abstract public void init_loop();

    public void init_loop(String state) {
        updateCurrentState(state);
        robot.telemetry.addData(this.getClass().getName() + " Init", state);
    }

    public void start() {
        if (robot.isTeleop()) {
            gameMode = GameMode.TELEOP_START;
        } else {
            gameMode = GameMode.AUTO_START;
        }
    }

    // Mechanism subclasses must define loop with 0 arguments.
    // However we want them to pass their internal state to the superclass loop function.
    abstract public void loop();

    public void loop(String state) {
        updateCurrentState(state);
        robot.telemetry.addData(this.getClass().getName(), state);
    }

    public void stop() {
        gameMode = GameMode.STOP;
    }

    private void updateCurrentState(String state) {
        if (!currentState.equalsIgnoreCase(state)) {
            currentState = state;
            runtimeCurrentStateStart = robot.getRuntime();
            runtimeInCurrentState = 0.0;
        } else {
            runtimeInCurrentState += robot.getRuntime() - runtimeInCurrentState;
        }
    }
}
