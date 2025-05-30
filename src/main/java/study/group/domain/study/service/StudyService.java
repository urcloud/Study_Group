package study.group.domain.study.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.group.domain.apply.entity.Apply;
import study.group.domain.apply.repository.ApplyRepository;
import study.group.domain.likes.repository.LikesRepository;
import study.group.domain.member.entity.Member;
import study.group.domain.member.repository.MemberRepository;
import study.group.domain.study.dto.request.WritingRequest;
import study.group.domain.study.dto.response.ContentResponse;
import study.group.domain.study.dto.response.ListResponse;
import study.group.domain.study.dto.response.StudyListWrapper;
import study.group.domain.study.dto.response.WritingResponse;
import study.group.domain.study.entity.Category;
import study.group.domain.study.entity.Study;
import study.group.domain.study.repository.CategoryRepository;
import study.group.domain.study.repository.StudyRepository;
import study.group.global.exception.ErrorCode;
import study.group.global.exception.InvalidValueException;
import study.group.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class StudyService {
  private final StudyRepository studyRepository;
  private final LikesRepository likesRepository;
  private final MemberRepository memberRepository;
  private final ApplyRepository applyRepository;
  private final CategoryRepository categoryRepository;

  //스터디 모집 글 작성
  @Transactional
  public WritingResponse recruitWriting(WritingRequest writingRequest, HttpSession session) {

    if(session.getAttribute("userId") == null) {
      throw new NotFoundException(ErrorCode.LOGIN_FIRST);
    }

    Member member = memberRepository.findById((Long) session.getAttribute("userId"))
        .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

    Category category = categoryRepository.findByTypeAndAddrDoAndAddrGu(
        writingRequest.getType(),
        writingRequest.getRegionDo(),
        writingRequest.getRegionGu()
    ).orElseGet(() -> {
      Category newCategory = Category.builder()
          .type(writingRequest.getType())
          .addrDo(writingRequest.getRegionDo())
          .addrGu(writingRequest.getRegionGu())
          .build();
      return categoryRepository.save(newCategory);
    });

    Study study = Study.builder()
        .createdAt(LocalDateTime.now())
        .title(writingRequest.getTitle())
        .content(writingRequest.getContent())
        .closedAt(writingRequest.getClosedAt())
        .limitedPeople(writingRequest.getLimitedPeople())
        .currentPeople(0)
        .online(writingRequest.isOnline())
        .totalLikes(0)
        .category(category)
        .member(member)
        .build();

    studyRepository.save(study);

    Apply apply = Apply.builder()
        .member(member)
        .study(study)
        .build();

    applyRepository.save(apply);

    return WritingResponse.toDto(study);
  }


  //스터디 모집 글 수정
  @Transactional
  public WritingResponse recruitWritingUpdating(Long studyId, WritingRequest writingRequest, HttpSession session) {
    if(session.getAttribute("userId") == null) {
      throw new NotFoundException(ErrorCode.LOGIN_FIRST);
    }

    Study study = studyRepository.findById(studyId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.STUDY_NOT_FOUND));

    if (!studyRepository.existsByIdAndMemberId(studyId, (Long) session.getAttribute("userId"))) {
      throw new InvalidValueException(ErrorCode.NO_AUTHORIZATION);
    }

    Category updatedCategory = categoryRepository.findByTypeAndAddrDoAndAddrGu(
        writingRequest.getType(),
        writingRequest.getRegionDo(),
        writingRequest.getRegionGu()
    ).orElseGet(() -> {
      Category newCategory = Category.builder()
          .type(writingRequest.getType())
          .addrDo(writingRequest.getRegionDo())
          .addrGu(writingRequest.getRegionGu())
          .build();
      return categoryRepository.save(newCategory);
    });

    study.update(
        LocalDateTime.now(),
        writingRequest.getTitle(),
        writingRequest.getContent(),
        writingRequest.getClosedAt(),
        writingRequest.getLimitedPeople(),
        writingRequest.isOnline(),
        updatedCategory
    );

    studyRepository.save(study);

    return WritingResponse.toDto(study);
  }


  //스터디 모집 글 삭제
  public void recruitWritingDeleting(Long studyId, HttpSession session) {
    if(session.getAttribute("userId") == null) {
      throw new NotFoundException(ErrorCode.LOGIN_FIRST);
    }

    Study study = studyRepository.findById(studyId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.STUDY_NOT_FOUND));

    if (!studyRepository.existsByIdAndMemberId(studyId, (Long) session.getAttribute("userId"))) {
      throw new InvalidValueException(ErrorCode.NO_AUTHORIZATION);
    }
    studyRepository.delete(study);
  }


  //스터디 모집 목록조회(마감x)
  public StudyListWrapper listPending() {
    List<Study> studies = studyRepository.findOngoingStudies();
    List<ListResponse> responses = studies.stream()
        .map(ListResponse::toDto)
        .collect(Collectors.toList());
    return new StudyListWrapper(responses);
  }


  //스터디 모집 목록조회(마감o)
  public StudyListWrapper listCompleted() {
    List<Study> studies = studyRepository.findCompletedStudies();
    List<ListResponse> responses = studies.stream()
        .map(ListResponse::toDto)
        .collect(Collectors.toList());
    return new StudyListWrapper(responses);
  }


  //스터디 모집 글 내용 상세조회
  public ContentResponse getContent(Long studyId, HttpSession session) {
    Study study = studyRepository.findById(studyId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.STUDY_NOT_FOUND));

    long userId = (Long) session.getAttribute("userId");

    boolean likedByCurrentUser = likesRepository.existsByMemberIdAndStudyId(userId, studyId);
    return ContentResponse.toDto(study, likedByCurrentUser);
  }
}
