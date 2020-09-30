package com.ly.vrps.foreign.controller;

import com.github.pagehelper.PageInfo;
import com.ly.vrps.common.model.*;
import com.ly.vrps.common.util.DateUtil;
import com.ly.vrps.common.util.Tools;
import com.ly.vrps.foreign.service.*;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springfox.documentation.annotations.ApiIgnore;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 主页
 *
 * @author 10589
 * @date 2016/10/3
 * @time 19:51
 */
@Controller
@RequestMapping("/xl")
@ApiIgnore
public class IndexController {

    @Resource
    private CataLogService cataLogService;

    @Resource
    private SubClassService subClassService;

    @Resource
    private DecadeService decadeService;

    @Resource
    private LevelService levelService;

    @Resource
    private LocService locService;

    @Resource
    private TypeService typeService;

    @Resource
    private FilmService filmService;

    @Resource
    private ResService resService;

    @Resource
    private RatyService ratyService;

    final BASE64Decoder decoder = new BASE64Decoder();

    @RequestMapping(value = "/index.html")
    public String index(ModelMap map, HttpServletRequest request){
        getFilmList(map, request,1);
        String cataLogId = request.getParameter("cataLogId");
        if(Tools.notEmpty(cataLogId)){
            List<SubClass> subClassList =  subClassService.listIsUse(cataLogId);
            map.addAttribute("subClassList",subClassList);
            map.addAttribute("cataLogId",cataLogId);
        }

        String subClassId = request.getParameter("subClassId");
        if(Tools.notEmpty(subClassId)){
            List<Type> typeList = typeService.listIsUseBySubClassId(subClassId);
            map.addAttribute("subClassId",subClassId);
            map.addAttribute("typeList",typeList);

        }
        map.addAttribute("typeId",request.getParameter("typeId"));
        map.addAttribute("locId",request.getParameter("locId"));
        map.addAttribute("levelId",request.getParameter("levelId"));
        map.addAttribute("onDecade",request.getParameter("onDecade"));
        getCatalog(map);
        return "index/index";
    }

    @RequestMapping(value = "/pc.html")
    public String pc(String filmId, String src, String j,
                     Model model,HttpServletRequest request,HttpSession session) throws IOException {
        Film film = filmService.load(filmId);
        /**判断是否是VIP资源进行VIP身份校验*/
        if(film.getIsVip()==1){
            /**获取当前登录用户*/
            User u_skl = (User)session.getAttribute(AuthenticationController.USER_KEY);
            String referer = request.getHeader("referer");
            if(u_skl!=null){
                if(u_skl.getIsVip()==0){
                    model.addAttribute("error_info","not_vip");
                    return "redirect:"+referer;
                }
            }else{
                model.addAttribute("error_info","not_login");
                return "redirect:"+referer;
            }
        }

        if(Tools.notEmpty(src)){
            String s = new String(decoder.decodeBuffer(src), "UTF-8");
            src = s.substring(0, s.length()-1);
            model.addAttribute("src",src);
        }
        if(Tools.notEmpty(j)){
            model.addAttribute("j",j);
        }
        model.addAttribute("film",film);
        return "index/pc";
    }

    @RequestMapping(value = "/detail.html")
    public String detail(ModelMap map, String filmId, String src, String j, HttpServletRequest request, HttpSession session, RedirectAttributes redirectAttributes){

        Film film = filmService.load(filmId);
        /**判断是否是VIP资源进行VIP身份校验*/
        if(film.getIsVip()==1){
            /**获取当前登录用户*/
            User u_skl = (User)session.getAttribute(AuthenticationController.USER_KEY);
            String referer = request.getHeader("referer");
            if(u_skl!=null){
                if(u_skl.getIsVip()==0){
                    redirectAttributes.addFlashAttribute("error_info","not_vip");
                    return "redirect:"+referer;
                }
            }else{
                redirectAttributes.addFlashAttribute("error_info","not_login");
                return "redirect:"+referer;
            }
        }

        if(Tools.notEmpty(src)){
            map.addAttribute("src",src);
        }
        if(Tools.notEmpty(j)){
            map.addAttribute("j",j);
        }

        List<CataLog> cataLogList =  cataLogService.listIsUse();
        map.addAttribute("cataLogList",cataLogList);
        map.addAttribute("film",film);

        /**
         * 获取该影片总的评分人数
         */
        map.addAttribute("totalRatys",ratyService.getCountByfilmId(filmId));

        /**
         * 根据类型查询影片
         */

        List<Film> films = filmService.listByTypeId(film.getTypeId());
        map.addAttribute("films",films);

        return "index/detail";
    }
    @RequestMapping("/palyer.html")
    public String palyer(String filmId, String src, String j, Model model,HttpSession session,HttpServletRequest request){
        Film film = filmService.load(filmId);
        /**判断是否是VIP资源进行VIP身份校验*/
        if(film.getIsVip()==1){
            /**获取当前登录用户*/
            User u_skl = (User)session.getAttribute(AuthenticationController.USER_KEY);
            String referer = request.getHeader("referer");
            if(u_skl!=null){
                if(u_skl.getIsVip()==0){
                    model.addAttribute("error_info","not_vip");
                    return "redirect:"+referer;
                }
            }else{
                model.addAttribute("error_info","not_login");
                return "redirect:"+referer;
            }
        }

        if(Tools.notEmpty(src)){
            model.addAttribute("src",src);
        }
        if(Tools.notEmpty(j)){
            model.addAttribute("j",j);
        }
        model.addAttribute("film",film);

        return "index/palyer";

    }


    @RequestMapping(value = "/addRaty.html")
    public  @ResponseBody
    String addRaty(Raty raty){
        JSONObject jsonObject = new JSONObject();
        /*设置时间*/
        raty.setEnTime(DateUtil.getTime());
        if(ratyService.add(raty)!="0"){
            /**
             *  1. 查询出所有该影片的评分
             */
            List<Raty> list = ratyService.listALLByFilmId(raty.getFilmId());
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                count = count+Integer.parseInt(list.get(i).getScore());
            }

            /**
             * 2.总分除于总评分人数
             */
            long tem = count/list.size();

            double evaluation = Math.floor(tem*10d)/10;

            /**
             * 3.更改film的评分
             */
            Film film = filmService.load(raty.getFilmId());
            film.setEvaluation(evaluation);
            if(filmService.update(film)){
                jsonObject.put("code","1");
            }else{
                jsonObject.put("code","0");
            }
        }else{
            jsonObject.put("code","0");
        }
        return jsonObject.toString();

    }
    private void getFilmList(ModelMap map, HttpServletRequest request, int flag) {
        String name = request.getParameter("name");
        if(Tools.notEmpty(name)){
            map.addAttribute("name",name);
        }

        String pageNum = request.getParameter("pageNum");
        if(Tools.isEmpty(pageNum)){
            pageNum = "1";
        }
        String pageSize = request.getParameter("pageSize");
        if(Tools.isEmpty(pageSize)){
            pageSize = "18";
        }

        Film ob = Tools.toBean(request.getParameterMap(),Film.class);
        PageInfo<Film> pb = filmService.getPage(ob, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        map.addAttribute("page",pb);
    }

    private void getCatalog(ModelMap map) {

        //读取路径下的文件返回UTF-8类型json字符串
        map.addAttribute("cataLogList",cataLogService.listIsUse());
        map.addAttribute("locList",locService.listIsUse());
        map.addAttribute("levelList",levelService.listIsUse());
        map.addAttribute("decadeList",decadeService.listIsUse());
    }

}
