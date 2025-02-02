package com.systex.hw4.controller;

import com.systex.hw4.service.LotteryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;

@Controller
@RequestMapping("/lottery")
public class LotteryController {

    private final LotteryService lotteryService;

    public LotteryController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @GetMapping("/main")
    public String getMainPage() {
        return "lottery/main";
    }

    @GetMapping("/super_lottery")
    public String getSuperLotteryPage() {
        return "lottery/super_lottery";
    }

    @PostMapping("/lotteryController.do")
    public String generateLottery(@RequestParam("group") Optional<String> groupParam,
                                  @RequestParam("exclude") Optional<String> excludeNum, Model model) {
        try {
            int group = parseGroup(groupParam);

            HashSet<Integer> excludeNumberSet = new HashSet<>();
            if (excludeNum.isPresent() && !excludeNum.get().isEmpty()) {
                parseExcludeNumber(excludeNum.get(), excludeNumberSet);
            }

            ArrayList[] lotterys = lotteryService.getLottery(group, excludeNumberSet, 50);

            model.addAttribute("lotterys", lotterys);
            model.addAttribute("excludeNumber", excludeNumberSet);
            return "lottery/result";

        }  catch (Exception e) {
            LinkedList<String> errorMessages = new LinkedList<>();
            errorMessages.add(e.getMessage());
            model.addAttribute("errorMessages", errorMessages);
            return "lottery/main";
        }
    }

    @PostMapping("/superLotteryController.do")
    public String generateSuperLottery(@RequestParam("group") Optional<String> groupParam,
                                  @RequestParam("exclude") Optional<String> excludeNum, Model model) {
        try {
            int group = parseGroup(groupParam);

            HashSet<Integer> excludeNumberSet = new HashSet<>();
            if (excludeNum.isPresent() && !excludeNum.get().isEmpty()) {
                parseExcludeNumber(excludeNum.get(), excludeNumberSet);
            }

            ArrayList[] lotterys = lotteryService.getSuperLottery(group, excludeNumberSet, 37);

            model.addAttribute("lotterys", lotterys);
            model.addAttribute("excludeNumber", excludeNumberSet);
            return "lottery/result";

        }  catch (Exception e) {
            LinkedList<String> errorMessages = new LinkedList<>();
            errorMessages.add(e.getMessage());
            model.addAttribute("errorMessages", errorMessages);
            return "lottery/main";
        }
    }

    private int parseGroup(Optional<String> groupParam) {
        validGroupParam(groupParam);

        String group = groupParam.get();
        return convertStringToNumber(group);
    }

    private void validGroupParam(Optional<String> groupParam) {
        if (groupParam.isEmpty() || groupParam.get().isBlank()) {
            throw new IllegalArgumentException("您所輸入的組數為空");
        }
    }

    private void parseExcludeNumber(String excludeNumString, HashSet<Integer> excludeNumberSet) {
        String[] numberStrings = excludeNumString.split(" ");
        for (String num : numberStrings) {
            int excludeNumber = convertStringToNumber(num);
            excludeNumberSet.add(excludeNumber);
        }
    }

    private int convertStringToNumber(String num) {
        try {
            int number = Integer.parseInt(num.trim());
            if (number <= 0) {
                throw new IllegalArgumentException("您所輸入的組數與排除數字似乎有負數或是0");
            }
            return number;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("您所輸入的組數與排除數字似乎有非數字的值");
        }
    }

}
