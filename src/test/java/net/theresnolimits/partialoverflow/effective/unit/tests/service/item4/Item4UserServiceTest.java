package net.theresnolimits.partialoverflow.effective.unit.tests.service.item4;

import net.theresnolimits.partialoverflow.effective.unit.tests.domain.CreateUserProfileRequest;
import net.theresnolimits.partialoverflow.effective.unit.tests.domain.CreateUserProfileResponse;
import net.theresnolimits.partialoverflow.effective.unit.tests.domain.UserEntity;
import net.theresnolimits.partialoverflow.effective.unit.tests.domain.enums.EventType;
import net.theresnolimits.partialoverflow.effective.unit.tests.repositories.UserRepository;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.EmployeeNumberGenerator;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.EventBroadcaster;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.MarketingEmailService;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.item4.Item4UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class Item4UserServiceTest {

    private static final String UUID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
    private static final String TEST_EMPLOYEE_NUMBER = "EUS1002";
    private static final String TEST_REGION = "US-EAST";
    private static final String TEST_EMAIL_ADDRESS = "TestEmailAddress@test.com";
    private static final String TEST_UUID = UUID.randomUUID().toString();

    @InjectMocks
    private Item4UserService item4UserService;

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

    private UserEntity mockSavedUserEntity = new UserEntity();

    @Test
    void createSuccessItem4aBadTest() {
        mockSavedUserEntity.setEmployeeNumber(TEST_EMPLOYEE_NUMBER);
        mockSavedUserEntity.setEmailAddress(TEST_EMAIL_ADDRESS);
        mockSavedUserEntity.setRegion(TEST_REGION);
        mockSavedUserEntity.setId(TEST_UUID);
        given(userRepository.save(userEntityArgumentCaptor.capture())).willReturn(mockSavedUserEntity);
        given(employeeNumberGenerator.generate(TEST_REGION)).willReturn(TEST_EMPLOYEE_NUMBER);
        CreateUserProfileRequest createUserProfileRequest = new CreateUserProfileRequest();
        createUserProfileRequest.setRegion(TEST_EMAIL_ADDRESS);
        createUserProfileRequest.setRegion(TEST_REGION);
        createUserProfileRequest.shouldReceiveMarketingEmails(true);

        CreateUserProfileResponse createUserProfileResponse = item4UserService.create(createUserProfileRequest);

        assertThat(createUserProfileResponse.getEmployeeNumber(), is(equalTo("EUS1002")));
        assertThat(createUserProfileResponse.getRegion(), is(equalTo(createUserProfileRequest.getRegion())));
        assertThat(createUserProfileResponse.getEmailAddress(), is(equalTo(createUserProfileRequest.getEmailAddress())));
        assertThat(createUserProfileResponse.getId(), matchesPattern(UUID_PATTERN));

        assertThat(userEntityArgumentCaptor.getValue().getEmployeeNumber(), is("EUS1002"));
        assertThat(userEntityArgumentCaptor.getValue().getEmailAddress(), is(createUserProfileRequest.getEmailAddress()));
        assertThat(userEntityArgumentCaptor.getValue().getRegion(), is(equalTo(createUserProfileRequest.getRegion())));
        assertThat(userEntityArgumentCaptor.getValue().getId(), matchesPattern(UUID_PATTERN));

        then(eventBroadcaster).should().broadcast(EventType.USER_REGISTRATION, mockSavedUserEntity);
        then(emailService).should().registerSubscriber(mockSavedUserEntity);
    }

    @Test
    void createSuccessItem4bTestFix() {
        mockSavedUserEntity.setEmployeeNumber(TEST_EMPLOYEE_NUMBER);
        mockSavedUserEntity.setRegion(TEST_REGION);
        mockSavedUserEntity.setEmailAddress(TEST_EMAIL_ADDRESS);
        mockSavedUserEntity.setId(TEST_UUID);
        given(userRepository.save(userEntityArgumentCaptor.capture())).willReturn(mockSavedUserEntity);
        given(employeeNumberGenerator.generate(TEST_REGION)).willReturn(TEST_EMPLOYEE_NUMBER);
        CreateUserProfileRequest createUserProfileRequest = new CreateUserProfileRequest();
        createUserProfileRequest.setRegion(TEST_EMAIL_ADDRESS);
        createUserProfileRequest.setRegion(TEST_REGION);
        createUserProfileRequest.shouldReceiveMarketingEmails(true);

        CreateUserProfileResponse createUserProfileResponse = item4UserService.create(createUserProfileRequest);

        assertThat(createUserProfileResponse.getEmployeeNumber(), is(equalTo(TEST_EMPLOYEE_NUMBER)));
        assertThat(createUserProfileResponse.getEmailAddress(), is(equalTo(TEST_EMAIL_ADDRESS)));
        assertThat(createUserProfileResponse.getRegion(), is(equalTo(TEST_REGION)));
        assertThat(createUserProfileResponse.getId(), matchesPattern(UUID_PATTERN));

        assertThat(userEntityArgumentCaptor.getValue().getEmployeeNumber(), is(equalTo(TEST_EMPLOYEE_NUMBER)));
        assertThat(userEntityArgumentCaptor.getValue().getEmailAddress(), is(equalTo(TEST_EMAIL_ADDRESS)));
        assertThat(userEntityArgumentCaptor.getValue().getRegion(), is(equalTo(TEST_REGION)));
        assertThat(userEntityArgumentCaptor.getValue().getId(), matchesPattern(UUID_PATTERN));

        then(eventBroadcaster).should().broadcast(EventType.USER_REGISTRATION, mockSavedUserEntity);
        then(emailService).should().registerSubscriber(mockSavedUserEntity);
    }

    @Test
    void createSuccessItem4cTestFix() {
        mockSavedUserEntity.setEmployeeNumber(TEST_EMPLOYEE_NUMBER);
        mockSavedUserEntity.setRegion(TEST_REGION);
        mockSavedUserEntity.setEmailAddress(TEST_EMAIL_ADDRESS);
        mockSavedUserEntity.setId(TEST_UUID);
        given(userRepository.save(userEntityArgumentCaptor.capture())).willReturn(mockSavedUserEntity);
        given(employeeNumberGenerator.generate(TEST_REGION)).willReturn(TEST_EMPLOYEE_NUMBER);
        CreateUserProfileRequest createUserProfileRequest = new CreateUserProfileRequest();
        createUserProfileRequest.setEmailAddress(TEST_EMAIL_ADDRESS);
        createUserProfileRequest.setRegion(TEST_REGION);
        createUserProfileRequest.shouldReceiveMarketingEmails(true);

        CreateUserProfileResponse createUserProfileResponse = item4UserService.create(createUserProfileRequest);

        assertThat(createUserProfileResponse.getEmployeeNumber(), is(equalTo(TEST_EMPLOYEE_NUMBER)));
        assertThat(createUserProfileResponse.getEmailAddress(), is(equalTo(TEST_EMAIL_ADDRESS)));
        assertThat(createUserProfileResponse.getRegion(), is(equalTo(TEST_REGION)));
        assertThat(createUserProfileResponse.getId(), matchesPattern(UUID_PATTERN));

        assertThat(userEntityArgumentCaptor.getValue().getEmployeeNumber(), is(equalTo(TEST_EMPLOYEE_NUMBER)));
        assertThat(userEntityArgumentCaptor.getValue().getEmailAddress(), is(equalTo(TEST_EMAIL_ADDRESS)));
        assertThat(userEntityArgumentCaptor.getValue().getRegion(), is(equalTo(TEST_REGION)));
        assertThat(userEntityArgumentCaptor.getValue().getId(), matchesPattern(UUID_PATTERN));

        then(eventBroadcaster).should().broadcast(EventType.USER_REGISTRATION, mockSavedUserEntity);
        then(emailService).should().registerSubscriber(mockSavedUserEntity);
    }

    @Test
    void createSuccessItem4dTestFix() {
        mockSavedUserEntity.setEmployeeNumber(TEST_EMPLOYEE_NUMBER);
        mockSavedUserEntity.setRegion(TEST_REGION);
        mockSavedUserEntity.setEmailAddress(TEST_EMAIL_ADDRESS);
        mockSavedUserEntity.setId(TEST_UUID);
        given(userRepository.save(userEntityArgumentCaptor.capture())).willReturn(mockSavedUserEntity);
        given(employeeNumberGenerator.generate(TEST_REGION)).willReturn(TEST_EMPLOYEE_NUMBER);
        CreateUserProfileRequest createUserProfileRequest = new CreateUserProfileRequest();
        createUserProfileRequest.setEmailAddress(TEST_EMAIL_ADDRESS);
        createUserProfileRequest.setRegion(TEST_REGION);
        createUserProfileRequest.shouldReceiveMarketingEmails(true);

        CreateUserProfileResponse createUserProfileResponse = item4UserService.create(createUserProfileRequest);

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
}
