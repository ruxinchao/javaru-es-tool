package com.javaru.es.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.baomidou.mybatisplus.annotation.TableField;
import com.javaru.common.annotation.Excel;
import com.javaru.common.core.domain.BaseEntity;

/**
 * 索引管理对象 es_index
 * 
 * @author javaru
 * @date 2021-12-31
 */
public class EsIndex extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long indexId;

    /** 名称 */
    @Excel(name = "名称")
    private String indexName;

    /** 显示名称 */
    @Excel(name = "显示名称")
    private String indexShowName;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 索引字段信息 */
    @TableField(exist=false)
    private List<EsIndexColumn> esIndexColumnList;

    public void setIndexId(Long indexId) 
    {
        this.indexId = indexId;
    }

    public Long getIndexId() 
    {
        return indexId;
    }
    public void setIndexName(String indexName) 
    {
        this.indexName = indexName;
    }

    public String getIndexName() 
    {
        return indexName;
    }
    public void setIndexShowName(String indexShowName) 
    {
        this.indexShowName = indexShowName;
    }

    public String getIndexShowName() 
    {
        return indexShowName;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public List<EsIndexColumn> getEsIndexColumnList()
    {
        return esIndexColumnList;
    }

    public void setEsIndexColumnList(List<EsIndexColumn> esIndexColumnList)
    {
        this.esIndexColumnList = esIndexColumnList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("indexId", getIndexId())
            .append("indexName", getIndexName())
            .append("indexShowName", getIndexShowName())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("esIndexColumnList", getEsIndexColumnList())
            .toString();
    }
}
