<template>
  <div class="p-2">
    <div class="btn-bar">
      <el-button type="primary" icon="Search" @click="handleSelect">📋 选择笔录</el-button>
      <el-button type="success" icon="Printer" @click="handlePrint" :disabled="!formData.id">🖨️ 打印</el-button>
      <el-button type="danger" icon="Delete" @click="handleDelete" :disabled="!formData.id">🗑️ 删除</el-button>
      <el-button icon="RefreshRight" @click="handleClear">🔄 清空</el-button>
    </div>

    <div class="print-area" id="printArea">
      <!-- ========== 第1页 ========== -->
      <div class="page page-1">
        <div class="header-agency">汾阳市市场监督管理局</div>
        <div class="doc-title">现 场 笔 录</div>

        <!-- 基本信息 -->
        <table class="info-table">
          <tr>
            <td class="ilabel">时间：</td>
            <td class="ivalue">{{ formatDate(formData.startTime) }}</td>
            <td class="ilabel">至</td>
            <td class="ivalue">{{ formatDate(formData.endTime) }}</td>
          </tr>
          <tr>
            <td class="ilabel">地点：</td>
            <td class="ivalue" colspan="3">{{ formData.place }}</td>
          </tr>
          <tr>
            <td class="ilabel">检查人员：</td>
            <td class="ivalue">{{ formData.inspector }}</td>
            <td class="ilabel">执法证号：</td>
            <td class="ivalue">{{ inspectorCertNo }}</td>
          </tr>
          <tr>
            <td class="ilabel">检查人员：</td>
            <td class="ivalue">{{ formData.inspectorCopy }}</td>
            <td class="ilabel">执法证号：</td>
            <td class="ivalue">{{ inspectorCopyCertNo }}</td>
          </tr>
          <tr>
            <td class="ilabel">当事人：</td>
            <td class="ivalue" colspan="3">{{ formData.party }}</td>
          </tr>
          <tr>
            <td class="ilabel">主体资格证照名称：</td>
            <td class="ivalue" colspan="3">{{ formData.qualificationName }}</td>
          </tr>
          <tr>
            <td class="ilabel">统一社会信用代码：</td>
            <td class="ivalue" colspan="3">{{ formData.creditCode || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="ilabel">住所（住址）：</td>
            <td class="ivalue" colspan="3">{{ formData.address || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="ilabel">法定代表人：</td>
            <td class="ivalue" colspan="3">{{ formData.legalPerson || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="ilabel">身份证件号码：</td>
            <td class="ivalue">{{ formData.idNumber || '&emsp;' }}</td>
            <td class="ilabel">联系电话：</td>
            <td class="ivalue">{{ formData.phone || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="ilabel">通知当事人到场情况：</td>
            <td class="ivalue" colspan="3">{{ formData.partyPresenceNotice || '&emsp;' }}</td>
          </tr>
        </table>

        <!-- 标准开场 -->
        <div class="qa-block">
          <div class="qa-inquirer">
            检查人员：我们是<span class="u inline">{{ formData.agencyName || '汾阳市市场监督管理局' }}</span>的执法人员，依法就<span class="u inline">{{ formData.inspectTopic || '&emsp;&emsp;&emsp;' }}</span>进行现场检查，请予配合。现向你出示我们的执法证件，你是否看清楚？
          </div>
          <div class="qa-answer">
            当事人：<span class="u inline">{{ formData.answerStd1 || '&emsp;&emsp;&emsp;' }}</span>
          </div>
        </div>

        <div class="qa-block">
          <div class="qa-inquirer">
            检查人员：你有权进行陈述和申辩。你应当如实回答询问，并协助调查或者检查，不得拒绝或者阻挠。你认为检查人员与你（单位）有直接利害关系或者有其他关系可能影响公正执法的，依法有申请回避的权利。你是否申请检查人员回避？
          </div>
          <div class="qa-answer">
            当事人：<span class="u inline">{{ formData.answerStd2 || '&emsp;&emsp;&emsp;' }}</span>
          </div>
        </div>

        <!-- 现场情况 -->
        <div class="section-title">现场情况：</div>
        <div class="content-area">{{ formData.sceneSituation || '&emsp;' }}</div>

        <!-- 签名 -->
        <div class="page-footer">
          <div class="sign-row">
            <span>当事人：<span class="u" style="min-width:80px;">&emsp;&emsp;&emsp;</span></span>
            <span>年&emsp;月&emsp;日</span>
          </div>
          <div class="sign-row">
            <span>见证人：<span class="u" style="min-width:80px;">&emsp;&emsp;&emsp;</span></span>
            <span>年&emsp;月&emsp;日</span>
          </div>
          <div class="sign-row">
            <span>检查人员：<span class="u" style="min-width:60px;">&emsp;&emsp;&emsp;</span>、<span class="u" style="min-width:60px;">&emsp;&emsp;&emsp;</span></span>
            <span>年&emsp;月&emsp;日</span>
          </div>
          <div class="page-num">第 1 页共 {{ totalPages }} 页</div>
        </div>
      </div>

      <!-- ========== 中间页 ========== -->
      <div class="page" v-if="showMidPage">
        <div class="content-area continuation">{{ formData.sceneSituationCont || '&emsp;' }}</div>

        <!-- 强制措施告知（可选） -->
        <div class="section-title" v-if="formData.coerciveMeasureNote">行政强制措施告知：</div>
        <div class="content-area" v-if="formData.coerciveMeasureNote">{{ formData.coerciveMeasureNote || '&emsp;' }}</div>

        <div class="section-title">当事人的陈述和申辩：</div>
        <div class="content-area">{{ formData.partyStatement || '&emsp;' }}</div>

        <div class="page-footer">
          <div class="sign-row">
            <span>当事人：<span class="u" style="min-width:80px;">&emsp;&emsp;&emsp;</span></span>
            <span>年&emsp;月&emsp;日</span>
          </div>
          <div class="sign-row">
            <span>见证人：<span class="u" style="min-width:80px;">&emsp;&emsp;&emsp;</span></span>
            <span>年&emsp;月&emsp;日</span>
          </div>
          <div class="sign-row">
            <span>检查人员：<span class="u" style="min-width:60px;">&emsp;&emsp;&emsp;</span>、<span class="u" style="min-width:60px;">&emsp;&emsp;&emsp;</span></span>
            <span>年&emsp;月&emsp;日</span>
          </div>
          <div class="page-num">第 {{ midPageNum }} 页共 {{ totalPages }} 页</div>
        </div>
      </div>

      <!-- ========== 末页 ========== -->
      <div class="page page-last">
        <div class="qa-block final-check">
          <div class="qa-inquirer">
            检查人员：以上是本次现场检查的情况记录，请核对/已向你宣读。如果属实请签名。
          </div>
          <div class="qa-answer">
            当事人：<span class="u inline">{{ formData.finalConfirm || '&emsp;&emsp;&emsp;' }}</span>
          </div>
        </div>

        <div class="page-footer">
          <div class="sign-row">
            <span>当事人：<span class="u" style="min-width:80px;">{{ formData.party1 || '&emsp;&emsp;&emsp;' }}</span></span>
            <span>{{ formatSignDate(formData.startTime) || '年&emsp;月&emsp;日' }}</span>
          </div>
          <div class="sign-row">
            <span>见证人：<span class="u" style="min-width:80px;">&emsp;&emsp;&emsp;</span></span>
            <span>年&emsp;月&emsp;日</span>
          </div>
          <div class="sign-row">
            <span>检查人员：<span class="u" style="min-width:60px;">{{ formData.inspector1 || '&emsp;&emsp;&emsp;' }}</span>、<span class="u" style="min-width:60px;">{{ formData.inspector2 || '&emsp;&emsp;&emsp;' }}</span></span>
            <span>年&emsp;月&emsp;日</span>
          </div>
          <div class="page-num">第 {{ totalPages }} 页共 {{ totalPages }} 页</div>
        </div>
      </div>
    </div>

    <!-- 选择记录弹窗 -->
    <el-dialog v-model="selectVisible" title="选择现场笔录" width="1000px" append-to-body destroy-on-close>
      <el-table :data="tableData" border highlight-current-row @row-dblclick="selectRecord" v-loading="loading">
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="地点" prop="place" min-width="120" show-overflow-tooltip />
        <el-table-column label="当事人" prop="party" width="120" />
        <el-table-column label="检查人员" prop="inspector" width="100" />
        <el-table-column label="创建时间" prop="createTime" width="170" align="center" />
        <el-table-column label="操作" width="80" align="center">
          <template #default="scope"><el-button link type="primary" @click="selectRecord(scope.row)">选择</el-button></template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="loadList" />
    </el-dialog>
  </div>
</template>

<script setup name="BusinessOutputSceneNotesForm" lang="ts">
import { ref, reactive, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listSceneNotes, getSceneNotes, delSceneNotes } from '@/api/business/scene-notes/index';
import { getAllInspector } from '@/api/business/inspector/index';

const loading = ref(false); const selectVisible = ref(false);
const total = ref(0); const tableData = ref<any[]>([]);
const queryParams = reactive({ pageNum: 1, pageSize: 10 });

const emptyForm = () => ({
  id: undefined,
  startTime: '', endTime: '', place: '',
  inspector: '', inspectorCopy: '', party: '', qualificationName: '',
  creditCode: '', address: '', legalPerson: '', idNumber: '', phone: '',
  partyPresenceNotice: '', agencyName: '汾阳市市场监督管理局', inspectTopic: '',
  answerStd1: '', answerStd2: '',
  sceneSituation: '', sceneSituationCont: '',
  coerciveMeasureNote: '', partyStatement: '', finalConfirm: '',
  inspector1: '', party1: '', inspector2: '', party2: '',
  remark: '',
});

const formData = reactive<any>(emptyForm());
const inspectorCertNo = ref('');
const inspectorCopyCertNo = ref('');

const showMidPage = computed(() =>
  !!(formData.sceneSituationCont || formData.coerciveMeasureNote || formData.partyStatement)
);
const totalPages = computed(() => showMidPage.value ? 3 : 2);
const midPageNum = computed(() => 2);

const formatDate = (val: string) => {
  if (!val) return '';
  const d = new Date(val);
  return `${d.getFullYear()}年${d.getMonth()+1}月${d.getDate()}日${d.getHours()}时${d.getMinutes()}分`;
};

const formatSignDate = (val: string) => {
  if (!val) return '';
  const d = new Date(val);
  return `${d.getFullYear()}年${d.getMonth()+1}月${d.getDate()}日`;
};

const loadList = async () => {
  loading.value = true;
  try { const res = await listSceneNotes(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; }
  catch { /* ignore */ } finally { loading.value = false; }
};

const handleSelect = () => { loadList(); selectVisible.value = true; };

const selectRecord = async (row: any) => {
  const res = await getSceneNotes(row.id);
  if (res.code === 200) {
    Object.assign(formData, emptyForm());
    Object.assign(formData, res.data);
    // Fetch enforcement cert numbers
    fetchCertNos(res.data.inspector, res.data.inspectorCopy);
    selectVisible.value = false;
  }
};

const fetchCertNos = async (inspector: string, inspectorCopy: string) => {
  try {
    const res = await getAllInspector();
    const inspectors = res.data || [];
    const m1 = inspectors.find((i: any) => i.name === inspector);
    const m2 = inspectors.find((i: any) => i.name === inspectorCopy);
    inspectorCertNo.value = m1?.certNo || '';
    inspectorCopyCertNo.value = m2?.certNo || '';
  } catch { /* ignore */ }
};

const handlePrint = () => {
  window.print();
};

const handleDelete = () => {
  if (!formData.id) { ElMessage.warning('请先选择记录'); return; }
  ElMessageBox.confirm('确认删除该现场笔录？', '提示', { type: 'warning' }).then(async () => {
    const res = await delSceneNotes(String(formData.id));
    if (res.code === 200) { ElMessage.success('删除成功'); Object.assign(formData, emptyForm()); }
    else { ElMessage.error(res.msg || '删除失败'); }
  }).catch(() => {});
};

const handleClear = () => { Object.assign(formData, emptyForm()); };
</script>

<style scoped>
.btn-bar { margin-bottom: 12px; display: flex; gap: 10px; flex-wrap: wrap; }
.print-area { background: #fff; max-width: 210mm; margin: 0 auto; box-shadow: 0 2px 12px rgba(0,0,0,.1); }
.page { width: 210mm; padding: 20mm 23mm; box-sizing: border-box; background: #fff; margin-bottom: 15px; display: flex; flex-direction: column; }
.header-agency { text-align: center; font-size: 22px; font-weight: bold; letter-spacing: 4px; margin: 5mm 0 8mm; }
.doc-title { text-align: center; font-size: 22px; font-weight: bold; letter-spacing: 12px; margin-bottom: 8mm; }
.info-table { width: 100%; border-collapse: collapse; margin-bottom: 5mm; }
.info-table td { padding: 2px 6px; line-height: 2; font-size: 16px; }
.ilabel { white-space: nowrap; text-align: right; }
.ivalue { border-bottom: 1px solid #000; min-width: 60px; }
.qa-block { margin-bottom: 4mm; }
.qa-inquirer { line-height: 2; font-size: 16px; padding-bottom: 2mm; }
.qa-answer { line-height: 2; font-size: 16px; padding-left: 2em; margin-bottom: 2mm; }
.section-title { font-weight: bold; font-size: 16px; margin: 6mm 0 2mm; }
.content-area { min-height: 40mm; line-height: 2; font-size: 16px; white-space: pre-wrap; border: 1px solid #eee; padding: 4px; background: #fafafa; }
.continuation { min-height: 20mm; }
.u { border-bottom: 1px solid #000; display: inline-block; min-width: 30px; padding: 0 4px; }
.u.inline { min-width: 80px; }
.page-footer { margin-top: auto; padding-top: 8mm; font-size: 16px; }
.sign-row { margin-bottom: 3mm; display: flex; justify-content: space-between; font-size: 16px; }
.page-num { text-align: center; margin-top: 4mm; font-size: 14px; }
.final-check { margin-top: 10mm; }
@media print {
  .btn-bar, .noprint { display: none !important; }
  @page { size: A4; margin: 0; }
  .page { width: 210mm; height: 297mm; padding: 20mm 23mm; box-sizing: border-box; page-break-after: always; overflow: hidden; }
  .page:last-child { page-break-after: auto; }
  body { font-family: "SimSun","宋体",serif; font-size: 16px; color: #000; line-height: 2; }
  .print-area { box-shadow: none; padding: 0; }
  .content-area { border: none; background: #fff; }
}
</style>
