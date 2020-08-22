package com.project.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.project.app.domain.SUserRole;


@RepositoryRestResource
public interface SUserRoleDao extends JpaRepository<SUserRole, Integer>, UserRoleDaoCustom{


}

