package com.project.jagoga.accommodation.application;

import com.project.jagoga.accommodation.presentation.dto.AccommodationRequestDto;
import com.project.jagoga.exception.accommodation.DuplicatedAccommodationException;
import com.project.jagoga.exception.accommodation.NotExistAccommodationException;
import com.project.jagoga.accommodation.domain.Accommodation;
import com.project.jagoga.accommodation.domain.AccommodationRepository;
import com.project.jagoga.user.domain.AuthUser;
import com.project.jagoga.user.domain.User;
import com.project.jagoga.user.domain.UserRepository;
import com.project.jagoga.utils.VerificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;

    public Accommodation saveAccommodation(AccommodationRequestDto accommodationRequestDto, AuthUser loginUser) {
        User owner = userRepository.findById(loginUser.getId()).get();
        VerificationUtils.verifyPermission(loginUser, owner.getId());
        Accommodation accommodation = accommodationRequestDto.toEntity(owner);
        validateDuplicatedAccommodation(accommodation);
        return accommodationRepository.save(accommodation);
    }

    public Accommodation updateAccommodation(
        long accommodationId,
        AccommodationRequestDto accommodationRequestDto,
        AuthUser loginUser
    ) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(NotExistAccommodationException::new);
        User owner = accommodation.getOwner();
        VerificationUtils.verifyPermission(loginUser, owner.getId());
        Accommodation updatedAccommodation = accommodation.update(
            accommodationRequestDto.getAccommodationName(),
            accommodationRequestDto.getPhoneNumber(),
            accommodationRequestDto.getCity(),
            accommodationRequestDto.getAccommodationType(),
            accommodationRequestDto.getAccommodationName(),
            accommodationRequestDto.getInformation());
        return accommodationRepository.update(updatedAccommodation);
    }

    public Long deleteAccommodation(long accommodationId, AuthUser loginUser) {
        User manager = accommodationRepository.findById(accommodationId).get().getOwner();
        VerificationUtils.verifyPermission(loginUser, manager.getId());
        accommodationRepository.findById(accommodationId)
            .ifPresentOrElse(
                a -> accommodationRepository.delete(accommodationId),
                () -> {
                    throw new NotExistAccommodationException();
                });
        return accommodationId;
    }

    public Accommodation getAccommodation(long accommodationId) {
        return accommodationRepository.findById(accommodationId)
            .orElseThrow(NotExistAccommodationException::new);
    }

    public List<Accommodation> getAccommodationAllList() {
        return accommodationRepository.findAll();
    }

    public List<Accommodation> getAccommodationListByCategoryId(long categoryId) {
        return accommodationRepository.findByCategoryId(categoryId);
    }

    private void validateDuplicatedAccommodation(Accommodation accommodation) {
        accommodationRepository.findByName(accommodation.getAccommodationName())
            .ifPresent(a -> {
                throw new DuplicatedAccommodationException();
            });
    }
}
