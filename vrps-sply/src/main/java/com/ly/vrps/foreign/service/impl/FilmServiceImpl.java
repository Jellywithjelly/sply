package com.ly.vrps.foreign.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.vrps.common.dao.TBusFilmDao;
import com.ly.vrps.common.dao.impl.CataLogMapper;
import com.ly.vrps.common.dao.impl.FilmMapper;
import com.ly.vrps.common.dao.impl.SubClassMapper;
import com.ly.vrps.common.dao.impl.TypeMapper;
import com.ly.vrps.common.model.*;
import com.ly.vrps.foreign.service.FilmService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 影片service层接口实现
 *
 * @author 10589
 * @date 2016/10/4
 * @time 12:06
 */
@Component
public class FilmServiceImpl implements FilmService {
    @Resource
    private FilmMapper filmMapper;

    @Resource
    private TypeMapper typeMapper;

    @Resource
    private SubClassMapper subClassMapper;

    @Resource
    private CataLogMapper cataLogMapper;

    @Autowired
    private TBusFilmDao tBusFilmDao;

    static String basePath = "39.106.254.247";


    @Override
    public String add(Film film) {
        /**
         * 初始化参数
         */
        film.setIsUse(1);
        film.setUpdateTime(new Date());
        film.setEvaluation(0d);

        /*设置图片显示地址*/
       /* film.setImage("http://"+basePath+"/"+film.getImage());*/

        /**
         * 查询出类型
         */
        Type type = typeMapper.selectByPrimaryKey(film.getTypeId());

        /**
         * 查询二级分类
         */
        SubClass subClass = subClassMapper.selectByPrimaryKey(type.getSubclassId());
        film.setSubClassId(type.getSubclassId());
        film.setSubClassName(subClass.getName());
        /**
         * 查询目录
         */
        CataLog cataLog = cataLogMapper.selectByPrimaryKey(subClass.getCatalogId());
        film.setCataLogId(subClass.getCatalogId());
        film.setCataLogName(cataLog.getName());
        film.setId(UUID.randomUUID().toString().replaceAll("-",""));

        return filmMapper.insert(film) > 0 ? film.getId() : "0";
    }

    @Override
    public Film load(String filmId) {
        Film film = filmMapper.selectByPrimaryKey(filmId);
        return film;
    }

    /**
     * 更改信息
     *
     * @param film
     * @return
     */
    @Override
    public boolean update(Film film) {
        return filmMapper.updateByPrimaryKeySelective(film) > 0;
    }

    /**
     * 在线影片分页
     *
     * @param film
     * @param pc
     * @param ps
     * @return
     */
    @Override
    public PageInfo<Film> getPage(Film film, int pc, int ps) {
        PageHelper.startPage(pc, ps);
        List<Film> films = filmMapper.select(film);
        return new PageInfo<>(films);
    }

    /**
     * 通过type_id查询影片
     *
     * @param typeId
     * @return
     */
    @Override
    public List<Film> listByTypeId(String typeId) {
        Film film = new Film();
        film.setTypeId(typeId);
        return filmMapper.select(film);
    }

    @Override
    public List<Film> listByCataLogId(String id) {
        return getFilms(id, Comparator.comparing(Film::getUpdateTime));
    }

    @Override
    public List<Film> listByEvaluation(String id) {
        return getFilms(id, Comparator.comparing(Film::getEvaluation));
    }

    private List<Film> getFilms(String id, Comparator<Film> comparing) {
        Film film = new Film();
        film.setCataLogId(id);
        film.setIsUse(1);
        List<Film> films = filmMapper.select(film);
        films.sort(comparing.reversed());
        boolean empty = CollectionUtils.isEmpty(films);
        int end = 12;
        if(!empty){
            end = films.size() >= 12 ? 12 : films.size();
        }
        return empty? Collections.emptyList() : films.subList(0, end);
    }

    @Override
    public int getCountAll() {
        List<Film> films = filmMapper.select(new Film());
        return CollectionUtils.isEmpty(films) ? 0 : films.size();
    }

    @Override
    public int getCountByCataLog(String id) {
        Film film = new Film();
        film.setIsUse(1);
        film.setCataLogId(id);
        List<Film> films = filmMapper.select(film);
        return CollectionUtils.isEmpty(films) ? 0 : films.size();
    }

    @Override
    public List<Film> selectByCondition(Film film) {
        return filmMapper.select(film);
    }
}
