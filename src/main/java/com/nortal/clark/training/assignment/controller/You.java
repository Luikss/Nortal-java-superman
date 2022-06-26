package com.nortal.clark.training.assignment.controller;

import com.nortal.clark.training.assignment.model.CityMap;
import com.nortal.clark.training.assignment.model.Clark;
import com.nortal.clark.training.assignment.model.Direction;
import com.nortal.clark.training.assignment.model.Position;
import com.nortal.clark.training.assignment.model.SpeedLevel;
import com.nortal.clark.training.assignment.model.VoiceCommand;

import java.util.List;

public class You {

    private List<Position> targetsToCapture;
    public static final int MAGIC_RANGE = 2;
    // public static final double WATER_DRAG = 1.6;

    public VoiceCommand getNextStep(Clark clark, CityMap cityMap) {
        VoiceCommand voiceCommand = new VoiceCommand(Direction.SOUTH, SpeedLevel.L0_RUNNING_HUMAN);

        if (targetsToCapture == null) {
            targetsToCapture = cityMap.getTargets();
            // sortPositions();
        }

        Position targetToCapture = targetsToCapture.get(0);
        int index = 0;

        for (int i = 0; i < targetsToCapture.size(); i++) {
            Position testTargetToCapture = targetsToCapture.get(i);
            if (testTargetToCapture.x < targetToCapture.x) {
                targetToCapture = testTargetToCapture;
                index = i;
            }
        }

        System.out.println(clark + " ->> x=" + targetToCapture.x + ", y=" + targetToCapture.y);

        int diffX = Math.abs(targetToCapture.x - clark.getPosition().x);
        int diffY = Math.abs(targetToCapture.y - clark.getPosition().y);

        SpeedLevel horizontalSpeedLevel = thinkOfSpeedLevel(diffX);
        SpeedLevel verticalSpeedLevel = thinkOfSpeedLevel(diffY);

        if (diffX < MAGIC_RANGE && diffY < MAGIC_RANGE) {
            System.out.println("Removing target");
            targetsToCapture.remove(index); //Consider it captured
        } else if (targetToCapture.x > clark.getPosition().x && diffX > MAGIC_RANGE) {
            voiceCommand = (diffX > 15) ? new VoiceCommand(Direction.EAST, horizontalSpeedLevel) : new VoiceCommand(Direction.EAST, SpeedLevel.L3_SUPER_SONIC);
        } else if (targetToCapture.x < clark.getPosition().x && diffX > MAGIC_RANGE) {
            voiceCommand = new VoiceCommand(Direction.WEST, horizontalSpeedLevel);
        } else if (targetToCapture.y > clark.getPosition().y && diffY > MAGIC_RANGE) {
            voiceCommand = new VoiceCommand(Direction.NORTH, verticalSpeedLevel);
        } else if (targetToCapture.y < clark.getPosition().y && diffY > MAGIC_RANGE) {
            voiceCommand = new VoiceCommand(Direction.SOUTH, verticalSpeedLevel);
        }

        System.out.println(voiceCommand);
        return voiceCommand;
    }

    private SpeedLevel thinkOfSpeedLevel(int distanceDiff) {
        if (distanceDiff > 45)
            return SpeedLevel.L4_MACH_9350;
        if (distanceDiff > 10)
            return SpeedLevel.L2_SUB_SONIC;

        return SpeedLevel.L3_SUPER_SONIC;
    }

    // Tried Dijkstra shortest path algorithm, but did not give faster execution in test.
    /*
    private void sortPositions() {
        targetsToCapture.sort((Position lhs, Position rhs) -> {
            double d1 = Math.hypot(lhs.x, lhs.y);
            double d2 = Math.hypot(rhs.x, rhs.y);
            return Double.compare(d1, d2);
        });
    }
    */

    // Tried to use water drag factor to make smoothen out turns, but execution time suffered.
    /*
    private double getDragAcceleration(double currentSpeed) {
        double dragDirectionalModifier = -Math.signum(currentSpeed);
        double waterDrag = WATER_DRAG + (Math.pow(currentSpeed, 2) / 200);
        return dragDirectionalModifier * waterDrag;
    }
    */
}
