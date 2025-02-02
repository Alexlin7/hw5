package com.systex.hw4.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class LotteryService {

    public ArrayList[] getLottery(int group, HashSet<Integer> excludeNumberSet, Integer maxNumber) {
        ArrayList[] lotterys = new ArrayList[group];
        for (int i = 0; i < group; i++) {
            lotterys[i] = generateLottery(excludeNumberSet, maxNumber);
        }
        return lotterys;
    }

    public ArrayList[] getSuperLottery(int group, HashSet<Integer> excludeNumberSet, Integer maxNumber) {
        ArrayList[] lotterys = new ArrayList[group];
        for (int i = 0; i < group; i++) {
            lotterys[i] = generateLottery(excludeNumberSet, maxNumber);
        }
        return lotterys;
    }

    private ArrayList<Integer> generateLottery(HashSet<Integer> excludeNumberSet, Integer maxNumber) {
        ArrayList<Integer> lottery = new ArrayList<>();

        while (lottery.size() < 6) {
            generateLotteryNumber(excludeNumberSet, lottery, maxNumber);
        }
        
        lottery.sort(Comparator.naturalOrder());
        
        return lottery;
    }

    private void generateLotteryNumber(HashSet<Integer> excludeNumberSet, ArrayList<Integer> lottery, Integer maxNumber) {
        int luckNumber = ThreadLocalRandom.current().nextInt(1, maxNumber); //產生1到49的數字
        if (excludeNumberSet.contains(luckNumber)) {
            return;
        }
        if(lottery.contains(luckNumber)){
            return;
        }
            
        lottery.add(luckNumber);
    }

}
