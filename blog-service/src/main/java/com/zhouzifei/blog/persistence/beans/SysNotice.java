package com.zhouzifei.blog.persistence.beans;

import com.zhouzifei.blog.framework.object.AbstractDO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author Dabao (17611555590@163.com)
 * @version 1.0
 * @website https://www.zhouzifei.com
 * @date 2019年7月16日
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysNotice extends AbstractDO {
    private Long userId;
    private String status;
    private String title;
    private String content;
}
