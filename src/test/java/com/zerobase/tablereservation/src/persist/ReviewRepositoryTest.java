package com.zerobase.tablereservation.src.persist;

import com.zerobase.tablereservation.src.persist.entity.Reservation;
import com.zerobase.tablereservation.src.persist.entity.Review;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void testFindByReservationId() {
        // Create and save a Reservation entity
        Reservation reservation = Reservation.builder()
                .time(LocalDateTime.now())
                .details("Dinner reservation")
                .visit(true)
                .build();



        // Create and save a Review entity linked to the Reservation
        Review review = Review.builder()
                .rating(5)
                .content("Excellent service!")
                .build();

        reservation.setReview(review);
        review.setReservation(reservation);

        review = reviewRepository.save(review);
        reservation = reservationRepository.save(reservation);

        // Fetch the review by reservationId
        Review foundReview = reviewRepository.findByReservationId(reservation.getId()).get();

        // Assertions
        assertThat(foundReview).isNotNull();
        assertThat(foundReview.getId()).isEqualTo(review.getId());
        assertThat(foundReview.getContent()).isEqualTo("Excellent service!");
        assertThat(foundReview.getRating()).isEqualTo(5);
        assertThat(foundReview.getReservation().getId()).isEqualTo(reservation.getId());
    }
}