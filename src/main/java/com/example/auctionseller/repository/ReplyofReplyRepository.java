package com.example.auctionseller.repository;

import com.example.auctionseller.model.CommonReplyModel;
import com.example.auctionseller.model.CommonReplyOfReplyModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyofReplyRepository extends JpaRepository<CommonReplyOfReplyModel,Integer> {

    /**
     * @param replyId 댓글(대댓글 아님)의 아이디를 받아냄
     *
     * 댓글의 아이디를 받아 관련된 모든 대댓글들을 삭제함
     * */
    void deleteAllByCommonReplyId(int replyId);


    /**
     * @param commonModelId 게시판의 아이디를 받아내 삭제
     *
     * 게시판 아이디를 받아 관련된 모든 대댓글들을 삭제함
     * */
    void deleteAllByCommonModelId(int commonModelId);
}
