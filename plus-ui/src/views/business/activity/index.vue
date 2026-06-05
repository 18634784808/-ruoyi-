<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="名称" prop="name">
              <el-input v-model="queryParams.name" placeholder="请输入名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
                <el-option label="启用" value="0" />
                <el-option label="停用" value="1" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </transition>

    <el-card shadow="hover">
      <template #header>
        <el-row :gutter="10">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport">导出</el-button>
          </el-col>
          <right-toolbar v-model:show-search="showSearch" @query-table="getList" />
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="名称" align="center" prop="name" />
        <el-table-column label="状态" align="center" prop="status" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
              {{ scope.row.status === '0' ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total > 0"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        :total="total"
        @pagination="getList"
      />
    </el-card>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" append-to-body destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio value="0">启用</el-radio>
            <el-radio value="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="BusinessActivity" lang="ts">
import { ref, reactive, getCurrentInstance, ComponentInternalInstance } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const showSearch = ref(true);
const loading = ref(false);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const ids = ref<string[]>([]);
const tableData = ref<any[]>([]);
const dialogVisible = ref(false);
const dialogTitle = ref('');

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  status: ''
});

const form = reactive({
  id: '',
  name: '',
  status: '0',
  remark: ''
});

const rules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }]
};

const getList = async () => {
  loading.value = true;
  tableData.value = [];
  total.value = 0;
  loading.value = false;
};

const handleQuery = () => {
  queryParams.pageNum = 1;
  getList();
};

const resetQuery = () => {
  Object.assign(queryParams, { pageNum: 1, pageSize: 10, name: '', status: '' });
  handleQuery();
};

const handleSelectionChange = (selection: any[]) => {
  ids.value = selection.map((item: any) => item.id);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
};

const handleAdd = () => {
  dialogTitle.value = '新增';
  Object.assign(form, { id: '', name: '', status: '0', remark: '' });
  dialogVisible.value = true;
};

const handleUpdate = (row?: any) => {
  dialogTitle.value = '修改';
  const data = row || { id: '', name: '', status: '0', remark: '' };
  Object.assign(form, data);
  dialogVisible.value = true;
};

const handleDelete = (row?: any) => {
  const deleteIds = row?.id ? [row.id] : ids.value;
  ElMessageBox.confirm('确认删除所选数据？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => {
    ElMessage.success('删除成功');
    getList();
  });
};

const handleExport = () => {
  ElMessage.success('导出功能待接入');
};

const submitForm = async () => {
  ElMessage.success('保存成功');
  dialogVisible.value = false;
  getList();
};

getList();
</script>
