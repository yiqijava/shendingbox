package com.zhouzifei.blog.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.zhouzifei.blog.business.entity.RoleResources;
import com.zhouzifei.blog.business.service.SysRoleResourcesService;
import com.zhouzifei.blog.framework.holder.RequestHolder;
import com.zhouzifei.blog.persistence.beans.SysRoleResources;
import com.zhouzifei.blog.persistence.mapper.SysRoleResourcesMapper;
import com.zhouzifei.blog.util.IpUtil;

import tk.mybatis.mapper.entity.Example;

/**
 * 角色资源
 * @author Dabao (17611555590@163.com)
 * @version 1.0
 * @website https://www.zhouzifei.com
 * @date 2019年7月16日
 * @since 1.0
 */
@Service
public class SysRoleResourcesServiceImpl implements SysRoleResourcesService {
    @Autowired
    private SysRoleResourcesMapper resourceMapper;

    @Override
    public RoleResources insert(RoleResources entity) {
        Assert.notNull(entity, "RoleResources不可为空！");
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        resourceMapper.insert(entity.getSysRoleResources());
        return entity;
    }

    @Override
    public void insertList(List<RoleResources> entities) {
        Assert.notNull(entities, "entities不可为空！");
        List<SysRoleResources> sysRoleResources = new ArrayList<>();
        String regIp = IpUtil.getRealIp(RequestHolder.getRequest());
        for (RoleResources RoleResources : entities) {
            RoleResources.setUpdateTime(new Date());
            RoleResources.setCreateTime(new Date());
            sysRoleResources.add(RoleResources.getSysRoleResources());
        }
        resourceMapper.insertList(sysRoleResources);
    }

    @Override
    public boolean removeByPrimaryKey(Long primaryKey) {
        return resourceMapper.deleteByPrimaryKey(primaryKey) > 0;
    }

    @Override
    public boolean updateSelective(RoleResources entity) {
        Assert.notNull(entity, "RoleResources不可为空！");
        entity.setUpdateTime(new Date());
        return resourceMapper.updateByPrimaryKeySelective(entity.getSysRoleResources()) > 0;
    }

    @Override
    public RoleResources getByPrimaryKey(Long primaryKey) {
        Assert.notNull(primaryKey, "PrimaryKey不可为空！");
        SysRoleResources sysRoleResources = resourceMapper.selectByPrimaryKey(primaryKey);
        return null == sysRoleResources ? null : new RoleResources(sysRoleResources);
    }

    @Override
    public List<RoleResources> listAll() {
        List<SysRoleResources> sysRoleResources = resourceMapper.selectAll();
        return getRoleResources(sysRoleResources);
    }

    private List<RoleResources> getRoleResources(List<SysRoleResources> sysRoleResources) {
        if (CollectionUtils.isEmpty(sysRoleResources)) {
            return null;
        }
        List<RoleResources> RoleResources = new ArrayList<>();
        for (SysRoleResources r : sysRoleResources) {
            RoleResources.add(new RoleResources(r));
        }
        return RoleResources;
    }

    /**
     * 添加角色资源
     * @param roleId
     * @param resourcesId
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void addRoleResources(Long roleId, String resourcesId) {
        //删除
        removeByRoleId(roleId);
        //添加
        if (!StringUtils.isEmpty(resourcesId)) {
            String[] resourcesArr = resourcesId.split(",");
            SysRoleResources r = null;
            List<SysRoleResources> roleResources = new ArrayList<>();
            for (String ri : resourcesArr) {
                r = new SysRoleResources();
                r.setRoleId(roleId);
                r.setResourcesId(Long.parseLong(ri));
                r.setCreateTime(new Date());
                r.setUpdateTime(new Date());
                roleResources.add(r);

            }
            resourceMapper.insertList(roleResources);
        }
    }

    /**
     * 通过角色id批量删除
     * @param roleId
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void removeByRoleId(Long roleId) {
        //删除
        Example example = new Example(SysRoleResources.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        resourceMapper.deleteByExample(example);
    }
}
