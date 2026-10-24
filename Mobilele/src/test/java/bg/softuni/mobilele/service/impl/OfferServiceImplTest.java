package bg.softuni.mobilele.service.impl;

import bg.softuni.mobilele.model.entity.OfferEntity;
import bg.softuni.mobilele.model.entity.UserEntity;
import bg.softuni.mobilele.model.entity.UserRoleEntity;
import bg.softuni.mobilele.model.entity.enums.EngineEnum;
import bg.softuni.mobilele.model.entity.enums.TransmissionEnum;
import bg.softuni.mobilele.model.entity.enums.UserRoleEnum;
import bg.softuni.mobilele.model.service.OfferUpdateServiceModel;
import bg.softuni.mobilele.repository.ModelRepository;
import bg.softuni.mobilele.repository.OfferRepository;
import bg.softuni.mobilele.repository.UserRepository;
import bg.softuni.mobilele.web.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OfferServiceImplTest {

    private OfferRepository offerRepository;
    private UserRepository userRepository;
    private OfferServiceImpl service;

    private final UserEntity pesho = user("pesho", UserRoleEnum.USER);
    private final UserEntity gosho = user("gosho", UserRoleEnum.USER);
    private final UserEntity admin = user("admin", UserRoleEnum.ADMIN);

    @BeforeEach
    void setUp() {
        offerRepository = mock(OfferRepository.class);
        userRepository = mock(UserRepository.class);
        service = new OfferServiceImpl(offerRepository, new ModelMapper(), mock(ModelRepository.class), userRepository);

        for (UserEntity u : new UserEntity[]{pesho, gosho, admin}) {
            when(userRepository.findByUsername(u.getUsername())).thenReturn(Optional.of(u));
        }
    }

    @Test
    void findByIdThrowsNotFoundForMissingOffer() {
        when(offerRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.findById(42L, "pesho"));
    }

    @Test
    void sellerAndAdminAreOwnersOthersAreNot() {
        when(offerRepository.findById(1L)).thenReturn(Optional.of(new OfferEntity().setSeller(pesho)));

        assertTrue(service.isOwner("pesho", 1L));
        assertTrue(service.isOwner("admin", 1L));
        assertFalse(service.isOwner("gosho", 1L));
        assertFalse(service.isOwner("unknown", 1L));
    }

    @Test
    void offerWithoutSellerIsOwnedOnlyByAdmin() {
        when(offerRepository.findById(1L)).thenReturn(Optional.of(new OfferEntity()));

        assertFalse(service.isOwner("pesho", 1L));
        assertTrue(service.isOwner("admin", 1L));
    }

    @Test
    void missingOfferHasNoOwner() {
        when(offerRepository.findById(1L)).thenReturn(Optional.empty());

        assertFalse(service.isOwner("admin", 1L));
    }

    @Test
    void updateOfferCopiesFieldsAndSaves() {
        OfferEntity offer = new OfferEntity().setPrice(100).setSeller(pesho);
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        service.updateOffer(new OfferUpdateServiceModel()
                .setId(1L).setPrice(9999).setDescription("new").setEngine(EngineEnum.HYBRID)
                .setImageUrl("img").setMileage(10).setTransmission(TransmissionEnum.AUTOMATIC).setYear(2020));

        assertEquals(9999, offer.getPrice());
        assertEquals("new", offer.getDescription());
        assertEquals(EngineEnum.HYBRID, offer.getEngine());
        assertEquals(2020, offer.getYear());
        verify(offerRepository).save(offer);
    }

    @Test
    void updateOfferThrowsNotFoundForMissingOffer() {
        when(offerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
                () -> service.updateOffer(new OfferUpdateServiceModel().setId(1L)));
        verify(offerRepository, never()).save(any());
    }

    private static UserEntity user(String username, UserRoleEnum role) {
        return new UserEntity().setUsername(username).setRoles(Set.of(new UserRoleEntity().setRole(role)));
    }
}
