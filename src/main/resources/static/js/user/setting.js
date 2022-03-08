layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    //提交表单
    form.on("submit(saveBtn)",function(obj){
        alert(666);
       //获取页面表单域对象
        var fieldData=obj.field;
        console.log(fieldData);
        //userName=lisi&phone=13342342
        //发送ajax修改

        $.ajax({
            type:"post",
            url:ctx+"/user/updateUser",
            data:{
                userName:fieldData.userName,
                phone:fieldData.phone,
                email:fieldData.email,
                trueName:fieldData.trueName,
                id:fieldData.id
            },
            dataType:"json",
            success:function (data){
                if (data.code==200){
                    layer.msg("修改成功了",{icon:1});
                    //跳转
                    window.parent.location.href=ctx+"/index";
                }else{
                    //提示信息
                    layer.msg(data.msg);
                }
            }
        });

        //取消表单跳转你
        return false;
    });
});