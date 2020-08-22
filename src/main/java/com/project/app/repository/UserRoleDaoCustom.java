package com.project.app.repository;


import com.project.app.domain.SPermission;
import com.project.app.domain.SRole;

import java.util.List;

public interface UserRoleDaoCustom {
    public List<SRole> findByUser(Integer userId);

    public List<SRole> findByPermission(Integer permissionId);

    public List<SPermission> findPermissionByRole(Integer roleId);
}
