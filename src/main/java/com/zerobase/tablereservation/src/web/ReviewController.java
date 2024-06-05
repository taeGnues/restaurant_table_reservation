package com.zerobase.tablereservation.src.web;

import com.zerobase.tablereservation.src.model.ReviewRegisterDTO;
import com.zerobase.tablereservation.src.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    /*
    review 등록
     */
    @PostMapping
    public ResponseEntity<?> registerReview(@RequestBody ReviewRegisterDTO dto){
        reviewService.registerReview(dto);
        reviewService.changeAverageRating(dto);
        return ResponseEntity.ok("리뷰 등록에 성공했습니다.");
    }

    /*
    review 수정
     */

    /*
    review 제거
     */

    /*
    review 조회
     */
}
