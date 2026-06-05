<template>
  <div class="p-2">
    <div v-show="showSearch" class="mb-[10px]">
      <el-card shadow="hover">
        <el-form ref="queryFormRef" :model="queryParams" :inline="true">
          <el-form-item label="被检查单位" prop="inspectedUnit">
            <el-input v-model="queryParams.inspectedUnit" placeholder="请输入被检查单位" clearable @keyup.enter="handleQuery" />
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
          <template #default="scope">{{ scope.row.issuingAgency || '某市' }}监特令〔{{ scope.row.docYear }}〕{{ scope.row.docSeq }}第{{ scope.row.docNumber }}号</template>
        </el-table-column>
        <el-table-column label="被检查单位" align="center" prop="inspectedUnit" min-width="150" show-overflow-tooltip />
        <el-table-column label="违反条款" align="center" prop="violationArticle" width="120" show-overflow-tooltip />
        <el-table-column label="整改截止" align="center" prop="deadlineDate" width="120" />
        <el-table-column label="检查人员" align="center" prop="inspectorSign" width="100" />
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

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="900px" append-to-body destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-divider content-position="left">文书信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="发出部门"><el-input v-model="form.issuingAgency" placeholder="如汾阳市市场监督管理局" /></el-form-item></el-col>
          <el-col :span="5"><el-form-item label="年份" prop="docYear"><el-input v-model="form.docYear" placeholder="2026" /></el-form-item></el-col>
          <el-col :span="5"><el-form-item label="文书序号" prop="docSeq"><el-input v-model="form.docSeq" placeholder="序号" /></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="文书编号"><el-input v-model="form.docNumber" placeholder="编号" /></el-form-item></el-col>
        </el-row>

        <el-divider content-position="left">检查信息</el-divider>
        <el-form-item label="被检查单位" prop="inspectedUnit">
          <el-input v-model="form.inspectedUnit" placeholder="被检查单位名称" />
        </el-form-item>
        <el-form-item label="存在的问题">
          <el-input v-model="form.problemsFound" type="textarea" :rows="3" placeholder="经检查，你（单位）在特种设备安全方面存在下列问题：" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="违反规定"><el-input v-model="form.violationDesc" placeholder="违反的规定名称" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="违反条款"><el-input v-model="form.violationArticle" placeholder="第X条、第Y条" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="依据规定"><el-input v-model="form.basisRule" type="textarea" :rows="2" placeholder="根据...的规定" /></el-form-item>

        <el-divider content-position="left">整改要求</el-divider>
        <el-form-item label="整改截止日期"><el-date-picker v-model="form.deadlineDate" type="date" placeholder="选择整改截止日期" value-format="YYYY-MM-DD" style="width:250px" /></el-form-item>
        <el-form-item label="改正措施">
          <el-input v-model="form.correctiveMeasures" type="textarea" :rows="3" placeholder="责令你（单位）于X年X月X日前采取以下措施予以改正或者消除事故隐患：" />
        </el-form-item>

        <el-divider content-position="left">救济途径</el-divider>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="复议机关"><el-input v-model="form.appealGov" placeholder="人民政府" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="诉讼法院"><el-input v-model="form.appealCourt" placeholder="人民法院" /></el-form-item></el-col>
        </el-row>

        <el-divider content-position="left">签署信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="检查人员签名"><el-input v-model="form.inspectorSign" placeholder="检查人员签名" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="被检查单位签名"><el-input v-model="form.unitSign" placeholder="负责人签名" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="公章信息"><el-input v-model="form.sealInfo" placeholder="公章/专用章信息" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="签署日期"><el-date-picker v-model="form.signDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" :rows="2" placeholder="备注" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="BusinessTableEquipmentSafetyDirective" lang="ts">
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listEquipmentSafetyDirective, getEquipmentSafetyDirective, addEquipmentSafetyDirective, updateEquipmentSafetyDirective, delEquipmentSafetyDirective } from '@/api/business/equipment-safety-directive/index';

const showSearch = ref(true); const loading = ref(false); const submitLoading = ref(false);
const single = ref(true); const multiple = ref(true); const total = ref(0);
const ids = ref<string[]>([]); const tableData = ref<any[]>([]);
const formRef = ref(); const dialogVisible = ref(false); const dialogTitle = ref('');

const queryParams = reactive({ pageNum: 1, pageSize: 10, inspectedUnit: '' });

const initForm: any = {
  issuingAgency: '汾阳市市场监督管理局', docYear: '', docSeq: '', docNumber: '',
  inspectedUnit: '', problemsFound: '', violationDesc: '', violationArticle: '',
  basisRule: '', deadlineDate: '', correctiveMeasures: '',
  appealGov: '汾阳市人民政府', appealCourt: '汾阳市人民法院',
  inspectorSign: '', unitSign: '', sealInfo: '特种设备安全监察专用章', signDate: '', remark: '',
};
const form = reactive<any>({ ...initForm });

const rules = {
  docYear: [{ required: true, message: '请输入年份', trigger: 'blur' }],
  docSeq: [{ required: true, message: '请输入文书序号', trigger: 'blur' }],
  inspectedUnit: [{ required: true, message: '请输入被检查单位', trigger: 'blur' }],
};

const getList = async () => {
  loading.value = true;
  try { const res = await listEquipmentSafetyDirective(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; }
  catch { /* ignore */ } finally { loading.value = false; }
};

const handleQuery = () => { queryParams.pageNum = 1; getList(); };
const resetQuery = () => { Object.assign(queryParams, { pageNum: 1, pageSize: 10, inspectedUnit: '' }); handleQuery(); };
const handleSelectionChange = (sel: any[]) => { ids.value = sel.map((i: any) => i.id); single.value = sel.length !== 1; multiple.value = !sel.length; };

const resetForm = () => { Object.assign(form, JSON.parse(JSON.stringify(initForm))); };

const handleAdd = () => { resetForm(); dialogTitle.value = '新增特种设备安全监察指令书'; dialogVisible.value = true; };

const handleUpdate = (row?: any) => {
  resetForm();
  const id = row?.id || ids.value[0];
  if (!id) { ElMessage.warning('请选择一条记录'); return; }
  dialogTitle.value = '修改特种设备安全监察指令书';
  getEquipmentSafetyDirective(id).then(res => {
    if (res.code === 200) Object.assign(form, res.data);
    dialogVisible.value = true;
  });
};

const submitForm = async () => {
  const valid = await formRef.value?.validate().catch(() => false);
  if (!valid) return;
  submitLoading.value = true;
  try {
    const api = form.id ? updateEquipmentSafetyDirective : addEquipmentSafetyDirective;
    const res = await api(form);
    if (res.code === 200) { ElMessage.success(form.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList(); }
    else { ElMessage.error(res.msg || '操作失败'); }
  } finally { submitLoading.value = false; }
};

const handleDelete = (row?: any) => {
  const delIds = row?.id ? [row.id] : ids.value;
  if (!delIds.length) { ElMessage.warning('请选择记录'); return; }
  ElMessageBox.confirm('确认删除选中的记录？', '提示', { type: 'warning' }).then(() => {
    delEquipmentSafetyDirective(delIds.join(',')).then(res => {
      if (res.code === 200) { ElMessage.success('删除成功'); getList(); }
      else { ElMessage.error(res.msg || '删除失败'); }
    });
  }).catch(() => {});
};

getList();
</script>
