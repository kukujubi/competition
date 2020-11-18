package com.aeta.competition.dao;

import com.aeta.competition.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    int selectCountByEntityType(int entityType);

    int insertComment(Comment comment);

    Comment selectCommentById(int id);

    List<Comment> selectCommentsByEntity(int entityType,int entityId,int offset,int limit);
}
