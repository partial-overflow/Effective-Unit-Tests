package com.effective.unit.tests.repositories;

import com.effective.unit.tests.domain.UserEntity;

public interface UserRepository {
    UserEntity save(UserEntity userEntity);
    UserEntity save2(UserEntity userEntity);
}
