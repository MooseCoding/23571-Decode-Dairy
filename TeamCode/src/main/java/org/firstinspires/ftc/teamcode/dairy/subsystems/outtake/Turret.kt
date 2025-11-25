package org.firstinspires.ftc.teamcode.dairy.subsystems.outtake

import com.pedropathing.follower.Follower
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import dev.frozenmilk.dairy.mercurial.continuations.Actors
import dev.frozenmilk.dairy.mercurial.continuations.Continuations
import dev.nextftc.control.KineticState
import dev.nextftc.control.builder.controlSystem
import dev.nextftc.control.feedback.PIDCoefficients
import org.firstinspires.ftc.teamcode.dairy.subsystems.Alliance
import kotlin.math.PI
import kotlin.math.atan2

class Turret(hardwareMap: HardwareMap, alliance: Alliance, follower: Follower) {
    private val turretMotor: DcMotorEx = hardwareMap.dcMotor.get("turretMotor") as DcMotorEx

    private val turretPID = PIDCoefficients(2.0,0.0,0.2)
    private val turretController = controlSystem {
        posPid(turretPID)
    }

    private val ppr = 537.7
    private val gearRatio = 3.47
    private val rpt = 2*Math.PI/(ppr*gearRatio)

    enum class State {
        CONTROLLED,
        NOT_CONTROLLED
    }

    enum class Actions {
        STOP,
        SPIN
    }

    fun getYaw(input: Int): Double { // Get the current yaw of the turret from [-pi, pi]
        return normalizeAngle(input * rpt)
    }

    fun normalizeAngle(angleRadians: Double): Double { // Returns a normalized angle between [-pi, pi]
        var angle = angleRadians % (2.0 * PI)
        if (angle <= -PI) {
            angle += 2.0 * PI
        }
        if (angle > PI) {
            angle -= 2.0 * PI
        }
        return angle
    }

    private val goalY:Double = 136.0
    private val goalX:Double = if(alliance==Alliance.RED) 138.0 else 6.0

    val spin = Actors.Actor<State, Actions>(
        initializer = { State.NOT_CONTROLLED },
        messageHandler = { _, message ->
            when(message) {
                Actions.SPIN -> {
                    State.CONTROLLED
                }
                Actions.STOP -> {
                    State.NOT_CONTROLLED
                }
            }
        },
        automata = { stateRegister ->
            val state by stateRegister
            Continuations.exec {
                when(state) {
                    State.CONTROLLED -> {
                        val mu = atan2(goalY - follower.pose.y, goalX - follower.pose.x)
                        val deltaHeading = normalizeAngle(mu - follower.heading)
                        val clampedHeading = deltaHeading.coerceIn(-PI, PI)

                        turretController.goal = KineticState(clampedHeading, 0.0)
                        turretMotor.power = turretController.calculate(KineticState(getYaw(turretMotor.currentPosition), 0.0))
                    }
                    State.NOT_CONTROLLED -> {
                    }
                }
            }
        }
    )
}