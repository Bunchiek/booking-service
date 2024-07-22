package com.example.booking.web.controller;

import com.example.booking.entity.Hotel;
import com.example.booking.entity.RoleType;
import com.example.booking.mapper.HotelMapper;
import com.example.booking.service.HotelService;
import com.example.booking.web.model.HotelListResponse;
import com.example.booking.web.model.HotelRequest;
import com.example.booking.web.model.HotelResponse;
import com.example.booking.web.model.filter.HotelFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final HotelMapper hotelMapper;


    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<HotelListResponse> filterBy(HotelFilter filter) {
        return ResponseEntity.ok(hotelMapper.hotelListToListResponse(hotelService.filterBy(filter)));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<HotelListResponse> findAll() {
        return ResponseEntity.ok(hotelMapper.hotelListToListResponse(hotelService.findAll()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(hotelMapper.hotelToResponse(hotelService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> create(@RequestBody HotelRequest request) {
        Hotel newHotel = hotelService.save(hotelMapper.requestToHotel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelMapper.hotelToResponse(newHotel));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> update(@PathVariable("id") Long hotelId, @RequestBody HotelRequest request) {
        Hotel updatedHotel = hotelService.update(hotelMapper.requestToHotel(hotelId, request));
        return ResponseEntity.ok(hotelMapper.hotelToResponse(updatedHotel));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hotelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/rate/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> rateHotel(@PathVariable("id") Long hotelId, @RequestParam Double rate) {
        Hotel existedHotel = hotelService.findById(hotelId);
        Double totalRating = existedHotel.getRating() * existedHotel.getCountReview();
        totalRating = totalRating - existedHotel.getRating() + rate;
        if (existedHotel.getCountReview() != 0) {
            existedHotel.setRating(totalRating / existedHotel.getCountReview());
        } else {
            existedHotel.setRating(totalRating);
        }
        existedHotel.setCountReview(existedHotel.getCountReview() + 1);
        hotelService.save(existedHotel);
        return ResponseEntity.ok(hotelMapper.hotelToResponse(existedHotel));

    }
}
