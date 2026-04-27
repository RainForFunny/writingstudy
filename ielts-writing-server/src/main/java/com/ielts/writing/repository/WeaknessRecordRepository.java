package com.ielts.writing.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ielts.writing.model.entity.WeaknessRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WeaknessRecordRepository extends BaseMapper<WeaknessRecord> {
}