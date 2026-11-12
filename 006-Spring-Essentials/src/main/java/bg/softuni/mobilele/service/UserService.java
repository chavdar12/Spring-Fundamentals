package bg.softuni.mobilele.service;

import bg.softuni.mobilele.model.service.UserRegistrationServiceModel;

public interface UserService {

    void initializeUsersAndRoles();

    void registerAndLoginUser(UserRegistrationServiceModel userRegistrationServiceModel);

    boolean isUserNameFree(String username);
}
