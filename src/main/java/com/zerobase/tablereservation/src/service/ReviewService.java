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
    1. 해당 예약의 visit이 true인지 확인한다.
    2. 해당 예약에 대해 이미 리뷰가 있는지 확인
    3. 리뷰객체 생성 후 연관관계 맺어준다.
     */
    @Transactional
    public void registerReview(ReviewDTO dto) {
        Reservation reservation = getReservation(dto.getReservationId());

        checkAlreadyVisitedTrue(reservation);


        checkAlreadyHadReview(reservation);

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
    1. restaurantId로 모든 reservationId 찾기
    2. reservationId에 해당하는 review들 찾기
    3. 해당 review들을 바탕으로 평균구하기 (만약 review가 없다면, 0이다.)
     */
    @Transactional
    public void changeAverageRating(Long restaurantId){
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()->new BaseException(ExceptionCode.RESTAURANT_NOT_FOUND));

        List<Reservation> reservations = reservationRepository.findAllByRestaurant_IdWithFetch(restaurantId);


        List<Review> reviews = reservations.stream().map(Reservation::getReview).toList();

        double rating_avg = 0;

        if(!reviews.isEmpty()){

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
    1. 예약 번호로 리뷰를 찾는다.
    2. review를 update로 수정한다.
     */
    @Transactional
    public void updateReview(ReviewDTO dto) {
        Reservation reservation = getReservation(dto.getReservationId());

        Review review = reviewRepository.findByReservationId(reservation.getId())
                .orElseThrow(()->new BaseException(ExceptionCode.REVIEW_EMPTY));

        review.update(dto);
    }

    /*
    리뷰 삭제 (MANAGER)
    1. 해당 주인의 식당 찾기
    2. 해당 리뷰가 찾기.
    3. 해당 리뷰가 자기 자신의 식당에 달린 리뷰인지 확인하기. (Reservation에 reviewId & restaurantId가 존재하지 않으면 끝.)
    4. 해당 예약의 리뷰를 null로 설정한다. (orphanRemoval 설정에 의해 자동으로 하위 review는 삭제된다)
     */
    @Transactional
    public void deleteReview(ReviewDeleteDTO dto) {
        UserVO userVO = authService.getCurrentUserVO();

        Restaurant restaurant = restaurantRepository.findByManager_Id(userVO.getUserId())
                .orElseThrow(()->new BaseException(ExceptionCode.RESTAURANT_NOT_FOUND));


        Review review = reviewRepository.findById(dto.getReviewId())
                .orElseThrow(() -> new BaseException(ExceptionCode.REVIEW_EMPTY));


        Reservation reservation = reservationRepository.findByReview_IdAndRestaurant_Id(review.getId(), restaurant.getId())
                .orElseThrow(() -> new BaseException(ExceptionCode.REVIEW_NON_AUTHORITY_DELETE));

        reservation.setReview(null);
    }

    /*
    자신의 리뷰 삭제 (CUSTOMER)
    1. 해당 리뷰 찾기.
    2. 해당 리뷰가 자기가 작성한 리뷰인지 확인하기. (Reservation에 reviewId & customerId가 존재하지 않으면 끝.)
    3. 해당 예약의 리뷰를 null로 설정한다. (orphanRemoval 설정에 의해 자동으로 하위 review는 삭제된다)
     */
    @Transactional
    public void deleteReviewByCustomer(ReviewDeleteDTO dto) {
        UserVO userVO = authService.getCurrentUserVO();


        Review review = reviewRepository.findById(dto.getReviewId())
                .orElseThrow(() -> new BaseException(ExceptionCode.REVIEW_EMPTY));

        Reservation reservation = reservationRepository.findByReview_IdAndCustomer_Id(review.getId(), userVO.getUserId())
                .orElseThrow(() -> new BaseException(ExceptionCode.REVIEW_NON_AUTHORITY_DELETE));

        reservation.setReview(null);
    }

    private Reservation getReservation(Long reservationId) {
        UserVO userVO = authService.getCurrentUserVO();
        // 예약이 존재하는지 확인
        return reservationRepository.findByIdAndCustomer_Id(reservationId, userVO.getUserId())
                .orElseThrow(()->new BaseException(ExceptionCode.RESERVATION_NOT_FOUND));
    }

    /*
    해당 식당의 모든 리뷰 조회하기
     */
    public List<ReviewGetDTO> getReviews(Long restaurantId) {
        List<Reservation> reservations = reservationRepository.findAllByRestaurant_IdWithFetch(restaurantId);

        return reservations.stream()
                .map(Reservation::getReview)
                .toList()
                .stream()
                .map(ReviewGetDTO::fromEntity).toList();

    }
}
