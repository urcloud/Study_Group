package study.group.domain.apply.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.group.domain.apply.dto.TeamInfoResponse;
import study.group.domain.apply.entity.Apply;
import study.group.domain.apply.repository.ApplyRepository;
import study.group.domain.member.entity.Member;
import study.group.domain.member.repository.MemberRepository;
import study.group.domain.study.entity.Study;
import study.group.domain.study.repository.StudyRepository;
import study.group.global.exception.ErrorCode;
import study.group.global.exception.InvalidValueException;
import study.group.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class ApplyService {

  private final ApplyRepository applyRepository;
  private final MemberRepository memberRepository;
  private final StudyRepository studyRepository;

  //신청
  public void applyStudy(Long studyId, HttpSession session) {
    if(session.getAttribute("userId") == null) {
      throw new NotFoundException(ErrorCode.LOGIN_FIRST);
    }

    Study study = studyRepository.findById(studyId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.STUDY_NOT_FOUND));

    if (study.getClosedAt().isBefore(LocalDateTime.now()) || study.getCurrentPeople() >= study.getLimitedPeople()) {
      throw new InvalidValueException(ErrorCode.APPLY_FAILED);
    }

    Member member = memberRepository.findById((Long) session.getAttribute("userId"))
        .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

    boolean alreadyApplied = applyRepository.existsByMemberIdAndStudyId(member.getId(), study.getId());

    if (alreadyApplied) {
      throw new InvalidValueException(ErrorCode.APPLY_ALREADY);
    }

    Apply apply = Apply.builder()
        .member(member)
        .study(study)
        .build();

    applyRepository.save(apply);
  }


  //신청취소
  public void cancelStudy(Long studyId, HttpSession session) {
    if(session.getAttribute("userId") == null) {
      throw new NotFoundException(ErrorCode.LOGIN_FIRST);
    }

    Study study = studyRepository.findById(studyId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.STUDY_NOT_FOUND));

    Member member = memberRepository.findById((Long) session.getAttribute("userId"))
        .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

    if (study.getClosedAt().isBefore(LocalDateTime.now()) || study.getCurrentPeople() >= study.getLimitedPeople()) {
      throw new InvalidValueException(ErrorCode.CANCEL_APPLY_FAILED);
    }

    Apply apply = applyRepository.findByMemberIdAndStudyId(member.getId(), study.getId())
        .orElseThrow(() -> new InvalidValueException(ErrorCode.APPLY_NOT_FOUND));

    applyRepository.delete(apply);
  }


  //모집완료 후 팀원 조회
  @Transactional
  public TeamInfoResponse getTeamInfo(Long studyId, HttpSession session) {
    if(session.getAttribute("userId") == null) {
      throw new NotFoundException(ErrorCode.LOGIN_FIRST);
    }

    Study study = studyRepository.findCompletedStudyByIdAndMemberId(studyId, (Long) session.getAttribute("userId"))
        .orElseThrow(() -> new NotFoundException(ErrorCode.STUDY_NOT_FOUND));

    List<Apply> applies = applyRepository.findAllByStudyId(studyId);

    List<TeamInfoResponse.TeamMember> teamMembers = applies.stream()
        .map(Apply::getMember)
        .map(member -> TeamInfoResponse.TeamMember.builder()
            .memberId(member.getId())
            .nickname(member.getNickName())
            .email(member.getEmail())
            .build())
        .collect(Collectors.toList());

    return TeamInfoResponse.builder()
        .studyId(study.getId())
        .teamMembers(teamMembers)
        .build();
  }
}
