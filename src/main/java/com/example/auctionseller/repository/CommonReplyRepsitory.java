package com.example.auctionseller.repository;

import com.example.auctionseller.model.CommonModel;
import com.example.auctionseller.model.CommonReplyModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommonReplyRepsitory extends JpaRepository<CommonReplyModel,Integer> {

    List<CommonReplyModel> findAllByCommonModelId(int commonModelId);

    void deleteAllByCommonModelId(int id);
}
