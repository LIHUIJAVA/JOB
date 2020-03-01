package com.px.mis.ec.dao;

import java.util.List;

import com.px.mis.ec.entity.EcExpressSettings;
import com.px.mis.ec.entity.ExpressCodeAndName;

public interface EcExpressSettingsDao {
    public EcExpressSettings selectByPlatId(String platId);

    /**
     * С��������
     */
    public int intotExpressCodeAndName(List<ExpressCodeAndName> list);

    /**
     * С����ɾ��
     */
    public int deleteExpressCodeAndName();

    /**
     * С�����ѯ
     */
    public ExpressCodeAndName selectExpressCode(String platId);

    /**
     * С����Ʒ��ѯ
     */
    public ExpressCodeAndName selectExpressCodeXMYP(String platId);

}
