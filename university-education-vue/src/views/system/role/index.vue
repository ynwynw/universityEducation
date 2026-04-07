<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="search-form">
      <el-form-item label="角色名称">
        <el-input v-model="queryParams.roleName" placeholder="请输入角色名称" clearable />
      </el-form-item>
      <el-form-item label="角色标识">
        <el-input v-model="queryParams.roleKey" placeholder="请输入角色标识" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <div class="table-container">
      <div class="table-toolbar">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData">
        <el-table-column label="角色名称" prop="roleName" width="150" />
        <el-table-column label="角色标识" prop="roleKey" width="150" />
        <el-table-column label="排序" prop="sort" width="80" align="center" />
        <el-table-column label="状态" prop="status" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">{{ scope.row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="remark" min-width="200" show-overflow-tooltip />
        <el-table-column label="创建时间" prop="createTime" width="160" />
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" icon="el-icon-setting" @click="handleAssignMenu(scope.row)">分配菜单</el-button>
            <el-button type="text" icon="el-icon-delete" class="danger-btn" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    </div>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色标识" prop="roleKey">
          <el-input v-model="form.roleKey" placeholder="请输入角色标识" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="分配菜单" :visible.sync="menuDialogVisible" width="500px" append-to-body>
      <el-tree ref="menuTree" :data="menuTree" show-checkbox node-key="id" :default-checked-keys="checkedMenuIds"
        :props="{ label: 'menuName', children: 'children' }" />
      <div slot="footer">
        <el-button @click="menuDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitMenuAssign">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getRolePage, addRole, updateRole, deleteRole, assignMenus, getRoleMenuIds } from '@/api/role'
import { getMenuTree } from '@/api/menu'
import Pagination from '@/components/Pagination'
export default {
  name: 'Role',
  components: { Pagination },
  data() {
    return {
      loading: false, tableData: [], total: 0, dialogVisible: false, dialogTitle: '',
      menuDialogVisible: false, menuTree: [], checkedMenuIds: [], currentRoleId: null,
      queryParams: { pageNum: 1, pageSize: 10, roleName: '', roleKey: '' },
      form: {},
      rules: {
        roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
        roleKey: [{ required: true, message: '请输入角色标识', trigger: 'blur' }]
      }
    }
  },
  created() { this.getList() },
  methods: {
    async getList() { this.loading = true; try { const { data } = await getRolePage(this.queryParams); this.tableData = data.list; this.total = data.total } finally { this.loading = false } },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.handleQuery() },
    handleAdd() { this.dialogTitle = '新增角色'; this.form = { status: 1, sort: 0 }; this.dialogVisible = true },
    handleEdit(row) { this.dialogTitle = '编辑角色'; this.form = { ...row }; this.dialogVisible = true },
    async submitForm() { await this.$refs.form.validate(); if (this.form.id) { await updateRole(this.form) } else { await addRole(this.form) }; this.$message.success('操作成功'); this.dialogVisible = false; this.getList() },
    async handleDelete(row) { await this.$confirm('确定要删除该角色吗？', '提示', { type: 'warning' }); await deleteRole(row.id); this.$message.success('删除成功'); this.getList() },
    async handleAssignMenu(row) {
      this.currentRoleId = row.id
      const [treeRes, menuRes] = await Promise.all([getMenuTree(), getRoleMenuIds(row.id)])
      this.menuTree = treeRes.data; this.checkedMenuIds = menuRes.data
      this.menuDialogVisible = true
    },
    async submitMenuAssign() {
      const checkedKeys = this.$refs.menuTree.getCheckedKeys()
      const halfCheckedKeys = this.$refs.menuTree.getHalfCheckedKeys()
      await assignMenus(this.currentRoleId, [...checkedKeys, ...halfCheckedKeys])
      this.$message.success('分配成功'); this.menuDialogVisible = false
    }
  }
}
</script>
<style lang="scss" scoped>.danger-btn { color: #F56C6C; }</style>
