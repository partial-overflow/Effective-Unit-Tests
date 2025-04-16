package net.theresnolimits.partialoverflow.effective.unit.tests.item3;

import net.theresnolimits.partialoverflow.effective.unit.tests.domain.CreateUserProfileRequest;
import net.theresnolimits.partialoverflow.effective.unit.tests.domain.CreateUserProfileResponse;
import net.theresnolimits.partialoverflow.effective.unit.tests.domain.UserEntity;
import net.theresnolimits.partialoverflow.effective.unit.tests.domain.enums.EventType;
import net.theresnolimits.partialoverflow.effective.unit.tests.repositories.UserRepository;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.EmployeeNumberGenerator;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.EventBroadcaster;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.MarketingEmailService;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.item3.Item3UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class Item3UserServiceTest {

    private static final String UUID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    @InjectMocks
    private Item3UserService item3UserService;

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
    void createSuccessItem3a() {
        mockSavedUserEntity.setEmployeeNumber("EUS1002");
        mockSavedUserEntity.setEmailAddress("TestEmailAddress@test.com");
        mockSavedUserEntity.setId(UUID.randomUUID().toString());
        given(userRepository.save(userEntityArgumentCaptor.capture())).willReturn(mockSavedUserEntity);
        given(employeeNumberGenerator.generate("US-EAST")).willReturn("EUS1002");
        CreateUserProfileRequest createUserProfileRequest = new CreateUserProfileRequest();
        createUserProfileRequest.setRegion("TestEmailAddress@test.com");
        createUserProfileRequest.setRegion("US-EAST");
        createUserProfileRequest.shouldReceiveMarketingEmails(true);

        CreateUserProfileResponse createUserProfileResponse = item3UserService.create(createUserProfileRequest);

        assertThat(createUserProfileResponse.getEmployeeNumber(), is(equalTo("EUS1002")));
        assertThat(createUserProfileResponse.getRegion(), is(equalTo(createUserProfileRequest.getRegion())));
        assertThat(createUserProfileResponse.getEmailAddress(), is(equalTo(createUserProfileRequest.getEmailAddress())));
        assertThat(createUserProfileResponse.getId(), matchesPattern(UUID_PATTERN));

        assertThat(userEntityArgumentCaptor.getValue().getEmployeeNumber(), is("EUS1002"));
        assertThat(userEntityArgumentCaptor.getValue().getEmailAddress(), is(createUserProfileRequest.getEmailAddress()));
        assertThat(userEntityArgumentCaptor.getValue().getRegion(), is(equalTo(createUserProfileRequest.getRegion())));
        assertThat(userEntityArgumentCaptor.getValue().getId(), matchesPattern(UUID_PATTERN));

        then(eventBroadcaster).should().broadcast(EventType.USER_REGISTRATION, mockSavedUserEntity);
    }

    @Test
    void createSuccessItem3b() {
        final AtomicReference<UserEntity> passedInUserEntity = new AtomicReference<>();
        given(userRepository.save(userEntityArgumentCaptor.capture())).willAnswer(invocationOnMock -> {
            passedInUserEntity.set((UserEntity) invocationOnMock.getArguments()[0]);
            return passedInUserEntity.get();
        });
        given(employeeNumberGenerator.generate("US-EAST")).willReturn("EUS1002");
        CreateUserProfileRequest createUserProfileRequest = new CreateUserProfileRequest();
        createUserProfileRequest.setRegion("TestEmailAddress@test.com");
        createUserProfileRequest.setRegion("US-EAST");
        createUserProfileRequest.shouldReceiveMarketingEmails(true);

        CreateUserProfileResponse createUserProfileResponse = item3UserService.create(createUserProfileRequest);

        assertThat(createUserProfileResponse.getEmployeeNumber(), is(equalTo("EUS1002")));
        assertThat(createUserProfileResponse.getRegion(), is(equalTo(createUserProfileRequest.getRegion())));
        assertThat(createUserProfileResponse.getEmailAddress(), is(equalTo(createUserProfileRequest.getEmailAddress())));
        assertThat(createUserProfileResponse.getId(), matchesPattern(UUID_PATTERN));

        assertThat(userEntityArgumentCaptor.getValue().getEmployeeNumber(), is("EUS1002"));
        assertThat(userEntityArgumentCaptor.getValue().getEmailAddress(), is(createUserProfileRequest.getEmailAddress()));
        assertThat(userEntityArgumentCaptor.getValue().getRegion(), is(equalTo(createUserProfileRequest.getRegion())));
        assertThat(userEntityArgumentCaptor.getValue().getId(), matchesPattern(UUID_PATTERN));

        then(eventBroadcaster).should().broadcast(EventType.USER_REGISTRATION, passedInUserEntity.get());
        then(emailService).should().registerSubscriber(passedInUserEntity.get());
    }

    @Test
    void createSuccessItem3c() {
        mockSavedUserEntity.setEmployeeNumber("EUS1002");
        mockSavedUserEntity.setEmailAddress("TestEmailAddress@test.com");
        mockSavedUserEntity.setId(UUID.randomUUID().toString());
        given(userRepository.save(userEntityArgumentCaptor.capture())).willReturn(mockSavedUserEntity);
        given(employeeNumberGenerator.generate("US-EAST")).willReturn("EUS1002");
        CreateUserProfileRequest createUserProfileRequest = new CreateUserProfileRequest();
        createUserProfileRequest.setRegion("TestEmailAddress@test.com");
        createUserProfileRequest.setRegion("US-EAST");
        createUserProfileRequest.shouldReceiveMarketingEmails(true);

        CreateUserProfileResponse createUserProfileResponse = item3UserService.create(createUserProfileRequest);

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
}
