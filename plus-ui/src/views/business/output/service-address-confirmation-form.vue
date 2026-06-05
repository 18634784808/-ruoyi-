<template>
  <div class="p-2">
    <!-- 操作栏 -->
    <div class="btn-bar">
      <el-button type="primary" icon="Search" @click="handleSelect">📋 选择确认书</el-button>
      <el-button type="success" icon="Printer" @click="handlePrint" :disabled="!formData.id">🖨️ 打印</el-button>
      <el-button type="danger" icon="Delete" @click="handleDelete" :disabled="!formData.id">🗑️ 删除</el-button>
      <el-button icon="RefreshRight" @click="handleClear">🔄 清空</el-button>
    </div>

    <div class="print-area" id="printArea">
      <div class="page">
        <!-- 页头 -->
        <div class="header-agency">汾阳市市场监督管理局</div>
        <div class="doc-title">送达地址确认书</div>

        <!-- 填写说明 -->
        <div class="instruction-title">填写说明：</div>
        <div class="instruction-list">
          <p>1. 送达地址及送达方式应当由受送达人本人或者委托代理人填写；受送达人不能书写又没有代理人的，可以口述后由执法人员代为填写，并经执法人员向其宣读后，由受送达人签名、盖章或者以其他方式确认。</p>
          <p>2. 受送达人委托代理人签署本文书的，应当提供有相应权限的授权委托书及委托代理人的身份证明文件。</p>
          <p>3. 如受送达人对本文书的适用范围和期限有明确要求的，应当在备注栏中注明。</p>
        </div>

        <!-- 主体表格 -->
        <table class="main-table">
          <!-- 受送达人 -->
          <tr>
            <td class="tlabel">受送达人</td>
            <td class="tvalue" colspan="2">{{ formData.recipientName || '&emsp;' }}</td>
          </tr>

          <!-- 告知事项 -->
          <tr>
            <td class="tlabel">告 知 事 项</td>
            <td class="legal-text" colspan="2">
              <p>依据《市场监督管理行政处罚程序规定》第八十二条第三项、第八十三条的规定，告知如下：</p>
              <p>一、为便于及时收到市场监督管理部门的相关文书，保证案件调查的顺利进行，市场监督管理部门可以要求受送达人签署送达地址确认书，送达至受送达人确认的地址，即视为送达。</p>
              <p>二、受送达人送达地址发生变更的，应当及时书面告知市场监督管理部门；未及时告知的，市场监督管理部门按原地址送达，视为依法送达。</p>
              <p>三、因受送达人提供的送达地址不准确、送达地址变更未书面告知市场监督管理部门，导致执法文书未能被受送达人实际接收的，直接送达的，执法文书留在该地址之日为送达之日；邮寄送达的，执法文书被退回之日为送达之日。</p>
              <p>四、经受送达人同意，可以采用手机短信、传真、电子邮件、即时通讯账号等能够确认其收悉的电子方式送达执法文书，手机短信、传真、电子邮件、即时通讯信息等到达受送达人特定系统的日期为送达日期。</p>
            </td>
          </tr>

          <!-- 送达地址及送达方式 -->
          <tr>
            <td class="tlabel" rowspan="5">送 达 地 址 及 送 达 方 式</td>
            <td class="checkbox-cell">
              <span>是否接受电子送达</span>
              <span class="cbox" :class="{ checked: formData.acceptElectronic === '1' }">□是</span>
              <span class="cbox" :class="{ checked: formData.acceptElectronic === '0' }">□否</span>
            </td>
            <td class="contact-methods">
              <div>□手机号码：<span class="u">{{ formData.instantAccount || '&emsp;&emsp;&emsp;' }}</span></div>
              <div>□传真号码：<span class="u">{{ formData.faxNumber || '&emsp;&emsp;&emsp;' }}</span></div>
              <div>□电子邮件地址：<span class="u">{{ formData.emailAddress || '&emsp;&emsp;&emsp;' }}</span></div>
              <div>□即时通讯账号：<span class="u">&emsp;&emsp;&emsp;</span></div>
              <div class="note">以传真、电子邮件等到达本人特定系统的日期为送达日期。</div>
            </td>
          </tr>
          <tr>
            <td class="tlabel">送达地址</td>
            <td class="tvalue">{{ formData.serviceAddress || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="tlabel">收件人</td>
            <td class="tvalue">{{ formData.recipientPerson || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="tlabel">收件人联系电话</td>
            <td class="tvalue">{{ formData.recipientPhone || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="tlabel">邮政编码</td>
            <td class="tvalue">{{ formData.postalCode || '&emsp;' }}</td>
          </tr>

          <!-- 受送达人确认 -->
          <tr>
            <td class="tlabel">受送达人<br>确认</td>
            <td class="confirm-text" colspan="2">
              <p>本人已经阅读（已向本人宣读）上述告知事项，保证以上送达地址及送达方式准确、有效，清楚了解并同意本确认书内容及法律意义。</p>
              <div class="sign-line">
                <span>受送达人（委托代理人）：<span class="u" style="min-width:100px;">{{ formData.signName || '&emsp;&emsp;&emsp;' }}</span></span>
                <span class="sign-date">{{ formData.signDate || '&emsp;&emsp;年&emsp;&emsp;月&emsp;&emsp;日' }}</span>
              </div>
            </td>
          </tr>

          <!-- 备注 -->
          <tr>
            <td class="tlabel">备注</td>
            <td class="tvalue" colspan="2" style="height:60px;">{{ formData.remark || '&emsp;' }}</td>
          </tr>
        </table>
      </div>
    </div>

    <!-- 选择记录弹窗 -->
    <el-dialog v-model="selectVisible" title="选择送达地址确认书" width="1000px" append-to-body destroy-on-close>
      <el-table :data="tableData" border highlight-current-row @row-dblclick="selectRecord" v-loading="loading">
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="受送达人" prop="recipientName" min-width="150" show-overflow-tooltip />
        <el-table-column label="收件人" prop="recipientPerson" width="100" />
        <el-table-column label="收件人电话" prop="recipientPhone" width="130" />
        <el-table-column label="签名日期" prop="signDate" width="120" align="center" />
        <el-table-column label="创建时间" prop="createTime" width="170" align="center" />
        <el-table-column label="操作" width="80" align="center">
          <template #default="scope"><el-button link type="primary" @click="selectRecord(scope.row)">选择</el-button></template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="loadList" />
    </el-dialog>
  </div>
</template>

<script setup name="BusinessOutputServiceAddressConfirmationForm" lang="ts">
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listServiceAddressConfirmation, getServiceAddressConfirmation, delServiceAddressConfirmation } from '@/api/business/service-address-confirmation/index';

const loading = ref(false); const selectVisible = ref(false);
const total = ref(0); const tableData = ref<any[]>([]);
const queryParams = reactive({ pageNum: 1, pageSize: 10 });

const emptyForm = () => ({
  id: undefined,
  recipientName: '', acceptElectronic: '0', faxNumber: '', emailAddress: '',
  instantAccount: '', serviceAddress: '', recipientPerson: '', recipientPhone: '',
  postalCode: '', signName: '', signDate: '', remark: '',
});

const formData = reactive<any>(emptyForm());

const loadList = async () => {
  loading.value = true;
  try { const res = await listServiceAddressConfirmation(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; }
  catch { /* ignore */ } finally { loading.value = false; }
};

const handleSelect = () => { loadList(); selectVisible.value = true; };

const selectRecord = async (row: any) => {
  const res = await getServiceAddressConfirmation(row.id);
  if (res.code === 200) { Object.assign(formData, emptyForm()); Object.assign(formData, res.data); selectVisible.value = false; }
};

const handlePrint = () => { window.print(); };

const handleDelete = () => {
  if (!formData.id) { ElMessage.warning('请先选择记录'); return; }
  ElMessageBox.confirm('确认删除该送达地址确认书？', '提示', { type: 'warning' }).then(async () => {
    const res = await delServiceAddressConfirmation(String(formData.id));
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
.header-agency { text-align: center; font-size: 20px; font-weight: bold; letter-spacing: 4px; margin: 5mm 0 8mm; }
.doc-title { text-align: center; font-size: 22px; font-weight: bold; letter-spacing: 8px; margin-bottom: 6mm; }
.instruction-title { font-size: 14px; font-weight: bold; margin-bottom: 2mm; }
.instruction-list { font-size: 14px; line-height: 2; margin-bottom: 4mm; padding-left: 2em; color: #666; }
.instruction-list p { margin: 0; }
.main-table { width: 100%; border-collapse: collapse; border: 1px solid #000; margin-bottom: 10mm; }
.main-table td { border: 1px solid #000; padding: 6px 8px; vertical-align: top; font-size: 16px; line-height: 2; }
.tlabel { width: 16%; text-align: center; font-weight: bold; background: #f5f5f5; }
.legal-text { font-size: 14px; line-height: 2; }
.legal-text p { margin: 2px 0; }
.checkbox-cell { font-size: 16px; }
.cbox { margin-right: 20px; display: inline-block; font-family: "SimSun","宋体",serif; }
.contact-methods { font-size: 16px; line-height: 2; }
.contact-methods .note { font-size: 14px; color: #666; }
.confirm-text { font-size: 16px; line-height: 2; }
.sign-line { margin-top: 8mm; display: flex; justify-content: space-between; align-items: center; }
.u { border-bottom: 1px solid #000; display: inline-block; min-width: 30px; padding: 0 4px; }

@media print {
  .btn-bar, .noprint { display: none !important; }
  @page { size: A4; margin: 0; }
  .page { width: 210mm; height: 297mm; padding: 20mm 23mm; box-sizing: border-box; page-break-after: always; overflow: hidden; }
  .page:last-child { page-break-after: auto; }
  body { font-family: "SimSun","宋体",serif; font-size: 16px; color: #000; line-height: 2; }
  .print-area { box-shadow: none; max-width: none; margin: 0; }
  .tlabel { background: none !important; }
  .instruction-list, .contact-methods .note { color: #000 !important; }
}
</style>