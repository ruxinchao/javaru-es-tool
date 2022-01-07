package com.javaru.es.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.javaru.es.mapper.EsIndexColumnMapper;
import com.javaru.common.annotation.Log;
import com.javaru.common.core.controller.BaseController;
import com.javaru.common.core.domain.AjaxResult;
import com.javaru.common.core.text.Convert;
import com.javaru.common.enums.BusinessType;
import com.javaru.common.utils.StringUtils;
import com.javaru.es.domain.EsIndex;
import com.javaru.es.domain.EsIndexColumn;
import com.javaru.es.service.IEsIndexOptService;
import com.javaru.es.service.IEsIndexService;
import com.javaru.es.vo.IndexFieldVo;
import com.javaru.es.vo.IndexFieldVo.EsFieldFormat;
import com.javaru.es.vo.IndexFieldVo.EsFieldType;

/**
 * 索引管理Controller
 * 
 * @author javaru
 * @date 2021-12-31
 */
@RestController
@RequestMapping("/es/esApi")
public class EsApiController extends BaseController {
	@Autowired
	private IEsIndexService esIndexService;
	@Autowired
	private IEsIndexOptService esIndexOptService;
	@Autowired
	private EsIndexColumnMapper esIndexColumnMapper;

	@GetMapping(value = "getColumnFormatList")
	public AjaxResult getColumnFormatList() {
		List<JSONObject> list = getFiledListByClass(EsFieldFormat.class);
		return AjaxResult.success(list);
	}
	
	@GetMapping(value = "getColumnTypeList")
	public AjaxResult getColumnTypeList(){
		List<JSONObject> list = getFiledListByClass(EsFieldType.class);
		return AjaxResult.success(list);
	}


	/**
	 * 查询索引列表排除 已经导入的
	 */
	@GetMapping(value = "indexList")
	public AjaxResult indexList(String indexName) {
		EsIndex esIndex = new EsIndex();
		esIndex.setStatus("0");
		List<EsIndex> list = esIndexService.selectEsIndexList(esIndex);
		if (list == null || list.size() < 1) {
			return AjaxResult.success(transformData(esIndexOptService.getAllIndexs(indexName)));
		}
		List<String> indexList = esIndexOptService.getAllIndexs(indexName);
		List<String> resList = new ArrayList<String>();
		aaa: for (String string : indexList) {
			for (EsIndex esIndex2 : list) {
				if (string.equals(esIndex2.getIndexName())) {
					continue aaa;
				}
			}
			resList.add(string);
		}
		return AjaxResult.success(transformData(resList));
	}

	/**
	 * 导入索引（保存）
	 */
	@PreAuthorize("@ss.hasPermi('es:esIndex:add')")
	@Log(title = "导入索引", businessType = BusinessType.IMPORT)
	@PostMapping("/importIndex")
	public AjaxResult importIndex(String indexs) {
		String[] indexNames = Convert.toStrArray(indexs);
		// 查询表信息
		for (String indexName : indexNames) {
			EsIndex esIndex = new EsIndex();
			esIndex.setIndexName(indexName);
			esIndex.setIndexShowName(indexName);
			esIndex.setStatus("0");
			esIndex.setCreateBy(this.getUsername());
			esIndex.setCreateTime(new Date());
			esIndex.setUpdateBy(this.getUsername());
			esIndex.setUpdateTime(new Date());
			esIndex.setRemark("ES导入");

			List<IndexFieldVo> columnList = esIndexOptService.getIndexFieldAll(indexName);
			List<EsIndexColumn> esIndexColumnList = new ArrayList<EsIndexColumn>();
			for (IndexFieldVo vo : columnList) {
				EsIndexColumn column = new EsIndexColumn();
				column.setColumnName(vo.getName());
				column.setColumnType(vo.getType());
				column.setColumnShowName(vo.getName());
				column.setColumnFormat(vo.getFormat());
				column.setCreateBy(this.getUsername());
				column.setCreateTime(new Date());
				column.setUpdateBy(this.getUsername());
				column.setUpdateTime(new Date());
				column.setRemark("ES导入");
				column.setSort(columnList.indexOf(vo) + 0L);

				esIndexColumnList.add(column);
			}
			esIndex.setEsIndexColumnList(esIndexColumnList);
			esIndexService.insertEsIndex(esIndex);
		}

		return AjaxResult.success();
	}

	/**
	 * 同步索引（保存）
	 */
	@PreAuthorize("@ss.hasPermi('es:esIndex:add')")
	@PostMapping("/synIndexs")
	public AjaxResult synIndexs(Long indexId) {
		// 查询表信息
		EsIndex esIndex = esIndexService.selectEsIndexByIndexId(indexId);
		List<IndexFieldVo> columnList = esIndexOptService.getIndexFieldAll(esIndex.getIndexName());
		List<EsIndexColumn> esIndexColumnList = new ArrayList<EsIndexColumn>();
		for (IndexFieldVo vo : columnList) {
			QueryWrapper<EsIndexColumn> queryWrapper = Wrappers.query();
			if (StringUtils.isNotEmpty(esIndex.getIndexName())) {
				queryWrapper.eq("column_name", vo.getName());
			}
			if (esIndex.getIndexId() != null) {
				queryWrapper.eq("index_id", esIndex.getIndexId());
			}
			EsIndexColumn esIndexColumn = esIndexColumnMapper.selectOne(queryWrapper);
			if (esIndexColumn != null) { // 存在更新
				esIndexColumn.setColumnFormat(vo.getFormat());
				esIndexColumn.setColumnType(vo.getType());
				esIndexColumnList.add(esIndexColumn);
			} else { // 不存在新增
				EsIndexColumn column = new EsIndexColumn();
				column.setColumnName(vo.getName());
				column.setColumnType(vo.getType());
				column.setColumnShowName(vo.getName());
				column.setColumnFormat(vo.getFormat());
				column.setCreateBy(this.getUsername());
				column.setCreateTime(new Date());
				column.setUpdateBy(this.getUsername());
				column.setUpdateTime(new Date());
				column.setRemark("ES同步");
				column.setSort(columnList.indexOf(vo) + 0L);
				esIndexColumnList.add(column);
			}
		}
		esIndex.setEsIndexColumnList(esIndexColumnList);
		esIndexService.updateEsIndex(esIndex);
		return AjaxResult.success();
	}

	/**
	 * listString 转换成 List<JSONObject>
	 * 
	 * @param list
	 * @return
	 */
	private List<JSONObject> transformData(List<String> list) {
		List<JSONObject> resList = new ArrayList<JSONObject>();
		for (String str : list) {
			JSONObject json = new JSONObject();
			json.put("indexName", str);
			resList.add(json);
		}
		return resList;
	}

	/**
	 * 根据class 获取属性
	 * 
	 * @param cls
	 * @return
	 */
	private List<JSONObject> getFiledListByClass(Class cls) {
		List<JSONObject> list = new ArrayList<>();
		Field[] fields = cls.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				Field f = fields[i];
				f.setAccessible(true);
				JSONObject json = new JSONObject();
				json.put("name", f.getName());
				json.put("val", f.get(cls));
				list.add(json);
			} catch (Exception e) {
				logger.error("Es获取字段类型异常", e);
			}
		}
		return list;
	}
}
