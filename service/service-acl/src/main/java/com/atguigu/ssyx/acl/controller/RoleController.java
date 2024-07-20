package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags ="角色管理")
@RestController
@RequestMapping("/admin/acl/role")
@CrossOrigin
public class RoleController {
    @Autowired
    private RoleService roleService;

    //1 角色列表（条件分页查询）
    @ApiOperation("获取角色分页列表")
    @GetMapping("{page}/{limit}")
    public Result pageList(@PathVariable Long page, @PathVariable Long limit, RoleQueryVo roleQueryVo){
        Page<Role> pageParam = new Page<>(page,limit);
        IPage<Role> pageModel = roleService.selectRolePage(pageParam,roleQueryVo);
        return Result.ok(pageModel);
    }
    //2 根据id查询角色
    @ApiOperation("获取角色")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        Role role = roleService.getById(id);
        return Result.ok(role);
    }
    //3 添加角色
    @ApiOperation("新增角色")
    @PostMapping("save")
    public Result save(@RequestBody Role role){
        roleService.save(role);
        return Result.ok(null);
    }
    //4 修改角色
    @ApiOperation("修改角色")
    @PutMapping("update")
    public Result updateById(@RequestBody Role role){
        roleService.updateById(role);
        return Result.ok(null);
    }

    //5 根据id删除角色
    @ApiOperation("根据id删除角色")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        roleService.removeById(id);
        return Result.ok(null);
    }


    //6 批量删除角色
    @ApiOperation("批量删除多个角色")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList){
        roleService.removeByIds(idList);
        return Result.ok(null);
    }

}
