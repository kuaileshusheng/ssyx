package com.atguigu.ssyx.acl.service.impl;

import com.atguigu.ssyx.acl.mapper.PermissionMapper;
import com.atguigu.ssyx.acl.service.PermissionService;
import com.atguigu.ssyx.model.acl.Permission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    //查询所有菜单
    @Override
    public List<Permission> queryAllPermission() {
        //1.查询所有菜单
        List<Permission> allPermissionList = baseMapper.selectList(null);
        //2.转换要求数据格式

        return null;
    }

    //递归删除菜单
    @Override
    public boolean removeChildById(Long id) {
        List<Long> idList = new ArrayList<>();
        //根据当前id查询所有字id并封装到idList中
        this.selectChildListById(id,idList);
        baseMapper.deleteBatchIds(idList);
        return true;


    }


    //重点是找出菜单下的所有子菜单
    //第一个参数是当前菜单id
    //第二个参数是最终封装的idList
    private void selectChildListById(Long id, List<Long> idList) {
        //根据当前菜单id 查询下面字菜单
        //select * from permission where pid = 2
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Permission::getPid,id);
        List<Permission> childList = baseMapper.selectList(queryWrapper);

        //递归查询是否还有子菜单，用继续查询
        childList.stream().forEach(item->{
            idList.add(item.getId());
            this.selectChildListById(item.getId(),idList);

        });
    }
}
