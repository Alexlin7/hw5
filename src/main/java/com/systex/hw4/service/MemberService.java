package com.systex.hw4.service;

import com.systex.hw4.model.Member;
import com.systex.hw4.repository.MemberRepository;
import com.systex.hw4.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepo;

    public MemberService(MemberRepository memberRepo) {
        this.memberRepo = memberRepo;
    }

    public Optional<Member> login(Member member) {
        Optional<Member> obj =
                memberRepo.findByUsername(member.getUsername());
        if (obj.isEmpty()) {
            throw new IllegalArgumentException("查無使用者");
        }
        if(!PasswordUtil.verifyPassword(member.getPassword(), obj.get().getPassword())){
            throw new IllegalArgumentException("帳號密碼錯誤");
        }

        return obj;
    }

    public void saveMember(Member member) {
        checkMemberExisted(member);
        //密碼雜湊
        String hashedPassword = PasswordUtil.hashPassword(member.getPassword());
        member.setPassword(hashedPassword);
        memberRepo.save(member);
    }

    private void checkMemberExisted(Member member) {
        Optional<Member> obj = memberRepo.findByUsername(member.getUsername());
        if (obj.isPresent()) {
            throw new IllegalArgumentException("使用者名稱已存在");
        }
    }

}
