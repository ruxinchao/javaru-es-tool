package com.javaru.es.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.javaru.common.annotation.Log;
import com.javaru.common.core.controller.BaseController;
import com.javaru.common.core.domain.AjaxResult;
import com.javaru.common.core.text.Convert;
import com.javaru.common.enums.BusinessType;
import com.javaru.es.domain.EsIndex;
import com.javaru.es.service.IEsIndexOptService;
import com.javaru.es.service.IEsIndexService;
import com.javaru.generator.domain.GenTable;

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
		for (EsIndex esIndex2 : list) {
			indexList.remove(esIndex2.getIndexName());
		}
		return AjaxResult.success(transformData(indexList));
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
			esIndexService.insertEsIndex(esIndex);
		}
		
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

}
