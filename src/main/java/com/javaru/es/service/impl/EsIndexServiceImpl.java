package com.javaru.es.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaru.common.utils.DateUtils;
import com.javaru.common.utils.StringUtils;
import com.javaru.es.domain.EsIndex;
import com.javaru.es.domain.EsIndexColumn;
import com.javaru.es.mapper.EsIndexMapper;
import com.javaru.es.service.IEsIndexOptService;
import com.javaru.es.service.IEsIndexService;
import com.javaru.es.util.ExceptionUtils;
import com.javaru.es.vo.IndexFieldVo;
import com.javaru.es.vo.request.IndexAddRequest;

/**
 * 索引管理Service业务层处理
 * 
 * @author javaru
 * @date 2021-12-31
 */
@Service
public class EsIndexServiceImpl extends ServiceImpl<EsIndexMapper, EsIndex> implements IEsIndexService {
	@Autowired
	private EsIndexMapper esIndexMapper;

	@Autowired
	private IEsIndexOptService esIndexOptService;

	/**
	 * 查询索引管理
	 * 
	 * @param indexId 索引管理主键
	 * @return 索引管理
	 */
	@Override
	public EsIndex selectEsIndexByIndexId(Long indexId) {
		return esIndexMapper.selectEsIndexByIndexId(indexId);
	}

	/**
	 * 查询索引管理列表
	 * 
	 * @param esIndex 索引管理
	 * @return 索引管理
	 */
	@Override
	public List<EsIndex> selectEsIndexList(EsIndex esIndex) {
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
	public int insertEsIndex(EsIndex esIndex) {
		uniqueDetection(esIndex);
		esIndex.setCreateTime(DateUtils.getNowDate());
		IndexAddRequest indexAddRequest = new IndexAddRequest();
		if (esIndexOptService.existsIndex(esIndex.getIndexName())) {//索引在Es中存在，同步股过来
			int rows = esIndexMapper.insertEsIndex(esIndex);
			insertEsIndexColumn(esIndex);
			return rows;
		}
		indexAddRequest.setIndexName(esIndex.getIndexName());
		indexAddRequest.setFieldList(esIndexOptService.getDefaultField(indexAddRequest.getFieldList()));
		String index = esIndexOptService.creatIndex(esIndex.getIndexName(), indexAddRequest);
		if (StringUtils.isBlank(index)) {
			ExceptionUtils.throwBizException("创建索引异常");
		}
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
	public int updateEsIndex(EsIndex esIndex) {
		uniqueDetection(esIndex);
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
	public int deleteEsIndexByIndexIds(Long[] indexIds) {
		for (Long indexId : indexIds) {
			EsIndex index = esIndexMapper.selectEsIndexByIndexId(indexId);
			//删除Es
			esIndexOptService.deleteIndex(index.getIndexName());
		}
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
	public int deleteEsIndexByIndexId(Long indexId) {
		EsIndex index = esIndexMapper.selectEsIndexByIndexId(indexId);
		//删除Es
		esIndexOptService.deleteIndex(index.getIndexName());
		esIndexMapper.deleteEsIndexColumnByIndexId(indexId);
		return esIndexMapper.deleteEsIndexByIndexId(indexId);
	}

	/**
	 * 新增索引字段信息
	 * 
	 * @param esIndex 索引管理对象
	 */
	public void insertEsIndexColumn(EsIndex esIndex) {
		List<EsIndexColumn> esIndexColumnList = esIndex.getEsIndexColumnList();
		Long indexId = esIndex.getIndexId();
		if (StringUtils.isNotNull(esIndexColumnList)) {
			List<EsIndexColumn> list = new ArrayList<EsIndexColumn>();
			List<IndexFieldVo> fieldList = new ArrayList<IndexFieldVo>();
			for (EsIndexColumn esIndexColumn : esIndexColumnList) {
				esIndexColumn.setIndexId(indexId);
				Long columnId = esIndexColumn.getColumnId();
				if(columnId == null) { //说明是新增
					IndexFieldVo vo = new IndexFieldVo();
					vo.setName(esIndexColumn.getColumnName());
					vo.setType(esIndexColumn.getColumnType());
					vo.setFormat(esIndexColumn.getColumnFormat());
					fieldList.add(vo);
				}
				list.add(esIndexColumn);
			}
			if (fieldList.size() > 0) {// 新增ES列 
				esIndexOptService.addIndexFields(esIndex.getIndexName(), fieldList);
			}
			if (list.size() > 0) {
				esIndexMapper.batchEsIndexColumn(list);
			}
		}
	}

	private void uniqueDetection(EsIndex esIndex) {
		QueryWrapper<EsIndex> queryWrapper = Wrappers.query();
		if (StringUtils.isNotEmpty(esIndex.getIndexName())) {
			queryWrapper.eq("index_name", esIndex.getIndexName());
		}
		if (esIndex.getIndexId() != null) {
			queryWrapper.notIn("index_id", esIndex.getIndexId());
		}
		List<EsIndex> list = esIndexMapper.selectList(queryWrapper);
		if (null != list && list.size() > 0) {
			ExceptionUtils.throwBizException("名称已存在");
		}
	}

}
