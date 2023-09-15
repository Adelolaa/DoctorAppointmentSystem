//package com.Serah.DoctorAppointmentSystem.review;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//@RestController
//@RequestMapping("/api/review")
//public class ReviewController {
//
//    @Autowired
//    private ReviewRepository reviewRepository;
//
////    @PostMapping
////    public ReviewEntity createReview(@RequestBody ReviewEntity reviewEntity) {
////        return reviewRepository.createReview(reviewEntity);
//
//        @PostMapping
//        public ResponseEntity<ReviewEntity> createReview(@RequestBody ReviewEntity reviewEntity) {
//            ReviewEntity createdReview = reviewRepository.createReview(reviewEntity);
//            return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/{id}")
//    public Optional<ReviewEntity> getReview(@PathVariable Long id) {
//        return reviewRepository.findById(id);
//    }
//
//    @GetMapping
//    public List<ReviewEntity> getAllReviews() {
//
//        return reviewRepository.findAll();
//    }
//}
