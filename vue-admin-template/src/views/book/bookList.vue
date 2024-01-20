<template>
  <el-main>
    <!--搜索栏 -->
    <el-form :model="listParm" :inline="true" size="small">
      <el-form-item>
        <el-select v-model="listParm.categoryId" placeholder="请选择">
          <el-option v-for="item in options" :key="item.categoryId" :label="item.categoryName" :value="item.categoryId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-input placeholder="请输入图书名称" v-model="listParm.bookName"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input placeholder="请输入书架号" v-model="listParm.bookPlaceNum"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input placeholder="请输入作者" v-model="listParm.bookAuther"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">查询</el-button>
        <el-button style="color: #ff7670" icon="el-icon-close" @click="closeBtn">重置</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="addBtn">新增</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableData" border stripe>
      <el-table-column label="图书名称" prop="bookName"></el-table-column>
      <el-table-column label="图书分类" prop="categoryName"></el-table-column>
      <el-table-column label="书架号" prop="bookPlaceNum"></el-table-column>
      <el-table-column label="作者" prop="bookAuther"></el-table-column>
      <el-table-column label="出版社" prop="bookProduct"></el-table-column>
      <el-table-column label="价格" prop="bookPrice"></el-table-column>
      <el-table-column label="库存" prop="bookStore"></el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="editBtn(scope.row)">编辑</el-button>
          <el-button type="danger" icon="el-icon-delete" size="small" @click="deleteBtn(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination @size-change="sizeChange" @current-change="currentChange" :current-page.sync="listParm.currentPage"
      :page-sizes="[10, 20, 40, 80, 100]" :page-size="listParm.pageSize" layout="total, sizes, prev, pager, next, jumper"
      :total="listParm.total" background>
    </el-pagination>
    <!-- 弹框 -->
    <sys-dialog :title="this.dialog.title" :visible.sync="this.dialog.visible" :width="this.dialog.width"
      :height="this.dialog.height" @onConfirm="onConfirm" @onClose="onColse">
      <div slot="content">
        <el-form :model="addModel" ref="addRef" :rules="rules" label-width="80px" size="small">
          <el-row>
            <el-col :span="12">
              <el-form-item label="图书分类">
                <el-select v-model="addModel.categoryId" placeholder="请选择">
                  <el-option v-for="item in options" :key="item.categoryId" :label="item.categoryName"
                    :value="item.categoryId">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="图书名称">
                <el-input v-model="addModel.bookName"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="bookCode" label="图书编码">
                <el-input v-model="addModel.bookCode"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="bookPlaceNum" label="书架号">
                <el-input v-model="addModel.bookPlaceNum"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="bookAuther" label="图书作者">
                <el-input v-model="addModel.bookAuther"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="bookProduct" label="出版社">
                <el-input v-model="addModel.bookProduct"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="bookPrice" label="图书价格">
                <el-input v-model="addModel.bookPrice"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="bookStore" label="图书库存">
                <el-input v-model="addModel.bookStore"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </sys-dialog>
  </el-main>
</template>

<script>
import SysDialog from "@/components/system/SysDialog.vue";
import { getListApi, getCateListApi, addBookApi, editBookApi, deleteBookApi } from "@/api/book";
export default {
  components: {
    SysDialog
  },
  data() {
    return {
      dialog: {
        title: '',
        visible: false,
        width: 600,
        height: 250,
      },
      //表单属性
      addModel: {
        type: '',//0 新增，1 编辑
        bookId: "",
        categoryId: "",
        bookName: "",
        categoryName: "",
        bookCode: "",
        bookPlaceNum: "",
        bookProduct: "",
        bookAuther: "",
        bookPrice: "",
        bookStore: "",
      },
      rules: {
        categoryId: [
          {required: true, trigger: "blur",message: "请选择图书分类",},
        ],
        bookCode: [
          {required: true, trigger: "blur",message: "请填写图书编码", },
        ],
        bookPlaceNum: [
          {required: true, trigger: "blur",message: "请填写书架号",},
        ],
        bookProduct: [
          {required: true, trigger: "blur",message: "请填写图书出版社",},
        ],
        bookAuther: [
          {required: true, trigger: "blur",message: "请填写作者", },
        ],
        bookPrice: [
          { required: true, trigger: "blur", message: "请填写价格"},
          { type: 'number', trigger: 'blur', message: '价格必须为数字值',transform: (value) => Number(value)}
        ],
        bookStore: [
          {required: true,trigger: "blur",message: "请填写库存"},
          { type: 'number', trigger: 'blur', message: '库存必须为数字值',transform: (value) => Number(value)}
        ],
        bookName: [
          {required: true,trigger: "blur",message: "请填写图书名称"},
        ],
      },
      //下拉数据
      options: [],
      //表格高度
      tableHeight: 0,
      //表格数据
      tableData: [],
      //列表查询参数
      listParm: {
        currentPage: 1,
        pageSize: 10,
        categoryId: "",
        bookName: "",
        bookPlaceNum: "",
        bookAuther: "",
        total: 0,
      },
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220;
    });
  },
  created() {
    this.getCateList();
    this.getListApi();
  },
  methods: {
    //弹框确认
    onConfirm() {
      this.$refs.addRef.validate(async(valid)=>{
        if(valid){
          let res = null;
          if (this.addModel.type == '0') {
            res = await addBookApi(this.addModel);
          } else {
            res = await editBookApi(this.addModel);
          }
          console.log(res);
          if (res && res.code == 200) {
            this.$message({ type: "success", message: res.msg })
            this.getListApi();
            this.dialog.visible = false;
          }
        }
      })
    },
    //弹框关闭
    onColse() {
      this.dialog.visible = false;
    },
    //查询分类列表
    async getCateList() {
      let res = await getCateListApi();
      if (res && res.code == 200) {
        // console.log(res);
        this.options = res.data;
      }
    },
    async getListApi() {
      let res = await getListApi(this.listParm);
      console.log('测试', res);
      if (res && res.code == 200) {
        this.tableData = res.data.records;
        this.listParm.total = res.data.total;
      }
    },
    //页数改变时触发
    currentChange(val) {
      this.listParm.currentPage = val;
      this.getListApi();
    },
    //页容量改变时触发
    sizeChange(val) {
      this.listParm.pageSize = val;
      this.getListApi();
    },
    //删除按钮
    async deleteBtn(row) {
      let confirm = await this.$myconfirm("你确定要删除吗？")
      if (confirm) {
        let res = await deleteBookApi({ bookId: row.bookId });
        if (res && res.code == 200) {
          this.$message({ type: "success", message: res.msg })
          this.getListApi();
        }
      }
    },
    //编辑按钮
    editBtn(row) {
      this.addModel.type = 1;
      this.$resetForm('addRef', this.addModel);
      this.$objCopy(row, this.addModel);
      this.dialog.title = "编辑图书";
      this.dialog.visible = true;
    },
    //新增按钮
    addBtn() {
      this.addModel.type = 0;
      this.dialog.title = "新增图书";
      this.dialog.visible = true;

    },
    //重置按钮
    closeBtn() {
      this.listParm.categoryId = "";
      this.listParm.bookName = "";
      this.listParm.bookPlaceNum = "";
      this.listParm.bookAuther = "";
      this.getListApi();
    },
    //搜索按钮
    searchBtn() {
      this.getListApi();
    },
  },
};
</script>

<style lang="scss" scoped></style>