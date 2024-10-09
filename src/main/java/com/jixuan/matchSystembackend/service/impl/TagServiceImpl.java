package com.jixuan.matchSystembackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jixuan.matchSystembackend.model.domain.Tag;
import com.jixuan.matchSystembackend.service.TagService;
import com.jixuan.matchSystembackend.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author Jaychou
* @description 针对表【tag(标签)】的数据库操作Service实现
* @createDate 2024-09-09 11:18:46
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




