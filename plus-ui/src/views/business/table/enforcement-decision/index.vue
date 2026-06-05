<template>
  <div class="p-2">
    <div v-show="showSearch" class="mb-[10px]">
      <el-card shadow="hover">
        <el-form ref="queryFormRef" :model="queryParams" :inline="true">
          <el-form-item label="当事人" prop="partyName">
            <el-input v-model="queryParams.partyName" placeholder="请输入当事人" clearable @keyup.enter="handleQuery" />
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
          <right-toolbar v-model:show-search="showSearch" @query-table="getList" />
        </el-row>
      </template>
      <el-table v-loading="loading" border :data="tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="文书编号" align="center" min-width="180" show-overflow-tooltip>
          <template #default="scope">一市监-〔{{ scope.row.year }}〕{{ scope.row.docSeq }}{{ scope.row.docNumber }}号</template>
        </el-table-column>
        <el-table-column label="当事人" align="center" prop="partyName" min-width="120" show-overflow-tooltip />
        <el-table-column label="统一社会信用代码" align="center" prop="creditCode" width="180" show-overflow-tooltip />
        <el-table-column label="法定代表人" align="center" prop="legalPerson" width="100" />
        <el-table-column label="联系电话" align="center" prop="phone" width="120" />
        <el-table-column label="强制措施地点" align="center" prop="enforceLocation" min-width="150" show-overflow-tooltip />
        <el-table-column label="期限(日)" align="center" prop="enforceDuration" width="80" />
        <el-table-column label="签署日期" align="center" prop="signDate" width="120" />
        <el-table-column label="创建时间" align="center" prop="createTime" width="170" />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="1000px" append-to-body destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="140px">
        <el-divider content-position="left">文书信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="年份" prop="year"><el-input v-model="form.year" placeholder="如2026" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="文书序号" prop="docSeq"><el-input v-model="form.docSeq" placeholder="文书序号" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="文书编号"><el-input v-model="form.docNumber" placeholder="文书编号" /></el-form-item></el-col>
        </el-row>

        <el-divider content-position="left">当事人信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="当事人" prop="partyName"><el-input v-model="form.partyName" placeholder="当事人（单位）名称" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="主体资格证照名称"><el-input v-model="form.licenseName" placeholder="营业执照等" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="统一社会信用代码"><el-input v-model="form.creditCode" placeholder="统一社会信用代码或注册号" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="法定代表人"><el-input v-model="form.legalPerson" placeholder="法定代表人/负责人/经营者" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="住所（住址）"><el-input v-model="form.address" placeholder="住所或住址" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="身份证号码"><el-input v-model="form.idNumber" placeholder="身份证号" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="联系电话"><el-input v-model="form.phone" placeholder="当事人电话" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="其他联系方式"><el-input v-model="form.otherContact" placeholder="其他联系方式" /></el-form-item></el-col>
        </el-row>

        <el-divider content-position="left">违法行为与依据</el-divider>
        <el-form-item label="涉嫌违法描述"><el-input v-model="form.violationDesc" type="textarea" :rows="2" placeholder="经查，你（单位）涉嫌..." /></el-form-item>
        <el-form-item label="法律依据"><el-input v-model="form.legalBasis" type="textarea" :rows="2" placeholder="本局依据..." /></el-form-item>

        <el-divider content-position="left">强制措施</el-divider>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="财物清单编号"><el-input v-model="form.facilityListNo" placeholder="关联的场所/设施/财物清单文书编号" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="期限（日）"><el-input-number v-model="form.enforceDuration" :min="1" :max="365" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="强制措施地点"><el-input v-model="form.enforceLocation" type="textarea" :rows="1" placeholder="实施行政强制措施的场所/设施/财物地点" /></el-form-item>

        <el-divider content-position="left">救济途径</el-divider>
        <el-row :gutter="20">
          <el-col :span="6"><el-form-item label="复议期限(日)"><el-input v-model="form.appealDays" placeholder="60" /></el-form-item></el-col>
          <el-col :span="9"><el-form-item label="复议政府"><el-input v-model="form.appealGov" placeholder="人民政府" /></el-form-item></el-col>
          <el-col :span="9"><el-form-item label="复议机关"><el-input v-model="form.appealAgency" placeholder="市场监督管理局" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6"><el-form-item label="诉讼期限(日)"><el-input v-model="form.lawsuitDays" placeholder="6个月" /></el-form-item></el-col>
          <el-col :span="9"><el-form-item label="诉讼法院"><el-input v-model="form.lawsuitCourt" placeholder="人民法院" /></el-form-item></el-col>
        </el-row>

        <el-divider content-position="left">机关信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="机关联系人"><el-input v-model="form.contactPerson" placeholder="联系人" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="机关联系电话"><el-input v-model="form.contactPhone" placeholder="联系电话" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="签署日期"><el-input v-model="form.signDate" placeholder="如2026年6月3日" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="附件清单编号"><el-input v-model="form.attachmentListNo" placeholder="附件清单文书编号" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="文书份数"><el-input v-model="form.copyCount" placeholder="份数" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="送达份数"><el-input v-model="form.deliveryCount" placeholder="送达份数" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="份数备注"><el-input v-model="form.copyNote" placeholder="份数备注" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="BusinessTableEnforcementDecision" lang="ts">
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listEnforcementDecision, getEnforcementDecision, addEnforcementDecision, updateEnforcementDecision, delEnforcementDecision } from '@/api/business/enforcement-decision/index';

const showSearch = ref(true); const loading = ref(false); const submitLoading = ref(false);
const single = ref(true); const multiple = ref(true); const total = ref(0);
const ids = ref<string[]>([]); const tableData = ref<any[]>([]);
const formRef = ref(); const dialogVisible = ref(false); const dialogTitle = ref('');

const queryParams = reactive({ pageNum: 1, pageSize: 10, partyName: '' });

const initForm: any = {
  year: '', docSeq: '', docNumber: '',
  partyName: '', licenseName: '', creditCode: '', address: '', legalPerson: '',
  idNumber: '', phone: '', otherContact: '',
  violationDesc: '', legalBasis: '',
  facilityListNo: '', enforceLocation: '', enforceDuration: undefined,
  appealDays: '', appealGov: '', appealAgency: '', lawsuitDays: '', lawsuitCourt: '',
  contactPerson: '', contactPhone: '', attachmentListNo: '',
  signDate: '', copyCount: '', deliveryCount: '', copyNote: '',
  status: '0', remark: '',
};
const form = reactive<any>({ ...initForm });

const rules = {
  partyName: [{ required: true, message: '请输入当事人', trigger: 'blur' }],
  year: [{ required: true, message: '请输入年份', trigger: 'blur' }],
  docSeq: [{ required: true, message: '请输入文书序号', trigger: 'blur' }],
};

const getList = async () => {
  loading.value = true;
  try { const res = await listEnforcementDecision(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; }
  catch { /* ignore */ } finally { loading.value = false; }
};

const handleQuery = () => { queryParams.pageNum = 1; getList(); };
const resetQuery = () => { Object.assign(queryParams, { pageNum: 1, pageSize: 10, partyName: '' }); handleQuery(); };
const handleSelectionChange = (sel: any[]) => { ids.value = sel.map((i: any) => i.id); single.value = sel.length !== 1; multiple.value = !sel.length; };

const resetForm = () => { Object.assign(form, JSON.parse(JSON.stringify(initForm))); };

const handleAdd = () => {
  resetForm(); dialogTitle.value = '新增实施行政强制措施决定书'; dialogVisible.value = true;
};

const handleUpdate = (row?: any) => {
  resetForm();
  const id = row?.id || ids.value[0];
  if (!id) { ElMessage.warning('请选择一条记录'); return; }
  dialogTitle.value = '修改实施行政强制措施决定书';
  getEnforcementDecision(id).then(res => {
    if (res.code === 200) Object.assign(form, res.data);
    dialogVisible.value = true;
  });
};

const submitForm = async () => {
  const valid = await formRef.value?.validate().catch(() => false);
  if (!valid) return;
  submitLoading.value = true;
  try {
    const api = form.id ? updateEnforcementDecision : addEnforcementDecision;
    const res = await api(form);
    if (res.code === 200) { ElMessage.success(form.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList(); }
    else { ElMessage.error(res.msg || '操作失败'); }
  } finally { submitLoading.value = false; }
};

const handleDelete = (row?: any) => {
  const delIds = row?.id ? [row.id] : ids.value;
  if (!delIds.length) { ElMessage.warning('请选择记录'); return; }
  ElMessageBox.confirm('确认删除选中的记录？', '提示', { type: 'warning' }).then(() => {
    delEnforcementDecision(delIds.join(',')).then(res => {
      if (res.code === 200) { ElMessage.success('删除成功'); getList(); }
      else { ElMessage.error(res.msg || '删除失败'); }
    });
  }).catch(() => {});
};

getList();
</script>
