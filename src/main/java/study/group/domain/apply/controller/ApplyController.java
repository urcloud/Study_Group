package study.group.domain.apply.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import study.group.domain.apply.dto.TeamInfoResponse;
import study.group.domain.apply.service.ApplyService;

@RestController
@RequiredArgsConstructor
public class ApplyController {

  private final ApplyService applyService;

  //모집 신청
  @PostMapping("/api/users/apply/{studyId}")
  public void applyStudy(
      @PathVariable long studyId, HttpSession session) {
    applyService.applyStudy(studyId, session);
  }

  //모집 신청취소
  @DeleteMapping("/api/users/cancel/{studyId}")
  public void cancelStudy(
      @PathVariable long studyId, HttpSession session) {
    applyService.cancelStudy(studyId, session);
  }

  //모집완료 시 팀원 정보 조회
  @GetMapping("/api/users/lookup/{studyId}")
  public TeamInfoResponse getTeamInfo(
      @PathVariable long studyId, HttpSession session) {
    return applyService.getTeamInfo(studyId, session);
  }
}
