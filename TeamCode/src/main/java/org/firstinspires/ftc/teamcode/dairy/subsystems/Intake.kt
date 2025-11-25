package org.firstinspires.ftc.teamcode.dairy.subsystems

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import dev.frozenmilk.dairy.mercurial.continuations.Actors
import dev.frozenmilk.dairy.mercurial.continuations.Continuations
import kotlin.math.PI
import kotlin.math.atan2

class Intake(hardwareMap: HardwareMap) {
    private val intakeMotor:DcMotorEx = hardwareMap.dcMotor.get("intakeMotor") as DcMotorEx

    enum class State {
        SPINNING,
        REVERSING,
        NOT_CONTROLLED
    }

    enum class Actions {
        FORWARD,
        BACK,
        RELEASE
    }

    val spin = Actors.Actor<State, Actions>(
        initializer = { State.NOT_CONTROLLED },
        messageHandler = { _, message ->
            when(message) {
                Actions.FORWARD -> {
                    State.SPINNING
                }
                Actions.BACK -> {
                    State.REVERSING
                }
                Actions.RELEASE -> {
                    State.NOT_CONTROLLED
                }
            }
        },
        automata = { stateRegister ->
            val state by stateRegister
            Continuations.exec {
                when(state) {
                    State.SPINNING -> {
                        intakeMotor.power = 1.0
                    }
                    State.REVERSING -> {
                        intakeMotor.power = -1.0
                    }
                    State.NOT_CONTROLLED -> {
                        intakeMotor.power = 0.0
                    }
                }
            }
        }
    )
}