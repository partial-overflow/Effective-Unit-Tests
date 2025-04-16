package com.effective.unit.tests.domain;

public class CreateUserProfileRequest {
    private String region;
    private String emailAddress;
    private boolean shouldReceiveMarketingEmails;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean shouldReceiveMarketingEmails() {
        return shouldReceiveMarketingEmails;
    }

    public void shouldReceiveMarketingEmails(boolean shouldReceiveMarketingEmails) {
        this.shouldReceiveMarketingEmails = shouldReceiveMarketingEmails;
    }
}
