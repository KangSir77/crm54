package com.yjxxt.query;

import com.yjxxt.base.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleChanceQuery extends BaseQuery {
    private String customerName;
    private String createMan;
    private Integer state;
    private Integer devResult; // 开发状态
    private Integer assignMan;// 分配人

    public String getCreatMan() {
        return createMan;
    }

    public void setCreatMan(String creatMan) {
        this.createMan = creatMan;
    }
}
