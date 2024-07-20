package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.acl.service.AdminRoleService;
import com.atguigu.ssyx.acl.service.AdminService;
import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.common.utils.MD5;
import com.atguigu.ssyx.model.acl.Admin;
import com.atguigu.ssyx.model.acl.AdminRole;
import com.atguigu.ssyx.vo.acl.AdminQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/acl/user")
@CrossOrigin
public class AdminController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRoleService adminRoleService;
    @ApiOperation("获取管理用户分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page, @PathVariable Long limit , AdminQueryVo adminQueryVo){
        Page<Admin> pageParam = new Page<>(page,limit);
        IPage<Admin> pageModel = adminService.selectPage(pageParam,adminQueryVo);
        return Result.ok(pageModel);
    }
    @ApiOperation("获取管理用户")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id){
        Admin admin = adminService.getById(id);
        return Result.ok(admin);
    }

    @ApiOperation("新增管理用户")
    @PostMapping("save")
    public Result save(@RequestBody Admin user){

        //给密码加密
        String password = user.getPassword();
        String passwordMD5 = MD5.encrypt(password);
        user.setPassword(passwordMD5);

        adminService.save(user);
        return Result.ok(null);
    }

    @ApiOperation("修改管理用户")
    @PutMapping("update")
    public Result update(@RequestBody Admin user){
        adminService.updateById(user);
        return Result.ok(null);
    }

    @ApiOperation("删除管理用户")
    @DeleteMapping("remove/{id}")
    public Result  remove(@RequestBody Long id){
        adminService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation("根据id列表删除用户")
    @DeleteMapping("batchRemove/{ids}")
    public Result batchRemove(@PathVariable List<Long> ids){
        adminService.removeByIds(ids);
        return Result.ok(null);
    }

    @ApiOperation("根据用户获取角色数据")
    @GetMapping("/toAssign/{adminId}")
    public Result toAssign(@PathVariable Long adminId){
        Map<String,Object> roleMap = roleService.findRoleByUserId(adminId);
        return Result.ok(roleMap);
    }

    @ApiOperation("根据用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestParam Long adminId,@RequestParam Long[] roleId){
        roleService.saveUserRoleRealitionShip(adminId,roleId);
        return Result.ok(null);
    }

}
