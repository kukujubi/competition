package com.aeta.competition.service;

import com.aeta.competition.dao.CommentMapper;
import com.aeta.competition.entity.Comment;
import com.aeta.competition.entity.User;
import com.aeta.competition.util.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 通过评论id找这条评论
     * @param id
     * @return
     */
    public Comment findCommentById(int id){
        return commentMapper.selectCommentById(id);
    }


    /**
     * 通过entityId找到提问者
     * @param entityId
     * @return
     */
    public int findCommentUserId(int entityId){
        return commentMapper.findCommentUserId(entityId);
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

    public void sendReplyMail (int userId,Comment comment){
        User user = userService.findUserById(userId);
        //激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        String from = userService.findUserById(comment.getUserId()).getUsername();
        context.setVariable("from",from);
        context.setVariable("content",comment.getContent());
        String content = templateEngine.process("/mail/reply",context);
        mailClient.sendMail(user.getEmail(),"收到回复消息",content);


    }

}
