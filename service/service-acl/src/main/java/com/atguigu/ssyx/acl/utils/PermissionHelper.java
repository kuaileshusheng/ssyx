package com.atguigu.ssyx.acl.utils;

import com.atguigu.ssyx.model.acl.Permission;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {
    public static List<Permission> buildPermission(List<Permission> allList ){
        List<Permission> trees  = new ArrayList<>();
        for(Permission permission:allList){
            //找到第一层
            if(permission.getPid()==0){
                permission.setLevel(1);
                //调用方法,从第一层开始往下找
            }
        }
        return null;
    }
}
