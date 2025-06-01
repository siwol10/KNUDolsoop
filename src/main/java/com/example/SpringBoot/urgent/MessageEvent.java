package com.example.SpringBoot.urgent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageEvent {
    private final String region;

    public String getRegion() {
        return region;
    }
}