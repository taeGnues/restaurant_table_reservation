package com.zerobase.tablereservation.common.constant;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.lang.NonNull;

public class Auth {

    @Data
    public static class SignIn{
        @NonNull
        private String username;
        @NonNull
        private String password;
    }

    @Data
    public static class SignUp{
        @NotNull(message = "username을 입력해주세요.")
        private String username;
        @NotNull(message = "password를 입력해주세요.")
        private String password;
        @NotNull(message = "name를 입력해주세요.")
        private String name;
        @NotNull(message = "phone를 입력해주세요.")
        private String phone;
        @NotNull(message = "role를 입력해주세요.")
        private String role;
    }
}
