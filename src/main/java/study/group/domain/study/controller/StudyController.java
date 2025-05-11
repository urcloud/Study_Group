package study.group.domain.study.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.group.domain.study.dto.request.WritingRequest;
import study.group.domain.study.dto.response.ContentResponse;
import study.group.domain.study.dto.response.ListResponse;
import study.group.domain.study.dto.response.WritingResponse;
import study.group.domain.study.service.StudyService;

@RestController
@RequiredArgsConstructor
public class StudyController {

  private final StudyService studyService;

  //스터디 모집 글 작성
  @PostMapping("/api/users/recruit-writing")
  public WritingResponse recruitWriting(
      @RequestBody WritingRequest writingRequest) {
    return studyService.recruitWriting(writingRequest);
  }

  //스터디 모집 글 수정
  @PutMapping("/api/users/recruit-writing/{studyId}")
  public WritingResponse recruitWritingUpdating(
      @RequestBody WritingRequest writingRequest) {
    return studyService.recruitWritingUpdating(writingRequest);
  }

  //스터디 모집 글 삭제
  @DeleteMapping("/api/users/recruit-writing/{studyId}")
  public void recruitWritingDeleting(
      @RequestParam int studyId) {
    studyService.recruitWritingDeleting(studyId);
  }

  //스터디 모집 목록조회(마감x)
  @GetMapping("/api/common/list/pending")
  public ListResponse listPending() {
    studyService.listPending();
  }

  //스터디 모집 목록조회(마감o)
  @GetMapping("/api/common/list/completed")
  public ListResponse listCompleted() {
    studyService.listCompleted();
  }

  //스터디 모집 글 내용 상세조회
  @GetMapping("/api/common/content/{studyId}")
  public ContentResponse content() {
    studyService.getcontent();
  }
}
