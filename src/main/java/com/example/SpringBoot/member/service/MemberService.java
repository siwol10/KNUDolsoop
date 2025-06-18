package com.example.SpringBoot.member.service;

import com.example.SpringBoot.member.dto.MemberDTO;
import com.example.SpringBoot.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 직접 생성

    public MemberDTO login(String id, String password) {
        MemberDTO member = memberRepository.findById(id); // 아이디로 조회

        if (member != null && passwordEncoder.matches(password, member.getPassword())) {
            return member; // 로그인 성공
        }
        return null; // 로그인 실패
    }

    public MemberDTO findById(String id){
        return memberRepository.findById(id);
    }

    public MemberDTO findByMemNum(String memNum){
        return memberRepository.findByMemNum(memNum);
    }
}
