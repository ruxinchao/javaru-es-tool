package com.javaru.es.service;

import java.util.List;
import com.javaru.es.domain.EsIndex;

/**
 * 索引管理Service接口
 * 
 * @author javaru
 * @date 2021-12-31
 */
public interface IEsIndexService 
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
     * 批量删除索引管理
     * 
     * @param indexIds 需要删除的索引管理主键集合
     * @return 结果
     */
    public int deleteEsIndexByIndexIds(Long[] indexIds);

    /**
     * 删除索引管理信息
     * 
     * @param indexId 索引管理主键
     * @return 结果
     */
    public int deleteEsIndexByIndexId(Long indexId);
}
