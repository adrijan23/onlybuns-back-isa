package team5.onlybuns.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import team5.onlybuns.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	List<Role> findByName(String name);
}
