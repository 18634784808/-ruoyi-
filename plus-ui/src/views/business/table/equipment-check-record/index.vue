<template>
  <div class="p-2">
    <div v-show="showSearch" class="mb-[10px]">
      <el-card shadow="hover">
        <el-form :model="queryParams" :inline="true">
          <el-form-item label="单位名称" prop="unitName">
            <el-input v-model="queryParams.unitName" placeholder="请输入单位名称" clearable @keyup.enter="handleQuery" />
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
        <el-table-column label="单位名称" prop="unitName" min-width="160" show-overflow-tooltip />
        <el-table-column label="检查类别" prop="inspectionType" width="100" />
        <el-table-column label="设备名称" prop="equipmentName" min-width="140" show-overflow-tooltip />
        <el-table-column label="检查人员" prop="inspector" width="100" />
        <el-table-column label="记录员" prop="recorder" width="80" />
        <el-table-column label="处理措施" prop="disposition" width="120" show-overflow-tooltip />
        <el-table-column label="检查日期" prop="recordDate" width="120" />
        <el-table-column label="创建时间" prop="createTime" width="170" />
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
        <el-divider content-position="left">基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="检查类别" prop="inspectionType">
            <el-select v-model="form.inspectionType" placeholder="请选择" style="width:100%">
              <el-option label="常规监督检查" value="常规监督检查" />
              <el-option label="专项监督检查" value="专项监督检查" />
              <el-option label="证后监督检查" value="证后监督检查" />
              <el-option label="其他情形" value="其他情形" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="8"><el-form-item label="检查人员"><el-input v-model="form.inspector" placeholder="检查人员" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="记录员"><el-input v-model="form.recorder" placeholder="记录员" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="检查开始时间"><el-date-picker v-model="form.inspectionStartTime" type="datetime" placeholder="开始时间" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="检查结束时间"><el-date-picker v-model="form.inspectionEndTime" type="datetime" placeholder="结束时间" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="日期"><el-date-picker v-model="form.recordDate" type="date" placeholder="日期" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
        </el-row>

        <el-divider content-position="left">被检查单位信息</el-divider>
        <el-form-item label="单位名称" prop="unitName"><el-input v-model="form.unitName" placeholder="请输入单位名称" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="统一社会信用代码"><el-input v-model="form.creditCode" placeholder="统一社会信用代码" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="单位地址"><el-input v-model="form.unitAddress" placeholder="单位地址" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="法定代表人"><el-input v-model="form.legalRep" placeholder="法定代表人" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="安全管理负责人"><el-input v-model="form.safetyManager" placeholder="安全管理负责人" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="联系人"><el-input v-model="form.contactPerson" placeholder="联系人" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="职务"><el-input v-model="form.contactPosition" placeholder="职务" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="联系电话"><el-input v-model="form.contactPhone" placeholder="联系电话" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="单位类别">
            <el-select v-model="form.unitCategory" multiple placeholder="请选择(可多选)" style="width:100%">
              <el-option v-for="c in unitCategoryOptions" :key="c" :label="c" :value="c" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="抽查设备名称"><el-input v-model="form.equipmentName" placeholder="抽查设备名称" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="使用登记代码"><el-input v-model="form.registrationCode" placeholder="使用登记代码/产品编号" /></el-form-item>

        <el-divider content-position="left">检查情况</el-divider>
        <el-form-item label="发现问题"><el-input v-model="form.findings" type="textarea" :rows="4" placeholder="检查情况及发现的问题" /></el-form-item>
        <el-form-item label="处理措施">
          <el-checkbox-group v-model="dispositionList">
            <el-checkbox label="下达监察指令书" />
            <el-checkbox label="实施查封" />
            <el-checkbox label="实施扣押" />
            <el-checkbox label="其他" />
          </el-checkbox-group>
        </el-form-item>

        <el-divider content-position="left">被检查单位意见</el-divider>
        <el-form-item label="意见"><el-input v-model="form.unitOpinion" type="textarea" :rows="2" placeholder="被检查单位意见" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="签名"><el-input v-model="form.unitSign" placeholder="签名" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="签名日期"><el-date-picker v-model="form.signDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
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

<script setup name="BusinessTableEquipmentCheckRecord" lang="ts">
import { ref, reactive, watch } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listEquipmentCheckRecord, getEquipmentCheckRecord, addEquipmentCheckRecord, updateEquipmentCheckRecord, delEquipmentCheckRecord } from '@/api/business/equipment-check-record/index';

const showSearch = ref(true); const loading = ref(false); const submitLoading = ref(false);
const single = ref(true); const multiple = ref(true); const total = ref(0);
const ids = ref<string[]>([]); const tableData = ref<any[]>([]);
const formRef = ref(); const dialogVisible = ref(false); const dialogTitle = ref('');
const dispositionList = ref<string[]>([]);

const unitCategoryOptions = ['设计','制造','安装','改造','修理','维保','使用','充装','经营','检验','检测','其他'];

const queryParams = reactive({ pageNum: 1, pageSize: 10, unitName: '' });

const initForm: any = {
  inspector: '', recorder: '', recordDate: '', inspectionType: '',
  inspectionStartTime: '', inspectionEndTime: '',
  unitName: '', creditCode: '', legalRep: '', safetyManager: '',
  unitAddress: '', contactPerson: '', contactPosition: '', contactPhone: '',
  unitCategory: '', equipmentName: '', registrationCode: '',
  findings: '', disposition: '', unitOpinion: '', unitSign: '', signDate: '', remark: '',
};
const form = reactive<any>({ ...initForm });

const rules = {
  unitName: [{ required: true, message: '请输入单位名称', trigger: 'blur' }],
  inspectionType: [{ required: true, message: '请选择检查类别', trigger: 'change' }],
};

watch(dispositionList, (v) => { form.disposition = v.join('、'); });

const resetForm = () => {
  Object.assign(form, JSON.parse(JSON.stringify(initForm)));
  dispositionList.value = [];
};

const getList = async () => {
  loading.value = true;
  try { const res = await listEquipmentCheckRecord(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; }
  catch { /* ignore */ } finally { loading.value = false; }
};

const handleQuery = () => { queryParams.pageNum = 1; getList(); };
const resetQuery = () => { Object.assign(queryParams, { pageNum: 1, pageSize: 10, unitName: '' }); handleQuery(); };
const handleSelectionChange = (sel: any[]) => { ids.value = sel.map(i => i.id); single.value = sel.length !== 1; multiple.value = !sel.length; };

const handleAdd = () => { resetForm(); dialogTitle.value = '新增特种设备安全监督检查记录'; dialogVisible.value = true; };

const handleUpdate = (row?: any) => {
  resetForm();
  const id = row?.id || ids.value[0];
  if (!id) { ElMessage.warning('请选择一条记录'); return; }
  dialogTitle.value = '修改特种设备安全监督检查记录';
  getEquipmentCheckRecord(id).then(res => {
    if (res.code === 200) {
      Object.assign(form, res.data);
      if (res.data.disposition) dispositionList.value = res.data.disposition.split('、').filter(Boolean);
    }
    dialogVisible.value = true;
  });
};

const submitForm = async () => {
  const valid = await formRef.value?.validate().catch(() => false);
  if (!valid) return;
  submitLoading.value = true;
  try {
    const api = form.id ? updateEquipmentCheckRecord : addEquipmentCheckRecord;
    const res = await api(form);
    if (res.code === 200) { ElMessage.success(form.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList(); }
    else { ElMessage.error(res.msg || '操作失败'); }
  } finally { submitLoading.value = false; }
};

const handleDelete = (row?: any) => {
  const delIds = row?.id ? [row.id] : ids.value;
  if (!delIds.length) { ElMessage.warning('请选择记录'); return; }
  ElMessageBox.confirm('确认删除选中的记录？', '提示', { type: 'warning' }).then(() => {
    delEquipmentCheckRecord(delIds.join(',')).then(res => {
      if (res.code === 200) { ElMessage.success('删除成功'); getList(); }
      else { ElMessage.error(res.msg || '删除失败'); }
    });
  }).catch(() => {});
};

getList();
</script>
