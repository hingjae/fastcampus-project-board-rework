package com.fastcampus.fastcampusboardrework.security;

import lombok.Getter;

public enum RoleType {
    USER("USER"), ADMIN("ADMIN");

    @Getter
    private String name;

    RoleType(String name) {
        this.name = name;
    }
}
