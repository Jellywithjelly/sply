package com.ly.vrps.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 影片
 *
 * @author sunkl
 * @date 2019年03月31日 10:45:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "t_bus_film")
public class Film extends BaseModel implements Serializable {

    private String id;

    /**
     * 演员
     */
    private String actor;

    /**
     * 目录名称
     */
    private String cataLogName;

    /**
     * 目录id
     */
    private String cataLogId;

    /**
     * 评价
     */
    private Double evaluation;

    /**
     * 海报
     */
    private String image;

    /**
     * 是否在使用
     */
    private Integer isUse;

    /**
     * 地区名称
     */
    private String locName;

    /**
     * 地区id
     */
    private String locId;

    /**
     * 影片名称
     */
    private String name;

    /**
     * 年代
     */
    private String onDecade;

    /**
     * 分辨率
     */
    private String resolution;

    /**
     * 状态
     */
    private String status;

    /**
     * 子分类名称
     */
    private String subClassName;

    /**
     * 子分类id
     */
    private String subClassId;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 类型id
     */
    private String typeId;

    /**
     * 是否vip
     */
    private Integer isVip;

    /**
     * 情节
     */
    private String plot;

    /**
     * 链接
     */
    private String link;


    /**
     * 视频格式
     */

    private String format;


    private Date createTime;


    private String createBy;

    /**
     * 操作时间
     */
    private Date updateTime;


    private String updateBy;





}