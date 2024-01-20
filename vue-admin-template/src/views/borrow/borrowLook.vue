<template>
    <el-main>
      <!-- 搜索栏 -->
      <el-form :model="lookParm" label-width="80px" :inline="true" size="mini">
        <el-form-item label="读者学号">
          <el-input v-model="lookParm.username"></el-input>
        </el-form-item>
        <el-form-item label="读者姓名">
          <el-input v-model="lookParm.learnNum"></el-input>
        </el-form-item>
        <el-form-item label="图书名称">
          <el-input v-model="lookParm.bookName"></el-input>
        </el-form-item>
        <el-form-item label="借书状态">
          <el-select v-model="lookParm.borrowStatus" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="异常状态">
          <el-select v-model="lookParm.returnStatus" placeholder="请选择">
            <el-option
              v-for="item in exceoptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="到期状态">
          <el-select v-model="lookParm.timeStatus" placeholder="请选择">
            <el-option
              v-for="item in timetions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select v-model="lookParm.applyStatus" placeholder="请选择">
            <el-option
              v-for="item in applytions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-search" @click="searchBtn">搜索</el-button>
          <el-button style="color: #ff7670" icon="el-icon-close">重置</el-button>
        </el-form-item>
      </el-form>
      <!-- 表格 -->
      <el-table
        size="small"
        ref="tables"
        :height="tableHeight"
        :data="tableData"
        border
        stripe
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="bookName" label="图书名称"> </el-table-column>
        <el-table-column prop="bookPlaceNum" label="书架号"> </el-table-column>
        <el-table-column prop="username" label="学号"> </el-table-column>
        <el-table-column prop="learnNum" label="读者"> </el-table-column>
        <el-table-column prop="className" label="班级"> </el-table-column>
        <el-table-column prop="phone" label="电话"> </el-table-column>
        <el-table-column prop="borrowTime" label="借书时间"> </el-table-column>
        <el-table-column prop="returnTime" label="还书时间"> </el-table-column>
        <el-table-column prop="applyText" label="审核备注"> </el-table-column>
        <el-table-column prop="excepionText" label="还书备注"> </el-table-column>
        <el-table-column
          prop="borrowStatus"
          label="借书状态"
          align="center"
          width="100"
        >
          <template slot-scope="scope">
            <el-tag v-if="scope.row.borrowStatus == '1'">在借中</el-tag>
            <el-tag type="success" v-if="scope.row.borrowStatus == '2'"
              >已还</el-tag
            >
            <el-tag type="danger" v-if="scope.row.borrowStatus == '3'"
              >拒绝</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column
          prop="timeStatus"
          label="到期状态"
          align="center"
          width="100"
        >
          <template slot-scope="scope">
            <el-tag type="danger" v-if="scope.row.timeStatus == '1'">到期</el-tag>
            <el-tag type="success" v-if="scope.row.timeStatus == '0'"
              >未到期</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column
          prop="applyStatus"
          label="审核状态"
          align="center"
          width="100"
        >
          <template slot-scope="scope">
            <el-tag type="danger" v-if="scope.row.applyStatus == '0'">待审核</el-tag>
            <el-tag type="success" v-if="scope.row.applyStatus == '1'">已审核</el-tag>
            <el-tag type="danger" v-if="scope.row.applyStatus == '2'">拒绝</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <el-pagination
        @size-change="sizeChange"
        @current-change="currentChange"
        :current-page.sync="lookParm.currentPage"
        :page-sizes="[10, 20, 40, 80, 100]"
        :page-size="lookParm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="lookParm.total"
        background
      >
      </el-pagination>
    </el-main>
  </template>
  
  <script>
  import { getLookBorrowListApi } from "@/api/borrow";
  export default {
    data() {
      return {
        tableData: [],
        applytions: [
          {
            value: "0",
            label: "待审核",
          },
          {
            value: "1",
            label: "已审核",
          },
          {
            value: "2",
            label: "拒绝",
          },
        ],
        timetions: [
          {
            value: "1",
            label: "到期",
          },
          {
            value: "0",
            label: "未到期",
          },
        ],
        exceoptions: [
          {
            value: "1",
            label: "正常还书",
          },
          {
            value: "2",
            label: "异常还书",
          },
        ],
        options: [
          {
            value: "1",
            label: "在借中",
          },
          {
            value: "2",
            label: "已还",
          },
          {
            value: "3",
            label: "拒绝",
          },
        ],
        lookParm: {
          total: 0,
          currentPage: 1,
          pageSize: 10,
          username: "",
          learnNum: "",
          bookName: "",
          applyStatus: "",
          borrowStatus: "",
          returnStatus: "",
          timeStatus: "",
        },
        tableHeight:0
      };
    },
    mounted() {
      this.$nextTick(() =>{
        this.tableHeight = window.innerHeight -250
      })
      this.getLookBorrowList();
    },
    methods: {
      searchBtn(){
        this.getLookBorrowList()
      },
      currentChange(){
  
      },
      sizeChange(){
  
      },
      async getLookBorrowList() {
        let res = await getLookBorrowListApi(this.lookParm);
        if (res && res.code == 200) {
          this.tableData = res.data.records;
          this.lookParm.total = res.data.total;
        }
      },
    },
  };
  </script>
  
  <style lang="scss" scoped>
  
  </style>