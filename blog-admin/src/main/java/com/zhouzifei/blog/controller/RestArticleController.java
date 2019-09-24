package com.zhouzifei.blog.controller;

import java.util.Map;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhouzifei.blog.business.annotation.BussinessLog;
import com.zhouzifei.blog.business.entity.Article;
import com.zhouzifei.blog.business.enums.BaiduPushTypeEnum;
import com.zhouzifei.blog.business.enums.ConfigKeyEnum;
import com.zhouzifei.blog.business.enums.ResponseStatus;
import com.zhouzifei.blog.business.service.BizArticleService;
import com.zhouzifei.blog.business.service.SysConfigService;
import com.zhouzifei.blog.business.util.BaiduPushUtil;
import com.zhouzifei.blog.business.vo.ArticleConditionVO;
import com.zhouzifei.blog.framework.object.PageResult;
import com.zhouzifei.blog.framework.object.ResponseVO;
import com.zhouzifei.blog.util.ResultUtil;
import com.zhouzifei.blog.util.UrlBuildUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 文章管理类
 * @author Dabao (17611555590@163.com)
 * @version 1.0
 * @website https://www.zhouzifei.com
 * @date 2019年7月16日
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/article")
public class RestArticleController {
    @Autowired
    private BizArticleService articleService;
    @Autowired
    private SysConfigService configService;

    @RequiresPermissions("articles")
    @PostMapping("/list")
    public PageResult list(ArticleConditionVO vo) {
        PageInfo<Article> pageInfo = articleService.findPageBreakByCondition(vo);
        return ResultUtil.tablePage(pageInfo);
    }

    @RequiresPermissions(value = {"article:batchDelete", "article:delete"}, logical = Logical.OR)
    @PostMapping(value = "/remove")
    @BussinessLog("删除文章[{1}]")
    public ResponseVO remove(Long[] ids) {
        if (null == ids) {
            return ResultUtil.error(500, "请至少选择一条记录");
        }
        for (Long id : ids) {
            articleService.removeByPrimaryKey(id);
        }
        return ResultUtil.success("成功删除 [" + ids.length + "] 篇文章");
    }

    @RequiresPermissions("article:get")
    @PostMapping("/get/{id}")
    @BussinessLog("获取文章[{1}]详情")
    public ResponseVO get(@PathVariable Long id) {
        return ResultUtil.success(null, this.articleService.getByPrimaryKey(id));
    }

    @RequiresPermissions(value = {"article:edit", "article:publish"}, logical = Logical.OR)
    @PostMapping("/save")
    @BussinessLog("发布文章")
    public ResponseVO edit(Article article, Long[] tags, MultipartFile file) {
        articleService.publish(article, tags, file);
        return ResultUtil.success(ResponseStatus.SUCCESS);
    }

    @RequiresPermissions(value = {"article:top", "article:recommend"}, logical = Logical.OR)
    @PostMapping("/update/{type}")
    @BussinessLog("修改文章[{2}]的状态[{1}]")
    public ResponseVO update(@PathVariable("type") String type, Long id) {
        articleService.updateTopOrRecommendedById(type, id);
        return ResultUtil.success(ResponseStatus.SUCCESS);
    }

    @RequiresPermissions(value = {"article:batchPush", "article:push"}, logical = Logical.OR)
    @PostMapping(value = "/pushToBaidu/{type}")
    @BussinessLog("推送文章[{2}]到百度站长平台")
    public ResponseVO pushToBaidu(@PathVariable("type") BaiduPushTypeEnum type, Long[] ids) {
        if (null == ids) {
            return ResultUtil.error(500, "请至少选择一条记录");
        }
        Map config = configService.getConfigs();
        String siteUrl = (String) config.get(ConfigKeyEnum.SITE_URL.getKey());
        StringBuilder params = new StringBuilder();
        for (Long id : ids) {
            params.append(siteUrl).append("/article/").append(id).append("\n");
        }
        // urls: 推送, update: 更新, del: 删除
        String url = UrlBuildUtil.getBaiduPushUrl(type.toString(), (String) config.get(ConfigKeyEnum.SITE_URL.getKey()), (String) config.get(ConfigKeyEnum.BAIDU_PUSH_TOKEN.getKey()));
        String result = BaiduPushUtil.doPush(url, params.toString(), (String) config.get(ConfigKeyEnum.BAIDU_PUSH_COOKIE.getKey()));
        log.info(result);
        JSONObject resultJson = JSONObject.parseObject(result);

        if (resultJson.containsKey("error")) {
            return ResultUtil.error(resultJson.getString("message"));
        }
        return ResultUtil.success(null, result);
    }

    @RequiresPermissions(value = {"article:publish"}, logical = Logical.OR)
    @PostMapping(value = "/batchPublish")
    @BussinessLog("批量发布文章[{1}]")
    public ResponseVO batchPublish(Long[] ids) {
        if (null == ids) {
            return ResultUtil.error(500, "请至少选择一条记录");
        }
        articleService.batchUpdateStatus(ids, true);
        return ResultUtil.success("批量发布完成");
    }
}
