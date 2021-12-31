package com.javaru.es.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.javaru.common.annotation.Log;
import com.javaru.common.core.controller.BaseController;
import com.javaru.common.core.domain.AjaxResult;
import com.javaru.common.enums.BusinessType;
import com.javaru.es.domain.EsIndex;
import com.javaru.es.service.IEsIndexService;
import com.javaru.common.utils.poi.ExcelUtil;
import com.javaru.common.core.page.TableDataInfo;

/**
 * 索引管理Controller
 * 
 * @author javaru
 * @date 2021-12-31
 */
@RestController
@RequestMapping("/es/esIndex")
public class EsIndexController extends BaseController
{
    @Autowired
    private IEsIndexService esIndexService;

    /**
     * 查询索引管理列表
     */
    @PreAuthorize("@ss.hasPermi('es:esIndex:list')")
    @GetMapping("/list")
    public TableDataInfo list(EsIndex esIndex)
    {
        startPage();
        List<EsIndex> list = esIndexService.selectEsIndexList(esIndex);
        return getDataTable(list);
    }

    /**
     * 导出索引管理列表
     */
    @PreAuthorize("@ss.hasPermi('es:esIndex:export')")
    @Log(title = "索引管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(EsIndex esIndex)
    {
        List<EsIndex> list = esIndexService.selectEsIndexList(esIndex);
        ExcelUtil<EsIndex> util = new ExcelUtil<EsIndex>(EsIndex.class);
        return util.exportExcel(list, "索引管理数据");
    }

    /**
     * 获取索引管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('es:esIndex:query')")
    @GetMapping(value = "/{indexId}")
    public AjaxResult getInfo(@PathVariable("indexId") Long indexId)
    {
        return AjaxResult.success(esIndexService.selectEsIndexByIndexId(indexId));
    }

    /**
     * 新增索引管理
     */
    @PreAuthorize("@ss.hasPermi('es:esIndex:add')")
    @Log(title = "索引管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EsIndex esIndex)
    {
        return toAjax(esIndexService.insertEsIndex(esIndex));
    }

    /**
     * 修改索引管理
     */
    @PreAuthorize("@ss.hasPermi('es:esIndex:edit')")
    @Log(title = "索引管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EsIndex esIndex)
    {
        return toAjax(esIndexService.updateEsIndex(esIndex));
    }

    /**
     * 删除索引管理
     */
    @PreAuthorize("@ss.hasPermi('es:esIndex:remove')")
    @Log(title = "索引管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{indexIds}")
    public AjaxResult remove(@PathVariable Long[] indexIds)
    {
        return toAjax(esIndexService.deleteEsIndexByIndexIds(indexIds));
    }
}
