<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="24">
        <el-card shadow="hover">
          <div class="welcome-container">
            <h2>市场监督管理局西河所</h2>
            <p>企业登记管理平台</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :sm="24" :lg="6" v-for="item in shortcuts" :key="item.label">
        <el-card shadow="hover" class="shortcut-card" @click="$router.push(item.path)">
          <el-icon :size="32">
            <component :is="item.icon" />
          </el-icon>
          <span>{{ item.label }}</span>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index" lang="ts">
const shortcuts = [
  { label: '台账', path: '/table/ledger', icon: 'Tickets' },
  { label: '商户信息库', path: '/table/merchant', icon: 'Shop' },
  { label: '检查记录表', path: '/table/inspection', icon: 'DocumentChecked' },
  { label: '问题清单', path: '/table/issue', icon: 'Warning' },
  { label: '签名档案', path: '/table/signature', icon: 'EditPen' }
];
</script>

<style lang="scss" scoped>
.home {
  .welcome-container {
    text-align: center;
    padding: 60px 0;
    h2 {
      font-size: 28px;
      color: #303133;
      margin-bottom: 12px;
    }
    p {
      font-size: 16px;
      color: #909399;
    }
  }
  .shortcut-card {
    text-align: center;
    cursor: pointer;
    transition: all 0.3s;
    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }
    .el-icon {
      margin-bottom: 8px;
    }
    span {
      display: block;
      font-size: 14px;
      color: #606266;
    }
  }
}
</style>
