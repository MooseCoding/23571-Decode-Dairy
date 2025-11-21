package org.firstinspires.ftc.teamcode.dairy.subsystems.outtake

import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.BasicPID
import com.ThermalEquilibrium.homeostasis.Controllers.Feedforward.BasicFeedforward
import com.ThermalEquilibrium.homeostasis.Controllers.Feedforward.FeedforwardController
import com.ThermalEquilibrium.homeostasis.Filters.Estimators.RawValue
import com.ThermalEquilibrium.homeostasis.Parameters.FeedforwardCoefficients
import com.ThermalEquilibrium.homeostasis.Parameters.PIDCoefficients
import com.ThermalEquilibrium.homeostasis.Systems.BasicSystem
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import dev.frozenmilk.dairy.mercurial.continuations.Actors
import dev.frozenmilk.dairy.mercurial.continuations.Continuations
import kotlin.math.PI

class Flywheel(hardwareMap: HardwareMap) {
    val flywheel1Motor:DcMotorEx = hardwareMap.dcMotor.get("f1M") as DcMotorEx
    // val flywheel2Motor:DcMotorEx = hardwareMap.dcMotor.get("f2M") as DcMotorEx

    val pid:BasicPID = BasicPID(PIDCoefficients(0.0033, 0.0, 0.0))
    val ff: BasicFeedforward = BasicFeedforward(FeedforwardCoefficients(1.66667E-4, 0.0, 0.003))
    val flywheelSystem: BasicSystem = BasicSystem(RawValue{ flywheel1Motor.velocity }, pid, ff)

    enum class State {
        SPINNING,
        STOPPED
    }

    val state: State = State.STOPPED

    private val targetVelocity = 1000.0

    enum class Actions {
        SPIN,
        STOP
    }

    val spin = Actors.Actor<State, Actions>(
        name = "Spin",
        initializer = { State.STOPPED },
        messageHandler = { _, message ->
            when(message) {
                Actions.SPIN -> {
                    State.SPINNING
                }
                Actions.STOP -> {
                    State.STOPPED
                }
            }
        },
        automata = { stateRegister ->
            val state by stateRegister
            Continuations.exec {
                when(state) {
                    State.SPINNING -> {
                        flywheel1Motor.power = flywheelSystem.update(targetVelocity)
                        // flywheel2Motor.power = 1.0
                    }
                    State.STOPPED -> {
                        flywheel1Motor.power = flywheelSystem.update(targetVelocity)
                        // flywheel2Motor.power = 0.0
                    }
                }
            }
        }
    )
}