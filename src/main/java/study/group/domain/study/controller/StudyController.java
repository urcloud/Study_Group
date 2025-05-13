package study.group.domain.study.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.group.domain.study.dto.request.WritingRequest;
import study.group.domain.study.dto.response.ContentResponse;
import study.group.domain.study.dto.response.StudyListWrapper;
import study.group.domain.study.dto.response.WritingResponse;
import study.group.domain.study.service.StudyService;

@RestController
@RequiredArgsConstructor
public class StudyController {

  private final StudyService studyService;

  //스터디 모집 글 작성
  @PostMapping("/api/users/recruit-writing")
  public WritingResponse recruitWriting(
      @RequestBody WritingRequest writingRequest, HttpSession session) {
    return studyService.recruitWriting(writingRequest, session);
  }

  //스터디 모집 글 수정
  @PutMapping("/api/users/recruit-writing/{studyId}")
  public WritingResponse recruitWritingUpdating(
      @PathVariable long studyId,
      @RequestBody WritingRequest writingRequest, HttpSession session) {
    return studyService.recruitWritingUpdating(studyId, writingRequest, session);
  }

  //스터디 모집 글 삭제
  @DeleteMapping("/api/users/recruit-writing/{studyId}")
  public void recruitWritingDeleting(
      @PathVariable long studyId, HttpSession session) {
    studyService.recruitWritingDeleting(studyId, session);
  }

  //스터디 모집 목록조회(마감x)
  @GetMapping("/api/common/list/pending")
  public StudyListWrapper listPending() {
    return studyService.listPending();
  }

  //스터디 모집 목록조회(마감o)
  @GetMapping("/api/common/list/completed")
  public StudyListWrapper listCompleted() {
    return studyService.listCompleted();
  }

  //스터디 모집 글 내용 상세조회
  @GetMapping("/api/common/content/{studyId}")
  public ContentResponse getContent(
      @PathVariable long studyId, HttpSession session) {
    return studyService.getContent(studyId, session);
  }
}