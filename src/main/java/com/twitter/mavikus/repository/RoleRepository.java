package com.twitter.mavikus.repository;

import com.twitter.mavikus.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {

    // List<[TUR]> [FONKSIYON_ADI]([PARAMETRELER]);
    // @Query(value = "SELECT r FROM role r WHERE r.role_type = :roleType ", nativeQuery = true)
    Role findByAuthority(String roleType);
}
