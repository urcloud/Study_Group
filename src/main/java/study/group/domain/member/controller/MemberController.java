package study.group.domain.member.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.group.domain.member.dto.request.LoginRequest;
import study.group.domain.member.dto.request.SignupRequest;
import study.group.domain.member.dto.response.LoginResponse;
import study.group.domain.member.dto.response.MyInfoResponse;
import study.group.domain.member.dto.response.SignupResponse;
import study.group.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  //회원가입
  @PostMapping("/api/common/signup")
  public SignupResponse signUp(
      @RequestBody SignupRequest signupRequest) {
    return memberService.signUp(signupRequest);
  }

  //로그인
  @PostMapping("/api/common/login")
  public LoginResponse login(
      @RequestBody LoginRequest loginRequest, HttpSession session) {
    return memberService.login(loginRequest, session);
  }

  //로그아웃
  @PostMapping("/api/users/logout")
  public void logout(HttpSession session) {
    memberService.logout(session);
  }

  //정보조회
  @GetMapping("/api/users/info")
  public MyInfoResponse getMyInfo(HttpSession session) {
    return memberService.getMyInfo(session);
  }
}