package com.example.project.rest.services;

import com.example.project.domain.entities.Reviews;
import com.example.project.domain.repositories.ReviewsRepository;
import com.example.project.rest.dto.ReviewRequestDto;
import com.example.project.rest.services.exceptions.CustomException;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewsRepository reviewsRepository;
    private final UsersService usersService;
    private final MovieService movieService;
    private final JwtService jwtService;

    @Transactional
    public void addReview(ReviewRequestDto dto, HttpServletRequest request){
        try{
            var userId = jwtService.getClaimId(request);
            var user = usersService.findById(userId);
            var movie = movieService.findById(dto.getMovieId());
            var review = reviewsRepository.save(Reviews.of(dto,user,movie));
            movie.getReviews().add(review);

        } catch(ConstraintViolationException e){
            throw new CustomException("enter a grade between 1 and 5");
        }
    }

    public Reviews findById(Long id){
        return reviewsRepository.findById(id).orElseThrow(() ->
            new ObjectNotFoundExceptions("this review is not found"));
    }

    @Transactional
    public void updateReview(ReviewRequestDto dto,Long id){
        var review = findById(id);
        updateData(dto,review);
        reviewsRepository.save(review);
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
