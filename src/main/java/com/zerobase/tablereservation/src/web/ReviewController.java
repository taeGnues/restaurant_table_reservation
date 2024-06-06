package com.zerobase.tablereservation.src.web;

import com.zerobase.tablereservation.src.model.ReviewDTO;
import com.zerobase.tablereservation.src.model.ReviewDeleteDTO;
import com.zerobase.tablereservation.src.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    /*
    review 등록
     */

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> registerReview(@RequestBody ReviewDTO dto){
        reviewService.registerReview(dto);
        reviewService.changeAverageRating(dto.getRestaurantId());
        return ResponseEntity.ok("리뷰 등록에 성공했습니다.");
    }

    /*
    review 수정
     */
    @PutMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> updateReview(@RequestBody ReviewDTO dto){
        reviewService.updateReview(dto);
        reviewService.changeAverageRating(dto.getRestaurantId());
        return ResponseEntity.ok("리뷰 수정에 성공했습니다.");
    }

    /*
    review 제거
     */
    @DeleteMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> deleteReview(@RequestBody ReviewDeleteDTO dto){
        reviewService.deleteReview(dto);
        reviewService.changeAverageRating(dto.getRestaurantId());
        return ResponseEntity.ok("리뷰 삭제에 성공했습니다.");
    }

    /*
    특정 식당의 review 조회
     */

    @GetMapping("/{restaurantId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    public ResponseEntity<?> getReviews(@PathVariable Long restaurantId){
        var result = reviewService.getReviews(restaurantId);
        return ResponseEntity.ok(result);
    }
}
