package study.group.domain.member.service;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.group.domain.member.dto.request.LoginRequest;
import study.group.domain.member.dto.request.SignupRequest;
import study.group.domain.member.dto.response.LoginResponse;
import study.group.domain.member.dto.response.MyInfoResponse;
import study.group.domain.member.dto.response.SignupResponse;
import study.group.domain.member.entity.Member;
import study.group.domain.member.repository.MemberRepository;
import study.group.global.exception.ErrorCode;
import study.group.global.exception.InvalidValueException;
import study.group.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class MemberService {
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  //회원가입
  public SignupResponse signUp(SignupRequest signupRequest) {
    if(memberRepository.existsByEmail(signupRequest.getEmail())) {
      throw new InvalidValueException(ErrorCode.EMAIL_DUPLICATION);
    }

    if(memberRepository.existsByNickName(signupRequest.getNickName())) {
      throw new InvalidValueException(ErrorCode.NICKNAME_DUPLICATION);
    }

    Member member =
        Member.builder()
                .name(signupRequest.getName())
                    .age(signupRequest.getAge())
                        .nickName(signupRequest.getNickName())
                            .email(signupRequest.getEmail())
                                .password(passwordEncoder.encode(signupRequest.getPassword()))
        .build();

    memberRepository.save(member);

    return SignupResponse.toDto(member);
  }


  //로그인
  public LoginResponse login(LoginRequest loginRequest, HttpSession session) {
    Member member = memberRepository
        .findByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new InvalidValueException(ErrorCode.MEMBER_NOT_FOUND));

    if(!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
      throw new InvalidValueException(ErrorCode.PASSWORD_MISMATCH);
    }

    session.setAttribute("userId", member.getId());
    return LoginResponse.toDto(member);
  }


  //로그아웃

  public void logout(HttpSession session) {
    if(session.getAttribute("userId") == null) {
      throw new NotFoundException(ErrorCode.LOGIN_FIRST);
    }

    session.invalidate();
  }


  //내 정보조회
  public MyInfoResponse getMyInfo(HttpSession session) {
    if(session.getAttribute("userId") == null) {
      throw new NotFoundException(ErrorCode.LOGIN_FIRST);
    }

    Member member = memberRepository.findById(Long.parseLong(session.getAttribute("userId").toString()))
        .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

    return MyInfoResponse.toDto(member);
  }
}
