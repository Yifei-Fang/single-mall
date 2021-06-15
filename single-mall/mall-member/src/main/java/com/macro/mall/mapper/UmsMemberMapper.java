package com.macro.mall.mapper;

import com.macro.mall.domain.UmsMember;
import com.macro.mall.domain.UmsMemberExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsMemberMapper {
    long countByExample(UmsMemberExample example);

    int deleteByExample(UmsMemberExample example);

    int deleteByPrimaryKey(Long id);

    /**
     * 如果选择insert 那么所有的字段都会添加一遍，即使有的字段没有值
     * @param record
     * @return
     */
    int insert(UmsMember record);

    /**
     * 但是如果使用inserSelective就会只给有值的字段赋值（会对传进来的值做非空判断
     * @param record
     * @return
     */
    int insertSelective(UmsMember record);

    List<UmsMember> selectByExample(UmsMemberExample example);

    UmsMember selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UmsMember record, @Param("example") UmsMemberExample example);

    int updateByExample(@Param("record") UmsMember record, @Param("example") UmsMemberExample example);

    int updateByPrimaryKeySelective(UmsMember record);

    int updateByPrimaryKey(UmsMember record);
}