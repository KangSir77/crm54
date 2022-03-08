layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    form.on("submit()",function (obj){

        var fieldData=obj.field;
        //发送ajax修改密码
        $.ajax({
            type:"post",
            url:ctx+"/user/updatePwd",
            data:{
                oldPassword:fieldData.old_password,
                newPassword:fieldData.new_password,
                confirmPawd:fieldData.again_password
            },
            dataType:"json",
            success:function (data){
                if(data.code==200){
                    layer.msg("修改成功了,3秒后消失",function (){
                        //清空Cookie信息
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});

                    });
                    //跳转
                    window.parent.location.href=ctx+"/index";
                }else{
                    //提示一次啊
                    layer.msg(data.msg);
                }
            }
        });
        //取消默认表单的跳转
        return false;
    });
});