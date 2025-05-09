package study.group.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.group.domain.member.dto.request.SignupRequest;
import study.group.domain.member.dto.response.SignupResponse;
import study.group.domain.member.service.MemberService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {
}
