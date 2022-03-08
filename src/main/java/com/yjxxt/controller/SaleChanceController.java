package com.yjxxt.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.base.BaseController;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.bean.SaleChance;
import com.yjxxt.query.SaleChanceQuery;
import com.yjxxt.service.SaleChanceService;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Autowired
    private SaleChanceService saleChanceService;

    @Resource
    private UserService userService;

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> list(SaleChanceQuery query){
        //查询数据
        Map<String, Object> map = saleChanceService.selectSaleChanceByParam(query);
        //返回目标map--json
        return  map;
    }


    /**
     * 进入营销机会页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }


    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(HttpServletRequest req,SaleChance saleChance){
        //指定分配人
        Integer userId=LoginUserUtil.releaseUserIdFromCookie(req);
        //查询用户信息
        String trueName=userService.selectByPrimaryKey(userId).getTrueName();
        //赋值
        saleChance.setCreateMan(trueName);
        //调用方法添加
        saleChanceService.save(saleChance);
        //返回目标对象
        return success("销售机会添加成功了");
    }

    /**
     * 更新营销机会数据
     * @param request
     * @param saleChance
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(HttpServletRequest request,SaleChance saleChance){
        // 更新营销机会的数据
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据更新成功！");
    }

    /**
     * 机会数据添加与更新表单页面视图转发
     *     id为空 添加操作
     *     id非空 修改操作
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateSaleChancePage")
    public String addOrUpdateSaleChancePage(Integer id,Model model){
        // 如果id不为空，表示是修改操作，修改操作需要查询被修改的数据
        if(null!=id){
            // 通过主键查询营销机会数据
            SaleChance saleChance=saleChanceService.selectByPrimaryKey(id);
            // 将数据存到作用域中
            model.addAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }

    /**
     * 删除营销机会数据
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance (Integer[] ids) {
        System.out.println(Arrays.toString(ids)+"--------------------------------------");
        // 删除营销机会的数据
        saleChanceService.deleteBatch(ids);
        return success("营销机会数据删除成功！");
    }

    /**
     * 多条件分页查询营销机会
     * @param query
     * @return
     */
    @RequestMapping("querySaleChanceByParams")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery query,Integer flag,HttpServletRequest request){
        // 查询参数 flag=1 代表当前查询为开发计划数据，设置查询分配人参数
        if(null!=flag && flag==1){
            // 获取当前登录用户的ID
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            query.setAssignMan(userId);
        }

        return saleChanceService.selectSaleChanceByParam(query);
    }
}
