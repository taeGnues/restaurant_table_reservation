package com.zerobase.tablereservation.src.service;

import com.zerobase.tablereservation.common.exceptions.BaseException;
import com.zerobase.tablereservation.common.exceptions.ExceptionCode;
import com.zerobase.tablereservation.src.model.ReviewRegisterDTO;
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
    public void registerReview(ReviewRegisterDTO dto) {
        UserVO userVO = authService.getCurrentUserVO();

        // 1. 해당 예약이 존재하는지 확인
        Reservation reservation = reservationRepository.findByIdAndCustomer_Id(dto.getReservationId(), userVO.getUserId())
                .orElseThrow(()->new BaseException(ExceptionCode.RESERVATION_NOT_FOUND));

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
    public void changeAverageRating(ReviewRegisterDTO dto){
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(()->new BaseException(ExceptionCode.RESTAURANT_NOT_FOUND));

        // 5-1. restaurantId로 모든 reservationId 찾기
        List<Reservation> reservations = reservationRepository.findAllByRestaurant_IdWithFetch(dto.getRestaurantId());

        // 5-2. reservationId에 해당하는 review들 찾기
        List<Review> reviews = reservations.stream().map(Reservation::getReview).toList();

        // 6. 해당 review들을 바탕으로 평균구하기
        double rating_avg = reviews.stream().mapToInt(Review::getRating).average().orElseThrow(
                () -> new BaseException(ExceptionCode.REVIEW_EMPTY)
        );

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
}
