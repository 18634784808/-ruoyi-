<template>
  <div class="p-2">
    <div class="btn-bar">
      <el-button type="primary" icon="Search" @click="handleSelect">📋 选择记录</el-button>
      <el-button type="success" icon="Printer" @click="handlePrint" :disabled="!formData.id">🖨️ 打印</el-button>
      <el-button type="danger" icon="Delete" @click="handleDelete" :disabled="!formData.id">🗑️ 删除</el-button>
      <el-button icon="RefreshRight" @click="handleClear">🔄 清空</el-button>
    </div>

    <div class="print-area" id="printArea">
      <div class="page">
        <!-- 页头 -->
        <div class="header-note">附件4</div>
        <div class="doc-title">特种设备安全监督检查记录表</div>
        <div class="header-row">
          <span>检查人员：<span class="u">{{ formData.inspector || '&emsp;&emsp;' }}</span></span>
          <span>记录员：<span class="u">{{ formData.recorder || '&emsp;&emsp;' }}</span></span>
          <span>日期：<span class="u">{{ formData.recordDate || '&emsp;年&emsp;月&emsp;日' }}</span></span>
        </div>

        <!-- 主体表格 -->
        <table class="main-table">
          <!-- 基本情况标题行 -->
          <tr>
            <td class="tlabel" rowspan="10">基<br>本<br>情<br>况</td>
            <td class="tlabel" style="width:12%;">检查类别</td>
            <td colspan="3">{{ checkTypeLabel || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="tlabel">检查日期</td>
            <td colspan="3">{{ formatDate(formData.inspectionStartTime) }} 至 {{ formatDate(formData.inspectionEndTime) || '&emsp;' }}</td>
          </tr>
          <!-- 被检查单位情况 -->
          <tr>
            <td class="tlabel" rowspan="6">被<br>检<br>查<br>单<br>位<br>情<br>况</td>
            <td class="tlabel" style="width:12%;">单位名称</td>
            <td colspan="2">{{ formData.unitName || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="tlabel">统一社会信用代码</td>
            <td colspan="2">{{ formData.creditCode || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="tlabel">法定代表人</td>
            <td>{{ formData.legalRep || '&emsp;' }}</td>
            <td>安全管理负责人：{{ formData.safetyManager || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="tlabel">单位地址</td>
            <td colspan="2">{{ formData.unitAddress || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="tlabel">联系人</td>
            <td>{{ formData.contactPerson || '&emsp;' }}&emsp;职务：{{ formData.contactPosition || '&emsp;' }}</td>
            <td>联系电话：{{ formData.contactPhone || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="tlabel">类别</td>
            <td colspan="2">{{ formData.unitCategory || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="tlabel">抽查设备名称</td>
            <td colspan="3">{{ formData.equipmentName || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="tlabel">使用登记代码<br>（产品编号）</td>
            <td colspan="3">{{ formData.registrationCode || '&emsp;' }}</td>
          </tr>

          <!-- 检查情况及发现的问题 -->
          <tr>
            <td class="tlabel">检查情况<br>及发现的<br>问题</td>
            <td colspan="4" class="content-cell">{{ formData.findings || '（注：如问题较多也可另附续页）' }}</td>
          </tr>

          <!-- 处理措施 -->
          <tr>
            <td class="tlabel">处理<br>措施</td>
            <td colspan="4">
              <span class="cbox" v-for="m in dispositionOptions" :key="m" :class="{ checked: isDispositionChecked(m) }">□{{ m }}</span>
            </td>
          </tr>

          <!-- 被检查单位意见 -->
          <tr>
            <td class="tlabel" colspan="2">被检查单位意见：</td>
            <td colspan="3" class="content-cell">{{ formData.unitOpinion || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="tlabel" colspan="2">签名：</td>
            <td colspan="3">{{ formData.unitSign || '&emsp;' }}<span class="sign-date"> 日期：{{ formData.signDate || '&emsp;&emsp;年&emsp;&emsp;月&emsp;&emsp;日' }}</span></td>
          </tr>
        </table>

        <!-- 签名区 -->
        <div class="sign-area">
          <span>检查人员签名：</span>
          <span>记录员签名：</span>
        </div>
      </div>
    </div>

    <!-- 选择记录弹窗 -->
    <el-dialog v-model="selectVisible" title="选择特种设备安全监督检查记录" width="1000px" append-to-body destroy-on-close>
      <el-table :data="tableData" border highlight-current-row @row-dblclick="selectRecord" v-loading="loading">
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="单位名称" prop="unitName" min-width="160" show-overflow-tooltip />
        <el-table-column label="检查类别" prop="inspectionType" width="100" />
        <el-table-column label="检查人员" prop="inspector" width="100" />
        <el-table-column label="检查日期" prop="recordDate" width="120" align="center" />
        <el-table-column label="创建时间" prop="createTime" width="170" align="center" />
        <el-table-column label="操作" width="80" align="center">
          <template #default="scope"><el-button link type="primary" @click="selectRecord(scope.row)">选择</el-button></template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="loadList" />
    </el-dialog>
  </div>
</template>

<script setup name="BusinessOutputEquipmentCheckRecordForm" lang="ts">
import { ref, reactive, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listEquipmentCheckRecord, getEquipmentCheckRecord, delEquipmentCheckRecord } from '@/api/business/equipment-check-record/index';

const loading = ref(false); const selectVisible = ref(false);
const total = ref(0); const tableData = ref<any[]>([]);
const queryParams = reactive({ pageNum: 1, pageSize: 10 });

const dispositionOptions = ['下达监察指令书', '实施查封', '实施扣押', '其他'];

const emptyForm = () => ({
  id: undefined,
  inspector: '', recorder: '', recordDate: '', inspectionType: '',
  inspectionStartTime: '', inspectionEndTime: '',
  unitName: '', creditCode: '', legalRep: '', safetyManager: '',
  unitAddress: '', contactPerson: '', contactPosition: '', contactPhone: '',
  unitCategory: '', equipmentName: '', registrationCode: '',
  findings: '', disposition: '', unitOpinion: '', unitSign: '', signDate: '', remark: '',
});

const formData = reactive<any>(emptyForm());

const checkTypeLabel = computed(() => formData.inspectionType || '');

const isDispositionChecked = (m: string) => formData.disposition && formData.disposition.includes(m);

const formatDate = (val: string) => {
  if (!val) return '';
  const d = new Date(val);
  return `${d.getFullYear()}年${d.getMonth()+1}月${d.getDate()}日${d.getHours()}时${d.getMinutes()}分`;
};

const loadList = async () => {
  loading.value = true;
  try { const res = await listEquipmentCheckRecord(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; }
  catch { /* ignore */ } finally { loading.value = false; }
};

const handleSelect = () => { loadList(); selectVisible.value = true; };

const selectRecord = async (row: any) => {
  const res = await getEquipmentCheckRecord(row.id);
  if (res.code === 200) { Object.assign(formData, emptyForm()); Object.assign(formData, res.data); selectVisible.value = false; }
};

const handlePrint = () => {
  window.print();
};

const handleDelete = () => {
  if (!formData.id) { ElMessage.warning('请先选择记录'); return; }
  ElMessageBox.confirm('确认删除该记录？', '提示', { type: 'warning' }).then(async () => {
    const res = await delEquipmentCheckRecord(String(formData.id));
    if (res.code === 200) { ElMessage.success('删除成功'); Object.assign(formData, emptyForm()); }
    else { ElMessage.error(res.msg || '删除失败'); }
  }).catch(() => {});
};

const handleClear = () => { Object.assign(formData, emptyForm()); };
</script>

<style scoped>
.btn-bar { margin-bottom: 12px; display: flex; gap: 10px; flex-wrap: wrap; }
.print-area { background: #fff; max-width: 210mm; margin: 0 auto; box-shadow: 0 2px 12px rgba(0,0,0,.1); }
.page { width: 210mm; padding: 20mm 23mm; box-sizing: border-box; background: #fff; margin-bottom: 15px; }
.header-note { text-align: right; font-size: 14px; margin-bottom: 4mm; }
.doc-title { text-align: center; font-size: 22px; font-weight: bold; letter-spacing: 4px; margin-bottom: 6mm; }
.header-row { margin-bottom: 4mm; display: flex; gap: 20px; font-size: 16px; }
.main-table { width: 100%; border-collapse: collapse; border: 1px solid #000; }
.main-table td { border: 1px solid #000; padding: 4px 6px; vertical-align: top; font-size: 16px; line-height: 2; }
.tlabel { text-align: center; font-weight: bold; background: #f8f8f8; width: 8%; }
.content-cell { min-height: 50px; white-space: pre-wrap; }
.cbox { margin-right: 12px; display: inline-block; font-family: "SimSun","宋体",serif; }
.sign-date { margin-left: 30px; }
.u { border-bottom: 1px solid #000; display: inline-block; min-width: 30px; padding: 0 4px; }
.sign-area { margin-top: 15px; display: flex; justify-content: space-between; font-size: 16px; }

@media print {
  .btn-bar, .noprint { display: none !important; }
  .print-area { box-shadow: none; max-width: none; }
  .page { width: 210mm; height: 297mm; padding: 20mm 23mm; box-sizing: border-box; page-break-after: always; overflow: hidden; margin-bottom: 0; }
  .page:last-child { page-break-after: auto; }
  .tlabel { background: none; }
  .main-table td { font-size: 16px; line-height: 2; }
  .sign-area { font-size: 16px; }
}
</style>

<style>
@media print {
  @page { size: A4; margin: 0; }
  body { font-family: "SimSun","宋体",serif; font-size: 16px; color: #000; line-height: 2; }
  .el-overlay, .el-dialog__wrapper, .el-popper, .el-message-box__wrapper { display: none !important; }
}
</style>
