package com.aeta.competition.service;

import com.aeta.competition.dao.CommentMapper;
import com.aeta.competition.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    /**
     * 通过评论id找这条评论
     * @param id
     * @return
     */
    public Comment findCommentById(int id){
        return commentMapper.selectCommentById(id);
    }

    /**
     * 添加评论
     * @param comment
     * @return
     */
    public int addComment(Comment comment){
        if(comment==null)
            throw new IllegalArgumentException("参数不能为空");
        //添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        //这里还可以再做敏感词过滤 先不做

        int rows=commentMapper.insertComment(comment);


        return rows;
    }


    public int selectCountByEntityType(int entityType){
        return commentMapper.selectCountByEntityType(entityType);
    }

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit){
        return commentMapper.selectCommentsByEntity(entityType,entityId,offset,limit);
    }

}
