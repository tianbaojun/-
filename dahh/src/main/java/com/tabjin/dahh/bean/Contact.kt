package com.tabjin.dahh.bean

import java.io.Serializable

class Contact:Serializable {
    var contactId = 0
    var contactName = ""
    var contactPhone = ""
    var contactGroupName = ""
    var contactGroupId = 0
    var checkstate = true
    var sendsuccess = ""
    var delevedsuccess = false
    //该次发送需要拆分成几条
    var perNum = 0
    //已经发送了几条
    var sentNum = 0
    //发送成功条数
    var sentSuccess = 0
    //发送失败条数
    var sentFaild = 0
}