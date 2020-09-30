package com.ly.vrps.management.busmanager;

import com.github.pagehelper.PageInfo;
import com.ly.vrps.common.model.CataLog;
import com.ly.vrps.common.model.SubClass;
import com.ly.vrps.common.model.TBusFilm;
import com.ly.vrps.common.service.impl.CustomFilmService;
import com.ly.vrps.foreign.service.CataLogService;
import com.ly.vrps.foreign.service.DecadeService;
import com.ly.vrps.foreign.service.LocService;
import com.ly.vrps.foreign.service.SubClassService;
import io.swagger.annotations.ApiOperation;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;
import java.util.List;

@Controller
@RequestMapping("/film")
public class FilmController {


    @Autowired
    private CustomFilmService customFilmService;

    @Autowired
    private CataLogService cataLogService;

    @Autowired
    private DecadeService decadeService;

    @Autowired
    private LocService locService;

    @Autowired
    private SubClassService subClassService;

    @ApiOperation(value = "删除管理")
    @GetMapping("index")
    @ApiIgnore
    public String index(Model model){
        List<CataLog> cataLogs = cataLogService.listIsUse();
        model.addAttribute("cataLogs",cataLogs);
        return "film/filmList";
    }

    @RequestMapping("dataGrid")
    @ResponseBody
    public JSONObject dataGrid(TBusFilm film, Integer page, Integer limit){
        JSONObject obj = null;
        try {
            PageInfo<TBusFilm> pageInfo = customFilmService.dataGrid(film, page, limit);
            obj = getPageJson(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }


    @RequestMapping("toadd")
    public String toadd(Model model){
        //获取年代信息
        model.addAttribute("decadeList", decadeService.listIsUse());
        model.addAttribute("cataLogList",cataLogService.listIsUse());
        model.addAttribute("locList",locService.listIsUse());
        return "film/filmAdd";
    }

    @RequestMapping("change")
    @ResponseBody
    public  List<SubClass> change(String id){
        List<SubClass> classList = this.subClassService.listIsUse(id);
        return  classList;
    }


    @RequestMapping("addSave")
    @ResponseBody
    public JSONObject addSave(@RequestBody  TBusFilm film){
        return  this.customFilmService.addSave(film);
    }


    @RequestMapping("toUpdate")
    public String toUpdate(Model model,String id){
        TBusFilm film = this.customFilmService.getRecordById(id);
        model.addAttribute("film", film);
        //获取年代信息
        model.addAttribute("decadeList", decadeService.listIsUse());
        model.addAttribute("cataLogList",cataLogService.listIsUse());
        model.addAttribute("locList",locService.listIsUse());
        return "film/filmUpdate";
    }
    @RequestMapping("updateSave")
    @ResponseBody
    public JSONObject updateSave(@RequestBody  TBusFilm film){
        return this.customFilmService.updateSave(film);
    }



    @RequestMapping("delete")
    @ResponseBody
    public JSONObject delete(String ids){
        return this.customFilmService.delete(ids);
    }


    //拿到每一页数据的json信息
    public JSONObject getPageJson(PageInfo pageInfo) {
        JSONObject obj = new JSONObject();
        try {
            //从pageInfo当中拿到查询的到的总记录数total，并封装在count变量当中用于前台取出
            obj.put("count", pageInfo.getTotal());
            //从pageInfo当中拿到查询的到的Node列表总数据list，并封装在data变量当中用于前台取出
            obj.put("data", pageInfo.getList());
            obj.put("pageInfo", pageInfo);
            //设置前台相应码0，代表成功响应，数据可以回显出来
            obj.put("code", "0");
            //定义响应信息
            obj.put("msg", "");
            //从pageInfo当中拿到查询的总页数Pages，并封装在pages变量当中用于前台取出
            obj.put("pages", pageInfo.getPages());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }


}
