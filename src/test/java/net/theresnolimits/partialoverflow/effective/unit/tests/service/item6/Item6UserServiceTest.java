package net.theresnolimits.partialoverflow.effective.unit.tests.service.item6;

import net.theresnolimits.partialoverflow.effective.unit.tests.domain.CreateUserProfileRequest;
import net.theresnolimits.partialoverflow.effective.unit.tests.domain.CreateUserProfileResponse;
import net.theresnolimits.partialoverflow.effective.unit.tests.domain.UserEntity;
import net.theresnolimits.partialoverflow.effective.unit.tests.domain.enums.EventType;
import net.theresnolimits.partialoverflow.effective.unit.tests.repositories.UserRepository;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.EmployeeNumberGenerator;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.EventBroadcaster;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.MarketingEmailService;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.item5.Item5UserService;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.item5.TestDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static net.theresnolimits.partialoverflow.effective.unit.tests.service.item5.TestDataCreator.TEST_EMAIL_ADDRESS;
import static net.theresnolimits.partialoverflow.effective.unit.tests.service.item5.TestDataCreator.TEST_EMPLOYEE_NUMBER;
import static net.theresnolimits.partialoverflow.effective.unit.tests.service.item5.TestDataCreator.TEST_REGION;
import static net.theresnolimits.partialoverflow.effective.unit.tests.service.item5.TestDataCreator.TEST_UUID;
import static net.theresnolimits.partialoverflow.effective.unit.tests.service.item5.TestDataCreator.UUID_PATTERN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class Item6UserServiceTest {

    @InjectMocks
    private Item5UserService item5UserService;

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
    void createSuccessItem6() {
        given(userRepository.save(userEntityArgumentCaptor.capture())).willReturn(mockSavedUserEntity);
        given(employeeNumberGenerator.generate(TEST_REGION)).willReturn(TEST_EMPLOYEE_NUMBER);

        CreateUserProfileResponse createUserProfileResponse = item5UserService.create(createUserProfileRequest);

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
    void createSuccessWithShouldReceiveMarketingEmailsFalseItem6() {
        given(userRepository.save(userEntityArgumentCaptor.capture())).willReturn(mockSavedUserEntity);
        given(employeeNumberGenerator.generate(TEST_REGION)).willReturn(TEST_EMPLOYEE_NUMBER);
        createUserProfileRequest.shouldReceiveMarketingEmails(false);

        CreateUserProfileResponse createUserProfileResponse = item5UserService.create(createUserProfileRequest);

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
    void createWithNullRegionItem6() {
        createUserProfileRequest.setRegion(null);

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> item5UserService.create(createUserProfileRequest));

        assertThat(thrownException.getMessage(), is(equalTo("Region must be less than 30 characters in length")));
        then(employeeNumberGenerator).shouldHaveNoInteractions();
        then(userRepository).shouldHaveNoInteractions();
        then(emailService).shouldHaveNoInteractions();
        then(eventBroadcaster).shouldHaveNoInteractions();
    }

    @Test
    void createWith31CharacterRegionItem6() {
        createUserProfileRequest.setRegion("ThisRegionIs31CharactersLongThi");

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> item5UserService.create(createUserProfileRequest));

        assertThat(thrownException.getMessage(), is(equalTo("Region must be less than 30 characters in length")));
        then(employeeNumberGenerator).shouldHaveNoInteractions();
        then(userRepository).shouldHaveNoInteractions();
        then(emailService).shouldHaveNoInteractions();
        then(eventBroadcaster).shouldHaveNoInteractions();
    }
}
