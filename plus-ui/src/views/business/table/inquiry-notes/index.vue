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
          <el-form-item label="询问人" prop="inquirer">
            <el-input v-model="queryParams.inquirer" placeholder="请输入询问人" clearable @keyup.enter="handleQuery" />
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
        <el-table-column label="地点" align="center" prop="place" width="120" show-overflow-tooltip />
        <el-table-column label="次数" align="center" prop="times" width="60" />
        <el-table-column label="问题数" align="center" prop="questionCount" width="70" />
        <el-table-column label="询问人" align="center" prop="inquirer" width="100" />
        <el-table-column label="询问人副本" align="center" prop="inquirerCopy" width="100" />
        <el-table-column label="当事人" align="center" prop="party" width="100" />
        <el-table-column label="性别" align="center" prop="gender" width="60" />
        <el-table-column label="职务" align="center" prop="position" width="100" />
        <el-table-column label="其他联系方式" align="center" prop="otherContact" width="130" show-overflow-tooltip />
        <el-table-column label="询问人1" align="center" prop="inquirer1" width="100" />
        <el-table-column label="被询问人1" align="center" prop="respondent1" width="100" />
        <el-table-column label="询问人2" align="center" prop="inquirer2" width="100" />
        <el-table-column label="被询问人2" align="center" prop="respondent2" width="100" />
        <el-table-column v-for="n in 10" :key="'q'+n" :label="'问题'+n" align="center" :prop="'question'+n" width="150" show-overflow-tooltip />
        <el-table-column v-for="n in 10" :key="'a'+n" :label="'回答'+n" align="center" :prop="'answer'+n" width="150" show-overflow-tooltip />
        <el-table-column label="备注" align="center" prop="remark" width="120" show-overflow-tooltip />
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
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="1000px" append-to-body destroy-on-close top="1vh">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="130px">
        <el-divider content-position="left">基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="开始时间" prop="startTime"><el-date-picker v-model="form.startTime" type="datetime" placeholder="请选择开始时间" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="截止时间" prop="endTime"><el-date-picker v-model="form.endTime" type="datetime" placeholder="请选择截止时间" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="地点" prop="place"><el-input v-model="form.place" placeholder="请输入地点" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="次数" prop="times">
            <el-select v-model="form.times" placeholder="请选择次数" style="width:100%">
              <el-option v-for="item in inquiryTimesOptions" :key="item.value" :label="item.label" :value="Number(item.value)" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="4"><el-form-item label="问题数" prop="questionCount"><el-input-number v-model="form.questionCount" :min="0" :max="10" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-divider content-position="left">人员信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="询问人" prop="inquirer">
            <el-select v-model="form.inquirer" placeholder="请选择询问人" filterable style="width:100%">
              <el-option v-for="item in inspectorOptions" :key="item.id" :label="item.name + ' 执法证号：' + item.certNo" :value="item.name" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="询问人副本" prop="inquirerCopy">
            <el-select v-model="form.inquirerCopy" placeholder="请选择询问人副本" filterable style="width:100%">
              <el-option v-for="item in inspectorOptions" :key="item.id" :label="item.name + ' 执法证号：' + item.certNo" :value="item.name" />
            </el-select>
          </el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="当事人" prop="party"><el-input v-model="form.party" placeholder="请输入当事人" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="性别" prop="gender">
            <el-select v-model="form.gender" placeholder="请选择性别" style="width:100%">
              <el-option v-for="item in genderOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="职务" prop="position"><el-input v-model="form.position" placeholder="请输入职务" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="其他联系方式" prop="otherContact"><el-input v-model="form.otherContact" placeholder="请输入其他联系方式" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="询问人1" prop="inquirer1"><el-input v-model="form.inquirer1" placeholder="请输入询问人1" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="被询问人1" prop="respondent1"><el-input v-model="form.respondent1" placeholder="请输入被询问人1" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="询问人2" prop="inquirer2"><el-input v-model="form.inquirer2" placeholder="请输入询问人2" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="被询问人2" prop="respondent2"><el-input v-model="form.respondent2" placeholder="请输入被询问人2" /></el-form-item></el-col>
        </el-row>
        <el-divider content-position="left">一问一答记录</el-divider>
        <el-collapse accordion>
          <el-collapse-item v-for="n in 10" :key="n" :name="n">
            <template #title>
              <span style="font-weight: 500">第{{ n }}组</span>
              <span v-if="form['question' + n]" style="color: #909399; margin-left: 12px; font-size: 13px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 400px">
                {{ form['question' + n].substring(0, 50) }}{{ form['question' + n].length > 50 ? '...' : '' }}
              </span>
            </template>
            <el-form-item :label="'问题' + n" :prop="'question' + n"><el-input v-model="form['question' + n]" type="textarea" :placeholder="'请输入问题' + n" :rows="2" /></el-form-item>
            <el-form-item :label="'回答' + n" :prop="'answer' + n"><el-input v-model="form['answer' + n]" type="textarea" :placeholder="'请输入回答' + n" :rows="3" /></el-form-item>
          </el-collapse-item>
        </el-collapse>
        <el-divider content-position="left">其他</el-divider>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="状态" prop="status"><el-select v-model="form.status" placeholder="请选择状态" style="width:100%"><el-option v-for="item in inspectionStatusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="备注" prop="remark"><el-input v-model="form.remark" placeholder="请输入备注" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup name="BusinessTableInquiryNotes" lang="ts">
import { ref, reactive, onMounted, watch, getCurrentInstance, ComponentInternalInstance } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRoute, useRouter } from 'vue-router';
import { listInquiryNotes, getInquiryNotes, addInquiryNotes, updateInquiryNotes, delInquiryNotes, exportInquiryNotes } from '@/api/business/inquiry-notes/index';
import { getAllInspector } from '@/api/business/inspector/index';
import { getBizDictByType } from '@/api/business/dict/index';
const { proxy } = getCurrentInstance() as ComponentInternalInstance; const route = useRoute(); const router = useRouter();
const showSearch = ref(true); const loading = ref(false); const submitLoading = ref(false); const single = ref(true); const multiple = ref(true); const total = ref(0); const ids = ref<string[]>([]); const tableData = ref<any[]>([]); const formRef = ref(); const dialogVisible = ref(false); const dialogTitle = ref(''); const inspectorOptions = ref<any[]>([]);
const inspectionStatusOptions = ref<any[]>([]);
const genderOptions = ref<any[]>([]);
const inquiryTimesOptions = ref<any[]>([]);
const getDictLabel = (options: any[], value: string) => options.find(o => o.value === value)?.label ?? value;
const getDictTagType = (options: any[], value: string) => options.find(o => o.value === value)?.tagType ?? '';
const queryParams = reactive({ pageNum: 1, pageSize: 10, place: '', party: '', inquirer: '', status: '', inspectionId: '' });
const initForm: any = {
  startTime: '', endTime: '', times: undefined, questionCount: undefined, place: '', inquirer: '', inquirerCopy: '',
  party: '', gender: '', otherContact: '', position: '', inquirer1: '', respondent1: '', inquirer2: '', respondent2: '',
  question1: '', answer1: '', question2: '', answer2: '', question3: '', answer3: '', question4: '', answer4: '',
  question5: '', answer5: '', question6: '', answer6: '', question7: '', answer7: '', question8: '', answer8: '',
  question9: '', answer9: '', question10: '', answer10: '', status: '0', remark: ''
};
const form = reactive({ ...initForm });
const rules = {
  place: [{ required: true, message: '请输入地点', trigger: 'blur' }],
  party: [{ required: true, message: '请输入当事人', trigger: 'blur' }]
};
const getList = async () => { loading.value = true; try { const res = await listInquiryNotes(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; } catch { /* 拦截器已处理错误 */ } finally { loading.value = false; } };
const handleQuery = () => { queryParams.pageNum = 1; getList(); };
const resetQuery = () => { Object.assign(queryParams, { pageNum: 1, pageSize: 10, place: '', party: '', inquirer: '', status: '', inspectionId: '' }); handleQuery(); };
watch(() => route.query.inspectionId, (val) => { if (val) { queryParams.inspectionId = val as string; handleQuery(); } }, { immediate: true });
const handleSelectionChange = (sel: any[]) => { ids.value = sel.map((i: any) => i.id); single.value = sel.length !== 1; multiple.value = !sel.length; };
const handleAdd = () => { Object.assign(form, { ...initForm, id: undefined, inspectionId: queryParams.inspectionId || undefined }); dialogTitle.value = '新增询问笔录'; dialogVisible.value = true; };
const handleUpdate = async (row?: any) => { const id = row?.id || (ids.value.length === 1 ? ids.value[0] : null); if (!id) return; const res = await getInquiryNotes(id); Object.assign(form, res.data); dialogTitle.value = '修改询问笔录'; dialogVisible.value = true; };
const handleDelete = (row?: any) => { const deleteIds = row?.id ? row.id : ids.value.join(','); const msg = row?.id ? '确认删除？' : '确认删除所选数据？'; ElMessageBox.confirm(msg, '提示', { type: 'warning' }).then(async () => { await delInquiryNotes(deleteIds); ElMessage.success('删除成功'); getList(); }); };
const handleExport = async () => { await exportInquiryNotes(queryParams); ElMessage.success('导出成功'); };
const submitForm = async () => { try { await formRef.value.validate(); } catch { return; } submitLoading.value = true; try { await (form.id ? updateInquiryNotes(form) : addInquiryNotes(form)); ElMessage.success(form.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList(); } catch (e) { console.error(e); } finally { submitLoading.value = false; } };
const loadInspectorOptions = async () => { try { const res = await getAllInspector(); inspectorOptions.value = res.data || []; } catch { /* ignore */ } };
const loadDictOptions = () => {
  getBizDictByType('inspection_status').then(res => { inspectionStatusOptions.value = res.data || []; });
  getBizDictByType('gender').then(res => { genderOptions.value = res.data || []; });
  getBizDictByType('inquiry_times').then(res => { inquiryTimesOptions.value = res.data || []; });
};
onMounted(() => { loadInspectorOptions(); loadDictOptions(); });
getList();
</script>
