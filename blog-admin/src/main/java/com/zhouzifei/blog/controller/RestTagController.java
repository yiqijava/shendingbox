package com.zhouzifei.blog.controller;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.zhouzifei.blog.business.annotation.BussinessLog;
import com.zhouzifei.blog.business.entity.Tags;
import com.zhouzifei.blog.business.enums.ResponseStatus;
import com.zhouzifei.blog.business.service.BizTagsService;
import com.zhouzifei.blog.business.vo.TagsConditionVO;
import com.zhouzifei.blog.framework.object.PageResult;
import com.zhouzifei.blog.framework.object.ResponseVO;
import com.zhouzifei.blog.util.ResultUtil;

/**
 * 文章标签管理
 * @author Dabao (17611555590@163.com)
 * @version 1.0
 * @website https://www.zhouzifei.com
 * @date 2019年7月16日
 * @since 1.0
 */
@RestController
@RequestMapping("/tag")
public class RestTagController {
    @Autowired
    private BizTagsService tagsService;

    @RequiresPermissions("tags")
    @PostMapping("/list")
    public PageResult list(TagsConditionVO vo) {
        PageInfo<Tags> pageInfo = tagsService.findPageBreakByCondition(vo);
        return ResultUtil.tablePage(pageInfo);
    }

    @RequiresPermissions("tag:add")
    @PostMapping(value = "/add")
    @BussinessLog("添加标签")
    public ResponseVO add(Tags tags) {
        tags = tagsService.insert(tags);
        return ResultUtil.success("标签添加成功！新标签 - " + tags.getName(), tags);
    }

    @RequiresPermissions(value = {"tag:batchDelete", "tag:delete"}, logical = Logical.OR)
    @PostMapping(value = "/remove")
    @BussinessLog("删除标签")
    public ResponseVO remove(Long[] ids) {
        if (null == ids) {
            return ResultUtil.error(500, "请至少选择一条记录");
        }
        for (Long id : ids) {
            tagsService.removeByPrimaryKey(id);
        }
        return ResultUtil.success("成功删除 [" + ids.length + "] 个标签");
    }

    @RequiresPermissions("tag:get")
    @PostMapping("/get/{id}")
    @BussinessLog("获取标签详情")
    public ResponseVO get(@PathVariable Long id) {
        return ResultUtil.success(null, this.tagsService.getByPrimaryKey(id));
    }

    @RequiresPermissions("tag:edit")
    @PostMapping("/edit")
    @BussinessLog("编辑标签")
    public ResponseVO edit(Tags tags) {
        try {
            tagsService.updateSelective(tags);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("标签修改失败！");
        }
        return ResultUtil.success(ResponseStatus.SUCCESS);
    }

    @PostMapping("/listAll")
    public ResponseVO list() {
        return ResultUtil.success(null, tagsService.listAll());
    }

}
