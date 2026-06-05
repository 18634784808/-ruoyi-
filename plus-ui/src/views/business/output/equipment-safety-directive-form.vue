<template>
  <div class="p-2">
    <div class="btn-bar">
      <el-button type="primary" icon="Search" @click="handleSelect">📋 选择指令书</el-button>
      <el-button type="success" icon="Printer" @click="handlePrint" :disabled="!formData.id">🖨️ 打印</el-button>
      <el-button type="danger" icon="Delete" @click="handleDelete" :disabled="!formData.id">🗑️ 删除</el-button>
      <el-button icon="RefreshRight" @click="handleClear">🔄 清空</el-button>
    </div>

    <div class="print-area" id="printArea">
      <div class="page">
        <!-- 文书编号 -->
        <div class="doc-no">（{{ formData.issuingAgency || '某市' }}）市监特令〔{{ formData.docYear || '&emsp;' }}〕第{{ formData.docNumber || '&emsp;' }}号</div>

        <!-- 标题 -->
        <div class="doc-title">特种设备安全监察指令书</div>

        <p class="checked-unit">被检查单位：<span class="u inline">{{ formData.inspectedUnit || '&emsp;&emsp;&emsp;' }}</span></p>

        <!-- 正文 -->
        <p class="body-text">经检查，你（单位）在特种设备安全方面存在下列问题：</p>

        <div class="content-block">
          <p class="indent">{{ formData.problemsFound || '&emsp;' }}</p>
        </div>

        <p class="body-text">
          上述问题违反了<span class="u inline">{{ formData.violationDesc || '&emsp;&emsp;&emsp;' }}</span>
          第<span class="u">{{ formData.violationArticle || '&emsp;' }}</span>条的规定，
        </p>
        <p class="body-text">
          根据<span class="u inline">{{ formData.basisRule || '&emsp;&emsp;&emsp;' }}</span>的规定，责令你（单位）于
          <span class="u">{{ formatDate(formData.deadlineDate) || '&emsp;&emsp;年&emsp;&emsp;月&emsp;&emsp;日' }}</span>前采取以下措施予以改正或者消除事故隐患：
        </p>

        <div class="content-block">
          <p class="indent">{{ formData.correctiveMeasures || '&emsp;' }}</p>
        </div>

        <!-- 救济途径 -->
        <div class="legal-note">
          <p>如对本指令书不服，可以自收到本指令书之日起六十日内向
            <span class="u">{{ formData.appealGov || '&emsp;&emsp;' }}</span>人民政府申请行政复议，或者六个月内向
            <span class="u">{{ formData.appealCourt || '&emsp;&emsp;' }}</span>人民法院提起行政诉讼。复议或者诉讼期间，不得停止改正或者停止消除事故隐患。</p>
        </div>

        <!-- 签名区 -->
        <div class="sign-area">
          <div class="sign-row">
            <span>检查人员签名：<span class="u" style="min-width:120px;">{{ formData.inspectorSign || '&emsp;&emsp;&emsp;' }}</span></span>
            <span class="sign-spacer"></span>
          </div>
          <div class="sign-row">
            <span>被检查单位（负责人）签名：<span class="u" style="min-width:120px;">{{ formData.unitSign || '&emsp;&emsp;&emsp;' }}</span></span>
          </div>
        </div>

        <!-- 公章 -->
        <div class="seal-area">
          <div class="seal-text">（<span class="u">{{ formData.sealInfo || '特种设备安全监察专用章' }}</span>）</div>
          <div class="seal-date">{{ formData.signDate || '&emsp;&emsp;年&emsp;&emsp;月&emsp;&emsp;日' }}</div>
        </div>

        <!-- 备注 -->
        <div class="remark-text">备注：本指令书一式两份，发出部门、被检查单位各一份。</div>
      </div>
    </div>

    <!-- 选择记录弹窗 -->
    <el-dialog v-model="selectVisible" title="选择特种设备安全监察指令书" width="1000px" append-to-body destroy-on-close>
      <el-table :data="tableData" border highlight-current-row @row-dblclick="selectRecord" v-loading="loading">
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="被检查单位" prop="inspectedUnit" min-width="160" show-overflow-tooltip />
        <el-table-column label="文书编号" min-width="180" show-overflow-tooltip>
          <template #default="scope">（{{ scope.row.issuingAgency || '某市' }}）市监特令〔{{ scope.row.docYear }}〕第{{ scope.row.docNumber }}号</template>
        </el-table-column>
        <el-table-column label="整改截止" prop="deadlineDate" width="120" align="center" />
        <el-table-column label="创建时间" prop="createTime" width="170" align="center" />
        <el-table-column label="操作" width="80" align="center">
          <template #default="scope"><el-button link type="primary" @click="selectRecord(scope.row)">选择</el-button></template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="loadList" />
    </el-dialog>
  </div>
</template>

<script setup name="BusinessOutputEquipmentSafetyDirectiveForm" lang="ts">
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listEquipmentSafetyDirective, getEquipmentSafetyDirective, delEquipmentSafetyDirective } from '@/api/business/equipment-safety-directive/index';

const loading = ref(false); const selectVisible = ref(false);
const total = ref(0); const tableData = ref<any[]>([]);
const queryParams = reactive({ pageNum: 1, pageSize: 10 });

const emptyForm = () => ({
  id: undefined,
  issuingAgency: '汾阳市市场监督管理局', docYear: '', docSeq: '', docNumber: '',
  inspectedUnit: '', problemsFound: '', violationDesc: '', violationArticle: '',
  basisRule: '', deadlineDate: '', correctiveMeasures: '',
  appealGov: '汾阳市人民政府', appealCourt: '汾阳市人民法院',
  inspectorSign: '', unitSign: '', sealInfo: '特种设备安全监察专用章', signDate: '', remark: '',
});

const formData = reactive<any>(emptyForm());

const formatDate = (val: string) => {
  if (!val) return '';
  const d = new Date(val);
  return `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日`;
};

const loadList = async () => {
  loading.value = true;
  try { const res = await listEquipmentSafetyDirective(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; }
  catch { /* ignore */ } finally { loading.value = false; }
};

const handleSelect = () => { loadList(); selectVisible.value = true; };

const selectRecord = async (row: any) => {
  const res = await getEquipmentSafetyDirective(row.id);
  if (res.code === 200) { Object.assign(formData, emptyForm()); Object.assign(formData, res.data); selectVisible.value = false; }
};

const handlePrint = () => { window.print(); };

const handleDelete = () => {
  if (!formData.id) { ElMessage.warning('请先选择记录'); return; }
  ElMessageBox.confirm('确认删除该指令书？', '提示', { type: 'warning' }).then(async () => {
    const res = await delEquipmentSafetyDirective(String(formData.id));
    if (res.code === 200) { ElMessage.success('删除成功'); Object.assign(formData, emptyForm()); }
    else { ElMessage.error(res.msg || '删除失败'); }
  }).catch(() => {});
};

const handleClear = () => { Object.assign(formData, emptyForm()); };
</script>

<style scoped>
/* --- screen styles --- */
.btn-bar { margin-bottom: 12px; display: flex; gap: 10px; flex-wrap: wrap; }
.print-area { background: #fff; max-width: 210mm; margin: 0 auto; box-shadow: 0 2px 12px rgba(0,0,0,.1); }
.page { width: 210mm; padding: 20mm 23mm; box-sizing: border-box; background: #fff; margin-bottom: 15px; }
.doc-no { text-align: right; font-size: 16px; margin-bottom: 6mm; }
.doc-title { text-align: center; font-size: 22px; font-weight: bold; letter-spacing: 6px; margin-bottom: 8mm; }
.checked-unit { font-size: 16px; line-height: 2; margin-bottom: 3mm; }
.body-text { text-indent: 2em; font-size: 16px; line-height: 2; margin-bottom: 3mm; }
.content-block { margin: 4mm 0; padding-left: 2em; min-height: 60px; font-size: 16px; line-height: 2; }
.content-block p { font-size: 16px; line-height: 2; }
.indent { text-indent: 2em; }
.legal-note { margin-top: 8mm; font-size: 14px; line-height: 2; }
.sign-area { margin-top: 10mm; font-size: 16px; line-height: 3; }
.sign-row { display: flex; justify-content: space-between; }
.seal-area { text-align: right; margin-top: 12mm; padding-right: 15mm; font-size: 16px; line-height: 2.5; }
.seal-date { margin-top: 4mm; }
.remark-text { margin-top: 15mm; font-size: 14px; }
.u { border-bottom: 1px solid #000; display: inline-block; min-width: 30px; padding: 0 4px; }
.u.inline { min-width: 100px; }

/* --- print styles --- */
@media print {
  .btn-bar, .noprint { display: none !important; }
  @page { size: A4; margin: 0; }
  .page { width: 210mm; height: 297mm; padding: 20mm 23mm; box-sizing: border-box; page-break-after: always; overflow: hidden; }
  .page:last-child { page-break-after: auto; }
  body { font-family: "SimSun","宋体",serif; font-size: 16px; color: #000; line-height: 2; }
  .print-area { background: #fff; max-width: none; margin: 0; box-shadow: none; }
  .remark-text { color: #000; }
}
</style>
