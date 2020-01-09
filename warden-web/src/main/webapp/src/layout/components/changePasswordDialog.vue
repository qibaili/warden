<template>
  <el-dialog
    destroy-on-close
    title="关联角色"
    :visible.sync="dialogVisible"
    width="450px"
    center
    append-to-body
    :close-on-click-modal="false"
  >
    <el-form
      ref="ruleForm"
      :model="ruleForm"
      status-icon
      :rules="rules"
      label-width="100px"
      class="demo-ruleForm"
    >
      <el-form-item label="原密码" prop="oldPassword">
        <el-input v-model="ruleForm.oldPassword" type="password" autocomplete="off" />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="ruleForm.newPassword" type="password" autocomplete="off" />
      </el-form-item>
      <el-form-item label="确认新密码" prop="checkPassword">
        <el-input v-model="ruleForm.checkPassword" type="password" autocomplete="off" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary">确 定</el-button>
      </div>
    </el-form>
  </el-dialog>
</template>

<script>
import { changePassword } from '@/api/system/user'

export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.ruleForm.checkPassword !== '') {
          this.$refs.ruleForm.validateField('checkPassword')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.ruleForm.newPassword) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      dialogVisible: false,
      userId: '',
      ruleForm: {
        oldPassword: '',
        newPassword: '',
        checkPassword: ''
      },
      rules: {
        oldPassword: [{ validator: validatePass, trigger: 'blur' }],
        newPassword: [{ validator: validatePass, trigger: 'blur' }],
        checkPassword: [{ validator: validatePass2, trigger: 'blur' }]
      }
    }
  },
  methods: {
    showDialog(userId) {
      this.dialogVisible = true
      this.userId = userId
      this.ruleForm.oldPassword = ''
      this.ruleForm.newPassword = ''
      this.ruleForm.checkPassword = ''
    },

    resetForm(formName) {
      this.$refs[formName].resetFields()
    },

    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          const data = {
            oldPassword: this.ruleForm.oldPassword,
            newPassword: this.ruleForm.newPassword
          }
          changePassword(this.userId, data).then(response => {
            if (response.success) {
              this.$message({
                type: 'success',
                duration: 2 * 1000,
                message: response.message
              })
              this.dialogVisible = false
            }
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>
