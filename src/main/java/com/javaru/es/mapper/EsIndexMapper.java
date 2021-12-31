package com.javaru.es.mapper;

import java.util.List;
import com.javaru.es.domain.EsIndex;
import com.javaru.es.domain.EsIndexColumn;

/**
 * 索引管理Mapper接口
 * 
 * @author javaru
 * @date 2021-12-31
 */
public interface EsIndexMapper 
{
    /**
     * 查询索引管理
     * 
     * @param indexId 索引管理主键
     * @return 索引管理
     */
    public EsIndex selectEsIndexByIndexId(Long indexId);

    /**
     * 查询索引管理列表
     * 
     * @param esIndex 索引管理
     * @return 索引管理集合
     */
    public List<EsIndex> selectEsIndexList(EsIndex esIndex);

    /**
     * 新增索引管理
     * 
     * @param esIndex 索引管理
     * @return 结果
     */
    public int insertEsIndex(EsIndex esIndex);

    /**
     * 修改索引管理
     * 
     * @param esIndex 索引管理
     * @return 结果
     */
    public int updateEsIndex(EsIndex esIndex);

    /**
     * 删除索引管理
     * 
     * @param indexId 索引管理主键
     * @return 结果
     */
    public int deleteEsIndexByIndexId(Long indexId);

    /**
     * 批量删除索引管理
     * 
     * @param indexIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEsIndexByIndexIds(Long[] indexIds);

    /**
     * 批量删除索引字段
     * 
     * @param indexIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEsIndexColumnByIndexIds(Long[] indexIds);
    
    /**
     * 批量新增索引字段
     * 
     * @param esIndexColumnList 索引字段列表
     * @return 结果
     */
    public int batchEsIndexColumn(List<EsIndexColumn> esIndexColumnList);
    

    /**
     * 通过索引管理主键删除索引字段信息
     * 
     * @param indexId 索引管理ID
     * @return 结果
     */
    public int deleteEsIndexColumnByIndexId(Long indexId);
}
