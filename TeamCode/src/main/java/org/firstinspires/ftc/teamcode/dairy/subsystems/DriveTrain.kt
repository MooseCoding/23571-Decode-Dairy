package org.firstinspires.ftc.teamcode.dairy.subsystems

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import dev.frozenmilk.dairy.mercurial.continuations.Continuation
import dev.frozenmilk.dairy.mercurial.continuations.Continuations
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.exec
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.loop
import dev.frozenmilk.dairy.mercurial.continuations.Fiber
import dev.frozenmilk.dairy.mercurial.continuations.registers.Register
import dev.frozenmilk.dairy.mercurial.continuations.registers.ValRegister
import kotlin.math.abs
import kotlin.math.max

class DriveTrain(gamepad:Gamepad, hardwareMap: HardwareMap) {
    private val fL: DcMotorEx = hardwareMap.dcMotor.get("frontLeft") as DcMotorEx
    private val fR: DcMotorEx = hardwareMap.dcMotor.get("frontRight") as DcMotorEx
    private val bL: DcMotorEx = hardwareMap.dcMotor.get("backLeft") as DcMotorEx
    private val bR: DcMotorEx = hardwareMap.dcMotor.get("backRight") as DcMotorEx

    val driveContinuation:Continuation = Continuations.exec {
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
    }.close()

    val driveFiber:Fiber = Fiber(
        loop(
            exec {
                driveContinuation
            }
        ).close()
    )

    init {
        Fiber.CANCEL(driveFiber)
    }
}