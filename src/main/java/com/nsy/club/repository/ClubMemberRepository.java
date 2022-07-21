package com.nsy.club.repository;

import com.nsy.club.entity.ClubMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {

    /**
     *
     * "@EntityGraph :  left outer join으로 ClubMemberRole 처리 될 수 있도록 해줌
     *
     *   - attributePaths = {"변수명"}
     *   - type
     *    1. EntityGraph.EntityGraphType.FETCH(기본값) : attributePaths에 정의한 변수만 Fetch타입을 Eager로 불러오고
     *                                                  나머지는 LAZY로 불러옴
     *    2. EntityGraph.EntityGraphType.LOAD : attributePaths에 정의한 변수만 Fetch타입을 Eager로 불러오고
     *                                          나머지 멤버변수는 각자의 FetchType을 존중하여 불러옴
     **/

    /**
     * 회원 데이터 조회
     * @param email
     * @param social
     * @return
     */
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from ClubMember m where m.fromSocial = :social and m.email= :email ")
    Optional<ClubMember> findByEmail(String email, Boolean social);



}
