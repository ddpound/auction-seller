package com.example.auctionseller.repository;

import com.example.auctionseller.model.CommonReplyModel;
import com.example.auctionseller.model.CommonReplyOfReplyModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyofReplyRepository extends JpaRepository<CommonReplyOfReplyModel,Integer> {


}
