package com.systex.hw4.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class LotteryService {

    public ArrayList[] getLottery(int group, HashSet<Integer> excludeNumberSet) {
        ArrayList[] lotterys = new ArrayList[group];
        for (int i = 0; i < group; i++) {
            lotterys[i] = generateLottery(excludeNumberSet);
        }
        return lotterys;
    }

    private ArrayList<Integer> generateLottery(HashSet<Integer> excludeNumberSet) {
        ArrayList<Integer> lottery = new ArrayList<>();

        while (lottery.size() < 6) {
            generateLotteryNumber(excludeNumberSet, lottery);
        }
        return lottery;
    }

    private void generateLotteryNumber(HashSet<Integer> excludeNumberSet, ArrayList<Integer> lottery) {
        int luckNumber = ThreadLocalRandom.current().nextInt(1, 50); //產生1到49的數字
        if (!excludeNumberSet.contains(luckNumber)) {
            lottery.add(luckNumber);
        }
    }

}
