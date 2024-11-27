package com.lephuduy.laptopshop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.lephuduy.laptopshop.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User save(User lephuduy); // lưu user lephuduy
}
