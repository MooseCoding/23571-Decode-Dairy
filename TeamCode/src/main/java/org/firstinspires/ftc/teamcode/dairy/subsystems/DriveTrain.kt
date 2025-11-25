package org.firstinspires.ftc.teamcode.dairy.subsystems

import com.pedropathing.follower.Follower
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import dev.frozenmilk.dairy.mercurial.continuations.Closure
import dev.frozenmilk.dairy.mercurial.continuations.Continuation
import dev.frozenmilk.dairy.mercurial.continuations.Continuations
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.async
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.exec
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.loop
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.noop
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.wait
import kotlin.math.abs
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import kotlin.math.max

class DriveTrain(gamepad:Gamepad, hardwareMap: HardwareMap, telemetry: Telemetry) {
    var alliance: Alliance = Alliance.RED

    private val fL: DcMotorEx = hardwareMap.dcMotor.get("frontLeft") as DcMotorEx
    private val fR: DcMotorEx = hardwareMap.dcMotor.get("frontRight") as DcMotorEx
    private val bL: DcMotorEx = hardwareMap.dcMotor.get("backLeft") as DcMotorEx
    private val bR: DcMotorEx = hardwareMap.dcMotor.get("backRight") as DcMotorEx
    val follower: Follower = Constants.createFollower(hardwareMap)

    val driveContinuation:Closure = Continuations.exec {
        val y = -gamepad.left_stick_y
        val x = gamepad.left_stick_x
        val h = gamepad.right_stick_x

        val denominator = max(abs(y.toDouble()) + abs(x.toDouble()) + abs(h.toDouble()), 1.0)
        val frontLeftPower: Double = (y + x + h) / denominator
        val backLeftPower: Double = (y - x + h) / denominator
        val frontRightPower: Double = (y - x - h) / denominator
        val backRightPower: Double = (y + x - h) / denominator

        fL.power = frontLeftPower
        bL.power = backLeftPower
        fR.power = frontRightPower
        bR.power = backRightPower

        follower.update()
    }

    val driveTrainAsync:Closure = async (
        {
            Continuations.sequence(
                loop(
                    exec {
                        driveContinuation
                    }
                )
            )
        },
        { noop() } // Code to run after detaching the drivetrain's fiber
    )
}