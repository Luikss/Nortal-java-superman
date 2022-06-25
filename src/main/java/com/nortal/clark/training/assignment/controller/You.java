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

    public VoiceCommand getNextStep(Clark clark, CityMap cityMap) {
        VoiceCommand voiceCommand = new VoiceCommand(Direction.SOUTH, SpeedLevel.L0_RUNNING_HUMAN);

        if (targetsToCapture == null) {
            targetsToCapture = cityMap.getTargets();
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

        SpeedLevel horizontalSpeedLevel = thinkOfSpeedLevel(diffX, clark.getHorizontal());
        SpeedLevel verticalSpeedLevel = thinkOfSpeedLevel(diffY, clark.getVertical());

        if (diffX < 2 && diffY < 2) {
            System.out.println("Removing target");
            targetsToCapture.remove(index); //Consider it captured

        } else if (targetToCapture.x > clark.getPosition().x && diffX > 2) {
            if (diffX > 15) {
                voiceCommand = new VoiceCommand(Direction.EAST, horizontalSpeedLevel);
            } else {
                voiceCommand = new VoiceCommand(Direction.EAST, SpeedLevel.L3_SUPER_SONIC);
            }
        } else if (targetToCapture.x < clark.getPosition().x && diffX > 2) {
            voiceCommand = new VoiceCommand(Direction.WEST, horizontalSpeedLevel);
        } else if (targetToCapture.y > clark.getPosition().y && diffY > 2) {
            voiceCommand = new VoiceCommand(Direction.NORTH, verticalSpeedLevel);
        } else if (targetToCapture.y < clark.getPosition().y && diffY > 2) {
            voiceCommand = new VoiceCommand(Direction.SOUTH, verticalSpeedLevel);
        }

        System.out.println(voiceCommand);
        return voiceCommand;
    }

    private SpeedLevel thinkOfSpeedLevel(int distanceDiff, double speed) {
        if (distanceDiff > 45)
            return SpeedLevel.L4_MACH_9350;
        if (distanceDiff > 10)
            return SpeedLevel.L2_SUB_SONIC;

        return SpeedLevel.L3_SUPER_SONIC;
    }
}
