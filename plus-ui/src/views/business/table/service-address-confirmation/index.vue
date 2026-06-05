<template>
  <div class="p-2">
    <div v-show="showSearch" class="mb-[10px]">
      <el-card shadow="hover">
        <el-form ref="queryFormRef" :model="queryParams" :inline="true">
          <el-form-item label="受送达人" prop="recipientName">
            <el-input v-model="queryParams.recipientName" placeholder="请输入受送达人" clearable @keyup.enter="handleQuery" />
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
        <el-table-column label="受送达人" align="center" prop="recipientName" min-width="140" show-overflow-tooltip />
        <el-table-column label="电子送达" align="center" prop="acceptElectronic" width="80">
          <template #default="scope">{{ scope.row.acceptElectronic === '1' ? '是' : '否' }}</template>
        </el-table-column>
        <el-table-column label="收件人" align="center" prop="recipientPerson" width="100" />
        <el-table-column label="收件人电话" align="center" prop="recipientPhone" width="130" />
        <el-table-column label="送达地址" align="center" prop="serviceAddress" min-width="180" show-overflow-tooltip />
        <el-table-column label="电子邮件" align="center" prop="emailAddress" min-width="150" show-overflow-tooltip />
        <el-table-column label="签名" align="center" prop="signName" width="100" />
        <el-table-column label="签名日期" align="center" prop="signDate" width="120" />
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
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="800px" append-to-body destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="130px">
        <el-divider content-position="left">受送达人信息</el-divider>
        <el-form-item label="受送达人" prop="recipientName">
          <el-input v-model="form.recipientName" placeholder="请输入受送达人名称" />
        </el-form-item>

        <el-divider content-position="left">电子送达</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="接受电子送达">
              <el-radio-group v-model="form.acceptElectronic">
                <el-radio value="1">是</el-radio>
                <el-radio value="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="传真号码"><el-input v-model="form.faxNumber" placeholder="传真号码" /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="手机号码"><el-input v-model="form.instantAccount" placeholder="即时通讯账号" /></el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="电子邮件地址"><el-input v-model="form.emailAddress" placeholder="电子邮件地址" /></el-form-item>

        <el-divider content-position="left">送达地址</el-divider>
        <el-form-item label="送达地址" prop="serviceAddress">
          <el-input v-model="form.serviceAddress" type="textarea" :rows="2" placeholder="请输入送达地址" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="收件人"><el-input v-model="form.recipientPerson" placeholder="收件人姓名" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="收件人联系电话"><el-input v-model="form.recipientPhone" placeholder="收件人联系电话" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="邮政编码"><el-input v-model="form.postalCode" placeholder="邮政编码" /></el-form-item></el-col>
        </el-row>

        <el-divider content-position="left">签署信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="签名"><el-input v-model="form.signName" placeholder="受送达人/委托代理人" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="签名日期"><el-date-picker v-model="form.signDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
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

<script setup name="BusinessTableServiceAddressConfirmation" lang="ts">
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listServiceAddressConfirmation, getServiceAddressConfirmation, addServiceAddressConfirmation, updateServiceAddressConfirmation, delServiceAddressConfirmation } from '@/api/business/service-address-confirmation/index';

const showSearch = ref(true); const loading = ref(false); const submitLoading = ref(false);
const single = ref(true); const multiple = ref(true); const total = ref(0);
const ids = ref<string[]>([]); const tableData = ref<any[]>([]);
const formRef = ref(); const dialogVisible = ref(false); const dialogTitle = ref('');

const queryParams = reactive({ pageNum: 1, pageSize: 10, recipientName: '' });

const initForm: any = {
  recipientName: '', acceptElectronic: '0', faxNumber: '', emailAddress: '',
  instantAccount: '', serviceAddress: '', recipientPerson: '', recipientPhone: '',
  postalCode: '', signName: '', signDate: '', remark: '',
};
const form = reactive<any>({ ...initForm });

const rules = {
  recipientName: [{ required: true, message: '请输入受送达人', trigger: 'blur' }],
  serviceAddress: [{ required: true, message: '请输入送达地址', trigger: 'blur' }],
};

const getList = async () => {
  loading.value = true;
  try { const res = await listServiceAddressConfirmation(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; }
  catch { /* ignore */ } finally { loading.value = false; }
};

const handleQuery = () => { queryParams.pageNum = 1; getList(); };
const resetQuery = () => { Object.assign(queryParams, { pageNum: 1, pageSize: 10, recipientName: '' }); handleQuery(); };
const handleSelectionChange = (sel: any[]) => { ids.value = sel.map((i: any) => i.id); single.value = sel.length !== 1; multiple.value = !sel.length; };

const resetForm = () => { Object.assign(form, JSON.parse(JSON.stringify(initForm))); };

const handleAdd = () => {
  resetForm(); dialogTitle.value = '新增送达地址确认书'; dialogVisible.value = true;
};

const handleUpdate = (row?: any) => {
  resetForm();
  const id = row?.id || ids.value[0];
  if (!id) { ElMessage.warning('请选择一条记录'); return; }
  dialogTitle.value = '修改送达地址确认书';
  getServiceAddressConfirmation(id).then(res => {
    if (res.code === 200) Object.assign(form, res.data);
    dialogVisible.value = true;
  });
};

const submitForm = async () => {
  const valid = await formRef.value?.validate().catch(() => false);
  if (!valid) return;
  submitLoading.value = true;
  try {
    const api = form.id ? updateServiceAddressConfirmation : addServiceAddressConfirmation;
    const res = await api(form);
    if (res.code === 200) { ElMessage.success(form.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList(); }
    else { ElMessage.error(res.msg || '操作失败'); }
  } finally { submitLoading.value = false; }
};

const handleDelete = (row?: any) => {
  const delIds = row?.id ? [row.id] : ids.value;
  if (!delIds.length) { ElMessage.warning('请选择记录'); return; }
  ElMessageBox.confirm('确认删除选中的记录？', '提示', { type: 'warning' }).then(() => {
    delServiceAddressConfirmation(delIds.join(',')).then(res => {
      if (res.code === 200) { ElMessage.success('删除成功'); getList(); }
      else { ElMessage.error(res.msg || '删除失败'); }
    });
  }).catch(() => {});
};

getList();
</script>
