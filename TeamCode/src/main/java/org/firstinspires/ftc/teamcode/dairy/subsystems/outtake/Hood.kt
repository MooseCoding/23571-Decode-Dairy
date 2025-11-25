package org.firstinspires.ftc.teamcode.dairy.subsystems.outtake

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import dev.frozenmilk.dairy.mercurial.continuations.Actors
import dev.frozenmilk.dairy.mercurial.continuations.Continuations

class Hood(hardwareMap: HardwareMap) {
    val servo:Servo = hardwareMap.servo.get("hoodServo")

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
                    is MOVING -> servo.position = (state as MOVING).target
                    is STOPPED -> {}
                }
            }
        }
    )
}