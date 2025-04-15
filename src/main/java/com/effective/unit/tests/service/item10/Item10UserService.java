package com.effective.unit.tests.service.item10;

import com.effective.unit.tests.domain.CreateUserProfileRequest;
import com.effective.unit.tests.domain.CreateUserProfileResponse;
import com.effective.unit.tests.domain.UserEntity;
import com.effective.unit.tests.domain.enums.EventType;
import com.effective.unit.tests.repositories.UserRepository;
import com.effective.unit.tests.service.EmployeeNumberGenerator;
import com.effective.unit.tests.service.EventBroadcaster;
import com.effective.unit.tests.service.MarketingEmailService;

import java.util.UUID;

public class Item10UserService {

    private MarketingEmailService marketingEmailService;
    private UserRepository userRepository;
    private EventBroadcaster eventBroadcaster;
    private EmployeeNumberGenerator employeeNumberGenerator;

    public CreateUserProfileResponse create(CreateUserProfileRequest createUserProfileRequest) {
        validate(createUserProfileRequest);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID().toString());
        userEntity.setRegion(createUserProfileRequest.getRegion());
        userEntity.setEmailAddress(createUserProfileRequest.getEmailAddress());
        userEntity.setEmployeeNumber(employeeNumberGenerator.generate(createUserProfileRequest.getRegion()));
        userEntity.setEmployeeNumber(employeeNumberGenerator.generate(createUserProfileRequest.getRegion()));
        UserEntity createdUserEntity = userRepository.save(userEntity);

        eventBroadcaster.broadcast(EventType.USER_REGISTRATION, createdUserEntity);
        if(createUserProfileRequest.shouldReceiveMarketingEmails()) {
            marketingEmailService.registerSubscriber(createdUserEntity);
        }

        CreateUserProfileResponse createUserProfileResponse = new CreateUserProfileResponse();
        createUserProfileResponse.setId(createdUserEntity.getId());
        createUserProfileResponse.setEmployeeNumber(createdUserEntity.getEmployeeNumber());
        createUserProfileResponse.setEmailAddress(createdUserEntity.getEmailAddress());
        createUserProfileResponse.setRegion(createdUserEntity.getRegion());
        return createUserProfileResponse;
    }

    private void validate(CreateUserProfileRequest createUserProfileRequest) {
        if(createUserProfileRequest.getRegion() == null || createUserProfileRequest.getRegion().length() > 30) {
            throw new RuntimeException("Region must be less than 30 characters in length");
        }
    }
}
