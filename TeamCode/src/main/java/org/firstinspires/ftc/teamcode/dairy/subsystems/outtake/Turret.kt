package org.firstinspires.ftc.teamcode.dairy.subsystems.outtake

import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.BasicPID
import com.ThermalEquilibrium.homeostasis.Controllers.Feedforward.BasicFeedforward
import com.ThermalEquilibrium.homeostasis.Filters.Estimators.RawValue
import com.ThermalEquilibrium.homeostasis.Parameters.FeedforwardCoefficients
import com.ThermalEquilibrium.homeostasis.Parameters.PIDCoefficients
import com.ThermalEquilibrium.homeostasis.Systems.BasicSystem
import com.pedropathing.follower.Follower
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import dev.frozenmilk.dairy.mercurial.continuations.Actors
import dev.frozenmilk.dairy.mercurial.continuations.Continuations
import kotlin.math.PI
import kotlin.math.atan2

class Turret(hardwareMap: HardwareMap, follower: Follower) {
    val turretMotor: DcMotorEx = hardwareMap.dcMotor.get("turretMotor") as DcMotorEx

    val pid: BasicPID = BasicPID(PIDCoefficients(0.0033, 0.0, 0.0))
    val ff: BasicFeedforward = BasicFeedforward(FeedforwardCoefficients(1.66667E-4, 0.0, 0.003))
    val turretSystem: BasicSystem = BasicSystem(RawValue{ turretMotor.currentPosition.toDouble() }, pid, ff)

    private val ppr = 537.7
    private val gearRatio = 3.47
    private val rpt = 2*Math.PI/(ppr*gearRatio)

    enum class State {
        CONTROLLED,
        NOT_CONTROLLED
    }

    enum class Actions {
        STOP,
        RELEASE
    }

    fun getYaw(input:Double): Double { // Get the current yaw of the turret from [-pi, pi]
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

    val spin = Actors.Actor<State, Actions>(
        name = "Turret Spin",
        initializer = { State.CONTROLLED },
        messageHandler = { _, message ->
            when(message) {
                Actions.RELEASE -> {
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

                        turretMotor.power = turretSystem.update(clampedHeading, 0.0)
                    }
                    State.NOT_CONTROLLED -> {
                    }
                }
            }
        }
    )
}