package com.systex.hw4.service;

import com.systex.hw4.model.Member;
import com.systex.hw4.repository.MemberRepository;
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
                memberRepo.findByUsernameAndPassword(member.getUsername(), member.getPassword());
        if (obj.isEmpty()) {
            throw new IllegalArgumentException("帳號或密碼錯誤");
        }
        return obj;
    }

    public void saveMember(Member member) {
        checkMemberExisted(member);
        memberRepo.save(member);
    }

    private void checkMemberExisted(Member member) {
        Optional<Member> obj = memberRepo.findByUsername(member.getUsername());
        if (obj.isPresent()) {
            throw new IllegalArgumentException("使用者名稱已存在");
        }
    }

}
