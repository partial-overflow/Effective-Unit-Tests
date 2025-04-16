package net.theresnolimits.partialoverflow.effective.unit.tests.item5;

import net.theresnolimits.partialoverflow.effective.unit.tests.domain.CreateUserProfileRequest;
import net.theresnolimits.partialoverflow.effective.unit.tests.domain.UserEntity;

import java.util.UUID;

public class TestDataCreator {

    public static final String UUID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
    public static final String TEST_EMPLOYEE_NUMBER = "EUS1002";
    public static final String TEST_REGION = "US-EAST";
    public static final String TEST_EMAIL_ADDRESS = "TestEmailAddress@test.com";
    public static final String TEST_UUID = UUID.randomUUID().toString();

    public static CreateUserProfileRequest createTestCreateUserProfileRequest() {
        CreateUserProfileRequest createUserProfileRequest = new CreateUserProfileRequest();
        createUserProfileRequest.setEmailAddress(TEST_EMAIL_ADDRESS);
        createUserProfileRequest.setRegion(TEST_REGION);
        createUserProfileRequest.shouldReceiveMarketingEmails(true);
        return createUserProfileRequest;
    }

    public static UserEntity createTestUserEntity() {
        UserEntity mockSavedUserEntity = new UserEntity();
        mockSavedUserEntity.setEmployeeNumber(TEST_EMPLOYEE_NUMBER);
        mockSavedUserEntity.setRegion(TEST_REGION);
        mockSavedUserEntity.setEmailAddress(TEST_EMAIL_ADDRESS);
        mockSavedUserEntity.setId(TEST_UUID);
        return mockSavedUserEntity;
    }
}
