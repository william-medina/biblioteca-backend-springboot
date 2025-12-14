package com.williammedina.biblioteca.domain.user.service.context;

import com.williammedina.biblioteca.domain.user.entity.UserEntity;

public interface AuthenticatedUserProvider {

    UserEntity getAuthenticatedUser();

}
