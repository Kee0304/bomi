package com.bomi.main.repository;

import com.bomi.main.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberEmail(String email);

    @Query("SELECT m.memberId FROM Member m")
    Set<Long> findAllMemberIds();
}
