package com.ly.vrps.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.vrps.common.dao.TBusFilmDao;
import com.ly.vrps.common.dao.impl.FilmMapper;
import com.ly.vrps.common.model.Film;
import com.ly.vrps.common.model.TBusFilm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CustomFilmService {
    @Autowired
    private TBusFilmDao tBusFilmDao;


    public PageInfo<TBusFilm> dataGrid(TBusFilm film,Integer page, Integer limit){
        PageInfo<TBusFilm> pageInfo = null;
        try {
            PageHelper.startPage(page, limit);
            List<TBusFilm> list = this.tBusFilmDao.dataGrid(film);
            pageInfo = new PageInfo<>(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageInfo;
    }


    public JSONObject delete(String ids){
        JSONObject object = new JSONObject();
        try {
            object.put("falg",true);
            this.tBusFilmDao.batchDelete(ids.split(","));

        }catch (Exception e){
            e.printStackTrace();
            object.put("falg",false);
        }
        return  object;
    }

    public JSONObject addSave(TBusFilm film){
        JSONObject object = new JSONObject();
        try {
            object.put("falg",true);
            film.setId(UUID.randomUUID().toString().replace("-",""));
            film.setCreateTime(new Date());
            film.setUpdateTime(new Date());
            film.setEvaluation(0.0);
            film.setIsUse(1);
            this.tBusFilmDao.insert(film);
        }catch (Exception e){
            e.printStackTrace();
            object.put("falg",false);
        }
        return  object;
    }


    public TBusFilm getRecordById(String id){
        return  this.tBusFilmDao.selectByPrimaryKey(id);
    }

    public JSONObject updateSave(TBusFilm film){
        JSONObject object = new JSONObject();
        try {
            object.put("falg",true);
            film.setUpdateTime(new Date());
            this.tBusFilmDao.updateByPrimaryKey(film);
        }catch (Exception e){
            e.printStackTrace();
            object.put("falg",false);
        }
        return  object;
    }


}
