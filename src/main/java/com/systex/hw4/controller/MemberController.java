package com.systex.hw4.controller;

import com.systex.hw4.model.Member;
import com.systex.hw4.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedList;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public ModelAndView loginView(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("member/login", "command", new Member());

        // 從 request 中提取錯誤訊息，並添加到模型中
        LinkedList<String> errorMessages = (LinkedList<String>) session.getAttribute("errorMessages");
        if (errorMessages != null) {
            modelAndView.addObject("errorMessages", errorMessages);
            session.removeAttribute("errorMessages");  // 移除 session 中的錯誤訊息
        }

        return modelAndView;
    }

    @GetMapping("/ajaxlogin")
    public ModelAndView ajaxloginView() {
        return new ModelAndView("member/ajaxLogin", "command", new Member());
    }

    @GetMapping("/createMember")
    public ModelAndView createMemberView() {
        return new ModelAndView("member/sign", "command", new Member());
    }

    //建議使用者
    @PostMapping("/createMember")
    public ModelAndView createMember(@Valid @ModelAttribute Member member,
                                     Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("member/sign", "command", new Member());
        }
        try{
            //驗證傳入有沒有值
            ValidInput(member);

            memberService.saveMember(member);
            return new ModelAndView("redirect:/login");
        }catch (Exception e) {
            LinkedList<String> errorMessages = new LinkedList<>();
            errorMessages.add(e.getMessage());
            model.addAttribute("errorMessages", errorMessages);
            return new ModelAndView("member/sign", "command", new Member());
        }
    }

    private static void ValidInput(Member member) {
        if (member.getUsername() == null || member.getPassword() == null) {
            throw new IllegalArgumentException("輸入的帳號或密碼為空");
        }
    }

}
