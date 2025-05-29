package com.FarmVibe.api.Repository;

import com.FarmVibe.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
