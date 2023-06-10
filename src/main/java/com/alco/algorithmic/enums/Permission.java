package com.alco.algorithmic.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    USER_UPDATE("USER_UPDATE"),
    SELF_UPDATE("SELF_UPDATE");


    @Getter
    private final String permission;
}
