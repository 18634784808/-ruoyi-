<template>
  <div class="p-2">
    <div v-show="showSearch" class="mb-[10px]">
      <el-card shadow="hover">
        <el-form ref="queryFormRef" :model="queryParams" :inline="true">
          <el-form-item label="地点" prop="place">
            <el-input v-model="queryParams.place" placeholder="请输入地点" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="当事人" prop="party">
            <el-input v-model="queryParams.party" placeholder="请输入当事人" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="检察人员" prop="inspector">
            <el-input v-model="queryParams.inspector" placeholder="请输入检察人员" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="检查记录ID" prop="inspectionId">
            <el-input v-model="queryParams.inspectionId" placeholder="请输入检查记录ID" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
              <el-option v-for="item in inspectionStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
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
        <el-table-column label="开始时间" align="center" prop="startTime" width="170" />
        <el-table-column label="截止时间" align="center" prop="endTime" width="170" />
        <el-table-column label="地点" align="center" prop="place" min-width="150" show-overflow-tooltip />
        <el-table-column label="检察人员" align="center" prop="inspector" width="120" show-overflow-tooltip />
        <el-table-column label="当事人" align="center" prop="party" width="100" />
        <el-table-column label="检查人员1" align="center" prop="inspector1" width="100" />
        <el-table-column label="当事人1" align="center" prop="party1" width="100" />
        <el-table-column label="检察人员2" align="center" prop="inspector2" width="100" />
        <el-table-column label="当事人2" align="center" prop="party2" width="100" />
        <el-table-column label="状态" align="center" width="80">
          <template #default="scope"><el-tag :type="getDictTagType(inspectionStatusOptions, scope.row.status)">{{ getDictLabel(inspectionStatusOptions, scope.row.status) }}</el-tag></template>
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
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="900px" append-to-body destroy-on-close top="2vh">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="160px">
        <el-divider content-position="left">基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="开始时间" prop="startTime"><el-date-picker v-model="form.startTime" type="datetime" placeholder="请选择开始时间" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="截止时间" prop="endTime"><el-date-picker v-model="form.endTime" type="datetime" placeholder="请选择截止时间" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="地点" prop="place"><el-input v-model="form.place" placeholder="请输入地点" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="检察人员" prop="inspector">
            <el-select v-model="form.inspector" placeholder="请选择检察人员" filterable style="width:100%">
              <el-option v-for="item in inspectorOptions" :key="item.id" :label="item.name + ' 执法证号：' + item.certNo" :value="item.name" />
            </el-select>
          </el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="检查人员副本" prop="inspectorCopy">
            <el-select v-model="form.inspectorCopy" placeholder="请选择检查人员副本" filterable style="width:100%">
              <el-option v-for="item in inspectorOptions" :key="item.id" :label="item.name + ' 执法证号：' + item.certNo" :value="item.name" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="当事人" prop="party"><el-input v-model="form.party" placeholder="请输入当事人" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="主体资格证照名称" prop="qualificationName">
            <el-select v-model="form.qualificationName" placeholder="请选择证照名称" style="width:100%">
              <el-option v-for="item in qualificationNameOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="通知当事人到场情况" prop="partyPresenceNotice"><el-input v-model="form.partyPresenceNotice" placeholder="请输入通知当事人到场情况" /></el-form-item></el-col>
        </el-row>
        <el-divider content-position="left">检查人员与当事人</el-divider>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="检查人员1" prop="inspector1"><el-input v-model="form.inspector1" placeholder="请输入检查人员1" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="当事人1" prop="party1"><el-input v-model="form.party1" placeholder="请输入当事人1" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="检察人员2" prop="inspector2"><el-input v-model="form.inspector2" placeholder="请输入检察人员2" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="当事人2" prop="party2"><el-input v-model="form.party2" placeholder="请输入当事人2" /></el-form-item></el-col>
        </el-row>
        <el-divider content-position="left">现场记录</el-divider>
        <el-form-item label="现场情况" prop="sceneSituation">
          <el-input v-model="form.sceneSituation" type="textarea" placeholder="如实施行政强制措施，当场告知当事人采取行政强制措施的理由、依据以及依法享有的权利、救济途径" :rows="4" />
        </el-form-item>
        <el-form-item label="当事人的陈述和申辩" prop="partyStatement">
          <el-input v-model="form.partyStatement" type="textarea" placeholder="请输入当事人的陈述和申辩" :rows="4" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="状态" prop="status"><el-select v-model="form.status" placeholder="请选择状态" style="width:100%"><el-option v-for="item in inspectionStatusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="备注" prop="remark"><el-input v-model="form.remark" placeholder="请输入备注" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup name="BusinessTableSceneNotes" lang="ts">
import { ref, reactive, onMounted, watch, getCurrentInstance, ComponentInternalInstance } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRoute, useRouter } from 'vue-router';
import { listSceneNotes, getSceneNotes, addSceneNotes, updateSceneNotes, delSceneNotes, exportSceneNotes } from '@/api/business/scene-notes/index';
import { getAllInspector } from '@/api/business/inspector/index';
import { getBizDictByType } from '@/api/business/dict/index';
const { proxy } = getCurrentInstance() as ComponentInternalInstance; const route = useRoute(); const router = useRouter();
const showSearch = ref(true); const loading = ref(false); const submitLoading = ref(false); const single = ref(true); const multiple = ref(true); const total = ref(0); const ids = ref<string[]>([]); const tableData = ref<any[]>([]); const formRef = ref(); const dialogVisible = ref(false); const dialogTitle = ref(''); const inspectorOptions = ref<any[]>([]);
const inspectionStatusOptions = ref<any[]>([]);
const qualificationNameOptions = ref<any[]>([]);
const getDictLabel = (options: any[], value: string) => options.find(o => o.value === value)?.label ?? value;
const getDictTagType = (options: any[], value: string) => options.find(o => o.value === value)?.tagType ?? '';
const queryParams = reactive({ pageNum: 1, pageSize: 10, place: '', party: '', inspector: '', status: '', inspectionId: '' });
const initForm: any = { startTime: '', endTime: '', place: '', inspector: '', inspectorCopy: '', party: '', qualificationName: '', partyPresenceNotice: '', inspector1: '', party1: '', inspector2: '', party2: '', sceneSituation: '', partyStatement: '', status: '0', inspectionId: undefined, remark: '' };
const form = reactive({ ...initForm });
const rules = {
  place: [{ required: true, message: '请输入地点', trigger: 'blur' }],
  party: [{ required: true, message: '请输入当事人', trigger: 'blur' }]
};
const getList = async () => { loading.value = true; try { const res = await listSceneNotes(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; } catch { /* 拦截器已处理错误 */ } finally { loading.value = false; } };
const handleQuery = () => { queryParams.pageNum = 1; getList(); };
const resetQuery = () => { Object.assign(queryParams, { pageNum: 1, pageSize: 10, place: '', party: '', inspector: '', status: '', inspectionId: '' }); handleQuery(); };
watch(() => route.query.inspectionId, (val) => { if (val) { queryParams.inspectionId = val as string; handleQuery(); } }, { immediate: true });
const handleSelectionChange = (sel: any[]) => { ids.value = sel.map((i: any) => i.id); single.value = sel.length !== 1; multiple.value = !sel.length; };
const handleAdd = () => { Object.assign(form, { ...initForm, id: undefined, inspectionId: queryParams.inspectionId || undefined }); dialogTitle.value = '新增现场笔录'; dialogVisible.value = true; };
const handleUpdate = async (row?: any) => { const id = row?.id || (ids.value.length === 1 ? ids.value[0] : null); if (!id) return; const res = await getSceneNotes(id); Object.assign(form, res.data); dialogTitle.value = '修改现场笔录'; dialogVisible.value = true; };
const handleDelete = (row?: any) => { const deleteIds = row?.id ? row.id : ids.value.join(','); const msg = row?.id ? '确认删除？' : '确认删除所选数据？'; ElMessageBox.confirm(msg, '提示', { type: 'warning' }).then(async () => { await delSceneNotes(deleteIds); ElMessage.success('删除成功'); getList(); }); };
const handleExport = async () => { await exportSceneNotes(queryParams); ElMessage.success('导出成功'); };
const submitForm = async () => { try { await formRef.value.validate(); } catch { return; } submitLoading.value = true; try { await (form.id ? updateSceneNotes(form) : addSceneNotes(form)); ElMessage.success(form.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList(); } catch (e) { console.error(e); } finally { submitLoading.value = false; } };
const loadInspectorOptions = async () => { try { const res = await getAllInspector(); inspectorOptions.value = res.data || []; } catch { /* ignore */ } };
const loadDictOptions = () => {
  getBizDictByType('inspection_status').then(res => { inspectionStatusOptions.value = res.data || []; });
  getBizDictByType('qualification_name').then(res => { qualificationNameOptions.value = res.data || []; });
};
onMounted(() => { loadInspectorOptions(); loadDictOptions(); });
getList();
</script>
