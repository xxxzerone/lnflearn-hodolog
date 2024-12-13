package com.hodolog.repository;

import com.hodolog.domain.Session;
import com.hodolog.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findByAccessToken(String accessToken);

}
