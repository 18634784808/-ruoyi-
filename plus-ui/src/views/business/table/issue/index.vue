<template>
  <div class="p-2">
    <div v-show="showSearch" class="mb-[10px]">
      <el-card shadow="hover">
        <el-form ref="queryFormRef" :model="queryParams" :inline="true">
          <el-form-item label="问题选项" prop="issueOption">
            <el-input v-model="queryParams.issueOption" placeholder="请输入问题选项" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="自增编号" prop="autoIncrementCode">
            <el-input v-model="queryParams.autoIncrementCode" placeholder="请输入自增编号" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="严重程度" prop="severity">
            <el-select v-model="queryParams.severity" placeholder="请选择严重程度" clearable>
              <el-option v-for="item in severityOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
              <el-option v-for="item in issueStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
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
        <el-table-column label="问题选项" align="center" prop="issueOption" min-width="300" show-overflow-tooltip />
        <el-table-column label="自增编号" align="center" prop="autoIncrementCode" width="150" />
        <el-table-column label="严重程度" align="center" prop="severity" width="100">
          <template #default="scope">
            <el-tag :type="getDictTagType(severityOptions, scope.row.severity)">{{ getDictLabel(severityOptions, scope.row.severity) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" width="100">
          <template #default="scope"><el-tag :type="getDictTagType(issueStatusOptions, scope.row.status)">{{ getDictLabel(issueStatusOptions, scope.row.status) }}</el-tag></template>
        </el-table-column>
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
        <el-form-item label="问题选项" prop="issueOption"><el-input v-model="form.issueOption" type="textarea" placeholder="请输入问题选项" :rows="4" /></el-form-item>
        <el-form-item label="自增编号" prop="autoIncrementCode"><el-input v-model="form.autoIncrementCode" placeholder="请输入自增编号" /></el-form-item>
        <el-form-item label="严重程度" prop="severity"><el-select v-model="form.severity" placeholder="请选择严重程度" style="width:100%"><el-option v-for="item in severityOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
        <el-form-item label="状态" prop="status"><el-select v-model="form.status" placeholder="请选择状态" style="width:100%"><el-option v-for="item in issueStatusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
        <el-form-item label="备注" prop="remark"><el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="2" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup name="BusinessTableIssue" lang="ts">
import { ref, reactive, onMounted, watch, getCurrentInstance, ComponentInternalInstance } from 'vue'; import { ElMessage, ElMessageBox } from 'element-plus';
import { useRoute } from 'vue-router';
import { listIssue, getIssue, addIssue, updateIssue, delIssue, exportIssue } from '@/api/business/issue/index';
import { getBizDictByType } from '@/api/business/dict/index';
const { proxy } = getCurrentInstance() as ComponentInternalInstance; const route = useRoute();
const showSearch = ref(true); const loading = ref(false); const submitLoading = ref(false); const single = ref(true); const multiple = ref(true); const total = ref(0); const ids = ref<string[]>([]); const tableData = ref<any[]>([]); const formRef = ref(); const dialogVisible = ref(false); const dialogTitle = ref('');
const severityOptions = ref<any[]>([]);
const issueStatusOptions = ref<any[]>([]);
const getDictLabel = (options: any[], value: string) => options.find(o => o.value === value)?.label ?? value;
const getDictTagType = (options: any[], value: string) => options.find(o => o.value === value)?.tagType ?? '';
onMounted(() => {
  getBizDictByType('severity').then(res => { severityOptions.value = res.data || []; });
  getBizDictByType('issue_status').then(res => { issueStatusOptions.value = res.data || []; });
});
const queryParams = reactive({ pageNum: 1, pageSize: 10, issueOption: '', autoIncrementCode: '', severity: '', status: '', inspectionId: '' });
const initForm: any = { issueOption: '', autoIncrementCode: '', severity: '0', status: '0', inspectionId: undefined, remark: '' };
const form = reactive({ ...initForm });
const rules = { issueOption: [{ required: true, message: '请输入问题选项', trigger: 'blur' }] };
const getList = async () => { loading.value = true; try { const res = await listIssue(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; } catch { /* 拦截器已处理错误 */ } finally { loading.value = false; } };
const handleQuery = () => { queryParams.pageNum = 1; getList(); };
const resetQuery = () => { Object.assign(queryParams, { pageNum: 1, pageSize: 10, issueOption: '', autoIncrementCode: '', severity: '', status: '', inspectionId: '' }); handleQuery(); };
watch(() => route.query.inspectionId, (val) => { if (val) { queryParams.inspectionId = val as string; handleQuery(); } }, { immediate: true });
const handleSelectionChange = (sel: any[]) => { ids.value = sel.map((i: any) => i.id); single.value = sel.length !== 1; multiple.value = !sel.length; };
const handleAdd = () => { Object.assign(form, { ...initForm, id: undefined, inspectionId: queryParams.inspectionId || undefined }); dialogTitle.value = '新增问题'; dialogVisible.value = true; };
const handleUpdate = async (row?: any) => { const id = row?.id || (ids.value.length === 1 ? ids.value[0] : null); if (!id) return; const res = await getIssue(id); Object.assign(form, res.data); dialogTitle.value = '修改问题'; dialogVisible.value = true; };
const handleDelete = (row?: any) => { const deleteIds = row?.id ? row.id : ids.value.join(','); const msg = row?.id ? '确认删除？' : '确认删除所选数据？'; ElMessageBox.confirm(msg, '提示', { type: 'warning' }).then(async () => { await delIssue(deleteIds); ElMessage.success('删除成功'); getList(); }); };
const handleExport = async () => { await exportIssue(queryParams); ElMessage.success('导出成功'); };
const submitForm = async () => { try { await formRef.value.validate(); } catch { return; } submitLoading.value = true; try { await (form.id ? updateIssue(form) : addIssue(form)); ElMessage.success(form.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList(); } catch (e) { console.error(e); } finally { submitLoading.value = false; } };
getList();
</script>
