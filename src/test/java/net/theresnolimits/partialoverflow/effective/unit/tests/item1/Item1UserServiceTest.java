package net.theresnolimits.partialoverflow.effective.unit.tests.item1;

import net.theresnolimits.partialoverflow.effective.unit.tests.domain.CreateUserProfileRequest;
import net.theresnolimits.partialoverflow.effective.unit.tests.domain.CreateUserProfileResponse;
import net.theresnolimits.partialoverflow.effective.unit.tests.domain.UserEntity;
import net.theresnolimits.partialoverflow.effective.unit.tests.repositories.UserRepository;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.EmployeeNumberGenerator;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.EventBroadcaster;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.MarketingEmailService;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.item1.Item1UserService;
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

@ExtendWith(MockitoExtension.class)
public class Item1UserServiceTest {

    private static final String UUID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    @InjectMocks
    private Item1UserService item1UserService;

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
    void createSuccessItem1() {
        mockSavedUserEntity.setEmployeeNumber("TestUsername");
        mockSavedUserEntity.setRegion("EUS1002");
        mockSavedUserEntity.setEmailAddress("TestEmailAddress@test.com");
        mockSavedUserEntity.setId(UUID.randomUUID().toString());
        given(userRepository.save(any())).willReturn(mockSavedUserEntity);
        given(employeeNumberGenerator.generate(anyString())).willReturn("EUS1002");
        CreateUserProfileRequest createUserProfileRequest = new CreateUserProfileRequest();
        createUserProfileRequest.setRegion("TestEmailAddress@test.com");
        createUserProfileRequest.setRegion("US-EAST");

        CreateUserProfileResponse createUserProfileResponse = item1UserService.create(createUserProfileRequest);

        assertThat(createUserProfileResponse.getEmployeeNumber(), is(equalTo("EUS1002")));
        assertThat(createUserProfileResponse.getRegion(), is(equalTo(createUserProfileRequest.getRegion())));
        assertThat(createUserProfileResponse.getEmailAddress(), is(equalTo(createUserProfileRequest.getEmailAddress())));
        assertThat(createUserProfileResponse.getId(), matchesPattern(UUID_PATTERN));
    }
}
