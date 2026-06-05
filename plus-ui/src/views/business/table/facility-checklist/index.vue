<template>
  <div class="p-2">
    <div v-show="showSearch" class="mb-[10px]">
      <el-card shadow="hover">
        <el-form ref="queryFormRef" :model="queryParams" :inline="true">
          <el-form-item label="文书编号" prop="documentNo">
            <el-input v-model="queryParams.documentNo" placeholder="请输入文书编号" clearable @keyup.enter="handleQuery" />
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
        <el-table-column label="文书编号" align="center" prop="documentNo" width="180" show-overflow-tooltip />
        <el-table-column label="当事人" align="center" prop="partySignature" width="100" />
        <el-table-column label="执法人员" align="center" prop="officerSignature" width="100" />
        <el-table-column label="物品数量" align="center" width="90">
          <template #default="scope">{{ scope.row.items?.length || 0 }}</template>
        </el-table-column>
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
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="文书编号" prop="documentNo"><el-input v-model="form.documentNo" placeholder="请输入文书编号" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="份数"><el-input-number v-model="form.copiesCount" :min="1" :max="10" style="width:100%" /></el-form-item></el-col>
          <el-col :span="4"><el-form-item label="第" label-width="30px"><el-input-number v-model="form.pageNo" :min="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="4"><el-form-item label="共" label-width="30px"><el-input-number v-model="form.totalPages" :min="1" style="width:100%" /></el-form-item></el-col>
        </el-row>

        <!-- 明细表格 -->
        <el-form-item label="物品清单" prop="items" class="mt-2">
          <el-table :data="form.items" border size="small" style="width:100%">
            <el-table-column label="序号" width="60" align="center">
              <template #default="scope">{{ scope.$index + 1 }}</template>
            </el-table-column>
            <el-table-column label="标称名称/场所" min-width="160">
              <template #default="scope"><el-input v-model="scope.row.nameOrVenue" placeholder="标称名称/场所" size="small" /></template>
            </el-table-column>
            <el-table-column label="规格(型号)/场所地址" min-width="180">
              <template #default="scope"><el-input v-model="scope.row.specOrAddress" placeholder="规格/地址" size="small" /></template>
            </el-table-column>
            <el-table-column label="单位" width="80">
              <template #default="scope"><el-input v-model="scope.row.unit" placeholder="单位" size="small" /></template>
            </el-table-column>
            <el-table-column label="数量" width="80">
              <template #default="scope"><el-input v-model="scope.row.quantity" placeholder="数量" size="small" /></template>
            </el-table-column>
            <el-table-column label="备注" min-width="120">
              <template #default="scope"><el-input v-model="scope.row.remark" placeholder="备注" size="small" /></template>
            </el-table-column>
            <el-table-column label="操作" width="70" align="center">
              <template #default="scope">
                <el-button link type="danger" size="small" icon="Delete" @click="removeItem(scope.$index)"></el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-button type="dashed" icon="Plus" size="small" class="mt-2" @click="addItem">添加一行</el-button>
        </el-form-item>

        <el-divider />
        <el-form-item label="核对确认"><el-input v-model="form.confirmationText" placeholder="上述物品品种、数量经核对无误。" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="当事人签名"><el-input v-model="form.partySignature" placeholder="当事人" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="当事人日期"><el-date-picker v-model="form.partyDate" type="date" value-format="YYYY-MM-DD" placeholder="日期" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="执法人员签名"><el-input v-model="form.officerSignature" placeholder="执法人员" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="执法人员日期"><el-date-picker v-model="form.officerDate" type="date" value-format="YYYY-MM-DD" placeholder="日期" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="见证人签名"><el-input v-model="form.witnessSignature" placeholder="见证人" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="2" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="BusinessTableFacilityChecklist" lang="ts">
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listFacilityChecklist, getFacilityChecklist, addFacilityChecklist, updateFacilityChecklist, delFacilityChecklist } from '@/api/business/facility-checklist/index';
import type { FacilityChecklistItemVO } from '@/api/business/facility-checklist/types';

const showSearch = ref(true); const loading = ref(false); const submitLoading = ref(false);
const single = ref(true); const multiple = ref(true); const total = ref(0);
const ids = ref<string[]>([]); const tableData = ref<any[]>([]);
const formRef = ref(); const dialogVisible = ref(false); const dialogTitle = ref('');

const queryParams = reactive({ pageNum: 1, pageSize: 10, documentNo: '' });

const emptyItem = (): FacilityChecklistItemVO => ({
  seqNo: 0, nameOrVenue: '', specOrAddress: '', unit: '', quantity: '', remark: '',
});

const initForm: any = {
  documentNo: '', confirmationText: '上述物品品种、数量经核对无误。',
  partySignature: '', officerSignature: '', witnessSignature: '',
  partyDate: '', officerDate: '', copiesCount: 2, pageNo: 1, totalPages: 1,
  status: '0', remark: '', items: [emptyItem()],
};
const form = reactive<any>({ ...initForm, items: [{ ...emptyItem() }] });

const rules = {
  documentNo: [{ required: true, message: '请输入文书编号', trigger: 'blur' }],
};

const getList = async () => {
  loading.value = true;
  try { const res = await listFacilityChecklist(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; }
  catch { /* ignore */ } finally { loading.value = false; }
};

const handleQuery = () => { queryParams.pageNum = 1; getList(); };
const resetQuery = () => { Object.assign(queryParams, { pageNum: 1, pageSize: 10, documentNo: '' }); handleQuery(); };
const handleSelectionChange = (sel: any[]) => { ids.value = sel.map((i: any) => i.id); single.value = sel.length !== 1; multiple.value = !sel.length; };

const addItem = () => { form.items.push({ ...emptyItem() }); };
const removeItem = (idx: number) => { if (form.items.length > 1) form.items.splice(idx, 1); };

const handleAdd = () => {
  Object.assign(form, { ...initForm, id: undefined, items: [{ ...emptyItem() }] });
  dialogTitle.value = '新增场所/设施/财物清单';
  dialogVisible.value = true;
};

const handleUpdate = async (row?: any) => {
  const id = row?.id || (ids.value.length === 1 ? ids.value[0] : null);
  if (!id) return;
  try {
    const res = await getFacilityChecklist(id);
    const data = res.data;
    Object.assign(form, {
      ...data,
      items: (data.items && data.items.length > 0) ? data.items : [{ ...emptyItem() }],
    });
    dialogTitle.value = '修改场所/设施/财物清单';
    dialogVisible.value = true;
  } catch { /* ignore */ }
};

const handleDelete = (row?: any) => {
  const deleteIds = row?.id ? row.id : ids.value.join(',');
  const msg = row?.id ? '确认删除？' : '确认删除所选数据？';
  ElMessageBox.confirm(msg, '提示', { type: 'warning' }).then(async () => {
    await delFacilityChecklist(deleteIds); ElMessage.success('删除成功'); getList();
  });
};

const submitForm = async () => {
  try { await formRef.value.validate(); } catch { return; }
  submitLoading.value = true;
  try {
    form.items.forEach((item: any, idx: number) => { item.seqNo = idx + 1; });
    await (form.id ? updateFacilityChecklist(form) : addFacilityChecklist(form));
    ElMessage.success(form.id ? '修改成功' : '新增成功');
    dialogVisible.value = false;
    getList();
  } catch (e) { console.error(e); }
  finally { submitLoading.value = false; }
};

getList();
</script>
