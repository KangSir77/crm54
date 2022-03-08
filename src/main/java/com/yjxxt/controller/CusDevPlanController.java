package com.yjxxt.controller;

import com.yjxxt.bean.SaleChance;
import com.yjxxt.service.SaleChanceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("cus_dev")
public class CusDevPlanController {

    @Resource
    private SaleChanceService saleChanceService;
    /**
     * 客户开发主页面
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "cusDevPlan/cus_dev_plan";
    }

    /**
     * 进入开发计划项数据页面
     * @param model
     * @param sid
     * @return
     */
    @RequestMapping("toCusDevPlanDataPage")
    public String toCusDevPlanDataPage (Model model, Integer sid) {
        // 通过id查询营销机会数据
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(sid);
        // 将数据存到作用域中
        model.addAttribute("saleChance", saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }
}
