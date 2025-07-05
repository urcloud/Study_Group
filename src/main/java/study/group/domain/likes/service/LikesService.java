package study.group.domain.likes.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.group.domain.likes.entity.Likes;
import study.group.domain.likes.repository.LikesRepository;
import study.group.domain.member.entity.Member;
import study.group.domain.member.repository.MemberRepository;
import study.group.domain.study.entity.Study;
import study.group.domain.study.repository.StudyRepository;
import study.group.global.exception.ErrorCode;
import study.group.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class LikesService {

  private final LikesRepository likesRepository;
  private final MemberRepository memberRepository;
  private final StudyRepository studyRepository;

  // 좋아요 추가 및 삭제
  public void toggleLikes(Long studyId, HttpSession session) {
    if (session.getAttribute("userId") == null) {
      throw new NotFoundException(ErrorCode.LOGIN_FIRST);
    }

    Study study = studyRepository.findById(studyId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.STUDY_NOT_FOUND));

    Member member = memberRepository.findById((Long) session.getAttribute("userId"))
        .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

    boolean isLiked = likesRepository.existsByMemberIdAndStudyId(member.getId(), studyId);

    if (Boolean.TRUE.equals(isLiked)) {
      Likes likes = likesRepository.findByMemberIdAndStudyId(member.getId(), studyId);
      if (likes != null) {
        likesRepository.delete(likes);
      }
    } else {
      likesRepository.save(new Likes(member, study));
    }
  }
}
