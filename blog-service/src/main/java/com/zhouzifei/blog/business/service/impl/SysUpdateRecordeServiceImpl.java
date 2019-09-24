package com.zhouzifei.blog.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhouzifei.blog.business.entity.UpdateRecorde;
import com.zhouzifei.blog.business.service.SysUpdateRecordeService;
import com.zhouzifei.blog.business.vo.UpdateRecordeConditionVO;
import com.zhouzifei.blog.persistence.beans.SysUpdateRecorde;
import com.zhouzifei.blog.persistence.mapper.SysUpdateRecordeMapper;

/**
 * 更新记录
 * @author Dabao (17611555590@163.com)
 * @version 1.0
 * @website https://www.zhouzifei.com
 * @date 2019年7月16日
 * @since 1.0
 */
@Service
public class SysUpdateRecordeServiceImpl implements SysUpdateRecordeService{

    @Autowired
    private SysUpdateRecordeMapper sysUpdateRecordeMapper;

    @Override
    public PageInfo<UpdateRecorde>findPageBreakByCondition(UpdateRecordeConditionVO vo){
        PageHelper.startPage(vo.getPageNumber(),vo.getPageSize());
        List<SysUpdateRecorde>list=sysUpdateRecordeMapper.findPageBreakByCondition(vo);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<UpdateRecorde> boList = new ArrayList<>();
        for(SysUpdateRecorde sysUpdateRecorde : list){
            boList.add(new UpdateRecorde(sysUpdateRecorde));
        }
        PageInfo bean = new PageInfo<SysUpdateRecorde>(list);
        bean.setList(boList);
        return bean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UpdateRecorde insert(UpdateRecorde entity){
        Assert.notNull(entity, "UpdateRecorde不可为空！");
        entity.setUpdateTime(new Date());
        entity.setCreateTime(new Date());
        sysUpdateRecordeMapper.insertSelective(entity.getSysUpdateRecorde());
        return entity;
    }

    @Override
    public void insertList(List<UpdateRecorde> entities){
        Assert.notNull(entities, "UpdateRecordes不可为空！");
        List<SysUpdateRecorde> list = new ArrayList<>();
        for (UpdateRecorde entity : entities) {
            entity.setUpdateTime(new Date());
            entity.setCreateTime(new Date());
            list.add(entity.getSysUpdateRecorde());
        }
        sysUpdateRecordeMapper.insertList(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPrimaryKey(Long primaryKey){
        return sysUpdateRecordeMapper.deleteByPrimaryKey(primaryKey) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSelective(UpdateRecorde entity){
        Assert.notNull(entity, "UpdateRecorde不可为空！");
        entity.setUpdateTime(new Date());
        return sysUpdateRecordeMapper.updateByPrimaryKeySelective(entity.getSysUpdateRecorde()) > 0;
    }

    @Override
    public UpdateRecorde getByPrimaryKey(Long primaryKey){
        Assert.notNull(primaryKey, "PrimaryKey不可为空！");
        SysUpdateRecorde entity = sysUpdateRecordeMapper.selectByPrimaryKey(primaryKey);
        return null == entity ? null : new UpdateRecorde(entity);
    }

    @Override
    public List<UpdateRecorde> listAll(){
        List<SysUpdateRecorde> entityList = sysUpdateRecordeMapper.selectAll();

        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<UpdateRecorde> list = new ArrayList<>();
        for (SysUpdateRecorde entity : entityList) {
            list.add(new UpdateRecorde(entity));
        }
        return list;
    }
}
