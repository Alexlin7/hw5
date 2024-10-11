package com.systex.hw4.config;

import com.systex.hw4.model.Member;
import com.systex.hw4.repository.MemberRepository;
import com.systex.hw4.util.PasswordUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) {
        //系統啟動時 新增一個root帳號
        if (memberRepository.count() == 0) {
            Member member = new Member();
            member.setUsername("root");
            String hashedPassword = PasswordUtil.hashPassword("1234");
            member.setPassword(hashedPassword);
            memberRepository.save(member);
        }
    }
}
