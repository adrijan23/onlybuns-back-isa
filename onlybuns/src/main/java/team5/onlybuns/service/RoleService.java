package team5.onlybuns.service;

import java.util.List;

import team5.onlybuns.model.Role;

public interface RoleService {
	Role findById(Long id);
	List<Role> findByName(String name);
}
