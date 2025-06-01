package com.example.SpringBoot.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {
    private Long memNum;
    private String id;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private String memberType;
    private String region;
}
