package org.firstinspires.ftc.teamcode.dairy.subsystems.outtake

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import dev.frozenmilk.dairy.mercurial.continuations.Actors
import dev.frozenmilk.dairy.mercurial.continuations.Continuations

class Flywheel(hardwareMap: HardwareMap) {
    val flywheel1Motor:DcMotorEx = hardwareMap.dcMotor.get("f1M") as DcMotorEx
    // val flywheel2Motor:DcMotorEx = hardwareMap.dcMotor.get("f2M") as DcMotorEx

    enum class State {
        SPINNING,
        STOPPED
    }

    val state: State = State.STOPPED

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
                        flywheel1Motor.power = 1.0
                        // flywheel2Motor.power = 1.0
                    }
                    State.STOPPED -> {
                        flywheel1Motor.power = 0.0
                        // flywheel2Motor.power = 0.0
                    }
                }
            }
        }
    )
}