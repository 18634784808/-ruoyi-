<template>
  <div class="p-2">
    <div class="btn-bar">
      <el-button type="primary" icon="Search" @click="handleSelect">📋 选择清单</el-button>
      <el-button type="success" icon="Printer" @click="handlePrint" :disabled="!formData.id">🖨️ 打印输出</el-button>
      <el-button type="danger" icon="Delete" @click="handleDelete" :disabled="!formData.id">🗑️ 删除</el-button>
      <el-button icon="RefreshRight" @click="handleClear">🔄 清空</el-button>
    </div>

    <div class="print-area">
    <div class="page" id="printArea">
      <div class="header">
        <div class="dept">汾阳市市场监督管理局</div>
        <div class="form-name">场所设施财物清单</div>
        <div class="doc-no">文书编号：{{ formData.documentNo }}</div>
      </div>

      <table class="main-table">
        <thead>
          <tr>
            <th class="col-seq">序号</th>
            <th class="col-name">标称名称/场所</th>
            <th class="col-spec">规格（型号）/场所地址</th>
            <th class="col-unit">单位</th>
            <th class="col-qty">数量</th>
            <th class="col-remark">备注</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, idx) in displayItems" :key="idx">
            <td class="col-seq">{{ idx + 1 }}</td>
            <td class="col-name">{{ item.nameOrVenue }}</td>
            <td class="col-spec">{{ item.specOrAddress }}</td>
            <td class="col-unit">{{ item.unit }}</td>
            <td class="col-qty">{{ item.quantity }}</td>
            <td class="col-remark">{{ item.remark }}</td>
          </tr>
          <tr class="info-row">
            <td colspan="6" style="text-align:left;">{{ formData.confirmationText || '上述物品品种、数量经核对无误。' }}</td>
          </tr>
        </tbody>
      </table>

      <div class="sign-row">
        <div class="sign-block"><span class="label">当事人（签名或者盖章）：</span>{{ formData.partySignature }}</div>
        <div class="sign-block" style="text-align:right;"><span class="date">年&emsp;&emsp;月&emsp;&emsp;日</span></div>
      </div>
      <div class="sign-row">
        <div class="sign-block"><span class="label">执法人员（签名或者盖章）：</span>{{ formData.officerSignature }}</div>
        <div class="sign-block" style="text-align:right;"><span class="date">年&emsp;&emsp;月&emsp;&emsp;日</span></div>
      </div>
      <div class="sign-row">
        <div class="sign-block" style="flex:2;"><span class="label">见证人（签名或者盖章）：</span>{{ formData.witnessSignature }}</div>
      </div>

      <div class="footer-row">
        <span>本文书一式&ensp;{{ formData.copiesCount || '__' }}&ensp;份，&ensp;{{ formData.copiesCount ? formData.copiesCount - 1 : '__' }}&ensp;份送达，一份归档，</span>
        <span>第&ensp;{{ formData.pageNo || '__' }}&ensp;页&ensp;共&ensp;{{ formData.totalPages || '__' }}&ensp;页</span>
      </div>
    </div>
    </div>

    <el-dialog v-model="selectVisible" title="选择场所设施财物清单" width="900px" append-to-body destroy-on-close>
      <el-table :data="tableData" border highlight-current-row @row-dblclick="selectRecord" v-loading="loading">
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="文书编号" prop="documentNo" min-width="180" show-overflow-tooltip />
        <el-table-column label="当事人" prop="partySignature" width="100" />
        <el-table-column label="执法人员" prop="officerSignature" width="100" />
        <el-table-column label="物品数" width="70" align="center">
          <template #default="scope">{{ scope.row.items?.length || 0 }}</template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="170" />
        <el-table-column label="操作" width="80" align="center">
          <template #default="scope"><el-button link type="primary" @click="selectRecord(scope.row)">选择</el-button></template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="loadList" />
    </el-dialog>
  </div>
</template>

<script setup name="FacilityChecklistForm" lang="ts">
import { ref, reactive, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listFacilityChecklist, getFacilityChecklist, delFacilityChecklist } from '@/api/business/facility-checklist/index';

const loading = ref(false); const selectVisible = ref(false);
const tableData = ref<any[]>([]); const total = ref(0);
const queryParams = reactive({ pageNum: 1, pageSize: 10, documentNo: '' });

const formData = reactive<any>({
  id: '', documentNo: '', confirmationText: '上述物品品种、数量经核对无误。',
  partySignature: '', officerSignature: '', witnessSignature: '',
  copiesCount: 2, pageNo: 1, totalPages: 1, items: [],
});

const displayItems = computed(() => {
  const items = formData.items || [];
  // 至少显示10行
  const rows = [...items];
  while (rows.length < 10) {
    rows.push({ nameOrVenue: '', specOrAddress: '', unit: '', quantity: '', remark: '' });
  }
  return rows;
});

const loadList = async () => {
  loading.value = true;
  try { const res = await listFacilityChecklist(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; }
  catch { /* ignore */ } finally { loading.value = false; }
};

const handleSelect = () => { queryParams.pageNum = 1; loadList(); selectVisible.value = true; };

const selectRecord = async (row: any) => {
  try {
    const res = await getFacilityChecklist(row.id);
    Object.assign(formData, res.data || {});
    selectVisible.value = false;
    ElMessage.success('已加载清单');
  } catch { /* ignore */ }
};

const handlePrint = () => { window.print(); };

const handleDelete = () => {
  if (!formData.id) return;
  ElMessageBox.confirm('确认删除该清单？', '提示', { type: 'warning' }).then(async () => {
    await delFacilityChecklist(formData.id);
    ElMessage.success('删除成功');
    clearForm();
  });
};

const clearForm = () => {
  Object.assign(formData, {
    id: '', documentNo: '', confirmationText: '上述物品品种、数量经核对无误。',
    partySignature: '', officerSignature: '', witnessSignature: '',
    copiesCount: 2, pageNo: 1, totalPages: 1, items: [],
  });
};

const handleClear = () => { clearForm(); ElMessage.success('已清空'); };
</script>

<style scoped>
.btn-bar { text-align:center; margin-bottom:10px; position:sticky; top:10px; z-index:100; }
.btn-bar .el-button { margin:0 6px; }

.print-area { background: #fff; max-width: 210mm; margin: 0 auto; box-shadow: 0 2px 12px rgba(0,0,0,.1); }

.page {
  width: 210mm;
  padding: 20mm 23mm;
  box-sizing: border-box;
  background: #fff;
  margin-bottom: 15px;
  font-family: "SimSun","宋体",serif;
  font-size: 16px;
  color: #000;
  line-height: 2;
}

.header { text-align:center; padding: 10mm 0 5mm; }
.header .dept { font-size: 22px; font-weight: bold; font-family: "SimHei","黑体",sans-serif; letter-spacing: 3px; }
.header .form-name { font-size: 22px; font-weight: bold; font-family: "SimHei","黑体",sans-serif; letter-spacing: 2px; margin-top: 3mm; }
.header .doc-no { text-align: right; font-size: 16px; margin-bottom: 3mm; }

.main-table { width: 100%; border-collapse: collapse; table-layout: fixed; }
.main-table td, .main-table th { border: 1px solid #000; font-size: 16px; padding: 8px 6px; text-align: center; vertical-align: middle; }
.main-table th { font-weight: bold; }
.main-table tbody td { height: 42px; }
.col-seq { width: 40px; }
.col-unit { width: 50px; }
.col-qty { width: 50px; }

.info-row td { padding: 3mm 5mm; line-height: 2; font-size: 16px; }

.sign-row { display: flex; margin-top: 5mm; gap: 15px; }
.sign-block { flex: 1; font-size: 16px; line-height: 2; }
.sign-block .label { font-weight: bold; }
.sign-block .date { margin-left: 6px; }

.footer-row { display: flex; justify-content: space-between; align-items: center; font-size: 14px; margin-top: 5mm; padding-bottom: 3mm; line-height: 2; }

@media print {
  .btn-bar, .noprint, .el-dialog { display: none !important; }
  @page { size: A4; margin: 0; }
  .print-area { box-shadow: none; background: #fff; }
  .page { width: 210mm; height: 297mm; padding: 20mm 23mm; box-sizing: border-box; page-break-after: always; overflow: hidden; box-shadow: none; }
  .page:last-child { page-break-after: auto; }
  body { font-family: "SimSun","宋体",serif; font-size: 16px; color: #000; line-height: 2; }
  .main-table th { background: none; }
}
</style>
