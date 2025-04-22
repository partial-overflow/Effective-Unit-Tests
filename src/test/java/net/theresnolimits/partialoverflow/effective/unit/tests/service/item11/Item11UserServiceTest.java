package com.effective.unit.tests.service.item11;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.effective.unit.tests.service.item5.TestDataCreator.TEST_EMAIL_ADDRESS;
import static com.effective.unit.tests.service.item5.TestDataCreator.TEST_EMPLOYEE_NUMBER;
import static com.effective.unit.tests.service.item5.TestDataCreator.TEST_REGION;
import static com.effective.unit.tests.service.item5.TestDataCreator.TEST_UUID;
import static com.effective.unit.tests.service.item5.TestDataCreator.UUID_PATTERN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class Item11UserServiceTest {

	@InjectMocks
	private Item11UserService item11UserService;

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
	@DisplayName("Create user profile signed up to email marketing")
	void createSuccess() {
		given(userRepository.save(userEntityArgumentCaptor.capture())).willReturn(mockSavedUserEntity);
		given(employeeNumberGenerator.generate(TEST_REGION)).willReturn(TEST_EMPLOYEE_NUMBER);

		CreateUserProfileResponse createUserProfileResponse = item11UserService.create(createUserProfileRequest);

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
	@DisplayName("Create user profile not signed up to email marketing")
	void createSuccessWithShouldReceiveMarketingEmailsFalse() {
		given(userRepository.save(userEntityArgumentCaptor.capture())).willReturn(mockSavedUserEntity);
		given(employeeNumberGenerator.generate(TEST_REGION)).willReturn(TEST_EMPLOYEE_NUMBER);
		createUserProfileRequest.shouldReceiveMarketingEmails(false);

		CreateUserProfileResponse createUserProfileResponse = item11UserService.create(createUserProfileRequest);

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

	@ParameterizedTest
	@MethodSource("invalidRegionsA")
	void createWithInvalidRegionsTestA(String region) {
		createUserProfileRequest.setRegion(region);

		RuntimeException thrownException = assertThrows(RuntimeException.class, () -> item11UserService.create(createUserProfileRequest));

		assertThat(thrownException.getMessage(), is(equalTo("Region must be less than 30 characters in length")));
		then(employeeNumberGenerator).shouldHaveNoInteractions();
		then(userRepository).shouldHaveNoInteractions();
		then(emailService).shouldHaveNoInteractions();
		then(eventBroadcaster).shouldHaveNoInteractions();
	}

	private static Stream<Arguments> invalidRegionsA() {
		return Stream.of(
				arguments(new Object[]{null}),
				arguments("ThisRegionIs31CharactersLongThi")
		);
	}

	@ParameterizedTest(name = "Scenario: {1}")
	@MethodSource("invalidRegionsB")
	void createWithInvalidRegionsTestB(String fieldValue, String testDisplayName) {
		createUserProfileRequest.setRegion(fieldValue);

		RuntimeException thrownException = assertThrows(RuntimeException.class, () -> item11UserService.create(createUserProfileRequest));

		assertThat(thrownException.getMessage(), is(equalTo("Region must be less than 30 characters in length")));
		then(employeeNumberGenerator).shouldHaveNoInteractions();
		then(userRepository).shouldHaveNoInteractions();
		then(emailService).shouldHaveNoInteractions();
		then(eventBroadcaster).shouldHaveNoInteractions();
	}

	private static Stream<Arguments> invalidRegionsB() {
		return Stream.of(
				arguments(null, "Region is null"),
				arguments("ThisRegionIs31CharactersLongThi", "Region has 31 characters")
		);
	}
}
