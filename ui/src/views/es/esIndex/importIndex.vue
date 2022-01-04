<template>
  <!-- 导入索引 -->
  <el-dialog
    title="导入索引"
    :visible.sync="visible"
    width="800px"
    top="5vh"
    append-to-body
  >
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="索引名称" prop="indexName">
        <el-input
          v-model="queryParams.indexName"
          placeholder="请输入索引名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <el-row>
      <el-table
        @row-click="clickRow"
        ref="table"
        :data="dbTableList"
        @selection-change="handleSelectionChange"
        height="460px"
      >
        <el-table-column type="selection"></el-table-column>
        <el-table-column
          prop="indexName"
          label="索引名称"
          :show-overflow-tooltip="true"
        ></el-table-column>
      </el-table>
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleImportTable">确 定</el-button>
      <el-button @click="visible = false">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { indexList, importIndex } from "@/api/es/esIndex";
export default {
  data() {
    return {
      // 遮罩层
      visible: false,
      // 选中数组值
      tables: [],
      // 表数据
      dbTableList: [],
      // 查询参数
      queryParams: {
        indexName: undefined,
      },
    };
  },
  methods: {
    // 显示弹框
    show() {
      this.getList();
      this.visible = true;
    },
    clickRow(row) {
      this.$refs.table.toggleRowSelection(row);
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      debugger;
      this.tables = selection.map((item) => item.indexName);
    },
    // 查询表数据
    getList() {
      indexList(this.queryParams).then((res) => {
        if (res.code === 200) {
          this.dbTableList = res.data;
        }
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 导入按钮操作 */
    handleImportTable() {
      const tableNames = this.tables.join(",");
      if (tableNames == "") {
        this.$modal.msgError("请选择要导入的表");
        return;
      }
      importIndex({ indexs: tableNames }).then((res) => {
        this.$modal.msgSuccess(res.msg);
        if (res.code === 200) {
          this.visible = false;
          this.$emit("ok");
        }
      });
    },
  },
};
</script>
