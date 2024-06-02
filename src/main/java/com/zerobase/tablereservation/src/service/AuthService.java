package com.zerobase.tablereservation.src.service;

import com.zerobase.tablereservation.common.constant.Auth;
import com.zerobase.tablereservation.common.constant.Authority;
import com.zerobase.tablereservation.src.persist.CustomerRepository;
import com.zerobase.tablereservation.src.persist.ManagerRepository;
import com.zerobase.tablereservation.src.persist.entity.Customer;
import com.zerobase.tablereservation.src.persist.entity.Manager;
import com.zerobase.tablereservation.src.model.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    /*
    form의 role를 통해
    점장 -> MANAGER
    고객 -> CUSTOMER 로
    유저 구분 후
    계정 저장하기.
     */
    public void register(Auth.SignUp form) {
        List<String> roles = new ArrayList<>();
        roles.add(form.getRole());

        if(form.getRole().equals(Authority.ROLE_CUSTOMER.toString())){
            customerRepository.save(
                    Customer.builder()
                            .username(form.getUsername())
                            .password(passwordEncoder.encode(form.getPassword()))
                            .name(form.getName())
                            .phone(form.getPhone())
                            .roles(roles)
                            .build()
            );
        } else if(form.getRole().equals(Authority.ROLE_MANAGER.toString())) {
            managerRepository.save(
                    Manager.builder()
                            .username(form.getUsername())
                            .password(passwordEncoder.encode(form.getPassword()))
                            .name(form.getName())
                            .phone(form.getPhone())
                            .roles(roles)
                            .build()
            );
        } else{
            throw new IllegalStateException("잘못된 role를 입력하셨습니다.");
        }
    }

    public Customer customerAuthentication(Auth.SignIn form) {
        var customer = customerRepository.findByUsername(form.getUsername())
                .orElseThrow(()->new IllegalStateException("해당 username을 찾을 수 없습니다."));
        if(!passwordEncoder.matches(form.getPassword(), customer.getPassword())){
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        return customer;
    }

    public Manager managerAuthentication(Auth.SignIn form) {

        var manager = managerRepository.findByUsername(form.getUsername())
                .orElseThrow(()->new IllegalStateException("해당 username을 찾을 수 없습니다."));
        if(!passwordEncoder.matches(form.getPassword(), manager.getPassword())){
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        return manager;
    }

    /*
     현재 등록된 유저 정보 가져오기 (UserVO, - userId와 userName)
     */
    public UserVO getCurrentUserVO() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication) && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                var name = ((UserDetails) principal).getUsername();
                var userdetails = loadUserByUsername(name);
                if (userdetails instanceof Customer){
                    return UserVO.builder()
                            .userId(((Customer) userdetails).getId())
                            .username(name)
                            .build();
                }

                if (userdetails instanceof Manager){
                    return UserVO.builder()
                            .userId(((Manager) userdetails).getId())
                            .username(name)
                            .build();
                }
            }
        }
        throw new IllegalStateException("현재 로그인을 하지 않았거나 유저 정보가 존재하지 않습니다.");
    }

    /*
       username으로 정보 가져오기.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customerOptional = customerRepository.findByUsername(username);
        if(customerOptional.isPresent()){
            return customerOptional.get();
        }

        return managerRepository.findByUsername(username).orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
    }
}
