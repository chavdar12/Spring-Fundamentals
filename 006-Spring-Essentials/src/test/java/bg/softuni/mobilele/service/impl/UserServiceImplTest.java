package bg.softuni.mobilele.service.impl;

import bg.softuni.mobilele.model.entity.UserEntity;
import bg.softuni.mobilele.model.validator.UniqueUserNameValidator;
import bg.softuni.mobilele.repository.UserRepository;
import bg.softuni.mobilele.repository.UserRoleRepository;
import bg.softuni.mobilele.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserServiceImpl service = new UserServiceImpl(mock(PasswordEncoder.class), userRepository,
            mock(UserRoleRepository.class), mock(MobileleUserServiceImpl.class));

    @Test
    void userNameIsFreeOnlyWhenNoCaseInsensitiveMatchExists() {
        when(userRepository.findByUsernameIgnoreCase("Pesho")).thenReturn(Optional.of(new UserEntity()));
        when(userRepository.findByUsernameIgnoreCase("newbie")).thenReturn(Optional.empty());

        assertFalse(service.isUserNameFree("Pesho"));
        assertTrue(service.isUserNameFree("newbie"));
    }

    @Test
    void uniqueUserNameValidatorAcceptsNullAndDelegatesOtherwise() {
        UserService userService = mock(UserService.class);
        when(userService.isUserNameFree("taken")).thenReturn(false);
        UniqueUserNameValidator validator = new UniqueUserNameValidator(userService);

        assertTrue(validator.isValid(null, null));
        assertFalse(validator.isValid("taken", null));
        verify(userService, never()).isUserNameFree(null);
    }
}
