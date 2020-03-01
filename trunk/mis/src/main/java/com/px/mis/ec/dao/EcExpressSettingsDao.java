package com.px.mis.ec.dao;

import java.util.List;

import com.px.mis.ec.entity.EcExpressSettings;
import com.px.mis.ec.entity.ExpressCodeAndName;

public interface EcExpressSettingsDao {
    public EcExpressSettings selectByPlatId(String platId);

    /**
     * 小红书新增
     */
    public int intotExpressCodeAndName(List<ExpressCodeAndName> list);

    /**
     * 小红书删除
     */
    public int deleteExpressCodeAndName();

    /**
     * 小红书查询
     */
    public ExpressCodeAndName selectExpressCode(String platId);

    /**
     * 小米有品查询
     */
    public ExpressCodeAndName selectExpressCodeXMYP(String platId);

}
