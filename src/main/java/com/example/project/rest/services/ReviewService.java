package com.example.project.rest.services;

import com.example.project.domain.entities.Reviews;
import com.example.project.domain.repositories.ReviewsRepository;
import com.example.project.rest.dto.ReviewRequestDto;
import com.example.project.rest.dto.ReviewsResponseDto;
import com.example.project.rest.services.exceptions.CustomException;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewsRepository reviewsRepository;
    private final UsersService usersService;
    private final MovieService movieService;
    private final JwtService jwtService;
    private final MessageSource messageSource;

    @Transactional
    public ReviewsResponseDto addReview(ReviewRequestDto dto, HttpServletRequest request){
        try{
            var userId = jwtService.getClaimId(request);
            var user = usersService.findById(userId);
            var movie = movieService.findById(dto.getMovieId());

            reviewsRepository.findByUserAndMovie(user, movie).ifPresent(e -> {
                throw new CustomException(
                        messageSource.getMessage("review.service.error.reviewAlreadyAdded", null, LocaleContextHolder.getLocale())
                );

            });

            var review = reviewsRepository.save(Reviews.of(dto,user,movie));
            movie.getReviews().add(review);
            return ReviewsResponseDto.of(review);

        } catch(ConstraintViolationException e){
            throw new CustomException(
                    messageSource.getMessage("review.service.error.constraintViolationException", null, LocaleContextHolder.getLocale())
            );
        } catch(DataIntegrityViolationException e) {
            throw new CustomException(
                    messageSource.getMessage("review.service.error.dataIntegrity",null, LocaleContextHolder.getLocale())
            );
        }
    }

    public Reviews findById(Long id){

        return reviewsRepository.findById(id).orElseThrow(() ->
            new ObjectNotFoundExceptions(
                    messageSource.getMessage("review.service.error.notFound", null, LocaleContextHolder.getLocale())
            ));
    }

    @Transactional
    public ReviewsResponseDto findByUserAndMovie(Long userId,Long movieId ){
        var user = usersService.findById(userId);
        var movie = movieService.findById(movieId   );

        return reviewsRepository.findByUserAndMovie(user, movie).map(ReviewsResponseDto::of)
                .orElseThrow(() -> new CustomException(
                        messageSource.getMessage("review.service.error.userReviewNotFound", null, LocaleContextHolder.getLocale())
                ));
    }


    @Transactional
    public ReviewsResponseDto updateReview(ReviewRequestDto dto,Long id){
        try{
            var review = findById(id);
            updateData(dto,review);
            var res = reviewsRepository.save(review);
            return ReviewsResponseDto.of(res);

        } catch(DataIntegrityViolationException e) {
            throw new CustomException(
                    messageSource.getMessage("review.service.error.dataIntegrity",null, LocaleContextHolder.getLocale())
            );
        }
    }

    @Transactional
    public void deleteReview(Long id){
        var review = findById(id);
        reviewsRepository.delete(review);
    }

    private void updateData(ReviewRequestDto dto,Reviews reviews){
        reviews.setComment(dto.getComment());
        reviews.setRating(dto.getRating());
    }
}
