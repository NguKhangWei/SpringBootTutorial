package com.codewithmosh.store.dtos;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    public String oldPassword;
    public String newPassword;
}
