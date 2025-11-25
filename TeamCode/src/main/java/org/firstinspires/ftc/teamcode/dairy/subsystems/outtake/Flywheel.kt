package org.firstinspires.ftc.teamcode.dairy.subsystems.outtake

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import dev.frozenmilk.dairy.mercurial.continuations.Actors
import dev.frozenmilk.dairy.mercurial.continuations.Continuations
import dev.nextftc.control.KineticState
import dev.nextftc.control.builder.controlSystem
import dev.nextftc.control.feedback.PIDCoefficients
import dev.nextftc.control.feedforward.BasicFeedforwardParameters

class Flywheel(hardwareMap: HardwareMap) {
    private val flywheel1Motor:DcMotorEx = hardwareMap.dcMotor.get("f1M") as DcMotorEx
    private val flywheel2Motor:DcMotorEx = hardwareMap.dcMotor.get("f2M") as DcMotorEx

    init {
        flywheel2Motor.direction = DcMotorSimple.Direction.REVERSE
    }

    private val flywheelPID = PIDCoefficients(0.0033, 0.0, 0.0)
    private val flywheelFF = BasicFeedforwardParameters(1.66667E-4, 0.0, 0.003)
    private val flywheelController = controlSystem {
        velPid(flywheelPID)
        basicFF(flywheelFF)
    }

    sealed interface State

    class MOVING(val target:Double) : State
    class STOPPED: State

    val spin = Actors.Actor<State, State>(
        initializer = { STOPPED() },
        messageHandler = { _, message ->
            message
        },
        automata = { stateRegister ->
            val state by stateRegister
            Continuations.exec {
                when(state) {
                    is MOVING -> flywheelController.goal = KineticState(0.0, (state as MOVING).target)
                    is STOPPED -> flywheelController.goal = KineticState(0.0, 0.0)
                }

                flywheel1Motor.power = flywheelController.calculate(KineticState(0.0, flywheel1Motor.velocity))
                flywheel2Motor.power = flywheel1Motor.power
            }
        }
    )
}