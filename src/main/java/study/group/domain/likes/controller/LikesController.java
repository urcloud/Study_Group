package study.group.domain.likes.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import study.group.domain.likes.service.LikesService;

@RestController
@RequiredArgsConstructor
public class LikesController {

  private final LikesService likesService;

  //좋아요 추가 및 삭제
  @PostMapping("/api/users/likes/{studyId}")
  public void addLikes(
      @PathVariable long studyId, HttpSession session) {
    likesService.toggleLikes(studyId, session);
  }
}
