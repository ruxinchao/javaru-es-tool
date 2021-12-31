package com.javaru.es.service.impl;

import java.util.List;
import com.javaru.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.javaru.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.javaru.es.domain.EsIndexColumn;
import com.javaru.es.mapper.EsIndexMapper;
import com.javaru.es.domain.EsIndex;
import com.javaru.es.service.IEsIndexService;

/**
 * 索引管理Service业务层处理
 * 
 * @author javaru
 * @date 2021-12-31
 */
@Service
public class EsIndexServiceImpl implements IEsIndexService 
{
    @Autowired
    private EsIndexMapper esIndexMapper;

    /**
     * 查询索引管理
     * 
     * @param indexId 索引管理主键
     * @return 索引管理
     */
    @Override
    public EsIndex selectEsIndexByIndexId(Long indexId)
    {
        return esIndexMapper.selectEsIndexByIndexId(indexId);
    }

    /**
     * 查询索引管理列表
     * 
     * @param esIndex 索引管理
     * @return 索引管理
     */
    @Override
    public List<EsIndex> selectEsIndexList(EsIndex esIndex)
    {
        return esIndexMapper.selectEsIndexList(esIndex);
    }

    /**
     * 新增索引管理
     * 
     * @param esIndex 索引管理
     * @return 结果
     */
    @Transactional
    @Override
    public int insertEsIndex(EsIndex esIndex)
    {
        esIndex.setCreateTime(DateUtils.getNowDate());
        int rows = esIndexMapper.insertEsIndex(esIndex);
        insertEsIndexColumn(esIndex);
        return rows;
    }

    /**
     * 修改索引管理
     * 
     * @param esIndex 索引管理
     * @return 结果
     */
    @Transactional
    @Override
    public int updateEsIndex(EsIndex esIndex)
    {
        esIndex.setUpdateTime(DateUtils.getNowDate());
        esIndexMapper.deleteEsIndexColumnByIndexId(esIndex.getIndexId());
        insertEsIndexColumn(esIndex);
        return esIndexMapper.updateEsIndex(esIndex);
    }

    /**
     * 批量删除索引管理
     * 
     * @param indexIds 需要删除的索引管理主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteEsIndexByIndexIds(Long[] indexIds)
    {
        esIndexMapper.deleteEsIndexColumnByIndexIds(indexIds);
        return esIndexMapper.deleteEsIndexByIndexIds(indexIds);
    }

    /**
     * 删除索引管理信息
     * 
     * @param indexId 索引管理主键
     * @return 结果
     */
    @Override
    public int deleteEsIndexByIndexId(Long indexId)
    {
        esIndexMapper.deleteEsIndexColumnByIndexId(indexId);
        return esIndexMapper.deleteEsIndexByIndexId(indexId);
    }

    /**
     * 新增索引字段信息
     * 
     * @param esIndex 索引管理对象
     */
    public void insertEsIndexColumn(EsIndex esIndex)
    {
        List<EsIndexColumn> esIndexColumnList = esIndex.getEsIndexColumnList();
        Long indexId = esIndex.getIndexId();
        if (StringUtils.isNotNull(esIndexColumnList))
        {
            List<EsIndexColumn> list = new ArrayList<EsIndexColumn>();
            for (EsIndexColumn esIndexColumn : esIndexColumnList)
            {
                esIndexColumn.setIndexId(indexId);
                list.add(esIndexColumn);
            }
            if (list.size() > 0)
            {
                esIndexMapper.batchEsIndexColumn(list);
            }
        }
    }
}
