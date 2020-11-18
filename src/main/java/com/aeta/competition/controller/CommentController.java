package com.aeta.competition.controller;

import com.aeta.competition.entity.Comment;
import com.aeta.competition.entity.Page;
import com.aeta.competition.entity.UrlMessageEntity;
import com.aeta.competition.entity.User;
import com.aeta.competition.service.CommentService;
import com.aeta.competition.service.UserService;
import com.aeta.competition.util.CompetitionConstant;
import com.aeta.competition.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class CommentController implements CompetitionConstant {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    //这个地方有点晕 是提问还是回复 entityType,entityId,targetId应该都能直接得到吧？
    @RequestMapping(path = "/addComment",method = RequestMethod.POST)
    public String addComment(@RequestParam("entityType") int entityType,@RequestParam("entityId") int entityId,
                             @RequestParam("targetId") int targetId,@RequestParam("content") String content) {
        Comment comment = new Comment();
        //comment.setUserId(hostHolder.getUser().getId());
        comment.setUserId(9);
        comment.setEntityType(entityType);
        comment.setEntityId(entityId);
        comment.setTargetId(targetId);
        comment.setContent(content);
        comment.setStatus(0);
        comment.setCreateTime(new Date());

        commentService.addComment(comment);
        //返回到详情页
        return "redirect:/comments";
    }

    /**
     * 分页显示提问 每页按多少个直接提问划分 不管有多少个提问下的回复
     * @param current
     * @return comments:查出来的提问 comment 某个提问  commentUser 这个提问的作者
     *                                     reply 回复 replyUser 这个回复的作者
     *
     */
    @RequestMapping(path = "/comments",method = RequestMethod.GET)
    @ResponseBody
    public UrlMessageEntity getComments( @RequestParam(value = "current",required = false) Integer current){

        //评论分页信息
        Page page = new Page();
        if (current != null)
            page.setCurrent(current);
        //一页显示多少这个上限之后改大一点
        page.setLimit(5);
        page.setPath("/comments");
        //所有提问总数
        int commentCount = commentService.selectCountByEntityType(ENTITY_TYPE_COMMENT);
        page.setRows(commentCount);

        //提问列表
        List<Comment> commentList=commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT,0,page.getOffset(),page.getLimit());
        //评论VO列表（显示）
        List<Map<String,Object>> commentVoList = new ArrayList<>();
        if(commentList!=null){
            for(Comment comment:commentList){
                //一个评论的VO
                Map<String,Object> commentVo=new HashMap<>();
                //评论 这种整个实体包装好返回的能解析出来嘛？ 还是要更细的写出来？
                commentVo.put("comment",comment);
                //作者
                commentVo.put("commentUser",userService.findUserById(comment.getUserId()));

                //回复列表
                List<Comment> replyList=commentService.findCommentsByEntity(ENTITY_TYPE_REPLY,comment.getId(),0,Integer.MAX_VALUE);
                //回复的列表
                List<Map<String,Object>> replyVoList = new ArrayList<>();
                if (replyList!=null){
                    for(Comment reply:replyList){
                        Map<String,Object> replyVo=new HashMap<>();
                        //回复
                        replyVo.put("reply",reply);
                        //回复作者
                        replyVo.put("replyUser",userService.findUserById(reply.getUserId()));
                        //回复目标
                        User target=reply.getTargetId()==0?null:userService.findUserById(reply.getTargetId());
                        replyVo.put("target",target);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys",replyVoList);


                commentVoList.add(commentVo);

            }
        }
        Map<String,Object> message = new HashMap<>();
        message.put("comments",commentVoList);
        String url = "/site/comments-detail";//?

        return UrlMessageEntity.getResponse(url,message);
    }

}
