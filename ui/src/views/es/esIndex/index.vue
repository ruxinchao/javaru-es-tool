<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="名称" prop="indexName">
        <el-input
          v-model="queryParams.indexName"
          placeholder="请输入名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="显示名称" prop="indexShowName">
        <el-input
          v-model="queryParams.indexShowName"
          placeholder="请输入显示名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择状态"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['es:esIndex:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload"
          size="mini"
          @click="openImportTable"
          v-hasPermi="['tool:gen:import']"
          >导入</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['es:esIndex:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['es:esIndex:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['es:esIndex:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="esIndexList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="indexId" />
      <el-table-column label="名称" align="center" prop="indexName" />
      <el-table-column label="显示名称" align="center" prop="indexShowName" />
      <el-table-column
        label="状态"
        align="center"
        prop="status"
        :formatter="statusFormat"
      />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['es:esIndex:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['es:esIndex:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改索引管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="indexName">
          <el-input v-model="form.indexName" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="显示名称" prop="indexShowName">
          <el-input v-model="form.indexShowName" placeholder="请输入显示名称" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
              >{{ dict.dictLabel }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入内容"
          />
        </el-form-item>
        <el-divider content-position="center">索引字段信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAddEsIndexColumn"
              >添加</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              @click="handleDeleteEsIndexColumn"
              >删除</el-button
            >
          </el-col>
        </el-row>
        <el-table
          :data="esIndexColumnList"
          :row-class-name="rowEsIndexColumnIndex"
          @selection-change="handleEsIndexColumnSelectionChange"
          ref="esIndexColumn"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column
            label="序号"
            align="center"
            prop="index"
            width="50"
          />
          <el-table-column label="列名称" prop="columnName">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.columnName"
                placeholder="请输入列名称"
              />
            </template>
          </el-table-column>
          <el-table-column label="显示名称" prop="columnShowName">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.columnShowName"
                placeholder="请输入显示名称"
              />
            </template>
          </el-table-column>
          <el-table-column label="列类型" prop="columnType">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.columnType"
                placeholder="请输入列类型"
              />
            </template>
          </el-table-column>
          <el-table-column label="列格式" prop="columnFormat">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.columnFormat"
                placeholder="请输入列格式"
              />
            </template>
          </el-table-column>
          <el-table-column label="排序" prop="sort">
            <template slot-scope="scope">
              <el-input v-model="scope.row.sort" placeholder="请输入排序" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!--导入索引弹框-->
    <import-index ref="import" @ok="handleQuery" />
  </div>
</template>

<script>
import {
  listEsIndex,
  getEsIndex,
  delEsIndex,
  addEsIndex,
  updateEsIndex,
  exportEsIndex,
} from "@/api/es/esIndex";
import importIndex from "./importIndex";

export default {
  name: "EsIndex",
  components: { importIndex },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedEsIndexColumn: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 索引管理表格数据
      esIndexList: [],
      // 索引字段表格数据
      esIndexColumnList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        indexName: null,
        indexShowName: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        status: [{ required: true, message: "状态不能为空", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_normal_disable").then((response) => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    /** 查询索引管理列表 */
    getList() {
      this.loading = true;
      listEsIndex(this.queryParams).then((response) => {
        this.esIndexList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        indexId: null,
        indexName: null,
        indexShowName: null,
        status: "0",
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
      };
      this.esIndexColumnList = [];
      this.resetForm("form");
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.indexId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加索引管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const indexId = row.indexId || this.ids;
      getEsIndex(indexId).then((response) => {
        this.form = response.data;
        this.esIndexColumnList = response.data.esIndexColumnList;
        this.open = true;
        this.title = "修改索引管理";
      });
    },
    /** 打开导入表弹窗 */
    openImportTable() {
      this.$refs.import.show();
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.form.esIndexColumnList = this.esIndexColumnList;
          if (this.form.indexId != null) {
            updateEsIndex(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEsIndex(this.form).then((response) => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const indexIds = row.indexId || this.ids;
      this.$confirm(
        '是否确认删除索引管理编号为"' + indexIds + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delEsIndex(indexIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 索引字段序号 */
    rowEsIndexColumnIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 索引字段添加按钮操作 */
    handleAddEsIndexColumn() {
      let obj = {};
      obj.columnName = "";
      obj.columnShowName = "";
      obj.columnType = "";
      obj.columnFormat = "";
      obj.sort = "";
      this.esIndexColumnList.push(obj);
    },
    /** 索引字段删除按钮操作 */
    handleDeleteEsIndexColumn() {
      if (this.checkedEsIndexColumn.length == 0) {
        this.msgError("请先选择要删除的索引字段数据");
      } else {
        const esIndexColumnList = this.esIndexColumnList;
        const checkedEsIndexColumn = this.checkedEsIndexColumn;
        this.esIndexColumnList = esIndexColumnList.filter(function (item) {
          return checkedEsIndexColumn.indexOf(item.index) == -1;
        });
      }
    },
    /** 复选框选中数据 */
    handleEsIndexColumnSelectionChange(selection) {
      this.checkedEsIndexColumn = selection.map((item) => item.index);
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有索引管理数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.exportLoading = true;
          return exportEsIndex(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
  },
};
</script>
