package com.atguigu.ssyx.acl.service.impl;

import com.atguigu.ssyx.acl.mapper.RoleMapper;
import com.atguigu.ssyx.acl.service.AdminRoleService;
import com.atguigu.ssyx.acl.service.AdminService;
import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.model.acl.AdminRole;
import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private AdminRoleService adminRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AdminService adminService;

    @Override
    public IPage<Role> selectRolePage(Page<Role> pageParam, RoleQueryVo roleQueryVo) {
        //获取条件值
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        String roleName = roleQueryVo.getRoleName();
        //判断条件值是否为空，为封装查询条件
        if (!StringUtils.isEmpty(roleName)) {
            wrapper.like(Role::getRoleName, roleName);
        }
        //调用方法实现分页查询
        Page<Role> pageModel = baseMapper.selectPage(pageParam, wrapper);
        //返回分页对象
        return pageModel;
    }

    @Override
    public Map<String, Object> findRoleByUserId(Long adminId) {

        //1查询所有的角色
        List<Role> allRolesList = baseMapper.selectList(null);
        //2拥有的角色id
        //2.1根据用户id从用户角色对应表中取出数据并且封装成List
        LambdaQueryWrapper<AdminRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminRole::getAdminId, adminId);
        List<AdminRole> adminRoleList = adminRoleService.list(queryWrapper);
        //2.2返回 List中的RoleId
        List<Long> roleIdsList = adminRoleList.stream()
                .map(item -> item.getRoleId())
                .collect(Collectors.toList());
        //2.3创建新的list集合，用于存储用户配置角色
        List<Role> assignRoleList = new ArrayList<>();

        //2.4遍历角色列表 allRolesList，得到每个角色
        //判断所有的角色里边是否包含已经分配的角色id,封装到2.3里面的新的list集合
        for (Role role : allRolesList) {
            if (roleIdsList.contains(role.getId())) {
                assignRoleList.add(role);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("allRolesList", allRolesList);
        result.put("assignRoles", assignRoleList);
        return result;
    }

    @Override
    public void saveUserRoleRealitionShip(Long adminId, Long[] roleId) {
        //删除用户分配的角色
        LambdaQueryWrapper<AdminRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminRole::getAdminId, adminId);
        adminRoleService.remove(queryWrapper);
        //分配角色
        List<AdminRole> adminRoles = new ArrayList<>();
        for (Long littleroleId : roleId) {
            if (StringUtils.isEmpty(littleroleId)) {
                continue;
            }
            AdminRole adminRole = new AdminRole();
            adminRole.setRoleId(littleroleId);
            adminRole.setAdminId(adminId);
            adminRoles.add(adminRole);
        }
        adminRoleService.saveBatch(adminRoles);
    }
}
