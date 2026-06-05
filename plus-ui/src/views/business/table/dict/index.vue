<template>
  <div class="p-2">
    <div v-show="showSearch" class="mb-[10px]">
      <el-card shadow="hover">
        <el-form ref="queryFormRef" :model="queryParams" :inline="true">
          <el-form-item label="字典类型" prop="dictType">
            <el-input v-model="queryParams.dictType" placeholder="请输入字典类型" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="选项标签" prop="label">
            <el-input v-model="queryParams.label" placeholder="请输入选项标签" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
              <el-option label="启用" value="0" /><el-option label="停用" value="1" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <el-card shadow="hover">
      <template #header>
        <el-row :gutter="10">
          <el-col :span="1.5"><el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button></el-col>
          <el-col :span="1.5"><el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()">修改</el-button></el-col>
          <el-col :span="1.5"><el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()">删除</el-button></el-col>
          <el-col :span="1.5"><el-button type="warning" plain icon="Download" @click="handleExport">导出</el-button></el-col>
          <right-toolbar v-model:show-search="showSearch" @query-table="getList" />
        </el-row>
      </template>
      <el-table v-loading="loading" border :data="tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="字典类型" align="center" prop="dictType" width="180" show-overflow-tooltip />
        <el-table-column label="选项标签" align="center" prop="label" width="180" show-overflow-tooltip />
        <el-table-column label="选项值" align="center" prop="value" width="180" show-overflow-tooltip />
        <el-table-column label="排序" align="center" prop="sort" width="70" />
        <el-table-column label="标签颜色" align="center" width="90">
          <template #default="scope"><el-tag v-if="scope.row.tagType" :type="scope.row.tagType" size="small">{{ scope.row.tagType }}</el-tag><span v-else>-</span></template>
        </el-table-column>
        <el-table-column label="状态" align="center" width="80">
          <template #default="scope"><el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">{{ scope.row.status === '0' ? '启用' : '停用' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" min-width="150" show-overflow-tooltip />
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="550px" append-to-body destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="form.dictType" placeholder="请输入字典类型" />
        </el-form-item>
        <el-form-item label="选项标签" prop="label">
          <el-input v-model="form.label" placeholder="请输入选项标签" />
        </el-form-item>
        <el-form-item label="选项值" prop="value">
          <el-input v-model="form.value" placeholder="请输入选项值" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :max="999" placeholder="排序号" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status"><el-radio value="0">启用</el-radio><el-radio value="1">停用</el-radio></el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup name="BusinessTableDict" lang="ts">
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listBizDict, getBizDict, addBizDict, updateBizDict, delBizDict, exportBizDict } from '@/api/business/dict/index';
const showSearch = ref(true); const loading = ref(false); const submitLoading = ref(false); const single = ref(true); const multiple = ref(true); const total = ref(0); const ids = ref<string[]>([]); const tableData = ref<any[]>([]); const formRef = ref(); const dialogVisible = ref(false); const dialogTitle = ref('');
const queryParams = reactive({ pageNum: 1, pageSize: 10, dictType: '', label: '', status: '' });
const initForm: any = { dictType: '', label: '', value: '', sort: 0, status: '0', remark: '' };
const form = reactive({ ...initForm });
const rules = {
  dictType: [{ required: true, message: '请输入字典类型', trigger: 'blur' }],
  label: [{ required: true, message: '请输入选项标签', trigger: 'blur' }],
  value: [{ required: true, message: '请输入选项值', trigger: 'blur' }]
};
const getList = async () => { loading.value = true; try { const res = await listBizDict(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; } catch { /* 拦截器已处理错误 */ } finally { loading.value = false; } };
const handleQuery = () => { queryParams.pageNum = 1; getList(); };
const resetQuery = () => { Object.assign(queryParams, { pageNum: 1, pageSize: 10, dictType: '', label: '', status: '' }); handleQuery(); };
const handleSelectionChange = (sel: any[]) => { ids.value = sel.map((i: any) => i.id); single.value = sel.length !== 1; multiple.value = !sel.length; };
const handleAdd = () => { Object.assign(form, { ...initForm, id: undefined }); dialogTitle.value = '新增字典选项'; dialogVisible.value = true; };
const handleUpdate = async (row?: any) => { const id = row?.id || (ids.value.length === 1 ? ids.value[0] : null); if (!id) return; const res = await getBizDict(id); Object.assign(form, res.data); dialogTitle.value = '修改字典选项'; dialogVisible.value = true; };
const handleDelete = (row?: any) => { const deleteIds = row?.id ? row.id : ids.value.join(','); const msg = row?.id ? '确认删除？' : '确认删除所选数据？'; ElMessageBox.confirm(msg, '提示', { type: 'warning' }).then(async () => { await delBizDict(deleteIds); ElMessage.success('删除成功'); getList(); }); };
const handleExport = async () => { await exportBizDict(queryParams); ElMessage.success('导出成功'); };
const submitForm = async () => { try { await formRef.value.validate(); } catch { return; } submitLoading.value = true; try { await (form.id ? updateBizDict(form) : addBizDict(form)); ElMessage.success(form.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList(); } catch (e) { console.error(e); } finally { submitLoading.value = false; } };
getList();
</script>
