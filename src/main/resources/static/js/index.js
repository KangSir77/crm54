layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    

    form.on("submit(login)",function(obj){
        //layer.msg("欢迎登录");
        var data=obj.field;

        //layer.msg(data.username+"--->"+data.password);

        if(data.username=='undifinded' || data.username.trim()==''){
            layer.msg("请输入用户名");
            return ;
        }
        if(data.password=='undifinded' || data.password.trim()==''){
            layer.msg("请输入用密码");
            return ;
        }

        $.ajax({
            type:"post",
            url:ctx+"/user/login",
            data:{
                userName:data.username,
                userPwd:data.password
            },
            dataType:"json",
            success:function (obj){
                if(obj.code==200){
                    layer.msg("登录成功了",function (){
                        //将用户信息存储Cookie,Session
                        //$.cookie("userId",data.)
                        var result=obj.result;
                        $.cookie("userIdStr",result.userIdStr);
                        $.cookie("userName",result.userName);
                        $.cookie("trueName",result.trueName);
                        //选择复选框
                        layer.msg($(":checkbox").prop("checked"));
                        if($(":checkbox").prop("checked")){
                            $.cookie("userIdStr",result.userIdStr,{expires:7});
                            $.cookie("userName",result.userName,{expires:7});
                            $.cookie("trueName",result.trueName,{expires:7});
                        }

                        //跳转
                        window.location.href=ctx+"/main";
                    });
                }else{
                    layer.msg(obj.msg,{icon:6});
                }
            }
        });


        return false;
    });
});