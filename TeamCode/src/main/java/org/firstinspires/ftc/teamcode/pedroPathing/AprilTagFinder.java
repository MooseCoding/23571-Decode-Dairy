package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;

@TeleOp(name = "AprilTagFinder", group = "Test")
public class AprilTagFinder extends OpMode {
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    @Override
    public void init() {
        initAprilTag();

        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.update();
    }

    @Override
    public void loop() {
        telemetryAprilTag();
        telemetry.update();
    }

    private void initAprilTag() {
        aprilTag = new AprilTagProcessor.Builder().build();
        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hardwareMap.get(WebcamName.class, "webcam1"));
        builder.addProcessor(aprilTag);
        visionPortal = builder.build();
    }

    private void telemetryAprilTag(){
        ArrayList<org.firstinspires.ftc.vision.apriltag.AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("AprilTags Detected: ", currentDetections.size());

        for(AprilTagDetection detection : currentDetections) {
            if(detection.metadata != null){
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("\nX:%6.1f Y:%6.1f Z:%6.1f  (inches)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("\nP:%6.1f R:%6.1f Y:%6.1f (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("\nR:%6.1f B:%6.1f E:%6.1f (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            }else {
                telemetry.addLine(String.format("\n==== (ID %d) Uknown", detection.id));
                telemetry.addLine(String.format("\nCenter X:%6.1f Y:%6.1f (pixels)", detection.center.x, detection.center.y));
            }
            }
        }
}
