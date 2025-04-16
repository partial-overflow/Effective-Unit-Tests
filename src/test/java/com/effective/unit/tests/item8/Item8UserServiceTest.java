package com.effective.unit.tests.service.item8;

import com.effective.unit.tests.domain.CreateUserProfileRequest;
import com.effective.unit.tests.domain.CreateUserProfileResponse;
import com.effective.unit.tests.domain.UserEntity;
import com.effective.unit.tests.domain.enums.EventType;
import com.effective.unit.tests.repositories.UserRepository;
import com.effective.unit.tests.service.EmployeeNumberGenerator;
import com.effective.unit.tests.service.EventBroadcaster;
import com.effective.unit.tests.service.MarketingEmailService;
import com.effective.unit.tests.service.item5.TestDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.effective.unit.tests.service.item5.TestDataCreator.TEST_EMAIL_ADDRESS;
import static com.effective.unit.tests.service.item5.TestDataCreator.TEST_EMPLOYEE_NUMBER;
import static com.effective.unit.tests.service.item5.TestDataCreator.TEST_REGION;
import static com.effective.unit.tests.service.item5.TestDataCreator.TEST_UUID;
import static com.effective.unit.tests.service.item5.TestDataCreator.UUID_PATTERN;
import static com.effective.unit.tests.service.item8.Item8UserService.INVALID_REGION_MESSAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class Item8UserServiceTest {

    @InjectMocks
    private Item8UserService item8UserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventBroadcaster eventBroadcaster;

    @Mock
    private MarketingEmailService emailService;

    @Mock
    private EmployeeNumberGenerator employeeNumberGenerator;

    @Captor
    private ArgumentCaptor<UserEntity> userEntityArgumentCaptor;

    private UserEntity mockSavedUserEntity;
    private CreateUserProfileRequest createUserProfileRequest;

    @BeforeEach
    void setup() {
        mockSavedUserEntity = TestDataCreator.createTestUserEntity();
        createUserProfileRequest = TestDataCreator.createTestCreateUserProfileRequest();
    }

    @Test
    void createSuccessItem8() {
        given(userRepository.save(userEntityArgumentCaptor.capture())).willReturn(mockSavedUserEntity);
        given(employeeNumberGenerator.generate(TEST_REGION)).willReturn(TEST_EMPLOYEE_NUMBER);

        CreateUserProfileResponse createUserProfileResponse = item8UserService.create(createUserProfileRequest);

        assertThat(createUserProfileResponse.getEmployeeNumber(), is(equalTo(TEST_EMPLOYEE_NUMBER)));
        assertThat(createUserProfileResponse.getEmailAddress(), is(equalTo(TEST_EMAIL_ADDRESS)));
        assertThat(createUserProfileResponse.getRegion(), is(equalTo(TEST_REGION)));
        assertThat(createUserProfileResponse.getId(), is(equalTo(TEST_UUID)));

        assertThat(userEntityArgumentCaptor.getValue().getEmployeeNumber(), is(equalTo(TEST_EMPLOYEE_NUMBER)));
        assertThat(userEntityArgumentCaptor.getValue().getEmailAddress(), is(equalTo(TEST_EMAIL_ADDRESS)));
        assertThat(userEntityArgumentCaptor.getValue().getRegion(), is(equalTo(TEST_REGION)));
        assertThat(userEntityArgumentCaptor.getValue().getId(), matchesPattern(UUID_PATTERN));

        then(eventBroadcaster).should().broadcast(EventType.USER_REGISTRATION, mockSavedUserEntity);
        then(emailService).should().registerSubscriber(mockSavedUserEntity);
    }

    @Test
    void createSuccessWithShouldReceiveMarketingEmailsFalseItem8() {
        given(userRepository.save(userEntityArgumentCaptor.capture())).willReturn(mockSavedUserEntity);
        given(employeeNumberGenerator.generate(TEST_REGION)).willReturn(TEST_EMPLOYEE_NUMBER);
        createUserProfileRequest.shouldReceiveMarketingEmails(false);

        CreateUserProfileResponse createUserProfileResponse = item8UserService.create(createUserProfileRequest);

        assertThat(createUserProfileResponse.getEmployeeNumber(), is(equalTo(TEST_EMPLOYEE_NUMBER)));
        assertThat(createUserProfileResponse.getEmailAddress(), is(equalTo(TEST_EMAIL_ADDRESS)));
        assertThat(createUserProfileResponse.getRegion(), is(equalTo(TEST_REGION)));
        assertThat(createUserProfileResponse.getId(), is(equalTo(TEST_UUID)));

        assertThat(userEntityArgumentCaptor.getValue().getEmployeeNumber(), is(equalTo(TEST_EMPLOYEE_NUMBER)));
        assertThat(userEntityArgumentCaptor.getValue().getEmailAddress(), is(equalTo(TEST_EMAIL_ADDRESS)));
        assertThat(userEntityArgumentCaptor.getValue().getRegion(), is(equalTo(TEST_REGION)));
        assertThat(userEntityArgumentCaptor.getValue().getId(), matchesPattern(UUID_PATTERN));

        then(eventBroadcaster).should().broadcast(EventType.USER_REGISTRATION, mockSavedUserEntity);
        then(emailService).shouldHaveNoInteractions();
    }

    @Test
    void createWithNullRegionItem8() {
        createUserProfileRequest.setRegion(null);

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> item8UserService.create(createUserProfileRequest));

        assertThat(thrownException.getMessage(), is(equalTo(INVALID_REGION_MESSAGE)));
        then(employeeNumberGenerator).shouldHaveNoInteractions();
        then(userRepository).shouldHaveNoInteractions();
        then(emailService).shouldHaveNoInteractions();
        then(eventBroadcaster).shouldHaveNoInteractions();
    }

    @Test
    void createWith31CharacterRegionItem8() {
        createUserProfileRequest.setRegion("ThisRegionIs31CharactersLongThi");

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> item8UserService.create(createUserProfileRequest));

        assertThat(thrownException.getMessage(), is(equalTo(INVALID_REGION_MESSAGE)));
        then(employeeNumberGenerator).shouldHaveNoInteractions();
        then(userRepository).shouldHaveNoInteractions();
        then(emailService).shouldHaveNoInteractions();
        then(eventBroadcaster).shouldHaveNoInteractions();
    }
}
