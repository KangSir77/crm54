package com.yjxxt.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.base.BaseService;
import com.yjxxt.bean.SaleChance;
import com.yjxxt.enums.DevResult;
import com.yjxxt.enums.StateStatus;
import com.yjxxt.mapper.SaleChanceMapper;
import com.yjxxt.query.SaleChanceQuery;
import com.yjxxt.utils.AssertUtil;
import com.yjxxt.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer>{

    @Resource
    private SaleChanceMapper saleChanceMapper;



    /*条件查询*/

    public Map<String,Object> selectSaleChanceByParam(SaleChanceQuery query){
        //初始化条件
        PageHelper.startPage(query.getPage(),query.getLimit());
        //没有分页的集合数据
        List<SaleChance> list=saleChanceMapper.selectByParams(query);
        //分页好的集合数据
        PageInfo<SaleChance> slist=new PageInfo<SaleChance>(list);
        System.out.println(slist);
        //实例化Map
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("msg","success");
        map.put("count",slist.getTotal());
        map.put("data",slist.getList());
        //返回目标map
        return map;
    }


    @Transactional(propagation=Propagation.REQUIRED)
    public void save(SaleChance saleChance) {
        //1.参数校验
        checkParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());

        //2.设置相关参数默认值
        // 未选择分配人
        saleChance.setState(0);//0-未分配,1-已分配
        saleChance.setDevResult(0);//0-未开发,1-开发中,2-已开发,3-开发失败
        // 已经有分配人
        if (StringUtils.isNotBlank(saleChance.getAssignMan())) {
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }
        //默认值
        saleChance.setIsValid(1);
        saleChance.setUpdateDate(new Date());
        saleChance.setCreateDate(new Date());

        //3.执行添加 判断结果
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)<1,"添加失败了");
    }

    /**
     * 验证数据
     * @param customerName 非空
     * @param linkMan 非空
     * @param linkPhone 非空,合法手机号
     */
    private void checkParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系电话不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"请输入合法的手机号");
    }

    /**
     * 营销机会数据更新
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        // 1.参数校验
        // 通过id查询记录
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        // 判断是否为空
        AssertUtil.isTrue(null == temp,"待更新记录不存在！");
        // 校验基础参数
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());

        // 2. 设置相关参数值
        saleChance.setUpdateDate(new Date());
        if(StringUtils.isBlank(temp.getAssignMan())
        && StringUtils.isNotBlank(saleChance.getAssignMan())){
            // 如果原始记录未分配，修改后改为已分配
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }else if(StringUtils.isNotBlank(temp.getAssignMan())
                && StringUtils.isBlank(saleChance.getAssignMan())){
            //如果原始记录已分配，修改后改为未分配
            saleChance.setAssignMan("");
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setAssignTime(null);
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        }

        // 3.执行更新 判断结果
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1,"营销机会数据更新失败！");
    }

    /**
     * 营销机会数据删除
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids){
        // 判断要删除的id是否为空
        AssertUtil.isTrue(null == ids || ids.length == 0,"请选择需要删除的数据！");
        //删除数据
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids)<0,"营销机会数据删除失败！");
    }
}
