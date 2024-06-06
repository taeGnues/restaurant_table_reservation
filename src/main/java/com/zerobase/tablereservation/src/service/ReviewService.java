package com.zerobase.tablereservation.src.service;

import com.zerobase.tablereservation.common.exceptions.BaseException;
import com.zerobase.tablereservation.common.exceptions.ExceptionCode;
import com.zerobase.tablereservation.src.model.ReviewDTO;
import com.zerobase.tablereservation.src.model.ReviewDeleteDTO;
import com.zerobase.tablereservation.src.model.ReviewGetDTO;
import com.zerobase.tablereservation.src.model.UserVO;
import com.zerobase.tablereservation.src.persist.ReservationRepository;
import com.zerobase.tablereservation.src.persist.RestaurantRepository;
import com.zerobase.tablereservation.src.persist.ReviewRepository;
import com.zerobase.tablereservation.src.persist.entity.Reservation;
import com.zerobase.tablereservation.src.persist.entity.Restaurant;
import com.zerobase.tablereservation.src.persist.entity.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final AuthService authService;

    /*
    리뷰 등록
     */
    @Transactional
    public void registerReview(ReviewDTO dto) {
        Reservation reservation = getReservation(dto.getReservationId());

        // 2. 해당 예약의 visit이 true인지 확인
        checkAlreadyVisitedTrue(reservation);

        // 3. 해당 예약에 대해 이미 리뷰가 있는지 확인
        checkAlreadyHadReview(reservation);

        // 4. 리뷰 등록
        Review review = Review.builder()
                .rating(dto.getRating())
                .content(dto.getContent())
                .build();

        review.setReservation(reservation);
        reservation.setReview(review);

        reviewRepository.save(review);
    }

    /*
    식당 평점 변경
     */
    @Transactional
    public void changeAverageRating(Long restaurantId){
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()->new BaseException(ExceptionCode.RESTAURANT_NOT_FOUND));

        // 5-1. restaurantId로 모든 reservationId 찾기
        List<Reservation> reservations = reservationRepository.findAllByRestaurant_IdWithFetch(restaurantId);

        // 5-2. reservationId에 해당하는 review들 찾기
        List<Review> reviews = reservations.stream().map(Reservation::getReview).toList();

        double rating_avg = 0;

        if(!reviews.isEmpty()){
            // 6. 해당 review들을 바탕으로 평균구하기
            rating_avg = reviews.stream().mapToInt(Review::getRating).average().orElseThrow(
                    () -> new BaseException(ExceptionCode.REVIEW_EMPTY)
            );
        }
        restaurant.evaluated(rating_avg);
    }

    private void checkAlreadyHadReview(Reservation reservation) {
        if(reviewRepository.findByReservationId(reservation.getId()).isPresent()){
            throw new BaseException(ExceptionCode.REVIEW_ALREADY_DONE);
        }


    }

    private void checkAlreadyVisitedTrue(Reservation reservation) {
        if(!reservation.isVisit()){
            throw new BaseException(ExceptionCode.REVIEW_NEEDED_VISIT);
        }
    }

    /*
    리뷰 수정
     */
    @Transactional
    public void updateReview(ReviewDTO dto) {
        Reservation reservation = getReservation(dto.getReservationId());

        // 2. 예약 번호로 리뷰 찾기.
        Review review = reviewRepository.findByReservationId(reservation.getId())
                .orElseThrow(()->new BaseException(ExceptionCode.REVIEW_EMPTY));

        // 3. review 수정하기
        review.update(dto);
    }

    /*
    리뷰 삭제
     */
    @Transactional
    public void deleteReview(ReviewDeleteDTO dto) {
        UserVO userVO = authService.getCurrentUserVO();

        // 1. 해당 주인의 식당 찾기.
        Restaurant restaurant = restaurantRepository.findByManager_Id(userVO.getUserId())
                .orElseThrow(()->new BaseException(ExceptionCode.RESTAURANT_NOT_FOUND));

        log.info("{} ->", restaurant.getId());

        // 2, 해당 리뷰가 찾기.
        Review review = reviewRepository.findById(dto.getReviewId())
                .orElseThrow(() -> new BaseException(ExceptionCode.REVIEW_EMPTY));

        log.info("{} ->", review.getId());

        // 3. 해당 리뷰가 자기 자신의 식당에 달린 리뷰인지 확인하기. (Reservation에 reviewId & restaurantId가 존재하지 않으면 끝.)
        Reservation reservation = reservationRepository.findByReview_IdAndRestaurant_Id(review.getId(), restaurant.getId())
                .orElseThrow(() -> new BaseException(ExceptionCode.REVIEW_NON_AUTHORITY_DELETE));

        reservation.setReview(null);
    }

    private Reservation getReservation(Long reservationId) {
        UserVO userVO = authService.getCurrentUserVO();
        // 예약이 존재하는지 확인
        return reservationRepository.findByIdAndCustomer_Id(reservationId, userVO.getUserId())
                .orElseThrow(()->new BaseException(ExceptionCode.RESERVATION_NOT_FOUND));
    }

    public List<ReviewGetDTO> getReviews(Long restaurantId) {
        List<Reservation> reservations = reservationRepository.findAllByRestaurant_IdWithFetch(restaurantId);

        return reservations.stream()
                .map(Reservation::getReview)
                .toList()
                .stream()
                .map(ReviewGetDTO::fromEntity).toList();

    }
}
