package com.example.SpringBoot.member.service;

import com.example.SpringBoot.member.dto.MemberDTO;
import com.example.SpringBoot.member.repository.MemberRepository;
import com.example.SpringBoot.member.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Long registerMember(MemberDTO dto, boolean agreement) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        memberRepository.save(dto);
        if ("SITTER".equals(dto.getMemberType())) {
            messageRepository.save(dto.getMemNum(), agreement);
        }
        return dto.getMemNum();
    }

    public boolean findByMemNum(Long memNum) {
        return messageRepository.findByMemNum(memNum);
    }

    public void updateMember(MemberDTO dto, boolean agreement) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        memberRepository.updateMember(dto);
        if ("SITTER".equals(dto.getMemberType())) {
            messageRepository.update(dto.getMemNum(), agreement);
        }
    }
}