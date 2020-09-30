package com.ly.vrps.common.dao;

import com.ly.vrps.common.model.Film;
import com.ly.vrps.common.model.TBusFilm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TBusFilmDao {
    int deleteByPrimaryKey(String id);

    int insert(TBusFilm record);

    int insertSelective(TBusFilm record);

    TBusFilm selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TBusFilm record);

    int updateByPrimaryKey(TBusFilm record);

    List<TBusFilm> dataGrid(@Param("film") TBusFilm record);

    int batchDelete(@Param("idArr") String[] idsArr);

    String getKey();
}