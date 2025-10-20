package com.mbproyect.campusconnect.infrastructure.repository.user;

import com.mbproyect.campusconnect.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
