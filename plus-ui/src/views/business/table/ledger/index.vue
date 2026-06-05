<template>
  <div class="p-2">
    <div v-show="showSearch" class="mb-[10px]">
      <el-card shadow="hover">
        <el-form ref="queryFormRef" :model="queryParams" :inline="true">
          <el-form-item label="被检查单位" prop="inspectedUnit">
            <el-input v-model="queryParams.inspectedUnit" placeholder="请输入被检查单位" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="法定代表人" prop="legalPerson">
            <el-input v-model="queryParams.legalPerson" placeholder="请输入法定代表人" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="整改进度" prop="rectificationProgress">
            <el-select v-model="queryParams.rectificationProgress" placeholder="请选择整改进度" clearable>
              <el-option v-for="item in rectificationProgressOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="检查人员" prop="inspectorCategory">
            <el-select v-model="queryParams.inspectorCategory" placeholder="请选择检查人员" clearable>
              <el-option v-for="item in inspectorOptions" :key="item.id" :label="item.name" :value="item.name" />
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
        <el-table-column label="日期" align="center" prop="date" width="120" />
        <el-table-column label="检查日期" align="center" prop="inspectionDate" width="120" />
        <el-table-column label="被检查单位" align="center" prop="inspectedUnit" min-width="180" show-overflow-tooltip />
        <el-table-column label="法定代表人" align="center" prop="legalPerson" width="100" />
        <el-table-column label="联系方式" align="center" prop="contactPhone" width="130" />
        <el-table-column label="地址" align="center" prop="address" min-width="180" show-overflow-tooltip />
        <el-table-column label="检查内容" align="center" prop="inspectionContent" min-width="200" show-overflow-tooltip />
        <el-table-column label="整改措施" align="center" prop="rectificationMeasure" min-width="200" show-overflow-tooltip />
        <el-table-column label="整改进度" align="center" width="100">
          <template #default="scope">
            <el-tag :type="getDictTagType(rectificationProgressOptions, scope.row.rectificationProgress)">{{ getDictLabel(rectificationProgressOptions, scope.row.rectificationProgress) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="检查人员" align="center" prop="inspectorCategory" width="120" />
        <el-table-column label="统一社会信用代码" align="center" prop="unifiedSocialCreditCode" width="200" show-overflow-tooltip />
        <el-table-column label="备注" align="center" prop="remark" min-width="150" show-overflow-tooltip />
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="750px" append-to-body destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="130px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="日期" prop="date"><el-date-picker v-model="form.date" type="date" placeholder="请选择日期" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="检查日期" prop="inspectionDate"><el-date-picker v-model="form.inspectionDate" type="date" placeholder="请选择检查日期" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="被检查单位" prop="inspectedUnit"><el-input v-model="form.inspectedUnit" placeholder="请输入被检查单位" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="法定代表人" prop="legalPerson"><el-input v-model="form.legalPerson" placeholder="请输入法定代表人" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="联系方式" prop="contactPhone"><el-input v-model="form.contactPhone" placeholder="请输入联系方式" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="地址" prop="address"><el-input v-model="form.address" placeholder="请输入地址" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="统一社会信用代码" prop="unifiedSocialCreditCode"><el-input v-model="form.unifiedSocialCreditCode" placeholder="请输入统一社会信用代码" maxlength="18" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查人员" prop="inspectorCategory">
              <el-select v-model="form.inspectorCategory" placeholder="请选择检查人员" style="width:100%">
                <el-option v-for="item in inspectorOptions" :key="item.id" :label="item.name" :value="item.name" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="检查内容" prop="inspectionContent"><el-input v-model="form.inspectionContent" type="textarea" placeholder="请输入检查内容" :rows="3" /></el-form-item>
        <el-form-item label="整改措施" prop="rectificationMeasure"><el-input v-model="form.rectificationMeasure" type="textarea" placeholder="请输入整改措施" :rows="3" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="整改进度" prop="rectificationProgress">
              <el-select v-model="form.rectificationProgress" placeholder="请选择整改进度" style="width:100%">
                <el-option v-for="item in rectificationProgressOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark"><el-input v-model="form.remark" placeholder="请输入备注" /></el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup name="BusinessTableLedger" lang="ts">
import { ref, reactive, onMounted } from 'vue'; import { ElMessage, ElMessageBox } from 'element-plus';
import { listLedger, getLedger, addLedger, updateLedger, delLedger, exportLedger } from '@/api/business/ledger/index';
import { getAllInspector } from '@/api/business/inspector/index';
import { getBizDictByType } from '@/api/business/dict/index';
const showSearch = ref(true); const loading = ref(false); const submitLoading = ref(false); const single = ref(true); const multiple = ref(true); const total = ref(0); const ids = ref<string[]>([]); const tableData = ref<any[]>([]); const formRef = ref(); const dialogVisible = ref(false); const dialogTitle = ref(''); const inspectorOptions = ref<any[]>([]);
const rectificationProgressOptions = ref<any[]>([]);
const getDictLabel = (options: any[], value: string) => options.find(o => o.value === value)?.label ?? value;
const getDictTagType = (options: any[], value: string) => options.find(o => o.value === value)?.tagType ?? '';
const queryParams = reactive({ pageNum: 1, pageSize: 10, inspectedUnit: '', legalPerson: '', rectificationProgress: '', inspectorCategory: '' });
const initForm: any = { date: '', inspectionDate: '', inspectedUnit: '', legalPerson: '', contactPhone: '', address: '', inspectionContent: '', rectificationMeasure: '', rectificationProgress: '0', inspectorCategory: '', remark: '', unifiedSocialCreditCode: '' };
const form = reactive({ ...initForm });
const rules = { inspectedUnit: [{ required: true, message: '请输入被检查单位', trigger: 'blur' }] };
const getList = async () => { loading.value = true; try { const res = await listLedger(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; } catch { /* 拦截器已处理错误 */ } finally { loading.value = false; } };
const handleQuery = () => { queryParams.pageNum = 1; getList(); };
const resetQuery = () => { Object.assign(queryParams, { pageNum: 1, pageSize: 10, inspectedUnit: '', legalPerson: '', rectificationProgress: '', inspectorCategory: '' }); handleQuery(); };
const handleSelectionChange = (sel: any[]) => { ids.value = sel.map((i: any) => i.id); single.value = sel.length !== 1; multiple.value = !sel.length; };
const handleAdd = () => { Object.assign(form, { ...initForm, id: undefined }); dialogTitle.value = '新增台账'; dialogVisible.value = true; };
const handleUpdate = async (row?: any) => { const id = row?.id || (ids.value.length === 1 ? ids.value[0] : null); if (!id) return; const res = await getLedger(id); Object.assign(form, res.data); dialogTitle.value = '修改台账'; dialogVisible.value = true; };
const handleDelete = (row?: any) => { const deleteIds = row?.id ? row.id : ids.value.join(','); const msg = row?.id ? '确认删除？' : '确认删除所选数据？'; ElMessageBox.confirm(msg, '提示', { type: 'warning' }).then(async () => { await delLedger(deleteIds); ElMessage.success('删除成功'); getList(); }); };
const handleExport = async () => { await exportLedger(queryParams); ElMessage.success('导出成功'); };
const submitForm = async () => { try { await formRef.value.validate(); } catch { return; } submitLoading.value = true; try { await (form.id ? updateLedger(form) : addLedger(form)); ElMessage.success(form.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList(); } catch (e) { console.error(e); } finally { submitLoading.value = false; } };
const loadInspectorOptions = async () => { try { const res = await getAllInspector(); inspectorOptions.value = res.data || []; } catch { /* ignore */ } };
const loadDictOptions = () => { getBizDictByType('rectification_progress').then(res => { rectificationProgressOptions.value = res.data || []; }); };
onMounted(() => { loadInspectorOptions(); loadDictOptions(); });
getList();
</script>
