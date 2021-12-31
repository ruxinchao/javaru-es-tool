package com.javaru.es.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaru.common.annotation.Excel;
import com.javaru.common.core.domain.BaseEntity;

/**
 * 索引字段对象 es_index_column
 * 
 * @author javaru
 * @date 2021-12-31
 */
public class EsIndexColumn extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long columnId;

    /** 归属索引 */
    @Excel(name = "归属索引")
    private Long indexId;

    /** 列名称 */
    @Excel(name = "列名称")
    private String columnName;

    /** 显示名称 */
    @Excel(name = "显示名称")
    private String columnShowName;

    /** 列类型 */
    @Excel(name = "列类型")
    private String columnType;

    /** 列格式 */
    @Excel(name = "列格式")
    private String columnFormat;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    public void setColumnId(Long columnId) 
    {
        this.columnId = columnId;
    }

    public Long getColumnId() 
    {
        return columnId;
    }
    public void setIndexId(Long indexId) 
    {
        this.indexId = indexId;
    }

    public Long getIndexId() 
    {
        return indexId;
    }
    public void setColumnName(String columnName) 
    {
        this.columnName = columnName;
    }

    public String getColumnName() 
    {
        return columnName;
    }
    public void setColumnShowName(String columnShowName) 
    {
        this.columnShowName = columnShowName;
    }

    public String getColumnShowName() 
    {
        return columnShowName;
    }
    public void setColumnType(String columnType) 
    {
        this.columnType = columnType;
    }

    public String getColumnType() 
    {
        return columnType;
    }
    public void setColumnFormat(String columnFormat) 
    {
        this.columnFormat = columnFormat;
    }

    public String getColumnFormat() 
    {
        return columnFormat;
    }
    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("columnId", getColumnId())
            .append("indexId", getIndexId())
            .append("columnName", getColumnName())
            .append("columnShowName", getColumnShowName())
            .append("columnType", getColumnType())
            .append("columnFormat", getColumnFormat())
            .append("sort", getSort())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
