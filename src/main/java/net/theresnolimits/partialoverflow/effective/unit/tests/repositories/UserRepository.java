package net.theresnolimits.partialoverflow.effective.unit.tests.repositories;

import net.theresnolimits.partialoverflow.effective.unit.tests.domain.UserEntity;

public interface UserRepository {
    UserEntity save(UserEntity userEntity);
    UserEntity save2(UserEntity userEntity);
}
