package com.shobhit.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shobhit.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
}