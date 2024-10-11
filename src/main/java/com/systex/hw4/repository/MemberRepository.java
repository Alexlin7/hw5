package com.systex.hw4.repository;

import com.systex.hw4.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByUsername(String username);
    Optional<Member> findByUsernameAndPassword(String username, String password);
}
