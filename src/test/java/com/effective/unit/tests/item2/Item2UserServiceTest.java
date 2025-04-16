package com.effective.unit.tests.service.item2;

import com.effective.unit.tests.domain.CreateUserProfileRequest;
import com.effective.unit.tests.domain.CreateUserProfileResponse;
import com.effective.unit.tests.domain.UserEntity;
import com.effective.unit.tests.domain.enums.EventType;
import com.effective.unit.tests.repositories.UserRepository;
import com.effective.unit.tests.service.EmployeeNumberGenerator;
import com.effective.unit.tests.service.EventBroadcaster;
import com.effective.unit.tests.service.MarketingEmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class Item2UserServiceTest {

    private static final String UUID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    @InjectMocks
    private Item2UserService item2UserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventBroadcaster eventBroadcaster;

    @Mock
    private MarketingEmailService emailService;

    @Mock
    private EmployeeNumberGenerator employeeNumberGenerator;

    private UserEntity mockSavedUserEntity = new UserEntity();

    @Test
    void createSuccessItem2Broken() {
        mockSavedUserEntity.setEmployeeNumber("EUS1002");
        mockSavedUserEntity.setEmailAddress("TestEmailAddress@test.com");
        mockSavedUserEntity.setRegion("US-EAST");
        mockSavedUserEntity.setId(UUID.randomUUID().toString());
        given(userRepository.save(any())).willReturn(mockSavedUserEntity);
        given(employeeNumberGenerator.generate(anyString())).willReturn("EUS1002");
        CreateUserProfileRequest createUserProfileRequest = new CreateUserProfileRequest();
        createUserProfileRequest.setRegion("TestEmailAddress@test.com");
        createUserProfileRequest.setRegion("US-EAST");
        createUserProfileRequest.shouldReceiveMarketingEmails(true);

        CreateUserProfileResponse createUserProfileResponse = item2UserService.create(createUserProfileRequest);

        assertThat(createUserProfileResponse.getEmployeeNumber(), is(equalTo("EUS1002")));
        assertThat(createUserProfileResponse.getRegion(), is(equalTo(createUserProfileRequest.getRegion())));
        assertThat(createUserProfileResponse.getEmailAddress(), is(equalTo(createUserProfileRequest.getEmailAddress())));
        assertThat(createUserProfileResponse.getId(), matchesPattern(UUID_PATTERN));

        then(eventBroadcaster).should().broadcast(EventType.USER_REGISTRATION, mockSavedUserEntity);
    }

    @Test
    void createSuccessItem2Fixed() {
        mockSavedUserEntity.setEmployeeNumber("EUS1002");
        mockSavedUserEntity.setEmailAddress("TestEmailAddress@test.com");
        mockSavedUserEntity.setRegion("US-EAST");
        mockSavedUserEntity.setId(UUID.randomUUID().toString());
        given(userRepository.save(any())).willReturn(mockSavedUserEntity);
        given(employeeNumberGenerator.generate("US-EAST")).willReturn("EUS1002");
        CreateUserProfileRequest createUserProfileRequest = new CreateUserProfileRequest();
        createUserProfileRequest.setRegion("TestEmailAddress@test.com");
        createUserProfileRequest.setRegion("US-EAST");
        createUserProfileRequest.shouldReceiveMarketingEmails(true);

        CreateUserProfileResponse createUserProfileResponse = item2UserService.create(createUserProfileRequest);

        assertThat(createUserProfileResponse.getEmployeeNumber(), is(equalTo("EUS1002")));
        assertThat(createUserProfileResponse.getRegion(), is(equalTo(createUserProfileRequest.getRegion())));
        assertThat(createUserProfileResponse.getEmailAddress(), is(equalTo(createUserProfileRequest.getEmailAddress())));
        assertThat(createUserProfileResponse.getId(), matchesPattern(UUID_PATTERN));

        then(eventBroadcaster).should().broadcast(EventType.USER_REGISTRATION, mockSavedUserEntity);
    }
}
