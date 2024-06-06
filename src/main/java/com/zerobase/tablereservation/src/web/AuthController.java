package com.zerobase.tablereservation.src.web;

import com.zerobase.tablereservation.common.constant.Auth;
import com.zerobase.tablereservation.common.security.JwtTokenProvider;
import com.zerobase.tablereservation.src.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * 회원 가입을 위한 Controller
 */

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    /*
    회원 가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody Auth.SignUp form){
        authService.register(form);
        return ResponseEntity.ok("회원가입에 성공했습니다.");
    }

    /*
    로그인은 customer 로그인과 manager 로그인을 구분한다.
    로그인은 password와 username에 의해 이뤄지며,
    인증 성공 시, username과 role을 담은 token을 발급한다.
     */
    @PostMapping("/customer-signin")
    public ResponseEntity<?> customerSignIn(@Valid @RequestBody Auth.SignIn form){
        var member = authService.customerAuthentication(form);
        var token = jwtTokenProvider.generateToken(member.getUsername(), member.getRoles());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/manager-signin")
    public ResponseEntity<?> managerSignIn(@Valid @RequestBody Auth.SignIn form){
        // 0. 비밀번호/아이디 유효한지 검사
        var member = authService.managerAuthentication(form);
        var token = jwtTokenProvider.generateToken(member.getUsername(), member.getRoles());
        // 1. 인증용 토큰 발급하기.
        return ResponseEntity.ok(token);
    }
}
