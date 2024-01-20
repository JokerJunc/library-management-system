<template>
  <el-main>
    <el-form size="small">
      <el-form-item>
        <el-button type="primary" icon="el-icon-plus" @click="addBtn">新增</el-button>
      </el-form-item>

    </el-form>
    <el-table :data="tableData" :height="tableHeight" row-key="menuId" border default-expand-all
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" stripe>
      <el-table-column prop="title" label="菜单名称">
      </el-table-column>
      <el-table-column prop="title" label="菜单图标">
        <template slot-scope="scope">
          <i :class="scope.row.icon"></i>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="菜单类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === '0'">目录</el-tag>
          <el-tag type="warning" v-if="scope.row.type === '1'">菜单</el-tag>
          <el-tag type="danger" v-if="scope.row.type === '2'">按钮</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="路由名称">
      </el-table-column>
      <el-table-column prop="path" label="路由地址">
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="primary" size="small" icon="el-icon-edit" @click="editBtn(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" icon="el-icon-delete" @click="deleteBtn(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 弹窗 -->
    <sys-dialog :title="dialog.title" :visible.sync="dialog.visible" :height="dialog.height" :width="dialog.width"
      @onClose="onClose" @onConfirm="onConfirm">
      <!-- 弹窗内容 -->
      <div slot="content">
        <el-form :model="addModel" ref="addRef" :rules="rules" size="small" label-width="80px" style="margin-right:8px">
          <el-row>
            <el-col :span="24" :offsrt="0">
              <el-form-item prop="type" label="菜单类型">
                <el-radio-group v-model="addModel.type">
                  <el-radio :label="'0'">目录</el-radio>
                  <el-radio :label="'1'">菜单</el-radio>
                  <el-radio :label="'2'">按钮</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offsrt="0">
              <el-form-item prop="parentName" label="上级菜单">
                <!-- <el-input v-model="addModel.parentId" ></el-input> -->
                <el-input @click.native="selectParent" readonly v-model="addModel.parentName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offsrt="0">
              <el-form-item prop="title" label="菜单名称">
                <el-input v-model="addModel.title"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col v-if="addModel.type != '2'" :span="12" :offsrt="0">
              <el-form-item label="菜单图标">
                <el-input v-model="addModel.icon"></el-input>
              </el-form-item>
            </el-col>
            <el-col v-if="addModel.type == '1'" :span="12" :offsrt="0">
              <el-form-item prop="name" label="路由名称">
                <el-input v-model="addModel.name"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="addModel.type == '1'">
            <el-col :span="12" :offsrt="0">
              <el-form-item prop="path" label="路由地址">
                <el-input v-model="addModel.path"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offsrt="0">
              <el-form-item prop="url" label="组件路径">
                <el-input v-model="addModel.url"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offsrt="0">
              <el-form-item label="权限字段">
                <el-input v-model="addModel.code"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offsrt="0">
              <el-form-item label="菜单序号">
                <el-input v-model="addModel.orderNum"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </sys-dialog>
    <!-- 上级菜单 -->
    <sys-dialog :title="parentDialog.title" :visible.sync="parentDialog.visible" :height="parentDialog.height"
      :width="parentDialog.width" @onClose="parentClose" @onConfirm="parentConfirm">
      <div slot="content">
        <el-tree ref="parentTree" :data="parentData" node-key="menuId" :props="defaultProps" empty-text="暂无数据"
          :show-checkbox="false" :highlight-current="true" default-expand-all :expand-on-click-node="false"
          @node-click="handleNodeClick">
          <div slot-scope="{ node, data }">
            <!-- 如果没有下级，显示文档图标 -->
            <span v-if="data.children.length == 0">
              <i style="margin-left: 3px" class="el-icon-document"></i>
            </span>
            <!-- 有下级，判断是否展开 -->
            <span v-else @click.stop="openBtn(data)">
              <i v-if="data.open" style="margin-left: 3px" class="el-icon-plus"></i>
              <i v-else style="margin-left: 3px" class="el-icon-minus"></i>
            </span>
            <span style="margin-left: 3px">{{ node.label }}</span>
          </div>
        </el-tree>
      </div>
    </sys-dialog>
  </el-main>
</template>

<script>
import SysDialog from '@/components/system/SysDialog.vue'
import { getMenuListApi, getParentMenuListApi, addMenuApi, editMenuApi, deleteMenuApi } from '@/api/menu'
export default {
  components: {
    SysDialog
  },
  data() {
    return {
      defaultProps: {
        children: "children",
        label: "title",
      },
      //上级菜单弹框
      parentDialog: {
        title: '上级菜单',
        width: 300,
        height: 450,
        visible: false,
      },
      selectNode: {
        id: '',
        title: '',
      },
      //上级菜单树数据
      parentData: [],
      //表单数据
      addModel: {
        editType: '',//编辑类型
        menuId: '',
        type: '',
        parentId: '',
        title: '',
        code: '',
        name: '',
        path: '',
        url: '',
        icon: '',
        parentName: '',
        orderNum: '',
      },
      rules: {
        type: [
          { required: true, message: '请选择菜单类型', trigger: 'blur' }
        ],
        parentName: [
          { required: true, message: '请选择上级菜单', trigger: 'blur' }
        ],
        title: [
          { required: true, message: '请输入菜单名称', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入路由名称', trigger: 'blur' }
        ],
        path: [
          { required: true, message: '请输入路由地址', trigger: 'blur' }
        ],
        url: [
          { required: true, message: '请输入组件路径', trigger: 'blur' }
        ],

      },
      // 弹框属性
      dialog: {
        tiele: '',
        width: 650,
        height: 280,
        visible: false,
      },
      //表格高度
      tableHeight: 0,
      //表格数据
      tableData: [],

    }
  },
  methods: {
    //删除按钮
    async deleteBtn(row) {
      const confirm = await this.$myconfirm('确认删除该菜单吗？')
      let res = null
      if(confirm){
        res = await deleteMenuApi({ menuId: row.menuId });
      }
      if (res && res.code == 200) {
        this.$message({type:'success',message: res.msg});
        this.getList();
      }
    },

    //编辑按钮
    editBtn(row) {
      this.addModel.editType = '1'
      this.dialog.title = '编辑'
      this.$resetForm('addRef',this.addModel)
      this.$objCopy(row, this.addModel)
      this.dialog.visible = true
    },
    //上级部门节点加号和减号点击事件
    openBtn(data) {
      data.open = !data.open;
      this.$refs.parentTree.store.nodesMap[data.menuId].expanded = !data.open;
    },
    //上级树节点点击事件
    handleNodeClick(node) {
      // console.log(node);
      this.selectNode.id = node.menuId
      this.selectNode.title = node.title
    },
    parentConfirm() {
      this.addModel.parentId = this.selectNode.id
      this.addModel.parentName = this.selectNode.title
      this.parentDialog.visible = false;
    },
    parentClose() {
      this.parentDialog.visible = false;
    },
    //选择上级菜单
    async selectParent() {
      let res = await getParentMenuListApi();
      if (res && res.code == 200) {
        // console.log(res);
        this.parentData = res.data;
      }
      // console.log(this.parentData);
      this.parentDialog.visible = true;
    },
    // 关闭弹窗
    onClose() {
      this.dialog.visible = false
    },
    // 确认弹窗
    onConfirm() {
      // console.log('点击确认按钮')
      this.$refs.addRef.validate(async (valid) => {
        if (valid) {
          // console.log(this.addModel.type)
          let res = null
          if (this.addModel.editType == '0') {
            res = await addMenuApi(this.addModel)
          } else {
            res = await editMenuApi(this.addModel)
          }

          if (res && res.code == 200) {
            this.getList()
            this.$message({ type: 'success', message: res.msg })
            this.dialog.visible = false
          }
        }
      })
    },
    addBtn() {
      this.dialog.title = '新增'
      this.dialog.visible = true
      //清空表单
      this.$resetForm("addRef", this.addModel)
      this.addModel.editType = '0'
    },

    async getList() {
      let res = await getMenuListApi()
      if (res && res.code == 200) {
        this.tableData = res.data
        // console.log(res)
      }
    },
    async getParentList() {
      let res = await getParentMenuListApi()
      if (res && res.code == 200) {
        this.parentData = res.data
      }
    }
  },
  created() {
    this.getList()
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 200;
    })
  }

}
</script>

<style lang="scss" scoped></style>
