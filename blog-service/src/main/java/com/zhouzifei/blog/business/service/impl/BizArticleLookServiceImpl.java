package com.zhouzifei.blog.business.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.zhouzifei.blog.business.entity.ArticleLook;
import com.zhouzifei.blog.business.service.BizArticleLookService;
import com.zhouzifei.blog.persistence.mapper.BizArticleLookMapper;

/**
 * 文章浏览记录
 * @author Dabao (17611555590@163.com)
 * @version 1.0
 * @website https://www.zhouzifei.com
 * @date 2019年7月16日
 * @since 1.0
 */
@Service
public class BizArticleLookServiceImpl implements BizArticleLookService {

    @Autowired
    private BizArticleLookMapper bizArticleLookMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleLook insert(ArticleLook entity) {
        Assert.notNull(entity, "ArticleLook不可为空！");
        entity.setUpdateTime(new Date());
        entity.setCreateTime(new Date());
        bizArticleLookMapper.insertSelective(entity.getBizArticleLook());
        return entity;
    }
}
